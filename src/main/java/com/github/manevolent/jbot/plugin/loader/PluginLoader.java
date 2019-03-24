package com.github.manevolent.jbot.plugin.loader;

import com.github.manevolent.jbot.artifact.Artifact;
import com.github.manevolent.jbot.artifact.ArtifactDependency;
import com.github.manevolent.jbot.artifact.LocalArtifact;
import com.github.manevolent.jbot.plugin.Plugin;
import com.github.manevolent.jbot.plugin.PluginLoadException;

import java.io.FileNotFoundException;
import java.util.Collection;

public interface PluginLoader {

    /**
     * Loads a plugin based on its local artifact.
     * @param artifact Plugin artifact to load.
     * @return Plugin instance.
     */
    Plugin load(LocalArtifact artifact) throws PluginLoadException, FileNotFoundException;

    /**
     * Finds a list of plugin artifact dependencies for a given plugin artifact.
     * @param artifact Plugin artifact to find dependencies for.
     * @return immutable collection of plugin dependencies.
     */
    Collection<ArtifactDependency> getDependencies(Artifact artifact);

}
