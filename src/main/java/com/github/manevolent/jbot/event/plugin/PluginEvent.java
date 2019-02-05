package com.github.manevolent.jbot.event.plugin;

import com.github.manevolent.jbot.event.Event;
import com.github.manevolent.jbot.platform.Platform;
import com.github.manevolent.jbot.plugin.Plugin;

public abstract class PluginEvent extends Event {
    private final Plugin plugin;

    public PluginEvent(Object sender, Plugin plugin) {
        super(sender);

        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
