package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandArgumentStringTest extends CommandArgumentTest {

    @Test
    public void testCast_OneWord() {
        String value = "one_word";
        ChainState state = parse(value);
        ChainPriority priority = new CommandArgumentString("testLabel").cast(state);

        assertEquals("Unexpected priority", ChainPriority.LOW, priority);
        assertEquals("Unexpected parsed argument count", 1, state.getParsedArguments().size());
        assertEquals("Unexpected argument parse value", value, state.getParsedArguments().get(0));
    }

    @Test
    public void testCast_MultipleWords() {
        String value = "one_word two_words";
        ChainState state = parse(value);
        ChainPriority priority = new CommandArgumentString("testLabel").cast(state);

        assertEquals("Unexpected priority", ChainPriority.LOW, priority);
        assertEquals("Unexpected parsed argument count", 1, state.getParsedArguments().size());
        assertEquals("Unexpected argument parse value", value.split(" ")[0],
                state.getParsedArguments().get(0).toString());
    }

    @Test
    public void testCast_NoWords() {
        String value = "";
        ChainState state = parse(value);
        ChainPriority priority = new CommandArgumentString("testLabel").cast(state);

        assertEquals("Unexpected priority", ChainPriority.LOW, priority);
        assertEquals("Unexpected parsed argument count", 1, state.getParsedArguments().size());
        assertEquals("Unexpected argument parse value", value, state.getParsedArguments().get(0));
    }

}
