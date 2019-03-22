package com.github.manevolent.jbot.chat;

import com.github.manevolent.jbot.command.response.CommandDetailsResponse;
import com.github.manevolent.jbot.command.response.CommandListResponse;
import com.github.manevolent.jbot.platform.PlatformUser;

import java.util.function.Function;

public interface ChatSender {

    /**
     * Gets the platform user that is associated with this chat sender.
     * @return PlatformUser instance.
     */
    PlatformUser getPlatformUser();

    /**
     * Gets the username of the command sender.
     */
    default String getUsername() {
        return getPlatformUser().getId();
    }

    /**
     * Gets the display name of the command sender.
     */
    default String getDisplayName() {
        return getPlatformUser().getNickname();
    }

    /**
     * Gets the chat the message was sent in.
     * @return Chat instance.
     */
    Chat getChat();

    /**
     * Opens the command buffer.
     * @return true if the buffer was opened, false if no changes were made.
     */
    boolean begin();

    /**
     * Adds a message to the command buffer or sends a message.
     * @param message message to add.
     */
    void sendMessage(String message);
    /**
     * Sends a text message to the sender.
     * @param message PlainText message to send.
     */
    default void sendMessage(ChatMessage message) {
        sendMessage(message.getMessage());
    }

    /**
     * Sends a rich message to the sender.
     * @param message Rich message to send.
     * @throws UnsupportedOperationException if the conversation does not support rich messaging.
     */
    default void sendMessage(RichChatMessage message) throws UnsupportedOperationException {
        getChat().sendMessage(message);
    }

    /**
     * Ends the command buffer.
     * @return number of lines sent.
     */
    int end();

    /**
     * Sends several messages to the remote.
     * @param messages Messages to send.
     */
    void sendMessage(String... messages);

    /**
     * Flushes the buffer.
     * @return number of lines sent.
     */
    int flush();


    /**
     * Creates a list response to send to the command sender, opportunistically formatting as rich content.
     * @param function Function providing a command response object from a builder.
     * @param <T> List item type.
     * @return CommandResponse object corresponding to the desired message; contains <b>send()</b> method to dispatch.
     */
    <T> CommandListResponse<T> list(
            Function<CommandListResponse.Builder<T>, CommandListResponse<T>> function
    );

    /**
     * Creates a details response to send to the command sender, opportunistically formatting as rich content.
     *
     * Details are formatted as such:
     *
     *      ObjectName "ObjectKey" details:
     *       Key: value
     *       Key: [value1,value2,value3]
     *
     * @param function Function providing a command response object from a builder.
     * @return CommandResponse object corresponding to the desired message; contains <b>send()</b> method to dispatch.
     */
    CommandDetailsResponse details(
            Function<CommandDetailsResponse.Builder, CommandDetailsResponse> function
    );

}
