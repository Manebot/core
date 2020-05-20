package io.manebot.database.search;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class SearchTest {
    private static final String sortFormat = "sort:%s:%s";
    private static final String pageFormat = "page:%s";

    @Test
    public void testParse_Empty() {
        Search search = Search.parse("");

        assertEquals("Unexpected search page", search.getPage(), 1);
        assertEquals("Unexpected search order", search.getOrders().size(), 0);

        List<SearchPredicate> actions = search.getLexicalClause().getActions();
        assertEquals("Unexpected search clauses: should only be one root predicate", actions.size(), 1);
        assertEquals("Unexpected root clause class type", actions.get(0).getClass(), Search.PushedLexicalClause.class);

        Search.PushedLexicalClause rootClause = (Search.PushedLexicalClause) actions.get(0);

        assertEquals("Unexpected search argument on root clause", rootClause.getOperator(), SearchOperator.UNSPECIFIED);
        assertNull("Unexpected search argument on root clause", rootClause.getArgument());
        assertEquals("Unexpected action set on root clause", rootClause.getActions().size(), 0);
    }

    @Test
    public void testParse_Page_Positive() {
        for (int pageNumber = 1; pageNumber <= 32; pageNumber ++) {
            Search search = Search.parse(String.format(pageFormat, Integer.toString(pageNumber)));
            assertEquals("Unexpected search page", search.getPage(), pageNumber);
        }
    }

    @Test
    public void testParse_Page_Invalid() {
        try {
            Search.parse(String.format(pageFormat, "not_a_number"));
            throw new AssertionError("Search did not fail for a given invalid page number");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        try {
            Search.parse(String.format(pageFormat, Integer.toString(1)) + " lastItem");
            throw new AssertionError("Search did not fail for a given invalid page number in search string");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        try {
            Search.parse(String.format(pageFormat, Integer.toString(1)) +
                    String.format(pageFormat, Integer.toString(1)));
            throw new AssertionError("Search did not fail for having multiple page numbers in search string");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        try {
            Search.parse("-" + String.format(pageFormat, Integer.toString(1)));
            throw new AssertionError("Search did not fail for having invalid operator on a page number in search string");
        } catch (IllegalArgumentException expected) {
            // Expected
        }
    }

    @Test
    public void testParse_Ordered_Valid() {
        String searchKey = "fieldKey";

        for (SortOrder searchOrder : SortOrder.values()) {
            for (String sortOrderKey : searchOrder.getKeys()) {
                Search search = Search.parse(String.format(sortFormat, searchKey, sortOrderKey));

                assertEquals("Unexpected search page", search.getPage(), 1);
                assertEquals("Unexpected search order", search.getOrders().size(), 1);

                List<SearchPredicate> actions = search.getLexicalClause().getActions();
                assertEquals("Unexpected search clauses: should only be one root predicate", actions.size(), 1);
                assertEquals("Unexpected root clause class type", actions.get(0).getClass(),
                        Search.PushedLexicalClause.class);

                Search.PushedLexicalClause rootClause = (Search.PushedLexicalClause) actions.get(0);

                assertNull("Unexpected search argument on root clause", rootClause.getArgument());
                assertEquals("Unexpected action set on root clause", rootClause.getActions().size(), 0);

                Search.Order resultingSearchOrder = search.getOrders().stream().findFirst()
                        .orElseThrow(() -> new AssertionError("Unexpected end of stream"));

                assertEquals("Unexpected search order key", searchKey, resultingSearchOrder.getKey());
                assertEquals("Unexpected search order for key " + sortOrderKey,
                        searchOrder, resultingSearchOrder.getOrder());
            }
        }
    }

    @Test
    public void testParse_Ordered_Invalid() {
        try {
            Search.parse(String.format(sortFormat, "validKey", "invalidKey"));
            throw new AssertionError("Search did not fail for a given invalid search order key");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        try {
            Search.parse(String.format(sortFormat, "validKey", "asc") + " " +
                    String.format(sortFormat, "validKey", "asc"));
            throw new AssertionError("Search did not fail for a multiple valid search order keys");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        try {
            Search.parse("-" + String.format(sortFormat, "validKey", "asc"));
            throw new AssertionError("Search did not fail for invalid operator on valid search order key");
        } catch (IllegalArgumentException expected) {
            // Expected
        }
    }

    @Test
    public void testParse_String() {
        String value = "testValue";

        Search search = Search.parse("\"" + value + "\"");

        assertEquals("Unexpected search page", search.getPage(), 1);
        assertEquals("Unexpected search order", search.getOrders().size(), 0);

        List<SearchPredicate> actions = search.getLexicalClause().getActions();
        assertEquals("Unexpected search clauses: should only be one root predicate", actions.size(), 1);
        assertEquals("Unexpected root clause class type", actions.get(0).getClass(), Search.PushedLexicalClause.class);

        Search.PushedLexicalClause rootClause = (Search.PushedLexicalClause) actions.get(0);

        assertNull("Unexpected search argument on root clause", rootClause.getArgument());
        assertEquals("Unexpected action set on root clause", rootClause.getActions().size(), 1);
        assertEquals("Unexpected action class in root clause", rootClause.getActions().get(0).getClass(),
                SearchPredicateString.class);

        SearchPredicateString predicate = (SearchPredicateString) rootClause.getActions().get(0);
        assertEquals("Unexpected predicate operator", predicate.getArgument().getOperator(), SearchOperator.UNSPECIFIED);
        assertEquals("Unexpected predicate value", predicate.getArgument().getValue(), value);
    }

    @Test
    public void testParse_String_Escaped() {
        String valueEscaped = "testValue\\\"";
        String valueReal = "testValue\"";

        Search search = Search.parse("\"" + valueEscaped + "\"");
        List<SearchPredicate> actions = search.getLexicalClause().getActions();
        Search.PushedLexicalClause rootClause = (Search.PushedLexicalClause) actions.get(0);
        SearchPredicateString predicate = (SearchPredicateString) rootClause.getActions().get(0);
        assertEquals("Unexpected predicate operator", predicate.getArgument().getOperator(), SearchOperator.UNSPECIFIED);
        assertEquals("Unexpected predicate value", predicate.getArgument().getValue(), valueReal);
    }

    @Test
    public void testParse_String_Invalid() {
        try {
            Search.parse("\"invalidUnclosedString");
            throw new AssertionError("Search did not fail for a given invalid, unclosed string.");
        } catch (IllegalArgumentException expected) {
            // Expected
        }

        try {
            Search.parse("\"\"\"\"\"");
            throw new AssertionError("Search did not fail for a given invalid, unclosed string.");
        } catch (IllegalArgumentException expected) {
            // Expected
        }
    }

    @Test
    public void testParse_Clause_Invalid() {
        try {
            Search.parse("validArgument)");
            throw new AssertionError("Search did not fail for a given unbalanced parenthesis.");
        } catch (IllegalArgumentException expected) {
            // Expected
        }


        try {
            Search.parse("((validArgument)");
            throw new AssertionError("Search did not fail for a given unbalanced parenthesis.");
        } catch (IllegalArgumentException expected) {
            // Expected
        }
    }

    @Test
    public void testParse_Argument() {
        String value = "testArgument";

        Search search = Search.parse(value);

        assertEquals("Unexpected search page", search.getPage(), 1);
        assertEquals("Unexpected search order", search.getOrders().size(), 0);

        List<SearchPredicate> actions = search.getLexicalClause().getActions();
        assertEquals("Unexpected search clauses: should only be one root predicate", actions.size(), 1);
        assertEquals("Unexpected root clause class type", actions.get(0).getClass(), Search.PushedLexicalClause.class);

        Search.PushedLexicalClause rootClause = (Search.PushedLexicalClause) actions.get(0);

        assertNull("Unexpected search argument on root clause", rootClause.getArgument());
        assertEquals("Unexpected action set on root clause", rootClause.getActions().size(), 1);
        assertEquals("Unexpected action class in root clause", rootClause.getActions().get(0).getClass(),
                SearchPredicateArgument.class);

        SearchPredicateArgument predicate = (SearchPredicateArgument) rootClause.getActions().get(0);
        assertEquals("Unexpected predicate operator", predicate.getArgument().getOperator(), SearchOperator.UNSPECIFIED);
        assertEquals("Unexpected predicate value", predicate.getArgument().getValue(), value);
    }

    @Test
    public void testParse_Operator_Invalid() {
        Set<Character> characters =
                Arrays.stream(SearchOperator.values())
                        .filter(SearchOperator::canSpecify)
                        .map(SearchOperator::getCharacter).collect(Collectors.toSet());

        for (Character a : characters) {
            for (Character b : characters) {
                try {
                    String prefix = a.toString() + b.toString();
                    Search.parse(prefix + "validArgument");
                    throw new AssertionError("Search did not fail for a given invalid argument " +
                            "with multiple invalid operators \"" + prefix + "\".");
                } catch (IllegalArgumentException expected) {
                    // Expected
                }
            }
        }


        Set<Character> unspecifiableCharacters =
                Arrays.stream(SearchOperator.values())
                        .filter(searchOperator -> !searchOperator.canSpecify())
                        .map(SearchOperator::getCharacter).collect(Collectors.toSet());

        for (Character unspecifiable : characters) {
            try {
                Search.parse(unspecifiable + "validArgument");
                throw new AssertionError("Search did not fail for a given invalid argument " +
                        "with given unspecifiable operator \"" + unspecifiable + "\".");
            } catch (IllegalArgumentException expected) {
                // Expected
            }
        }
    }

    @Test
    public void testParse_Operator_Valid() {
        String firstArgument = "firstArgument", secondArgument = "secondArgument";

        for (SearchOperator operator : Arrays.stream(SearchOperator.values()).filter(SearchOperator::canSpecify)
                .collect(Collectors.toSet())) {
            Search search = Search.parse(firstArgument + " " + operator.getCharacter() + secondArgument);

            List<SearchPredicate> actions = search.getLexicalClause().getActions();
            assertEquals("Unexpected search clauses: should only be one root predicate", actions.size(), 1);
            assertEquals("Unexpected root clause class type", actions.get(0).getClass(), Search.PushedLexicalClause.class);

            Search.PushedLexicalClause rootClause = (Search.PushedLexicalClause) actions.get(0);

            SearchPredicateArgument predicate1 = (SearchPredicateArgument) rootClause.getActions().get(0);
            assertEquals("Unexpected value on first predicate", predicate1.getArgument().getValue(),
                    firstArgument);
            assertEquals("Unexpected operator on first predicate", predicate1.getArgument().getOperator(),
                    SearchOperator.UNSPECIFIED);

            SearchPredicateArgument predicate2 = (SearchPredicateArgument) rootClause.getActions().get(1);
            assertEquals("Unexpected value on second predicate", predicate2.getArgument().getValue(),
                    secondArgument);
            assertEquals("Unexpected operator on second predicate", predicate2.getArgument().getOperator(),
                    operator);
        }
    }

    @Test
    public void testParse_MultipleArguments() {
        String value1 = "testArgument1";
        String value2 = "testArgument2";

        Search search = Search.parse(value1 + " \"" + value2 + "\"");

        assertEquals("Unexpected search page", search.getPage(), 1);
        assertEquals("Unexpected search order", search.getOrders().size(), 0);

        List<SearchPredicate> actions = search.getLexicalClause().getActions();
        assertEquals("Unexpected search clauses: should only be one root predicate", actions.size(), 1);
        assertEquals("Unexpected root clause class type", actions.get(0).getClass(), Search.PushedLexicalClause.class);

        Search.PushedLexicalClause rootClause = (Search.PushedLexicalClause) actions.get(0);

        assertNull("Unexpected search argument on root clause", rootClause.getArgument());
        assertEquals("Unexpected action set on root clause", rootClause.getActions().size(), 2);

        assertEquals("Unexpected action class in root clause", rootClause.getActions().get(0).getClass(),
                SearchPredicateArgument.class);

        SearchPredicateArgument predicate1 = (SearchPredicateArgument) rootClause.getActions().get(0);
        assertEquals("Unexpected predicate operator", predicate1.getArgument().getOperator(), SearchOperator.UNSPECIFIED);
        assertEquals("Unexpected predicate value", predicate1.getArgument().getValue(), value1);

        assertEquals("Unexpected action class in root clause", rootClause.getActions().get(1).getClass(),
                SearchPredicateString.class);

        SearchPredicateString predicate2 = (SearchPredicateString) rootClause.getActions().get(1);
        assertEquals("Unexpected predicate operator", predicate2.getArgument().getOperator(), SearchOperator.INCLUDE);
        assertEquals("Unexpected predicate value", predicate2.getArgument().getValue(), value2);
    }

    @Test
    public void testParse_GroupArguments() {
        String value1 = "testArgument1";
        String value2 = "testArgument2";
        String value3 = "testArgument3";

        Search search = Search.parse(value1 + " -(\"" + value2 + "\" " + value3 + ")");

        assertEquals("Unexpected search page", search.getPage(), 1);
        assertEquals("Unexpected search order", search.getOrders().size(), 0);

        List<SearchPredicate> actions = search.getLexicalClause().getActions();
        assertEquals("Unexpected search clauses: should only be one root predicate", actions.size(), 1);
        assertEquals("Unexpected root clause class type", actions.get(0).getClass(), Search.PushedLexicalClause.class);

        Search.PushedLexicalClause rootClause = (Search.PushedLexicalClause) actions.get(0);

        assertNull("Unexpected search argument on root clause", rootClause.getArgument());
        assertEquals("Unexpected action set on root clause", rootClause.getActions().size(), 2);

        assertEquals("Unexpected action class in root clause", rootClause.getActions().get(0).getClass(),
                SearchPredicateArgument.class);

        SearchPredicateArgument predicate1 = (SearchPredicateArgument) rootClause.getActions().get(0);
        assertEquals("Unexpected predicate operator", predicate1.getArgument().getOperator(), SearchOperator.UNSPECIFIED);
        assertEquals("Unexpected predicate value", predicate1.getArgument().getValue(), value1);

        assertEquals("Unexpected action class in root clause", rootClause.getActions().get(1).getClass(),
                Search.PushedLexicalClause.class);

        Search.PushedLexicalClause predicate2 = (Search.PushedLexicalClause) rootClause.getActions().get(1);
        assertNull("Unexpected search argument in group", predicate2.getArgument());
        assertEquals("Unexpected action set in group", predicate2.getActions().size(), 2);
        assertEquals("Unexpected search operator in group", predicate2.getOperator(), SearchOperator.EXCLUDE);

        assertEquals("Unexpected action class in group", predicate2.getActions().get(0).getClass(),
                SearchPredicateString.class);

        SearchPredicateString predicate3 = (SearchPredicateString) predicate2.getActions().get(0);
        assertEquals("Unexpected predicate operator", predicate3.getArgument().getOperator(), SearchOperator.UNSPECIFIED);
        assertEquals("Unexpected predicate value", predicate3.getArgument().getValue(), value2);

        assertEquals("Unexpected action class in root clause", predicate2.getActions().get(1).getClass(),
                SearchPredicateArgument.class);

        SearchPredicateArgument predicate4 = (SearchPredicateArgument) predicate2.getActions().get(1);
        assertEquals("Unexpected predicate operator", predicate4.getArgument().getOperator(), SearchOperator.INCLUDE);
        assertEquals("Unexpected predicate value", predicate4.getArgument().getValue(), value3);
    }
}
