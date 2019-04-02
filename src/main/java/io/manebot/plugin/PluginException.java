package io.manebot.plugin;

public class PluginException extends Exception {
    public PluginException(String message) {
        super(message);
    }
    public PluginException(String message, Throwable ex) {
        super(message, ex);
    }
    public PluginException(Throwable ex) {
        super(ex);
    }
}
