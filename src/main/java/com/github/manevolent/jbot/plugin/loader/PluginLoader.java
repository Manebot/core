package com.github.manevolent.jbot.plugin.loader;

import com.github.manevolent.jbot.artifact.LocalArtifact;
import com.github.manevolent.jbot.plugin.Plugin;
import com.github.manevolent.jbot.plugin.PluginLoadException;

import java.io.File;
import java.io.FileNotFoundException;

public interface PluginLoader {

    /**
     * Loads a plugin based on its root library file.
     * @param file Plugin file to load.
     * @return Plugin instance.
     */
    Plugin load(File file) throws PluginLoadException, FileNotFoundException;

    /**
     * Loads a plugin based on its local artifact.
     * @param artifact Plugin artifact to load.
     * @return Plugin instance.
     */
    default Plugin load(LocalArtifact artifact) throws PluginLoadException, FileNotFoundException {
        return load(artifact.getFile());
    }

}
