package com.github.manevolent.jbot.chat;

import com.github.manevolent.jbot.platform.Platform;
import com.github.manevolent.jbot.user.User;
import com.github.manevolent.jbot.user.UserAssociation;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

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
    default void removeUser(User user) {
        for (UserAssociation platformAssociation : user.getAssociations(getPlatform())) {
            removeMember(platformAssociation.getPlatformId());
        }
    }

    /**
     * Removes, or kicks, a platform-specific user from this conversation.
     * @param platformId Platform-specific Id to remove.
     */
    void removeMember(String platformId);

    /**
     * Adds a user to this conversation.
     * @param user User to add.
     */
    default void addUser(User user) {
        for (UserAssociation platformAssociation : user.getAssociations(getPlatform())) {
            addMember(platformAssociation.getPlatformId());
        }
    }

    /**
     * Adds a platform-specific user to this conversation.
     * @param platformId platform-specific Id.
     */
    void addMember(String platformId);

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
     * Gets the users in this conversation.
     *
     * @return immutable collection of users in this conversation.
     */
    default Collection<User> getMembers() {
        return Collections.unmodifiableCollection(
                getMemberAssociations().stream().map(UserAssociation::getUser).collect(Collectors.toList())
        );
    }

    /**
     * Gets the member user associations for this conversation.
     *
     * @return immutable collection of member user associations in this conversation.
     */
    default Collection<UserAssociation> getMemberAssociations() {
        return Collections.unmodifiableCollection(
                getPlatformMemberIds().stream()
                .map(x -> getPlatform().getConnection().getUserAssocation(x))
                .filter(x -> x.getUser() != null)
                .collect(Collectors.toList())
        );
    }

    /**
     * Gets a raw immutable collection of members, in the form of platform-specific Ids.
     *
     * @return immutable collection of all members, in the form of platform-specific Ids.
     */
    Collection<String> getPlatformMemberIds();

    /**
     * Finds if the conversation is private. Private conversations are conversations which are typically a direct
     * conversation between the Bot and a single User.
     * @return true if the conversation is private.
     */
    boolean isPrivate();

    /**
     * Sends a text message to the conversation.
     * @param message PlainText message to send.
     */
    void sendMessage(String message);

    /**
     * Sends a text message to the conversation.
     * @param message PlainText message to send.
     */
    default void sendMessage(ChatMessage message) {
        sendMessage(message.getMessage());
    }

    /**
     * Sends a rich message to the conversation.
     * @param message Rich message to send.
     * @throws UnsupportedOperationException if the conversation does not support rich messaging.
     */
    void sendMessage(RichChatMessage message) throws UnsupportedOperationException;

    /**
     * Sets the name, or title, of this conversation.
     * @param name Conversation title.
     * @throws UnsupportedOperationException
     */
    void setTitle(String name) throws UnsupportedOperationException;

    /**
     * Finds if it is possible to change the typing status in this chat.
     * @return true if typing status can be changed, false otherwise.
     */
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
     * Finds if the bot can send rich messages in this conversation.
     * @return true if the bot can send rich messages, false otherwise.
     */
    default boolean canSendRichMessages() { return false; }

    /**
     * Finds if the bot can receive messages in this conversation.
     * @return true if the bot receive send messages, false otherwise.
     */
    default boolean canReceiveMessages() { return true; }

    /**
     * Finds if the bot can send emoji in this conversation.
     * @return true if the bot receive send emoji, false otherwise.
     */
    default boolean canSendEmoji() { return false; }

    /**
     * Finds if the bot can send a specific rich chat element type in this conversation.
     * @param elementClass Element class type
     * @return true if the bot can send the specified rich element type in this chat, false otherwise.
     */
    default boolean canSendRichElement(Class<? extends RichChatMessage.Element> elementClass) {
        return canSendRichMessages();
    }

}
