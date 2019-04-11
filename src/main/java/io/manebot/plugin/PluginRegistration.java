package io.manebot.plugin;

import io.manebot.Bot;
import io.manebot.artifact.ArtifactIdentifier;

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
     * Finds if this registration is installed to the parent plugin manager..
     * @return true if the registration is installed, false otherwise.
     */
    default boolean isInstalled() {
        return getPluginManager().isInstalled(getIdentifier());
    }

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
     * @throws SecurityException if there was a security violation setting the property.
     */
    void setAutoStart(boolean autoStart) throws SecurityException;

    /**
     * Finds if the plugin will auto-start.
     * @return true if the plugin should auto-start, false otherwise.
     */
    boolean willAutoStart();

    /**
     * Sets if the plugin is required on start. Required plugins failing startup on auto-start will crash the bot purposefully.
     * @param required true if the plugin is required.
     * @throws SecurityException if there was a security violation setting the property.
     */
    void setRequired(boolean required) throws SecurityException;

    /**
     * Finds if the plugin is required. Required plugins failing startup on auto-start will crash the bot purposefully.
     * @return true if the plugin is required, false if it is not required during startup.
     */
    boolean isRequired();

    /**
     * Sets if the plugin is elevated. Elevated plugins can call routines as the system user.
     * @param elevated true if the plugin is elevated.
     * @throws SecurityException if there was a security violation setting the property.
     */
    void setElevated(boolean elevated) throws SecurityException;

    /**
     * Finds if the plugin is required. Elevated plugins can call routines as the system user.
     * @return true if the plugin is elevated, false otherwise.
     */
    boolean isElevated();

    /**
     * Gets the plugin properties for this plugin.
     * @return immutable collection of plugin properties.
     */
    Collection<PluginProperty> getProperties();

    /**
     * Changes the version of this plugin.
     * @param version version to change to.
     */
    void setVersion(String version);

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
