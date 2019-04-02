package io.manebot.chat;

import java.util.Date;

public abstract class AbstractChatMessage implements ChatMessage {
    private final ChatSender sender;
    private final Date date;

    public AbstractChatMessage(ChatSender sender, Date date) {
        this.sender = sender;
        this.date = date;
    }

    @Override
    public ChatSender getSender() {
        return sender;
    }

    @Override
    public Date getDate() {
        return date;
    }
}
