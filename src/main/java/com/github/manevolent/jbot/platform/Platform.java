package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.plugin.Plugin;

/**
 * Platforms are the structure of the bot that represent individual chatting platforms, such as Discord, Skype,
 * and so on.
 */
public interface Platform {

    /**
     * Gets the plugin associated with the ownership of this platform.
     * @return Plugin instance.
     */
    Plugin getPlugin();

    /**
     * Gets the platform's internal unique ID.
     *
     * @return platform ID.
     */
    String getId();

    /**
     * Gets the platform's user-friendly name.
     *
     * @return platform name.
     */
    String getName();

    /**
     * Finds if the platform is currently connected.
     *
     * @return true if the platform is connected, false otherwise.
     */
    default boolean isConnected() {
        return getConnection() != null;
    }

    /**
     * Gets the current platform connection instance for this platform.
     * @return PlatformConnection instance of this platform.
     */
    PlatformConnection getConnection();

}
