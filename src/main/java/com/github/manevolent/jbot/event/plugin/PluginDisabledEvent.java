package com.github.manevolent.jbot.event.plugin;

import com.github.manevolent.jbot.plugin.Plugin;

public class PluginDisabledEvent extends PluginEvent {
    public PluginDisabledEvent(Object sender, Plugin plugin) {
        super(sender, plugin);
    }
}
