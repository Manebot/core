import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.executor.CommandExecutor;
import com.github.manevolent.jbot.command.executor.chained.AnnotatedCommandExecutor;
import com.github.manevolent.jbot.command.executor.chained.argument.CommandArgumentLabel;
import com.github.manevolent.jbot.command.executor.chained.argument.CommandArgumentString;
import junit.framework.TestCase;


public class AnnotatedCommandExecutorTest extends TestCase {
    public static void main(String[] args) throws Exception {
        new AnnotatedCommandExecutorTest().testParser();
    }

    public void testParser() throws Exception {
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
