package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandArgumentPageTest extends CommandArgumentTest {

    @Test
    public void testCast_Provided() {
        String value = "page:2";
        ChainState state = parse(value);
        ChainPriority priority = new CommandArgumentPage().cast(state);

        assertEquals("Unexpected priority", ChainPriority.HIGH, priority);
        assertEquals("Unexpected parsed argument count", 1, state.getParsedArguments().size());
        assertEquals("Unexpected argument parse value", 2, state.getParsedArguments().get(0));
    }

    @Test
    public void testCast_NotProvided() {
        ChainState state = empty();
        ChainPriority priority = new CommandArgumentPage().cast(state);

        assertEquals("Unexpected priority", ChainPriority.HIGH, priority);
        assertEquals("Unexpected parsed argument count", 1, state.getParsedArguments().size());
        assertEquals("Unexpected argument parse value", 1, state.getParsedArguments().get(0));
    }

}
