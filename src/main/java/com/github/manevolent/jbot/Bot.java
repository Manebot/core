package com.github.manevolent.jbot;

import com.github.manevolent.jbot.artifact.ArtifactIdentifier;
import com.github.manevolent.jbot.artifact.ArtifactRepository;
import com.github.manevolent.jbot.command.CommandDispatcher;
import com.github.manevolent.jbot.conversation.ConversationProvider;
import com.github.manevolent.jbot.event.EventDispatcher;
import com.github.manevolent.jbot.platform.Platform;
import com.github.manevolent.jbot.plugin.Plugin;
import com.github.manevolent.jbot.plugin.loader.PluginLoader;
import com.github.manevolent.jbot.plugin.loader.PluginLoaderRegistry;
import com.github.manevolent.jbot.user.UserManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface Bot {

    /**
     * Gets the version of the bot.
     * @return Version.
     */
    Version getVersion();

    /**
     * Gets the API version of the bot.
     * @return API Version.
     */
    Version getApiVersion();

    /**
     * Gets a list of platforms registered to the system.
     * @return list of Platforms.
     */
    Collection<Platform> getPlatforms();

    /**
     * Finds a platform by its ID.
     * @param id Platform ID to find.
     * @return Platform instance if a platform is found, null otherwise.
     */
    default Platform getPlatformById(String id) {
        return getPlatforms().stream().filter(platform -> platform.getId().equals(id)).findFirst().orElse(null);
    }

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
     * Gets the system command manager.
     * @return command manager.
     */
    EventDispatcher getEventDispatcher();

    /**
     * Gets the system command dispatcher.
     * @return command dispatcher.
     */
    CommandDispatcher getCommandDispatcher();

    /**
     * Gets the system user manager.
     * @return user manager.
     */
    UserManager getUserManager();

    /**
     * Gets the system conversation provider.
     * @return conversation provider.
     */
    ConversationProvider getConversationProvider();

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

    boolean registerStateListener(Consumer<BotState> listener);
    boolean unregisterStateListener(Consumer<BotState> listener);

}
