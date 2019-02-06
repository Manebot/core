package com.github.manevolent.jbot.chat;

import com.github.manevolent.jbot.platform.Platform;

/**
 * Provides chat connections.
 */
public interface ChatProvider {

    /**
     * Gets the platform associated with this chat provider.
     * @return Platform instance.
     */
    Platform getPlatform();

    /**
     * Gets a chat by ID.
     * @param id ID.
     * @return Chat instance, if one is found.
     */
    Chat get(String id);

}
