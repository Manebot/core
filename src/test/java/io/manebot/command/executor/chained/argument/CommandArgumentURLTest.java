package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainPriority;
import io.manebot.command.executor.chained.ChainState;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class CommandArgumentURLTest extends CommandArgumentTest {

    @Test
    public void testCast_Provided() throws MalformedURLException {
        URL url = URI.create("http://manebot.io/").toURL();

        ChainState state = parse(url.toExternalForm());
        ChainPriority priority = new CommandArgumentURL().cast(state);

        assertEquals("Unexpected priority", ChainPriority.HIGH, priority);
        assertEquals("Unexpected parsed argument count", 1, state.getParsedArguments().size());
        assertEquals("Unexpected argument parse value", url, state.getParsedArguments().get(0));
    }

    @Test
    public void testCast_Provided_Invalid() throws MalformedURLException {
        ChainState state = parse("http_invalid://manebot.io/");
        ChainPriority priority = new CommandArgumentURL().cast(state);

        assertEquals("Unexpected priority", ChainPriority.NONE, priority);
        assertEquals("Unexpected parsed argument count", 0, state.getParsedArguments().size());
    }

    @Test
    public void testCast_NotProvided() {
        ChainState state = empty();
        ChainPriority priority = new CommandArgumentURL().cast(state);

        assertEquals("Unexpected priority", ChainPriority.NONE, priority);
        assertEquals("Unexpected parsed argument count", 0, state.getParsedArguments().size());
    }

}
