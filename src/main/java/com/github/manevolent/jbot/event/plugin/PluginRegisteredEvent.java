package com.github.manevolent.jbot.event.plugin;

import com.github.manevolent.jbot.plugin.Plugin;

public class PluginRegisteredEvent extends PluginEvent {
    public PluginRegisteredEvent(Object sender, Plugin plugin) {
        super(sender, plugin);
    }
}
