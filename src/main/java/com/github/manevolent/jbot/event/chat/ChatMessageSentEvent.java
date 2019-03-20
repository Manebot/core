package com.github.manevolent.jbot.event.chat;

import com.github.manevolent.jbot.chat.Chat;
import com.github.manevolent.jbot.chat.ChatMessage;

public class ChatMessageSentEvent extends ChatEvent {
    private final ChatMessage message;

    public ChatMessageSentEvent(Object sender, Chat chat, ChatMessage chatMessage) {
        super(sender, chat);

        this.message = chatMessage;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
