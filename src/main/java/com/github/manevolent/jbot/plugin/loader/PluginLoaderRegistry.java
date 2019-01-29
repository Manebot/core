package com.github.manevolent.jbot.plugin.loader;

import com.github.manevolent.jbot.artifact.Artifact;
import com.github.manevolent.jbot.artifact.LocalArtifact;
import com.github.manevolent.jbot.plugin.Plugin;
import com.github.manevolent.jbot.plugin.PluginLoadException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PluginLoaderRegistry implements PluginLoader {
    private final Map<String, PluginLoader> loaderMap = new LinkedHashMap<>();

    /**
     * Gets a collection of all currently registered plugin loaders.
     * @return Plugin loader list.
     */
    public Collection<PluginLoader> getRegisteredLoaders() {
        return Collections.unmodifiableCollection(loaderMap.values());
    }

    /**
     * Registers a plugin loader for a given extension.
     * @param extension extension to map the loader to.
     * @param loader loader to map to the extension.
     */
    public void registerLoader(String extension, PluginLoader loader) {
        if (loaderMap.containsKey(extension))
            throw new IllegalArgumentException("loader for " + extension + " exists");

        loaderMap.put(extension, loader);
    }

    /**
     * Unregisters a plugin loader from the system.
     * @param extension extension to unmap a loader from.
     * @return removed PluginLoader instance.
     */
    public PluginLoader unregisterLoader(String extension) {
        return loaderMap.remove(extension);
    }

    /**
     * Gets a plugin loader by the specified file.
     * @param file File to get an associated plugin loader for.
     * @return PluginLoader instance responsible for the specified file.
     * @throws IllegalArgumentException if the file extension is not recognized.
     */
    public PluginLoader getLoader(File file) throws IllegalArgumentException {
        String extension = "";
        String[] fileParts = file.getName().split("[.]");
        if (fileParts.length > 1) extension = fileParts[fileParts.length - 1].toLowerCase();

        if (!loaderMap.containsKey(extension))
            throw new IllegalArgumentException(extension);

        return getLoader(extension);
    }

    /**
     * Gets a plugin loader by the specified extension.
     * @param extension Extension to get an associated plugin loader for.
     * @return PluginLoader instance responsible for the specified extension.
     * @throws IllegalArgumentException if the file extension is not recognized.
     */
    public PluginLoader getLoader(String extension) throws IllegalArgumentException {
        return loaderMap.get(extension);
    }

    /**
     * Helper method.  Loads a plugin based on the specified associated artifact.
     * @param artifact Local artifact/file to load.
     * @return Plugin instance.
     * @throws IllegalArgumentException if the artifact's file extension is not recognized.
     * @throws PluginLoadException if there is a problem loading the plugin file associated with the artifcat.
     * @throws FileNotFoundException if the plugin artifact or one of its artifact dependencies does not exist.
     */
    @Override
    public Plugin load(LocalArtifact artifact)
            throws IllegalArgumentException, PluginLoadException, FileNotFoundException {
        return getLoader(artifact.getFile()).load(artifact);
    }
}
