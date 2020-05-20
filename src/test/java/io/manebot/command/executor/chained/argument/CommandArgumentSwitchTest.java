package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommandArgumentSwitchTest extends CommandArgumentTest {

    @Test
    public void testCast_Provided() {
        List<String> switches = Arrays.asList("a", "b", "c");
        CommandArgumentSwitch argument = new CommandArgumentSwitch(switches);

        switches.forEach(label -> {
            ChainState state = parse(label);

            ChainPriority priority = argument.cast(state);

            assertEquals("Unexpected priority", ChainPriority.HIGH, priority);
            assertEquals("Unexpected parsed argument count", 1, state.getParsedArguments().size());
            assertEquals("Unexpected argument parse value", label, state.getParsedArguments().get(0));
        });
    }

    @Test
    public void testCast_Provided_Invalid() {
        List<String> switches = Arrays.asList("a", "b", "c");
        CommandArgumentSwitch argument = new CommandArgumentSwitch(switches);
        ChainState state = parse("d");
        ChainPriority priority = argument.cast(state);

        assertEquals("Unexpected priority", ChainPriority.NONE, priority);
        assertEquals("Unexpected parsed argument count", 0, state.getParsedArguments().size());
    }

    @Test
    public void testCast_NotProvided() {
        List<String> switches = Arrays.asList("a", "b", "c");
        CommandArgumentSwitch argument = new CommandArgumentSwitch(switches);
        ChainState state = empty();
        ChainPriority priority = argument.cast(state);

        assertEquals("Unexpected priority", ChainPriority.NONE, priority);
        assertEquals("Unexpected parsed argument count", 0, state.getParsedArguments().size());
    }

}
