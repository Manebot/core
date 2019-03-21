package com.github.manevolent.jbot.plugin;

public interface PluginReference {

    void load(Plugin plugin);

    void unload(Plugin plugin);

}
