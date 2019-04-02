package io.manebot.event.chat;

import io.manebot.chat.Chat;

public class ChatRegisteredEvent extends ChatEvent {
    public ChatRegisteredEvent(Object sender, Chat chat) {
        super(sender, chat);
    }
}
