package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;

public class CommandArgumentNone extends CommandArgument {
    public CommandArgumentNone() {

    }

    @Override
    public String getHelpString() {
        return "(none)";
    }


    @Override
    public ChainPriority cast(ChainState state) {
        return state.size() <= 0 ? ChainPriority.HIGH : ChainPriority.NONE;
    }

    @Override
    public boolean canExtend(CommandArgument b) {
        return false;
    }

    @Override
    public boolean canCoexist(CommandArgument b) {
        return !(b instanceof CommandArgumentNone);
    }
}
