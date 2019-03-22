package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.artifact.Artifact;
import com.github.manevolent.jbot.artifact.ManifestIdentifier;
import com.github.manevolent.jbot.command.executor.CommandExecutor;
import com.github.manevolent.jbot.database.Database;
import com.github.manevolent.jbot.event.EventListener;
import com.github.manevolent.jbot.platform.Platform;
import com.github.manevolent.jbot.platform.PlatformConnection;
import com.github.manevolent.jbot.platform.PlatformRegistration;

import java.util.Collection;
import java.util.function.Function;

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
     * Gets this plugin's name.  This is typically the lowercase <b>artifactId</b> of the plugin.
     * @return Plugin's name.
     */
    String getName();

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
         * Registers a command to this Plugin.
         * @param label global label to assign command to.
         * @param executor CommandExecutor to bind this label to.
         * @return Builder instance.
         */
        Builder command(String label, CommandExecutor executor);

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
         * @param instantiator instantiator function. Called during Plugin enable, cleared at disable.
         * @param <T> User-chosen type to bind the instance to.
         * @return Builder instance.
         */
        <T extends PluginReference>
        Builder instance(Class<T> instanceClass, Function<PluginRegistration, T> instantiator);

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
        void call() throws PluginException;
    }

}
