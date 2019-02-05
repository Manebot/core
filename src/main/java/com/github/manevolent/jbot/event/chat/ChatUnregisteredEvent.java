package com.github.manevolent.jbot.event.chat;

import com.github.manevolent.jbot.chat.Chat;

public class ChatUnregisteredEvent extends ChatEvent {
    public ChatUnregisteredEvent(Object sender, Chat chat) {
        super(sender, chat);
    }
}
