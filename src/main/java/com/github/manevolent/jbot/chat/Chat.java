package com.github.manevolent.jbot.chat;

import com.github.manevolent.jbot.platform.Platform;
import com.github.manevolent.jbot.user.User;

import java.util.Collection;

public interface Chat {

    /**
     * Gets the platform associated with facilitating this conversation.
     * @return Platform instance.
     */
    Platform getPlatform();

    /**
     * Gets this conversation's ID.
     *
     * Chat IDs should follow the format:
     *  platform:scope:internal
     *
     * Where,
     *  platform is the Platform instance name.
     *  scope is a Platform-specific scope.
     *  internal is a scope-specific identifier.
     *
     * @return Chat ID.
     */
    String getId();

    /**
     * Gets the user-friendly name of this chat.
     * @return chat user-friendly name.
     */
    default String getName() {
        return getId();
    }

    /**
     * Finds if this conversation is connected to the plugin's associated conversation resource.
     * @return true if the conversation is connected to a resource, false otherwise.
     */
    boolean isConnected();

    /**
     * Finds if this chat should buffer its output.
     *
     * Buffered output will send individual command response lines in "chunks" for most commands.
     *
     * @return true if buffering should take place, false otherwise.
     */
    default boolean isBuffered() {
        return true;
    }

    /**
     * Removes, or kicks, a user from the conversation.
     * @param user User to remove.
     */
    void remove(User user);

    /**
     * Adds a user to this conversation.
     * @param user User to add.
     */
    void add(User user);

    /**
     * Gets the last <i>n</i> messages in this chat.
     * @param max Maximum messages to return.
     * @return ChatMessage collection of previous messages.
     */
    Collection<ChatMessage> getLastMessages(int max);

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
     * Sets the name, or title, of this conversation.
     * @param name Conversation title.
     * @throws UnsupportedOperationException
     */
    void setName(String name) throws UnsupportedOperationException;

    boolean canChangeTypingStatus();

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
    default boolean canSendMessages() { return true; }

    /**
     * Finds if the bot can receive messages in this conversation.
     * @return true if the bot receive send messages, false otherwise.
     */
    default boolean canReceiveMessages() { return true; }

}
