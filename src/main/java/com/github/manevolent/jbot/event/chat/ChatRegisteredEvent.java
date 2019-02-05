package com.github.manevolent.jbot.event.chat;

import com.github.manevolent.jbot.chat.Chat;

public class ChatRegisteredEvent extends ChatEvent {
    public ChatRegisteredEvent(Object sender, Chat chat) {
        super(sender, chat);
    }
}
