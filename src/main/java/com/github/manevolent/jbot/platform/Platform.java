package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.conversation.Conversation;
import com.github.manevolent.jbot.plugin.Plugin;
import com.github.manevolent.jbot.user.User;

import java.util.List;

/**
 * Platforms are the structure of the bot that
 */
public interface Platform {

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

    /**
     * Gets the plugin associated with providing this platform.
     *
     * @return Platform plugin.
     */
    Plugin getPlugin();

    /**
     * Gets the conversations this platform provides.
     *
     * @return immutable list of presently provided Conversations.
     */
    List<Conversation> getConversations();

    /**
     * Gets a conversation by a specific Id for this platform.
     *
     * @param id Conversation Id,
     * @return Conversation instance if one exists, null otherwise.
     */
    default Conversation getConversationById(String id) {
        return getConversations().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Gets a private message conversation for a specific user.
     *
     * @param user User to get a PM conversation for.
     * @return Private message conversation for the specified user, null if one cannot be found.
     */
    default Conversation getPMConvesation(User user) {
        return getConversations().stream().filter(x -> x.isMember(user) && x.isPrivate()).findFirst().orElse(null);
    }

}
