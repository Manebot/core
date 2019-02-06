package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;

public class ChainedCommandArgumentPage extends ChainedCommandArgument {
    @Override
    public String getHelpString() {
        return "[Page:#]";
    }


    @Override
    public ChainPriority cast(ChainState state) {
        String next = state.next();
        if (next == null) {
            state.extend(1, (Integer)1);
            return ChainPriority.HIGH;
        }

        String s = state.next();

        try {
            if (!s.startsWith("page:")) return ChainPriority.NONE;
            state.extend(1, Integer.parseInt(s.replace("page:", "")));
            return ChainPriority.HIGH;
        } catch (NumberFormatException ex) {
        }

        try {
            if (!s.startsWith("p:")) return ChainPriority.NONE;
            state.extend(1, Integer.parseInt(s.replace("p:", "")));
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
