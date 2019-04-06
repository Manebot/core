package io.manebot.plugin.java;

import io.manebot.plugin.Plugin;
import io.manebot.plugin.PluginException;

public interface PluginEntry {

    /**
     * Instantiates a plugin
     * @param builder Plugin builder.
     * @throws PluginException when there is a problem instantiating a Plugin instance.
     */
    void instantiate(Plugin.Builder builder) throws PluginException;

    /**
     * Destructs the Plugin's resources.
     * @throws UnsupportedOperationException if the plugin doesn't support destructing completely.
     */
    default void destruct(Plugin plugin) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}
