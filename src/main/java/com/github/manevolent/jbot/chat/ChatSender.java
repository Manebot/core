package com.github.manevolent.jbot.chat;

public interface ChatSender {

    /**
     * Gets the username of the command sender.
     */
    String getUsername();

    /**
     * Gets the display name of the command sender.
     */
    default String getDisplayName() {
        return getUsername();
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

}
