import io.manebot.database.search.Search;
import junit.framework.TestCase;

public class SearchTest extends TestCase {

    public static void main(String[] args) throws Exception {
        new SearchTest().testParser();
    }

    public void testParser() throws Exception {
        Search search = Search.parse("(\"string in command\" \"\\\"\") (page:2)");
    }

}
