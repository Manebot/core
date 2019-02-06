package com.github.manevolent.jbot.command;

import com.github.manevolent.jbot.chat.ChatMessage;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

public interface CommandHandler {
    void execute(ChatMessage chatMessage) throws CommandExecutionException;
}