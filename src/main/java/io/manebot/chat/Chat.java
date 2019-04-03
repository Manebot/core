package io.manebot.chat;

import io.manebot.platform.Platform;
import io.manebot.platform.PlatformConnection;
import io.manebot.platform.PlatformUser;
import io.manebot.user.User;
import io.manebot.user.UserAssociation;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface Chat {

    /**
     * Gets the platform connection associated with facilitating this conversation.
     * @return PlatformConnection instance.
     */
    default PlatformConnection getPlatformConnection() {
        return getPlatform().getConnection();
    }

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
     * Sets the name, or title, of this conversation.
     * @param name Conversation title.
     * @throws UnsupportedOperationException
     */
    void setName(String name) throws UnsupportedOperationException;

    /**
     * Sets the chat's topic.
     * @param topic chat topic to set.
     * @throws UnsupportedOperationException if chat topics are not supported by the platform.
     */
    default void setTopic(String topic) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the current topic of the chat.
     * @return current topic if set, null otherwise.
     */
    default String getTopic() {
        return null;
    }

    /**
     * Finds if this conversation is connected to the plugin's associated conversation resource.
     * @return true if the conversation is connected to a resource, false otherwise.
     */
    boolean isConnected();

    /**
     * Finds if this chat should buffer its output.
     *
     * Buffered output will get individual command response lines in "chunks" for most commands.
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
     * Removes, or kicks, a platform-specific user from this conversation.
     * @param user Platform-specific Id to remove.
     */
    default void removeMember(PlatformUser user) {
        removeMember(user.getId());
    }

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
     * Adds a platform-specific user to this conversation.
     * @param user platform-specific user to add.
     */
    default void addMember(PlatformUser user) {
        addMember(user.getId());
    }

    /**
     * Gets the last <i>n</i> messages in this chat.
     * @param max Maximum messages to return.
     * @return ChatMessage collection of previous messages.
     */
    Collection<ChatMessage> getLastMessages(int max);

    /**
     * Finds if a given user is a member of this conversation.
     * @param user user instance to search for in this conversation.
     * @return true if the user is a member of this conversation, false otherwise.
     */
    default boolean isParticipant(User user) {
        return getUsers().contains(user);
    }

    /**
     * Finds if a given platform user is a member of this conversation.
     * @param user platform user instance to search for in this conversation.
     * @return true if the platform user is a member of this conversation, false otherwise.
     */
    default boolean isParticipant(PlatformUser user) {
        return getPlatformUsers().contains(user);
    }

    /**
     * Gets the users in this conversation.
     *
     * @return immutable collection of users in this conversation.
     */
    default Collection<User> getUsers() {
        return Collections.unmodifiableCollection(
                getUserAssociations().stream()
                        .map(UserAssociation::getUser)
                        .distinct()
                        .collect(Collectors.toList())
        );
    }

    /**
     * Gets platform-specific user participants.
     * @return PlatformUser instances.
     */
    Collection<PlatformUser> getPlatformUsers();

    /**
     * Gets the member user associations for this conversation.
     *
     * @return immutable collection of member user associations in this conversation.
     */
    default Collection<UserAssociation> getUserAssociations() {
        return Collections.unmodifiableCollection(
                getPlatformUserIds().stream()
                        .map(platformUserId -> getPlatform().getUserAssocation(platformUserId))
                        .filter(Objects::nonNull)
                        .filter(association -> association.getUser() != null)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Gets a raw immutable collection of members, in the form of platform-specific Ids.
     *
     * @return immutable collection of all members, in the form of platform-specific Ids.
     */
    default Collection<String> getPlatformUserIds() {
        return getPlatformUsers().stream().map(PlatformUser::getId).collect(Collectors.toList());
    }

    /**
     * Finds if the conversation is private. Private conversations are conversations which are typically a direct
     * conversation between the Bot and a single User.
     * @return true if the conversation is private.
     */
    boolean isPrivate();

    /**
     * Sends a simple text message to the conversation.
     * @param message PlainText message to get.
     * @return ChatMessage instance of the created message.
     */
    default ChatMessage sendRawMessage(String message) {
        return sendFormattedMessage(format -> format.appendRaw(message));
    }

    /**
     * Sends a simple text message to the conversation.
     * @param message PlainText message to get.
     * @return ChatMessage instance of the created message.
     */
    default ChatMessage sendMessage(String message) {
        return sendFormattedMessage(format -> format.append(message));
    }

    /**
     * Adds a message to the command buffer or sends a message.
     * @param function function to provide a formatted message.
     */
    default ChatMessage sendFormattedMessage(Consumer<TextBuilder> function) {
        return sendMessage(builder -> builder.message(function));
    }

    /**
     * Sends a simple text message to the conversation.
     * @param function function providing a platform-specific chat message to get.
     * @return ChatMessage instance of the created message.
     */
    ChatMessage sendMessage(Consumer<ChatMessage.Builder> function);

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
     * Finds if the bot can get simple text messages in this conversation.
     * @return true if the bot can get messages, false otherwise.
     */
    default boolean canSendMessages() { return true; }

    /**
     * Finds if this chat can send embedded messages in this conversation.
     * @return true if the bot can get rich messages, false otherwise.
     */
    default boolean canSendEmbeds() { return false; }

    /**
     * Gets the formatter associated with this chat.  <b>ChatFormatter</b>s are used to apply rich styles to text.
     * @return ChatFormatter instance.
     */
    default TextFormat getFormat() {
        return TextFormat.BASIC;
    }

    /**
     * Creates a new text builder for the chat's format.
     * @return TextBuilder instance.
     */
    default TextBuilder text() {
        return new DefaultTextBuilder(this, getFormat());
    }

    /**
     * Finds if this chat supports formatting messages.
     * @return true if the chat supports formatting messages.
     */
    default boolean canFormatMessages() {
        return getFormat() != TextFormat.BASIC;
    }

    /**
     * Finds if the bot can receive messages in this conversation.
     * @return true if the bot receive get messages, false otherwise.
     */
    default boolean canReceiveMessages() { return true; }

    /**
     * Finds if the bot can get emoji in this conversation.
     * @return true if the bot receive get emoji, false otherwise.
     */
    default boolean canSendEmoji() { return false; }

    /**
     * Gets the default command prefixes for this chat, for messages to be handles as commands.
     * @return immutable collection of command prefixes (such as, "." or "!").
     */
    default Collection<Character> getCommandPrefixes() {
        return Collections.singletonList('.');
    }

    /**
     * Processes a chat message and formats the message as a command.
     * @param message ChatMessage instance to check
     * @return ReceivedChatMessage instance of the parsed command, null if there is no command detected.
     */
    default ChatMessage parseCommand(ChatMessage message) {
        if (message.getMessage() == null) return null;

        for (Character prefix : getCommandPrefixes()) {
            if (prefix == null) return null;

            if (message.getMessage().startsWith(prefix.toString()) &&
                    message.getMessage().length() > 1 &&
                    Character.isLetterOrDigit(message.getMessage().charAt(1))) {
                final String substringedMessage = message.getMessage().substring(1);

                return new ChatMessage() {
                    @Override
                    public ChatSender getSender() {
                        return message.getSender();
                    }

                    @Override
                    public void delete() throws UnsupportedOperationException {
                        message.delete();
                    }

                    @Override
                    public ChatMessage edit(String string) {
                        return message.edit(string);
                    }

                    @Override
                    public ChatMessage edit(Consumer<Builder> function) {
                        return message.edit(function);
                    }

                    @Override
                    public boolean wasEdited() {
                        return message.wasEdited();
                    }

                    @Override
                    public Date getEditedDate() {
                        return message.getEditedDate();
                    }

                    @Override
                    public Date getDate() {
                        return message.getDate();
                    }

                    @Override
                    public String getMessage() {
                        return substringedMessage;
                    }

                    @Override
                    public Collection<ChatEmbed> getEmbeds() {
                        return message.getEmbeds();
                    }
                };
            }
        }

        return null;
    }

}
