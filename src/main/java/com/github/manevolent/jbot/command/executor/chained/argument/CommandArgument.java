package com.github.manevolent.jbot.command.executor.chained.argument;


import com.github.manevolent.jbot.command.exception.CommandExecutionException;
import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;

public abstract class CommandArgument {

    public abstract String getHelpString();
    public abstract ChainPriority cast(ChainState state) throws CommandExecutionException;
    public abstract boolean canExtend(CommandArgument b);
    public abstract boolean canCoexist(CommandArgument b);

}
