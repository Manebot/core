package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommandArgumentIntervalTest extends CommandArgumentTest {

    @Test
    public void testCast_Provided() {
        String intervalFormat = "%d%s";
        CommandArgumentInterval argument = new CommandArgumentInterval();

        Arrays.stream(CommandArgumentInterval.intervals).forEach(interval -> {
            interval.getMatches().forEach(match -> {
                for (int multiplier = 1; multiplier <= 2; multiplier ++) {
                    String input = String.format(intervalFormat, multiplier, match);
                    ChainState state = parse(input);

                    ChainPriority priority = argument.cast(state);

                    assertEquals("Unexpected priority for input \"" + input + "\"",
                            ChainPriority.HIGH, priority);

                    assertEquals("Unexpected parsed argument count for input \"" + input + "\"",
                            1, state.getParsedArguments().size());

                    assertEquals("Unexpected argument parse value for input \"" + input + "\"",
                            (double) interval.getMilliseconds() * multiplier, state.getParsedArguments().get(0));
                }
            });
        });
    }

    @Test
    public void testCast_Provided_Invalid() {
        List<String> inputs = Arrays.asList("1yesteryear", "2imaginaryunits", "5");

        inputs.forEach(input -> {
            CommandArgumentInterval argument = new CommandArgumentInterval();
            ChainState state = parse(input);
            ChainPriority priority = argument.cast(state);

            assertEquals("Unexpected priority", ChainPriority.NONE, priority);
            assertEquals("Unexpected parsed argument count", 0, state.getParsedArguments().size());
        });

    }

    @Test
    public void testCast_NotProvided() {
        CommandArgumentInterval argument = new CommandArgumentInterval();
        ChainState state = empty();
        ChainPriority priority = argument.cast(state);

        assertEquals("Unexpected priority", ChainPriority.NONE, priority);
        assertEquals("Unexpected parsed argument count", 0, state.getParsedArguments().size());
    }

}
