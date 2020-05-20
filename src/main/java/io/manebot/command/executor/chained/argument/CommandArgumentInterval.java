package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import io.manebot.command.executor.chained.AnnotatedCommandExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class CommandArgumentInterval extends CommandArgument {
    protected static final IntervalTime[] intervals = new IntervalTime[]
            {
                    new IntervalTime(1, new String[] {"s", "sec", "second"}),
                    new IntervalTime(60, new String[] {"m", "min", "minute"}),
                    new IntervalTime(3600, new String[] {"h", "hr", "hour"}),
                    new IntervalTime(86400, new String[] {"d", "dy", "day"}),
                    new IntervalTime(604800, new String[] {"w", "wk", "week"})
            };

    public CommandArgumentInterval() {

    }

    public CommandArgumentInterval(Argument argument) {

    }

    @Override
    public String getHelpString() {
        return "[Interval]";
    }

    @Override
    public ChainPriority cast(ChainState state) {
        String next = state.next();
        if (next == null) return ChainPriority.NONE;
        next = next.toLowerCase();

        IntervalTime intervalTime = null;

        for (IntervalTime x : intervals) {
            for (String match : x.matches) {
                if (next.endsWith(match)) {
                    intervalTime = x;
                    next = next.replace(match, "");
                    break;
                }
            }
            if (intervalTime != null) break;
        }

        if (intervalTime == null) return ChainPriority.NONE;

        try {
            state.extend(1, Double.parseDouble(next) * (double)intervalTime.milliseconds);
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
        return !(b instanceof CommandArgumentInterval);
    }

    protected static class IntervalTime {
        private final long milliseconds;
        private final String[] matches;

        private IntervalTime(long milliseconds, String[] matches) {
            this.milliseconds = milliseconds;
            this.matches = matches;
        }

        public long getMilliseconds() {
            return milliseconds;
        }

        public Collection<String> getMatches() {
            return Arrays.asList(matches);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AnnotatedCommandExecutor.Argument(type = CommandArgumentInterval.class)
    public @interface Argument {}
}
