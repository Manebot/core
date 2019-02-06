package com.github.manevolent.jbot.chat;

import java.util.Date;

public class DefaultChatMessage implements ChatMessage {
    private final ChatSender sender;
    private final String message;
    private final Date date;

    public DefaultChatMessage(ChatSender sender, String message, Date date) {
        this.sender = sender;
        this.message = message;
        this.date = date;
    }

    @Override
    public ChatSender getSender() {
        return sender;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Date getDate() {
        return date;
    }
}
