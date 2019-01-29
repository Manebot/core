package com.github.manevolent.jbot.bot;

import com.github.manevolent.jbot.artifact.ArtifactRepository;
import com.github.manevolent.jbot.artifact.LocalArtifactRepository;
import com.github.manevolent.jbot.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

public interface Bot {

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
     * Gets the global repository.
     * @return global repository.
     */
    ArtifactRepository getRepostiory();

    /**
     * Gets the local artifact repository.
     * @return local artifact repository.
     */
    LocalArtifactRepository getLocalRepository();

    /**
     * Gets a list of all plugins loaded into the system.
     * @return loaded plugins.
     */
    Collection<Plugin> getPlugins();

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

}
