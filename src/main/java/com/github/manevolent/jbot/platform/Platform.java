package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.chat.ChatProvider;

/**
 * Platforms are the structure of the bot that
 */
public interface Platform extends ChatProvider {

    /**
     * Gets the platform's internal unique ID.
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
    boolean isConnected();

}
