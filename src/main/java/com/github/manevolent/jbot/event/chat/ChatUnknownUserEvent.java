package com.github.manevolent.jbot.event.chat;

import com.github.manevolent.jbot.chat.ReceivedChatMessage;

public class ChatUnknownUserEvent extends ChatEvent {
    private final ReceivedChatMessage message;

    public ChatUnknownUserEvent(Object sender, ReceivedChatMessage chatMessage) {
        super(sender, chatMessage.getSender().getChat());

        this.message = chatMessage;
    }

    public ReceivedChatMessage getMessage() {
        return message;
    }
}
