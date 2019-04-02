package io.manebot.plugin.loader;

import io.manebot.artifact.Artifact;
import io.manebot.artifact.ArtifactDependency;
import io.manebot.artifact.ArtifactNotFoundException;
import io.manebot.artifact.LocalArtifact;
import io.manebot.plugin.Plugin;
import io.manebot.plugin.PluginLoadException;

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
    Collection<ArtifactDependency> getDependencies(Artifact artifact) throws ArtifactNotFoundException;

}
