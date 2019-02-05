package com.github.manevolent.jbot.event.chat;

import com.github.manevolent.jbot.chat.Chat;

public abstract class ChatEvent {
    private final Object sender;
    private final Chat chat;

    public ChatEvent(Object sender, Chat chat) {
        this.sender = sender;
        this.chat = chat;
    }

    public Object getSender() {
        return sender;
    }

    public Chat getChat() {
        return chat;
    }
}
