package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.Bot;
import com.github.manevolent.jbot.artifact.ArtifactIdentifier;

import java.util.Collection;

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
     * Sets if the plugin should auto-start.
     * @param autoStart true if the plugin should auto-start.
     * @return true if the plugin should auto-start, false otherwise.
     * @throws SecurityException if there was a security violation setting the property.
     */
    boolean setAutoStart(boolean autoStart) throws SecurityException;

    /**
     * Finds if the plugin will auto-start.
     * @return true if the plugin should auto-start, false otherwise.
     */
    boolean willAutoStart();
    /**
     * Gets the plugin properties for this plugin.
     * @return immutable collection of plugin properties.
     */
    Collection<PluginProperty> getProperties();

    /**
     * Gets the value for a specific plugin property.
     * @param name name of the property to require.
     * @return property value, or null if it is not defined.
     */
    String getProperty(String name);

    /**
     * Sets a property on this plugin registration.
     * @param name key to set.
     * @param value value to set; may be null.
     * @throws SecurityException if there was a security violation setting the property.
     */
    void setProperty(String name, String value) throws SecurityException;

    /**
     * Loads the plugin into the system.
     *
     * @return loaded Plugin instance.
     * @throws IllegalStateException if the plugin was already loaded.
     * @throws PluginLoadException if there was a problem loading the plugin.
     */
    Plugin load() throws IllegalStateException, PluginLoadException;

}
