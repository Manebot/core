package com.github.manevolent.jbot.command.executor.chained;

import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

public interface ChainExecutor {
    void execute(CommandSender sender, String label, Object[] args) throws CommandExecutionException;
}
