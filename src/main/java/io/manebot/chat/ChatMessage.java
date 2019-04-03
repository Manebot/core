package io.manebot.chat;

import io.manebot.platform.PlatformUser;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.function.Consumer;

public interface ChatMessage {

    /**
     * Gets the raw chat message string for this message.
     * @return message string.
     */
    String getMessage();

    /**
     * Gets the user-friendly chat message string for this message.
     * @return message string.
     */
    default String getRawMessage() {
        return getMessage();
    }

    /**
     * Gets a collection of embeds associated with this chat message.
     * @return embeds.
     */
    Collection<ChatEmbed> getEmbeds();

    /**
     * Gets the sender for this chat message.
     * @return sender.
     */
    ChatSender getSender();

    /**
     * Deletes the message.
     */
    void delete() throws UnsupportedOperationException;

    /**
     * Edits the message to match the provided message.
     * @param message message to edit to.
     * @return edited message.
     */
    default ChatMessage edit(String message) {
        return edit(builder -> builder.message(format -> format.append(message)));
    }

    /**
     * Edits the message to match the provided message.
     * @param function function to express a new message.
     * @return edited message.
     */
    ChatMessage edit(Consumer<Builder> function);

    /**
     * Finds if the message was edited.
     * @return true if the message was edited, false otherwise.
     */
    boolean wasEdited();

    /**
     * Gets the time at which the message was edited.
     * @return edited date.
     */
    Date getEditedDate();

    /**
     * Gets the date this message was received, or sent by the sender.
     * @return message date.
     */
    Date getDate();

    /**
     * Gets an immutable collections of mentions in this chat message.
     * @return collection of mentions in this message.
     */
    default Collection<PlatformUser> getMentions() {
        return Collections.emptyList();
    }

    /**
     * Finds if the chat message mentions the bot user.
     * @return true if the chat message mentions the bot user.
     */
    default boolean doesMentionSelf() {
        return getMentions().stream().anyMatch(PlatformUser::isSelf);
    }

    interface Builder {

        /**
         * Gets the platform user that will be sending this message.
         * @return platform user sending the message.
         */
        PlatformUser getUser();

        /**
         * Gets the chat associated with building this message.
         * @return Chat instance.
         */
        Chat getChat();

        /**
         * Sets the raw message to get on this chat message.
         * @param message raw message to set.
         * @return Builder instance.
         */
        Builder rawMessage(String message);

        /**
         * Sets the message to get on this chat message.
         * @param message message to set.
         * @return Builder instance.
         */
        default Builder message(String message) {
            return message(textBuilder -> textBuilder.append(message));
        }

        /**
         * Sets the message to get on this chat message.
         * @param function message formatter function to use.
         * @return Builder instance.
         */
        default Builder message(Consumer<TextBuilder> function) {
            TextBuilder textBuilder = getChat().text();
            function.accept(textBuilder);
            return rawMessage(textBuilder.build());
        }

        /**
         * Appends an abstract embed to the message.
         * @param function embed to append.
         * @return Builder instance.
         * @throws UnsupportedOperationException if embeds are not supported in this chat.
         */
        Builder embed(Consumer<ChatEmbed.Builder> function) throws UnsupportedOperationException;

    }

}
