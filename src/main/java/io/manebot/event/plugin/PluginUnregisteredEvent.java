package io.manebot.event.plugin;

import io.manebot.plugin.Plugin;

public class PluginUnregisteredEvent extends PluginEvent {
    public PluginUnregisteredEvent(Object sender, Plugin plugin) {
        super(sender, plugin);
    }
}
