package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;

public class ChainedCommandArgumentFollowing extends ChainedCommandArgument {
    @Override
    public String getHelpString() {
        return "...";
    }

    @Override
    public ChainPriority cast(ChainState state) {
        if (state.size() <= 0) return ChainPriority.NONE;
        else {
            String s = String.join(" ", state.getArguments());
            state.extend(state.size(), s);
            return ChainPriority.LOW;
        }
    }

    @Override
    public boolean canExtend(ChainedCommandArgument b) {
        return false; // nothing can extend a following chain
    }

    @Override
    public boolean canCoexist(ChainedCommandArgument b) {
        return !(b instanceof ChainedCommandArgumentFollowing);
    }
}
