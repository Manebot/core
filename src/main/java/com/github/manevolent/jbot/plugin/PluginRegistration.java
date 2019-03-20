package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.artifact.ArtifactIdentifier;

public interface PluginRegistration {

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
     */
    void load();

}
