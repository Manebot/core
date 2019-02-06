package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.exception.CommandExecutionException;
import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;

public class ChainedCommandArgumentOptional extends ChainedCommandArgument {
    private final Optional<?> defaultObj;
    private final ChainedCommandArgument argument;

    public ChainedCommandArgumentOptional(ChainedCommandArgument argument, Optional<?> defaultObj) {
        this.argument = argument;
        this.defaultObj = defaultObj;
    }

    public ChainedCommandArgumentOptional(ChainedCommandArgument argument, Object defaultObj) {
        this(argument, (state) -> defaultObj);
    }

    @Override
    public String getHelpString() {
        return "(" + argument.getHelpString() + ")";
    }

    @Override
    public ChainPriority cast(ChainState state) throws CommandExecutionException {
        int arguments = state.getParsedArguments().size();

        ChainPriority priority = argument.cast(state);

        if (priority == ChainPriority.NONE && arguments == state.getParsedArguments().size()) {
            try {
                state.extend(1, (Object) defaultObj.get(state));
            } catch (Exception e) {
                throw new CommandExecutionException(e);
            }

            priority = ChainPriority.LOW;
        }

        return priority;
    }

    @Override
    public boolean canExtend(ChainedCommandArgument b) {
        return true;
    }

    @Override
    public boolean canCoexist(ChainedCommandArgument b) {
        return true;
    }

    public interface Optional<T> {
        T get(ChainState state);
    }
}
