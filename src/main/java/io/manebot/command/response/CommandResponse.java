package io.manebot.command.response;

import io.manebot.chat.ChatMessage;
import io.manebot.chat.ChatSender;
import io.manebot.command.exception.CommandExecutionException;

import java.util.Collection;

public abstract class CommandResponse {
    private final ChatSender sender;

    protected CommandResponse(ChatSender sender) {
        this.sender = sender;
    }

    public ChatSender getSender() {
        return sender;
    }

    public abstract Collection<ChatMessage> send() throws CommandExecutionException;
}
