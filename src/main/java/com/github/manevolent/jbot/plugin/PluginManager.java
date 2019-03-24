package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.artifact.*;
import com.github.manevolent.jbot.plugin.loader.PluginLoaderRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * The <B>PluginManager</B> handles loading and unload plugins from the system.
 */
public interface PluginManager {

    /**
     * Gets the plugin and library repository.
     * @return global repository.
     */
    ArtifactRepository getRepostiory();

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
     * Gets an immutable collection of registered plugins.
     * @return Plugin collection.
     */
    Collection<PluginRegistration> getPlugins();

    /**
     * Finds all dependencies for a specific plugin artifact identifier.
     * @param identifier Plugin ArtifactIdentifier to find dependencies for.
     * @return immutable collection of artifact dependencies.
     */
    Collection<ArtifactDependency> getDependencies(ArtifactIdentifier identifier)
            throws ArtifactRepositoryException, ArtifactNotFoundException;

    /**
     * Adds a plugin to the system.
     *
     * @param artifactIdentifier Plugin ArtifactIdentifier to add.
     * @return PluginRegistration instance that was installed.
     * @throws IllegalArgumentException if there is a problem obtaining a plugin from the given artifact identifier
     * @throws PluginLoadException if there is a problem loading the plugin
     */
    PluginRegistration install(ArtifactIdentifier artifactIdentifier)
            throws IllegalArgumentException, PluginLoadException;

    /**
     * Removes a plugin from the system.
     *
     * @param registration Plugin registration instance to remove.
     * @return true if the plugin was removed, false otherwise.
     */
    boolean uninstall(PluginRegistration registration);

    /**
     * Finds a previously installed plugin by its artifact identifier.
     *
     * @param id artifact identifier
     * @return Plugin instance if one is found, null otherwise.
     */
    default PluginRegistration getPlugin(ArtifactIdentifier id) {
        return getPlugins()
                .stream()
                .filter(x -> x.getIdentifier().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a previously installed plugin by its artifact identifier.
     *
     * @param id manifest identifier
     * @return Plugin instance if one is found, null otherwise.
     */
    default PluginRegistration getPlugin(ManifestIdentifier id) {
        return getPlugins()
                .stream()
                .filter(x -> x.getIdentifier().isManifest(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a list of enabled plugins.
     * @return enabled plugins.
     */
    default Collection<Plugin> getEnabledPlugins() {
        return Collections.unmodifiableCollection(
                getLoadedPlugins().stream()
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
                getLoadedPlugins().stream()
                        .filter(x -> x != null && !x.isEnabled())
                        .collect(Collectors.toList())
        );
    }

    /**
     * Resolves an identifier by its friendly name, or returns an artifact identifier otherwise.
     * @param friendlyName Friendly name.
     * @return ArtifactIdentifier instance.
     */
    ArtifactIdentifier resolveIdentifier(String friendlyName);

}
