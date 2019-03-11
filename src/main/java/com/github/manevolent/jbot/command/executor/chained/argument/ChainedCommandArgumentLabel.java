package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;
import com.github.manevolent.jbot.command.executor.chained.ReflectiveCommandExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ChainedCommandArgumentLabel extends ChainedCommandArgument {
    private final String label;

    public ChainedCommandArgumentLabel(String label) {
        if (label == null || label.length() <= 0) throw new IllegalArgumentException("invalid label");
        this.label = label;
    }

    public ChainedCommandArgumentLabel(Argument argument) {
        this.label = argument.label();
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

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @ReflectiveCommandExecutor.Argument(type = ChainedCommandArgumentLabel.class)
    public @interface Argument {
        String label();
    }
}
