package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import io.manebot.command.executor.chained.AnnotatedCommandExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CommandArgumentNumeric extends CommandArgument {
    public CommandArgumentNumeric() {

    }

    public CommandArgumentNumeric(Argument argument) {

    }

    @Override
    public String getHelpString() {
        return "[#]";
    }

    @Override
    public ChainPriority cast(ChainState state) {
        String next = state.next();
        if (next == null) return ChainPriority.NONE;

        try {
            state.extend(1, Double.parseDouble(state.next()));
            return ChainPriority.HIGH;
        } catch (NumberFormatException ex) {
        }

        return ChainPriority.NONE;
    }

    @Override
    public boolean canExtend(CommandArgument b) {
        return true; // anything can extend this
    }

    @Override
    public boolean canCoexist(CommandArgument b) {
        return true;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AnnotatedCommandExecutor.Argument(type = CommandArgumentNumeric.class)
    public @interface Argument {}
}
