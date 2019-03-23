import com.github.manevolent.jbot.command.executor.chained.argument.search.Search;
import junit.framework.TestCase;

public class SearchTest extends TestCase {

    public static void main(String[] args) throws Exception {
        new SearchTest().testParser();
    }

    public void testParser() throws Exception {
        Search search = Search.parse("(\"string in command\" \"\\\"\")");
    }

}
