package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import io.manebot.command.executor.chained.AnnotatedCommandExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CommandArgumentPage extends CommandArgument {
    public CommandArgumentPage() {

    }

    public CommandArgumentPage(Argument argument) {

    }

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
    public boolean canExtend(CommandArgument b) {
        return true; // anything can extend this
    }

    @Override
    public boolean canCoexist(CommandArgument b) {
        return true;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AnnotatedCommandExecutor.Argument(type = CommandArgumentPage.class)
    public @interface Argument {}
}
