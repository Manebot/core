package com.github.manevolent.jbot.plugin.loader;

import com.github.manevolent.jbot.artifact.LocalArtifact;
import com.github.manevolent.jbot.plugin.Plugin;
import com.github.manevolent.jbot.plugin.PluginLoadException;

import java.io.FileNotFoundException;

public interface PluginLoader {

    /**
     * Loads a plugin based on its local artifact.
     * @param artifact Plugin artifact to load.
     * @return Plugin instance.
     */
    Plugin load(LocalArtifact artifact) throws PluginLoadException, FileNotFoundException;

}
