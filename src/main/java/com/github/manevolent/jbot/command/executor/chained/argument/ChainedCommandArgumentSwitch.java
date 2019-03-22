package com.github.manevolent.jbot.command.executor.chained.argument;

import com.github.manevolent.jbot.command.executor.chained.AnnotatedCommandExecutor;
import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import static com.github.manevolent.jbot.command.executor.chained.ChainPriority.LOW;

public class ChainedCommandArgumentSwitch extends ChainedCommandArgument {
    private final String[] labels;

    public ChainedCommandArgumentSwitch(String... labels) {
        if (labels == null || labels.length <= 0) throw new IllegalArgumentException("invalid labels");
        this.labels = labels;
    }

    public ChainedCommandArgumentSwitch(List<String> labels) {
        if (labels == null || labels.size() <= 0) throw new IllegalArgumentException("invalid labels");
        this.labels = new String[labels.size()];
        labels.toArray(this.labels);
    }

    public ChainedCommandArgumentSwitch(Argument argument) {
        this(Arrays.asList(argument.labels()));
    }

    @Override
    public String getHelpString() {
        return "[" + String.join(", " , labels) + "]";
    }

    @Override
    public ChainPriority cast(ChainState state) {
        String next = state.next();
        if (next == null) return ChainPriority.NONE;
        for (String s : labels)
            if (s.equalsIgnoreCase(next)) {
                state.extend(1, s);
                return ChainPriority.HIGH;
            }

        return ChainPriority.NONE;
    }

    @Override
    public boolean canExtend(ChainedCommandArgument b) {
        return true; // anything can extend this
    }

    @Override
    public boolean canCoexist(ChainedCommandArgument b) {
        if (b instanceof ChainedCommandArgumentFollowing) return false;
        else if (b instanceof ChainedCommandArgumentSwitch) {
            //if (Collections.disjoint(Arrays.asList(labels), Arrays.asList(((ChainedCommandArgumentSwitch) b).labels)))
            //    return false;
        }

        return true;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AnnotatedCommandExecutor.Argument(type = ChainedCommandArgumentSwitch.class)
    public @interface Argument {
        String[] labels();
    }
}
