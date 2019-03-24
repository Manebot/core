package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.artifact.Artifact;
import com.github.manevolent.jbot.artifact.ManifestIdentifier;
import com.github.manevolent.jbot.command.executor.CommandExecutor;
import com.github.manevolent.jbot.database.Database;
import com.github.manevolent.jbot.event.EventListener;
import com.github.manevolent.jbot.platform.Platform;
import com.github.manevolent.jbot.platform.PlatformRegistration;
import com.github.manevolent.jbot.virtual.Virtual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

public interface Plugin {

    /**
     * Gets an instance provided by the specified Plugin.
     * @param instanceClass Instance class to look for.
     * @param <T> Type to return
     * @return Instance
     */
    <T extends PluginReference> T getInstance(Class<? extends T> instanceClass);

    /**
     * Gets the registration used to map this Plugin object to the system.
     * @return PluginRegistration instance.
     */
    PluginRegistration getRegistration();

    /**
     * Gets the artifact associated with this plugin.
     * @return associated Artifact instance.
     */
    Artifact getArtifact();

    /**
     * Gets a collection of dependencies associated with this plugin.
     * @return Plugin dependencies.
     */
    Collection<Plugin> getDependencies();

    /**
     * Gets a collection of plugins depending on this plugin.
     * @return Plugin dependers.
     */
    Collection<Plugin> getDependers();

    /**
     * Gets a dependent plugin by its manifest identifier.
     * @param identifier manifest identifier to search for in this plugin's dependencies.
     * @return Plugin instance representing the dependent plugin described by the given identifier.
     */
    default Plugin getDependentPlugin(ManifestIdentifier identifier) {
        return getDependencies().stream()
                .filter(dependentPlugin -> dependentPlugin
                        .getArtifact().getIdentifier().withoutVersion().equals(identifier))
                .findFirst()
                .orElse(null);
    }

    /**
     * Depends on a plugin, enabling it if possible.
     * @param identifier Identifier.
     * @return Plugin instance of the desired dependency.
     */
    default Plugin depend(ManifestIdentifier identifier) throws PluginException {
        Plugin plugin = getDependentPlugin(identifier);
        if (plugin == null) throw new PluginException("Plugin not found: " + identifier);

        plugin.setEnabled(true);
        return plugin;
    }

    /**
     * Gets a list of platforms registered by this plugin.
     * @return associated Platform instances.
     */
    Collection<Platform> getPlatforms();

    /**
     * Gets a collection of Databases registered by this plugin.
     * @return collection of associated databases.
     */
    Collection<Database> getDatabases();

    /**
     * Gets the registered command labels for this plugin.
     * @return registered commands.
     */
    Collection<String> getCommands();

    /**
     * Gets the value for a specific plugin property.
     * @param name name of the property to require.
     * @return property value.
     * @throws IllegalArgumentException if the property is not defined, or is defined null.
     */
    default String requireProperty(String name) throws IllegalArgumentException {
        String value = getProperty(name);
        if (value == null) throw new IllegalArgumentException("Missing required property: " + name);
        return value;
    }

    /**
     * Gets the value for a specific plugin property.
     * @param name name of the property to require.
     * @return property value, or null if it is not defined.
     */
    default String getProperty(String name) {
        return getRegistration().getProperty(name);
    }

    /**
     * Gets the value for a specific plugin property.
     * @param name name of the property to obtain.
     * @param defaultValue default value when no value does exist.
     * @return property value, or <b>defaultValue</b> if the property does not yet exist, or is defined null.
     */
    default String getProperty(String name, String defaultValue) {
        String value = getProperty(name);
        if (value == null) return defaultValue;
        else return value;
    }

    /**
     * Gets this plugin's name.  This is typically the lowercase <b>artifactId</b> of the plugin.
     * @return Plugin's name.
     */
    String getName();

    /**
     * Gets the logger associated with this plugin.
     * @return Logger instance.
     */
    Logger getLogger();

    /**
     * Sets this artifact's enabled state.
     * @param enabled enabled state
     * @return true if the artifact's state was changed, false otherwise.
     */
    boolean setEnabled(boolean enabled) throws PluginException;

    /**
     * Finds if this plugin is enabled.
     * @return true if the plugin is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Plugin instance Builder
     */
    interface Builder {

        /**
         * Gets the plugin manager associated with this Plugin builder.
         * @return PluginManager instance.
         */
        PluginManager getPluginManager();

