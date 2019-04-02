package io.manebot.command.executor.chained.argument;

import io.manebot.command.exception.CommandExecutionException;
import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;

public class CommandArgumentOptional extends CommandArgument {
    private final Optional<?> defaultObj;
    private final CommandArgument argument;

    public CommandArgumentOptional(CommandArgument argument, Optional<?> defaultObj) {
        this.argument = argument;
        this.defaultObj = defaultObj;
    }

    public CommandArgumentOptional(CommandArgument argument, Object defaultObj) {
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
    public boolean canExtend(CommandArgument b) {
        return true;
    }

    @Override
    public boolean canCoexist(CommandArgument b) {
        return true;
    }

    public interface Optional<T> {
        T get(ChainState state);
    }
}
