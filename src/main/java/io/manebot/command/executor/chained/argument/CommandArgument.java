package io.manebot.command.executor.chained.argument;


import io.manebot.command.exception.CommandExecutionException;
import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;

public abstract class CommandArgument {

    public abstract String getHelpString();
    public abstract ChainPriority cast(ChainState state) throws CommandExecutionException;
    public abstract boolean canExtend(CommandArgument b);
    public abstract boolean canCoexist(CommandArgument b);

}