        /**
         * Gets the artifact associated with this Plugin builder.
         * @return Artifact instance.
         */
        Artifact getArtifact();

        /**
         * Signals dependency on another Plugin.  Note that, this does not load a Plugin instance.  This only obtains
         * an already-recognized dependent Plugin object.
         * @param identifier Manifest identifier.
         * @return Plugin instance.
         */
        Plugin getDependency(ManifestIdentifier identifier);

        /**
         * Registers an event listener to this Plugin.
         * @param listener event listener to bind.
         * @return Builder instance.
         */
        Builder listen(EventListener listener);

        /**
         * Gets a plugin property.
         * @param name property name.
         * @return property instance.
         */
        PluginProperty getProperty(String name);

        /**
         * Require that the specified identifier plugin is included, and enabled.
         * @param identifier ManifestIdentifier of a plugin to require.
         * @return Builder instance.
         */
        Builder require(ManifestIdentifier identifier);

        /**
         * Adds an event listener that is fired when a plugin depends on this plugin.
         * @param pluginConsumer Plugin consumer to fire when a dependency is loaded.
         * @return Builder instance.
         */
        Builder onDepend(Consumer<Plugin> pluginConsumer);

        /**
         * Registers a command to this Plugin.
         * @param label global label to assign command to.
         * @param executor CommandExecutor constructor function to bind this label to when a registration is created.
         * @return Builder instance.
         */
        Builder command(String label, Function<Future, CommandExecutor> executor);

        /**
         * Registers a command to this Plugin.
         * @param label global label to assign command to.
         * @param executor CommandExecutor to bind this label to.
         * @return Builder instance.
         */
        default Builder command(String label, CommandExecutor executor) {
            return command(label, (registration) -> executor);
        }

        /**
         * Registers a command to this Plugin.
         * @param labels labels to assign this command to.
         * @param executor CommandExecutor to bind this label to.
         * @return Builder instance.
         */
        default Builder command(Collection<String> labels, CommandExecutor executor) {
            for (String label : labels)
                command(label, executor);

            return this;
        }

        /**
         * Builds a platform for this Plugin instance.
         * @param function Platform building function.
         * @return Builder instance.
         */
        Builder platform(Function<Platform.Builder, PlatformRegistration> function);

        /**
         * Binds the specified class to an <i>instance</i>, which is a simple method of communicating functionality to
         * dependent plugins from another plugin.
         *
         * Instances are defined when a plugin is successfully enabled and has configured other features,
         * such as Platforms.
         *
         * @param instantiator instantiator function: load() called during Plugin enable, unload() at disable.
         * @param <T> User-chosen type to bind the instance to.
         * @return Builder instance.
         */
        <T extends PluginReference>
        Builder instance(Class<T> instanceClass, Function<Future, T> instantiator);

        /**
         * Calls the specified function when the Plugin is enabled.
         *
         * @param function Function to call.
         * @return Builder instance.
         */
        Builder onEnable(PluginFunction function);

        /**
         * Calls the specified function when the Plugin is enabled.
         *
         * @param function Function to call.
         * @return Builder instance.
         */
        Builder onDisable(PluginFunction function);

        /**
         * Constructs a Database immediately.
         * @param name Database name.
         * @param func Function used to construct the database.
         * @return Database instance.
         */
        Database database(String name, Function<Database.ModelConstructor, Database> func);

        /**
         * Builds a Plugin instance.
         * @return Plugin instance.
         */
        Plugin build();

    }

    interface PluginFunction {
        void call(Future future) throws PluginException;
    }

    class Future {
        private final PluginRegistration registration;
        private final List<Consumer<PluginRegistration>> postTasks = new ArrayList<>();

        public Future(PluginRegistration registration) {
            this.registration = registration;
        }

        public Plugin getPlugin() {
            return getRegistration().getInstance();
        }

        public PluginRegistration getRegistration() {
            return registration;
        }

        public void after(Consumer<PluginRegistration> registrationConsumer) {
            postTasks.add(registrationConsumer);
        }

        public void afterAsync(Consumer<PluginRegistration> registrationConsumer) {
            postTasks.add((registration) ->
                    Virtual.getInstance().create(() -> registrationConsumer.accept(registration)).start());
        }

        public Collection<Consumer<PluginRegistration>> getTasks() {
            return Collections.unmodifiableCollection(postTasks);
        }
    }
}
