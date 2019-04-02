package io.manebot.event.chat;

import io.manebot.chat.Chat;

public class ChatUnregisteredEvent extends ChatEvent {
    public ChatUnregisteredEvent(Object sender, Chat chat) {
        super(sender, chat);
    }
}
