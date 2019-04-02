package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import io.manebot.command.executor.chained.AnnotatedCommandExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CommandArgumentFollowing extends CommandArgument {
    public CommandArgumentFollowing() {

    }

    public CommandArgumentFollowing(Argument argument) {

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
    public boolean canExtend(CommandArgument b) {
        return false; // nothing can extend a following chain
    }

    @Override
    public boolean canCoexist(CommandArgument b) {
        return !(b instanceof CommandArgumentFollowing);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AnnotatedCommandExecutor.Argument(type = CommandArgumentFollowing.class)
    public @interface Argument {}
}
