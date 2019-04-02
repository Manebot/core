package io.manebot.command.executor;

import io.manebot.command.CommandSender;
import io.manebot.command.exception.CommandArgumentException;
import io.manebot.command.exception.CommandExecutionException;

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
