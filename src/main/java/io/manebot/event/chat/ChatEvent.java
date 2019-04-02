package io.manebot.event.chat;

import io.manebot.chat.Chat;
import io.manebot.event.Event;

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
