package io.manebot.plugin;

import io.manebot.Bot;
import io.manebot.artifact.Artifact;
import io.manebot.artifact.ArtifactDependency;
import io.manebot.artifact.ManifestIdentifier;
import io.manebot.command.executor.CommandExecutor;
import io.manebot.database.Database;
import io.manebot.event.EventListener;
import io.manebot.platform.Platform;
import io.manebot.platform.PlatformRegistration;
import io.manebot.virtual.Virtual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

public interface Plugin {

    /**
     * Gets the instance of the bot that is hosting this plugin.
     * @return Bot instance.
     */
    Bot getBot();

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
     * Gets a collection of required dependencies associated with this plugin.
     * @return Required plugin dependencies.
     */
    Collection<Plugin> getRequiredDependencies();

    /**
     * Gets the type of plugin this plugin is assuming.
     * @return plugin type.
     */
    PluginType getType();

    /**
     * Gets a collection of dependencies associated with this plugin.
     * @return Plugin dependencies.
     */
    Collection<Plugin> getDependencies();

    /**
     * Gets a collection of plugins artifact dependencies of this plugin.
     * @return Plugin artifact dependencies.
     */
    Collection<ArtifactDependency> getArtifactDependencies();

    /**
     * Gets a collection of plugins depending on this plugin.
     * @return Plugin dependers.
     */
    Collection<Plugin> getDependers();

    /**
     * Gets a collection of plugins artifact dependers of this plugin.
     * @return Plugin artifact dependers.
     */
    Collection<ArtifactDependency> getArtifactDependers();

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
     * Gets a platform by its ID.
     * @param id platform ID.
     * @return Platform instance if found, null otherwise.
     */
    default Platform getPlatformById(String id) {
        return getPlatforms().stream().filter(platform -> platform.getId().equals(id)).findFirst().orElse(null);
    }

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
        Builder addListener(EventListener listener);

        /**
         * Gets a plugin property.
         * @param name property name.
         * @return property instance.
         */
        PluginProperty getProperty(String name);

        /**
         * Require that the specified identifier plugin is included, and enabled.
         * @param identifier ManifestIdentifier of a plugin to require.
         * @return Plugin instance of the required plugin.
         */
        Plugin requirePlugin(ManifestIdentifier identifier) throws PluginLoadException;

        /**
         * Adds an event listener that is fired when a plugin depends on this plugin.
         * @param pluginConsumer Plugin consumer to fire when a dependency is loaded.
         * @return Builder instance.
         */
        Builder onDepend(Consumer<Plugin> pluginConsumer);

        /**
         * Registers a command to this Plugin.
         * @param label global label to assign command to.
         * @param function CommandExecutor constructor function to bind this label to when a registration is created.
         * @return Builder instance.
         */
        Builder addCommand(String label, Function<Future, CommandExecutor> function);

        /**
         * Registers a command to this Plugin.
         * @param label global label to assign command to.
         * @param executor CommandExecutor to bind this label to.
         * @return Builder instance.
         */
        default Builder addCommand(String label, CommandExecutor executor) {
            return addCommand(label, (registration) -> executor);
        }

        /**
         * Registers a command to this Plugin.
         * @param labels labels to assign this command to.
         * @param executor CommandExecutor to bind this label to.
         * @return Builder instance.
         */
        default Builder addCommand(Collection<String> labels, CommandExecutor executor) {
            for (String label : labels)
                addCommand(label, executor);

            return this;
        }

        /**
         * Registers a command to this Plugin.
         * @param labels labels to assign this command to.
         * @param function CommandExecutor constructor function to bind this label to when a registration is created.
         * @return Builder instance.
         */
        default Builder addCommand(Collection<String> labels, Function<Future, CommandExecutor> function) {
            for (String label : labels)
                addCommand(label, function);

            return this;
        }

        /**
         * Builds a platform for this Plugin instance.
         * @param consumer Platform building consumer.
         * @return Builder instance.
         */
        Builder addPlatform(Consumer<Platform.Builder> consumer);

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
        Builder setInstance(Class<T> instanceClass, Function<Future, T> instantiator);

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
         * @param consumer Function used to construct the database parameters.
         * @return Database instance.
         */
        Database addDatabase(String name, Consumer<Database.ModelConstructor> consumer);

        /**
         * Plugin type
         * @param type type
         * @return Builder instance
         */
        Builder setType(PluginType type);

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
