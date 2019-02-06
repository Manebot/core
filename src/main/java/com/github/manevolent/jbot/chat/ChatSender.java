package com.github.manevolent.jbot.chat;

import java.util.Date;

public interface ChatSender {

    /**
     * Gets the username of the command sender.
     */
    String getUsername();

    /**
     * Gets the display name of the command sender.
     */
    String getDisplayName();

    /**
     * Gets the chat the message was sent in.
     * @return Chat instance.
     */
    Chat getChat();

}
