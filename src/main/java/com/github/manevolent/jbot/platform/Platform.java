package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.plugin.Plugin;

/**
 * Platforms are the structure of the bot that represent individual chatting platforms, such as Discord, Skype,
 * and so on.
 */
public interface Platform {

    /**
     * Gets the runtime registration associated with this platform.
     * @return Platform runtime registration.
     */
    PlatformRegistration getRegistration();

    /**
     * Gets the platform's internal unique ID.
     *
     * @return platform ID.
     */
    String getId();

    /**
     * Finds if the platform is currently connected.
     *
     * @return true if the platform is connected, false otherwise.
     */
    default boolean isConnected() {
        return getConnection() != null && getConnection().isConnected();
    }

    /**
     * Gets the current platform connection instance for this platform.
     * @return PlatformConnection instance of this platform.
     */
    default PlatformConnection getConnection() {
        PlatformRegistration registration = getRegistration();
        if (registration == null) return null;
        else return registration.getConnection();
    }

    /**
     * Gets the current plugin instance for this platform.
     * @return Plugin instance of this platform.
     */
    default Plugin getPlugin() {
        PlatformRegistration registration = getRegistration();
        if (registration == null) return null;
        else return registration.getPlugin();
    }

}
