package com.github.manevolent.jbot.conversation;

import com.github.manevolent.jbot.artifact.Artifact;
import com.github.manevolent.jbot.user.User;

import java.util.Collection;

public interface Conversation {
    /**
     * Gets this conversation's ID.
     *
     * Conversation IDs should follow the format:
     *  plugin:scope:internal
     *
     * @return Conversation ID.
     */
    String getId();

    /**
     * Gets the plugin associated with this conversation.
     * @return Artifact instance.
     */
    Artifact getPlugin();

    /**
     * Finds if this conversation is connected to the plugin's associated conversation resource.
     * @return true if the conversation is connected to a resource, false otherwise.
     */
    boolean isConnected();

    /**
     * Kicks a user from the conversation.
     * @param user
     */
    void kick(User user);

    /**
     * Finds if a given user is a member of this conversation.
     * @param user User instance to search for in this conversation.
     * @return true if the user is a member of this conversation, false otherwise.
     */
    boolean isMember(User user);

    /**
     *
     * @return
     */
    Collection<User> getMembers();

    boolean isPrivate();

    void sendMessage(String message);

    void setTyping(boolean typing);

    boolean canSendMessages();
}
