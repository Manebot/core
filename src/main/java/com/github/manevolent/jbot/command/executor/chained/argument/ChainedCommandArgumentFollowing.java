package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;
import com.github.manevolent.jbot.command.executor.chained.AnnotatedCommandExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ChainedCommandArgumentFollowing extends ChainedCommandArgument {
    public ChainedCommandArgumentFollowing() {

    }

    public ChainedCommandArgumentFollowing(Argument argument) {

    }

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

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AnnotatedCommandExecutor.Argument(type = ChainedCommandArgumentFollowing.class)
    public @interface Argument {}
}
