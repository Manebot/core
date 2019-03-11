package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;
import com.github.manevolent.jbot.command.executor.chained.ReflectiveCommandExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.manevolent.jbot.command.executor.chained.ChainPriority.LOW;

public class ChainedCommandArgumentString extends ChainedCommandArgument {
    private final String label;
    private final ChainPriority priority;

    public ChainedCommandArgumentString(String label) {
        this(label, LOW);
    }

    public ChainedCommandArgumentString(String label, ChainPriority priority) {
        this.label = label;
        this.priority = priority;
    }

    public ChainedCommandArgumentString(Argument argument) {
        this.label = argument.label();
        this.priority = argument.priority();
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

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @ReflectiveCommandExecutor.Argument(type = ChainedCommandArgumentString.class)
    public @interface Argument {
        String label();
        ChainPriority priority() default LOW;
    }
}
