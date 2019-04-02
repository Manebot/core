package io.manebot.event.plugin;

import io.manebot.plugin.Plugin;

public class PluginEnabledEvent extends PluginEvent {
    public PluginEnabledEvent(Object sender, Plugin plugin) {
        super(sender, plugin);
    }
}
