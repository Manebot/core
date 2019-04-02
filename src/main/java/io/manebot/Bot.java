package io.manebot;

import io.manebot.chat.ChatDispatcher;
import io.manebot.command.CommandDispatcher;
import io.manebot.conversation.ConversationProvider;
import io.manebot.database.Database;
import io.manebot.event.EventDispatcher;
import io.manebot.platform.Platform;
import io.manebot.plugin.PluginManager;
import io.manebot.user.UserManager;
import io.manebot.user.UserRegistration;

import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;

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
     * Gets the system default user registration.
     * @return default user registration.
     */
    UserRegistration getDefaultUserRegistration();

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
     * Gets the system database.
     * @return System database.
     */
    Database getSystemDatabase();

    /**
     * Gets the time at which the bot was started.
     * @return Bot startup date.
     */
    Date getStarted();

    /**
     * Gets the plugin loader registry
     * @return plugin loader registry.
     */
    PluginManager getPluginManager();

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
     * Gets the system chat dispatcher.
     * @return chat dispatcher.
     */
    ChatDispatcher getChatDispatcher();

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
     * Registers a state listener into the bot.
     * @param listener state listener instance.
     * @return true if the listener was registered, false otherwise.
     */
    boolean registerStateListener(Consumer<BotState> listener);

    /**
     * Unregisters a state listener from the bot.
     * @param listener state listener instance.
     * @return true if the listener was removed, false otherwise.
     */
    boolean unregisterStateListener(Consumer<BotState> listener);

}
