package com.github.manevolent.jbot.chat;

import java.util.Date;

public interface ReceivedChatMessage extends ChatMessage {

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
     */
    void edit(ChatMessage message) throws UnsupportedOperationException;

    /**
     * Gets the date this message was received, or sent by the sender.
     * @return message date.
     */
    Date getDate();

}
