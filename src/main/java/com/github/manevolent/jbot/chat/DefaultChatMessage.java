package com.github.manevolent.jbot.chat;

public class DefaultChatMessage implements ChatMessage {
    private final String message;

    public DefaultChatMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
