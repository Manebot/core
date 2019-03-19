package com.github.manevolent.jbot.event.chat;

import com.github.manevolent.jbot.chat.Chat;
import com.github.manevolent.jbot.event.Event;

public abstract class ChatEvent extends Event {
    private final Chat chat;

    public ChatEvent(Object sender, Chat chat) {
        super(sender);

        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }
}
