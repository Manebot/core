package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.AnnotatedCommandExecutor;
import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static io.manebot.command.executor.chained.ChainPriority.*;

public class CommandArgumentURL extends CommandArgument {
    public CommandArgumentURL(Argument argument) { }

    @Override
    public String getHelpString() {
        return "<URL>";
    }

    @Override
    public ChainPriority cast(ChainState state) {
        String next = state.next();
        if (next == null) return ChainPriority.NONE;

        URI uri;
        try {
           uri = URI.create(next);
        } catch (IllegalArgumentException ex) {
            return NONE;
        }

        URL url;

        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            return NONE;
        }

        state.extend(1, url);
        return HIGH;
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
    @AnnotatedCommandExecutor.Argument(type = CommandArgumentURL.class)
    public @interface Argument {
        String label();
    }
}
