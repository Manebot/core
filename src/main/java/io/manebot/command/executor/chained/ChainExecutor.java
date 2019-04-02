package io.manebot.command.executor.chained;

import io.manebot.command.CommandSender;
import io.manebot.command.exception.CommandExecutionException;

public interface ChainExecutor {
    void execute(CommandSender sender, String label, Object[] args) throws CommandExecutionException;
}
