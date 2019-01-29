package com.github.manevolent.jbot.plugin;

public class PluginLoadException extends PluginException {
    public PluginLoadException(String message) {
        super(message);
    }
    public PluginLoadException(String message, Throwable ex) {
        super(message, ex);
    }
    public PluginLoadException(Throwable ex) {
        super(ex);
    }
}
