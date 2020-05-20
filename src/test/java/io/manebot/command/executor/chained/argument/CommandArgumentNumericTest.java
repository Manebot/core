package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import io.manebot.tuple.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class CommandArgumentNumericTest extends CommandArgumentTest {

    @Test
    public void testCast_ValidNumber() {
        Collection<Pair<String, Double>> validInputs = Arrays.asList(
                new Pair<>("1", 1D),
                new Pair<>("-1", -1D),
                new Pair<>("+1", 1D),
                new Pair<>("1.0", 1D),
                new Pair<>("-1", -1D),
                new Pair<>("1000", 1000D),
                new Pair<>("-1000.0", -1000D),
                new Pair<>("00001", 1D)
        );

        for (Pair<String, Double> input : validInputs) {
            ChainState state = parse(input.getLeft());

            assertEquals("Unexpected priority for input \"" + input.getLeft() + "\".", ChainPriority.HIGH,
                    new CommandArgumentNumeric().cast(state));

            assertEquals("Unexpected processed arguments for input \"" + input.getLeft() + "\".", 1,
                    state.getParsedArguments().size());

            assertEquals("Unexpected processed value for input \"" + input.getLeft() + "\".", input.getRight(),
                    state.getParsedArguments().get(0));
        }
    }


    @Test
    public void testCast_InvalidNumber() {
        Collection<String> invalidInputs = Arrays.asList("not_a_number", "AAA1", "");

        for (String input : invalidInputs) {
            ChainState state = parse(input);

            assertEquals("Unexpected priority for input \"" + input + "\".", ChainPriority.NONE,
                    new CommandArgumentNumeric().cast(state));

            assertEquals("Unexpected processed arguments for input \"" + input + "\".", 0,
                    state.getParsedArguments().size());
        }
    }

}
