package com.github.manevolent.jbot.conversation;

import com.github.manevolent.jbot.entity.Entity;
import com.github.manevolent.jbot.platform.Platform;
import com.github.manevolent.jbot.user.User;

import java.util.Collection;

public interface Conversation extends Entity {

    /**
     * Gets the platform associated with facilitating this conversation.
     * @return Platform instance.
     */
    Platform getPlatform();

    /**
     * Gets this conversation's ID.
     *
     * Conversation IDs should follow the format:
     *  platform:scope:internal
     *
     * Where,
     *  platform is the Platform instance name.
     *  scope is a Platform-specific scope.
     *  internal is a scope-specific identifier.
     *
     * @return Conversation ID.
     */
    String getId();

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
    default boolean isMember(User user) {
        return getMembers().contains(user);
    }

    /**
     * Gets the members for this conversation.
     * @return immutable collection of members in this conversation.
     */
    Collection<User> getMembers();

    /**
     * Finds if the conversation is private. Private conversations are conversations which are typically a direct
     * conversation between the Bot and a single User.
     * @return true if the conversation is private.
     */
    boolean isPrivate();

    /**
     * Sends a text message to the conversation.
     * @param message Text message to send.
     */
    void sendMessage(String message);

    /**
     * Sets the Bot's typing status on this conversation.
     * @param typing true to begin typing, false otherwise.
     */
    void setTyping(boolean typing);

    /**
     * Finds if the bot is typing in this conversation.
     * @return true if the bot is typing, false otherwise.
     */
    boolean isTyping();

    /**
     * Finds if the bot can send messages in this conversation.
     * @return true if the bot can send messages, false otherwise.
     */
    boolean canSendMessages();

    /**
     * Finds if the bot can receive messages in this conversation.
     * @return true if the bot receive send messages, false otherwise.
     */
    boolean canReceiveMessages();

}
