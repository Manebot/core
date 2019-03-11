package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;
import com.github.manevolent.jbot.command.executor.chained.ReflectiveCommandExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ChainedCommandArgumentNone extends ChainedCommandArgument {
    public ChainedCommandArgumentNone() {

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
    public boolean canExtend(ChainedCommandArgument b) {
        return false;
    }

    @Override
    public boolean canCoexist(ChainedCommandArgument b) {
        return !(b instanceof ChainedCommandArgumentNone);
    }
}
