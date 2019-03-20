package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.artifact.ArtifactIdentifier;
import com.github.manevolent.jbot.artifact.LocalArtifact;
import com.github.manevolent.jbot.plugin.loader.PluginLoader;
import com.github.manevolent.jbot.plugin.loader.PluginLoaderRegistry;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public interface PluginManager extends PluginLoader {

    /**
     * Gets this PluginManager's PluginLoaderRegistry instance, used to load plugins from LocalArtifacts.
     * @return PluginLoaderRegistry instance.
     */
    PluginLoaderRegistry getLoaderRegistry();

    /**
     * Gets an immutable collection of loaded plugins.
     * @return Plugin collection.
     */
    Collection<Plugin> getLoadedPlugins();

    /**
     * Gets an immutable collection of recognized plugins.
     * @return Plugin collection.
     */
    Collection<Plugin> getPlugins();

    /**
     * Unloads the specified plugin.
     * @param plugin Plugin to unload.
     */
    void unload(Plugin plugin);

    /**
     * Helper method.  Loads a plugin based on the specified associated artifact.
     * @param artifact Local artifact/file to load.
     * @return Plugin instance.
     * @throws IllegalArgumentException if the artifact's file extension is not recognized.
     * @throws PluginLoadException if there is a problem loading the plugin file associated with the artifcat.
     * @throws FileNotFoundException if the plugin artifact or one of its artifact dependencies does not exist.
     */
    @Override
    default Plugin load(LocalArtifact artifact)
            throws IllegalArgumentException, PluginLoadException, FileNotFoundException {
        return getLoaderRegistry().getLoader(artifact.getFile()).load(artifact);
    }

    /**
     * Finds a previously loaded plugin by its artifact identifier.
     * @param id artifact identifier
     * @return Plugin instance if one is found, null otherwise.
     */
    default Plugin getPlugin(ArtifactIdentifier id) {
        return getPlugins().stream()
                .filter(x -> x.getArtifact().getIdentifier().equals(id))
                .findFirst().orElse(null);
    }

    /**
     * Gets a list of enabled plugins.
     * @return enabled plugins.
     */
    default Collection<Plugin> getEnabledPlugins() {
        return Collections.unmodifiableCollection(
                getPlugins().stream()
                        .filter(x -> x != null && x.isEnabled())
                        .collect(Collectors.toList())
        );
    }

    /**
     * Gets a list of disabled plugins.
     * @return disabled plugins.
     */
    default Collection<Plugin> getDisabledPlugins() {
        return Collections.unmodifiableCollection(
                getPlugins().stream()
                        .filter(x -> x != null && !x.isEnabled())
                        .collect(Collectors.toList())
        );
    }

}
