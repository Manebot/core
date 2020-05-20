package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandArgumentLabelTest extends CommandArgumentTest {

    @Test
    public void testCast_Valid() {
        String value = "test";
        ChainState state = parse(value);
        ChainPriority priority = new CommandArgumentLabel(value).cast(state);

        assertEquals("Unexpected priority", ChainPriority.HIGH, priority);
        assertEquals("Unexpected parsed argument count", 1, state.getParsedArguments().size());
        assertEquals("Unexpected argument parse value", value, state.getParsedArguments().get(0));
    }

    @Test
    public void testCast_Invalid() {
        String value = "test";
        ChainState state = parse(value);
        ChainPriority priority = new CommandArgumentLabel(value + "_invalid").cast(state);

        assertEquals("Unexpected priority", ChainPriority.NONE, priority);
        assertEquals("Unexpected parsed argument count", 0, state.getParsedArguments().size());
    }

}
