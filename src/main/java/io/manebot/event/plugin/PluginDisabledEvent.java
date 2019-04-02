package io.manebot.event.plugin;

import io.manebot.plugin.Plugin;

public class PluginDisabledEvent extends PluginEvent {
    public PluginDisabledEvent(Object sender, Plugin plugin) {
        super(sender, plugin);
    }
}
