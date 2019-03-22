package com.github.manevolent.jbot.command.response;

import com.github.manevolent.jbot.chat.ChatSender;
import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

public abstract class CommandResponse {
    private final ChatSender sender;

    protected CommandResponse(ChatSender sender) {
        this.sender = sender;
    }

    public ChatSender getSender() {
        return sender;
    }

    public abstract void send() throws CommandExecutionException;
}
