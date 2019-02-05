package com.github.manevolent.jbot.event.chat;

import com.github.manevolent.jbot.chat.ChatMessage;
import com.github.manevolent.jbot.event.Event;

public class ChatMessageSentEvent extends ChatEvent {
    private final ChatMessage message;

    public ChatMessageSentEvent(Object sender, ChatMessage chatMessage) {
        super(sender, chatMessage.getChat());

        this.message = chatMessage;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
