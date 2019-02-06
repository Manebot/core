package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;

public class ChainedCommandArgumentLabel extends ChainedCommandArgument {
    private final String label;

    public ChainedCommandArgumentLabel(String label) {
        if (label == null || label.length() <= 0) throw new IllegalArgumentException("invalid label");
        this.label = label;
    }

    @Override
    public String getHelpString() {
        return label;
    }


    @Override
    public ChainPriority cast(ChainState state) {
        String next = state.next();
        if (next == null) return ChainPriority.NONE;
        if (next.equalsIgnoreCase(label)) {
            state.extend(1, label);
            return ChainPriority.HIGH;
        } else return ChainPriority.NONE;
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
