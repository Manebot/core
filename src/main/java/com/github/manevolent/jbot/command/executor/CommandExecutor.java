package com.github.manevolent.jbot.command.executor;

import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandArgumentException;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

import java.util.List;

public interface CommandExecutor {

    void execute(CommandSender sender, String label, String[] args) throws CommandExecutionException;

    default String getDescription() {
        return "No description";
    }

    default List<String> getHelp(CommandSender sender, String label, String[] args) throws CommandExecutionException {
        throw new CommandArgumentException("No help available.");
    }

    default boolean isBuffered() {
        return true;
    }
}
