package io.manebot.chat;

import io.manebot.platform.Platform;
import io.manebot.platform.PlatformUser;

import java.util.Collection;

public interface Community {

    /**
     * Gets the platform-specific ID for this community.
     * @return ID
     */
    String getId();

    /**
     * Gets the platform-specific user-friendly name for this community.
     * @return Community name.
     */
    default String getName() {
        return getId();
    }

    /**
     * Attempts to set the platform-specific name for this community.
     * @param name name to set.
     * @throws UnsupportedOperationException if the operation is not supported.
     */
    void setName(String name) throws UnsupportedOperationException;

    /**
     * Gets the platform associated with this community.
     * @return Platform.
     */
    Platform getPlatform();

    /**
     * Gets an immutable collection of chat IDs associated with this community.
     * @return Chat collection.
     */
    Collection<String> getChatIds();

    /**
     * Gets an immutable collection of chats associated with this community.
     * @return Chat collection.
     */
    Collection<Chat> getChats();

    /**
     * Gets an immutable collection of platform user IDs associated with this community.
     * @return Platform user ID collection.
     */
    Collection<String> getPlatformUserIds();

    /**
     * Gets an immutable collection of platform users associated with this community.
     * @return Platform user collection.
     */
    Collection<PlatformUser> getPlatformUsers();

    /**
     * Gets the default chat for this community.
     * @return default chat.
     */
    Chat getDefaultChat();

    /**
     * Finds if the community is connected.
     * @return true if the community is connected, false otherwise.
     */
    default boolean isConnected() {
        return getPlatform().isConnected();
    }

}
