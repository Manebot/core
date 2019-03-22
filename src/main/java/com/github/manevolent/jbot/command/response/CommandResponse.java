package com.github.manevolent.jbot.command.response;

import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

public abstract class CommandResponse {
    private final CommandSender sender;

    protected CommandResponse(CommandSender sender) {
        this.sender = sender;
    }

    public CommandSender getSender() {
        return sender;
    }

    public abstract void send() throws CommandExecutionException;
}
