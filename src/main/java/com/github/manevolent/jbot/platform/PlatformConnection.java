package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.chat.Chat;
import com.github.manevolent.jbot.user.User;
import com.github.manevolent.jbot.user.UserAssociation;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public interface PlatformConnection {

    /**
     * Calls to connect to the platform.  This process is called automatically upon registration.
     */
    void connect();

    /**
     * Calls to disconnect from the platform.
     */
    default void disconnect() {
        throw new UnsupportedOperationException();
    }

    /**
     * Finds if the platform connection holds a connection.
     * @return true if a connection is held, false otherwise.
     */
    default boolean isConnected() {
        return true;
    }

    /**
     * Gets a list of chats associated with this platform.
     * @return collection of Chat instances associated with this platform.
     */
    Collection<Chat> getChats();

    /**
     * Gets a chat by its user-specific ID.
     * @param id Chat ID to search for.
     * @return Chat instance if found, null otherwise.
     */
    default Chat getChatById(String id) {
        return getChats().stream()
                .filter(chat -> chat.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Gets an immutable collection of chats where the specified user is a member.
     *
     * @param member User member to search for.
     * @return Chat instances whose members contain the specified User.
     */
    default Collection<Chat> getChatsByMember(User member) {
        return Collections.unmodifiableCollection(
                getChats().stream()
                    .filter(chat -> chat.isMember(member))
                    .collect(Collectors.toList())
        );
    }

}
