package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;

public class ChainedCommandArgumentInterval extends ChainedCommandArgument {
    private final IntervalTime[] intervals = new IntervalTime[]
            {
                    new IntervalTime(1, new String[] {"s", "sec", "second"}),
                    new IntervalTime(60, new String[] {"m", "min", "minute"}),
                    new IntervalTime(3600, new String[] {"h", "hr", "hour"}),
                    new IntervalTime(86400, new String[] {"d", "dy", "day"}),
                    new IntervalTime(604800, new String[] {"w", "wk", "week"})
            };

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
    public boolean canExtend(ChainedCommandArgument b) {
        return true; // anything can extend this
    }

    @Override
    public boolean canCoexist(ChainedCommandArgument b) {
        return !(b instanceof ChainedCommandArgumentInterval);
    }

    private static class IntervalTime {
        private final long milliseconds;
        private final String[] matches;

        private IntervalTime(long milliseconds, String[] matches) {
            this.milliseconds = milliseconds;
            this.matches = matches;
        }
    }
}
