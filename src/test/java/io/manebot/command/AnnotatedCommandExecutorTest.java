package io.manebot.command;

import io.manebot.command.executor.CommandExecutor;
import io.manebot.command.executor.chained.AnnotatedCommandExecutor;
import io.manebot.command.executor.chained.argument.CommandArgumentLabel;
import io.manebot.command.executor.chained.argument.CommandArgumentString;
import junit.framework.TestCase;
import org.junit.Test;


public class AnnotatedCommandExecutorTest {
    @Test
    public void testExecute_Valid() throws Exception {
        CommandExecutor executor = new AnnotatedCommandExecutor() {
            @Command(description = "test description")
            public void test(CommandSender sender,
                             @CommandArgumentLabel.Argument(label = "test") String test,
                             @CommandArgumentString.Argument(label = "anything") String anything) {
                System.err.println(test + ", " + anything);
            }

            @Command(description = "null route")
            public void test(CommandSender sender) {
                System.err.println("null route");
            }
        };

        executor.execute(null, "test", new String[]{});
        executor.execute(null, "test", new String[]{"test", "test 2"});
    }
}
