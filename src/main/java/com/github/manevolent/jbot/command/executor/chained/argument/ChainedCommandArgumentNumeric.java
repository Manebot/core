package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;

public class ChainedCommandArgumentNumeric extends ChainedCommandArgument {
    @Override
    public String getHelpString() {
        return "[#]";
    }


    @Override
    public ChainPriority cast(ChainState state) {
        String next = state.next();
        if (next == null) return ChainPriority.NONE;

        try {
            state.extend(1, Double.parseDouble(state.next()));
            return ChainPriority.HIGH;
        } catch (NumberFormatException ex) {
        }

        return ChainPriority.NONE;
    }

    @Override
    public boolean canExtend(ChainedCommandArgument b) {
        return true; // anything can extend this
    }

    @Override
    public boolean canCoexist(ChainedCommandArgument b) {
        return true;
    }
}
