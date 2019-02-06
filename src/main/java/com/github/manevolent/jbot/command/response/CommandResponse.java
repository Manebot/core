package com.github.manevolent.jbot.command.response;

import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

public abstract class CommandResponse {
    public abstract void respond(CommandSender sender) throws CommandExecutionException;
}
