package com.github.manevolent.jbot.chat;

import com.github.manevolent.jbot.user.User;

import java.util.Date;

public interface ChatMessage {

    /**
     * Gets the chat this message was sent in.
     * @return chat instance.
     */
    Chat getChat();

    /**
     * Gets the user that sent this message.
     * @return user.
     */
    User getUser();

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
