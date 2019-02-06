package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;

public class ChainedCommandArgumentString extends ChainedCommandArgument {
    private final String label;
    private final ChainPriority priority;

    public ChainedCommandArgumentString(String label) {
        this(label, ChainPriority.LOW);
    }
    public ChainedCommandArgumentString(String label, ChainPriority priority) {
        this.label = label;
        this.priority = priority;
    }

    @Override
    public String getHelpString() {
        return "[" + label + "]";
    }

    @Override
    public ChainPriority cast(ChainState state) {
        String next = state.next();
        if (next == null) return ChainPriority.NONE;

        state.extend(1, state.next());
        return priority;
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
