package com.github.manevolent.jbot.plugin.java;

import com.github.manevolent.jbot.plugin.Plugin;
import com.github.manevolent.jbot.plugin.PluginException;

public interface PluginEntry {

    /**
     * Instantiates a plugin
     * @param builder Plugin builder.
     * @return Constructed Plugin instance
     * @throws PluginException when there is a problem instantiating a Plugin instance.
     */
    Plugin instantiate(Plugin.Builder builder) throws PluginException;

    /**
     * Destructs the Plugin's resources.
     */
    void destruct();

}
