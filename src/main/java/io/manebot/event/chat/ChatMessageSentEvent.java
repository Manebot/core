package io.manebot.event.chat;

import io.manebot.chat.Chat;
import io.manebot.chat.ChatMessage;

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
