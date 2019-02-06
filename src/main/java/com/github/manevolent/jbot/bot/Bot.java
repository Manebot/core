package com.github.manevolent.jbot.bot;

import com.github.manevolent.jbot.artifact.ArtifactIdentifier;
import com.github.manevolent.jbot.artifact.ArtifactRepository;
import com.github.manevolent.jbot.artifact.Version;
import com.github.manevolent.jbot.event.EventManager;
import com.github.manevolent.jbot.platform.Platform;
import com.github.manevolent.jbot.plugin.Plugin;
import com.github.manevolent.jbot.plugin.loader.PluginLoader;
import com.github.manevolent.jbot.plugin.loader.PluginLoaderRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public interface Bot {

    /**
     * Gets a list of platforms registered to the system.
     * @return list of Platforms.
     */
    List<Platform> getPlatforms();

    /**
     * Gets the state of the bot.
     * @return Bot state.
     */
    BotState getState();

    /**
     * Gets if the bot is running.
     * @return true if the bot is running, false otherwise.
     */
    default boolean isRunning() {
        return getState() == BotState.RUNNING;
    }

    /**
     * Gets if the bot is stopping.
     * @return true if the bot is stopping, false otherwise.
     */
    default boolean isStopping() {
        return getState() == BotState.STOPPING;
    }

    /**
     * Gets the time at which the bot was started.
     * @return Bot startup date.
     */
    Date getStarted();

    /**
     * Gets the plugin loader registry
     * @return plugin loader registry.
     */
    PluginLoaderRegistry getPluginLoaderRegistry();

    /**
     * Gets the system plugin/artifact loader.
     * @return PluginLoader instance.
     */
    default PluginLoader getPluginLoader() {
        return getPluginLoaderRegistry();
    }

    /**
     * Gets the global repository.
     * @return global repository.
     */
    ArtifactRepository getRepostiory();

    /**
     * Gets a list of all plugins loaded into the system.
     * @return loaded plugins.
     */
    Collection<Plugin> getPlugins();

    /**
     * Gets the system event manager.
     * @return event manager.
     */
    EventManager getEventManager();

    /**
     * Finds a previously loaded plugin by its artifact identifier.
     * @param id artifact identifier
     * @return Plugin instance.
     */
    default Plugin getPlugin(ArtifactIdentifier id) {
        return getPlugins().stream()
                .filter(x -> x.getArtifact().getIdentifier().equals(id))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("plugin not found"));
    }

    /**
     * Gets a list of enabled plugins.
     * @return enabled plugins.
     */
    default Collection<Plugin> getEnabledPlugins() {
        return Collections.unmodifiableCollection(
                getPlugins().stream().filter(x -> x != null && x.isEnabled()).collect(Collectors.toList())
        );
    }

    /**
     * Gets a list of disabled plugins.
     * @return disabled plugins.
     */
    default Collection<Plugin> getDisabledPlugins() {
        return Collections.unmodifiableCollection(
                getPlugins().stream().filter(x -> x != null && !x.isEnabled()).collect(Collectors.toList())
        );
    }

    /**
     * Starts the bot.
     *
     * @throws IllegalAccessException if the caller does not have the appropriate permission.
     */
    void start() throws IllegalAccessException;

    /**
     * Stops the bot.
     *
     * @throws IllegalAccessException if the caller does not have the appropriate permission.
     */
    void stop() throws IllegalAccessException;

    /**
     * Gets the version of the bot.
     * @return Version.
     */
    Version getVersion();

}
