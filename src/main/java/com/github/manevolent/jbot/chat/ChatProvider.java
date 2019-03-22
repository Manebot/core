package com.github.manevolent.jbot.chat;

import com.github.manevolent.jbot.user.User;

import java.util.List;

/**
 * Provides chat connections.
 */
public interface ChatProvider {

    /**
     * Gets the conversations this platform provides.
     *
     * @return immutable list of presently provided Conversations.
     */
    List<Chat> getChats();

    /**
     * Gets a conversation by a specific Id for this platform.
     *
     * @param id Chat Id,
     * @return Chat instance if one exists, null otherwise.
     */
    default Chat getChatById(String id) {
        return getChats().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Gets a private message conversation for a specific user.
     *
     * @param user User to get a PM conversation for.
     * @return Private message conversation for the specified user, null if one cannot be found.
     */
    default Chat getPrivateChat(User user) {
        return getChats().stream().filter(x -> x.isParticipant(user) && x.isPrivate()).findFirst().orElse(null);
    }

}
