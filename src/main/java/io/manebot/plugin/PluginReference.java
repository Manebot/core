package io.manebot.plugin;

public interface PluginReference {

    void load(Plugin.Future plugin);

    void unload(Plugin.Future plugin);

}
