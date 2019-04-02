package io.manebot.event.chat;

import io.manebot.chat.ChatMessage;

public class ChatUnknownUserEvent extends ChatEvent {
    private final ChatMessage message;

    public ChatUnknownUserEvent(Object sender, ChatMessage chatMessage) {
        super(sender, chatMessage.getSender().getChat());

        this.message = chatMessage;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
