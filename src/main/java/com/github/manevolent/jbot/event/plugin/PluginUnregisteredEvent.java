package com.github.manevolent.jbot.event.plugin;

import com.github.manevolent.jbot.plugin.Plugin;

public class PluginUnregisteredEvent extends PluginEvent {
    public PluginUnregisteredEvent(Object sender, Plugin plugin) {
        super(sender, plugin);
    }
}
