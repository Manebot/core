package io.manebot.command.executor.chained.argument;

import io.manebot.command.exception.CommandExecutionException;
import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandArgumentOptionalTest extends CommandArgumentTest {

    @Test
    public void testCast_Provided() throws CommandExecutionException {
        String value = "test";
        String defaultValue = "defaultValue";
        ChainState state = parse(value);
        ChainPriority priority = new CommandArgumentOptional(new CommandArgumentString("testArgument"),
                defaultValue).cast(state);

        assertEquals("Unexpected priority", ChainPriority.LOW, priority);
        assertEquals("Unexpected parsed argument count", 1, state.getParsedArguments().size());
        assertEquals("Unexpected argument parse value", value, state.getParsedArguments().get(0));
    }


    @Test
    public void testCast_NotProvided() throws CommandExecutionException {
        String defaultValue = "defaultValue";
        ChainState state = empty();
        ChainPriority priority = new CommandArgumentOptional(new CommandArgumentString("testArgument"),
                defaultValue).cast(state);

        assertEquals("Unexpected priority", ChainPriority.LOW, priority);
        assertEquals("Unexpected parsed argument count", 1, state.getParsedArguments().size());
        assertEquals("Unexpected argument parse value", defaultValue, state.getParsedArguments().get(0));
    }

}
