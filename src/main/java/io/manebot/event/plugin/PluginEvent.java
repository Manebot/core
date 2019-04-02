package io.manebot.event.plugin;

import io.manebot.event.Event;
import io.manebot.plugin.Plugin;

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
