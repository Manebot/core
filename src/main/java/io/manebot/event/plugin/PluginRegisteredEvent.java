package io.manebot.event.plugin;

import io.manebot.plugin.Plugin;

public class PluginRegisteredEvent extends PluginEvent {
    public PluginRegisteredEvent(Object sender, Plugin plugin) {
        super(sender, plugin);
    }
}
