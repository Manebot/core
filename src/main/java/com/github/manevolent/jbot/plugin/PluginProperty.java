package com.github.manevolent.jbot.plugin;

/**
 * A simple property storage system for plugins.  This is used mainly to store auth keys, global params, etc.
 */
public interface PluginProperty {

    /**
     * Gets the plugin registration associated with this property.
     */
    PluginRegistration getPluginRegistration();

    /**
     * Gets the unique name of this property.
     * @return name of the property.
     */
    String getName();

    /**
     * Gets the user-assigned value of this property.
     * @return user assigned value, null if not defined.
     */
    String getValue();



}
