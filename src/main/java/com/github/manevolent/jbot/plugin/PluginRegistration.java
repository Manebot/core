package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.Bot;
import com.github.manevolent.jbot.artifact.ArtifactIdentifier;

public interface PluginRegistration {

    /**
     * Gets the bot instance for this registration.
     * @return Bot instance associated with this Plugin registration.
     */
    Bot getBot();

    /**
     * Gets the ArtifactIdentifier associated with this registration.
     * @return ArtifactIdentifier instance.
     */
    ArtifactIdentifier getIdentifier();

    /**
     * Gets the PluginManager instance responsible for this registration.
     * @return PluginManager instance.
     */
    PluginManager getPluginManager();

    /**
     * Gets the current Plugin instance for this registration.
     * @return Plugin instance.
     */
    Plugin getInstance();

    /**
     * Finds if the plugin is loaded into the system.
     * @return true if the plugin is loaded, false otherwise.
     */
    default boolean isLoaded() {
        return getInstance() != null;
    }

    /**
     * Loads the plugin into the system.
     *
     * @return loaded Plugin instance.
     * @throws IllegalStateException if the plugin was already loaded.
     * @throws PluginLoadException if there was a problem loading the plugin.
     */
    Plugin load() throws IllegalStateException, PluginLoadException;

}
