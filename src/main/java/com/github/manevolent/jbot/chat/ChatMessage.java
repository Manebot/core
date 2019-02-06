package com.github.manevolent.jbot.chat;

import java.util.Date;

public interface ChatMessage {

    /**
     * Gets the sender for this chat message.
     * @return sender.
     */
    ChatSender getSender();

    /**
     * Gets the user-friendly chat message string for this message.
     * @return message string.
     */
    String getMessage();

    /**
     * Gets the date this message was sent.
     * @return message sent date.
     */
    Date getDate();

}
