package io.manebot.database.search;

import java.util.*;

/**
 * <b>Search</b> is the lexical token parsing component of the search argument system.  Search is responsible for
 * interpreting textual search queries during argument parsing.
 *
 * The structures represented by the <b>Search</b> class are transformed into database-friendly clauses by a
 * SearchHandler, using its SearchArgumentHandler bindings, which are defined by implementors of the API.
 */
public final class Search {
    public static final Search EMPTY = new Search(
            Collections.emptyList(),
            new PushedLexicalClause(null, SearchOperator.UNSPECIFIED),
            1
    );

    private final Collection<Order> orders;
    private final LexicalClause rootLexicalClause;
    private int page = 1;

    private Search(Collection<Order> orders, LexicalClause rootLexicalClause, int page) {
        this.orders = orders;
        this.rootLexicalClause = rootLexicalClause;
        this.page = page;
    }

    /**
     * Gets the orders that this search has.
     * @return collection of order statements.
     */
    public Collection<Order> getOrders() {
        return orders;
    }

    /**
     * Gets the root lexical clause encapsulated by this Search instance.
     *
     * @return root lexical clause.
     */
    public LexicalClause getLexicalClause() {
        return rootLexicalClause;
    }

    public Search withPage(int page) {
        return new Search(orders, rootLexicalClause, page);
    }

    public Search withOrders(Collection<Order> orders) {
        return new Search(Collections.unmodifiableCollection(orders), rootLexicalClause, page);
    }

    public Search withOrders(Order... orders) {
        return withOrders(Arrays.asList(orders));
    }

    public int getPage() {
        return page;
    }

    public interface Order {
        String getKey();
        SortOrder getOrder();
    }

    public static class DefaultOrder implements Order {
        private final String key;
        private final SortOrder order;

        public DefaultOrder(String key, SortOrder order) {
            this.key = key;
            this.order = order;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public SortOrder getOrder() {
            return order;
        }
    }

    public interface LexicalClause {

        /**
         * Adds a predicate to this clause.
         * @param predicate predicate.
         */
        void addPredicate(SearchPredicate predicate);

        /**
         * Gets a collection of actions on this clause.
         * @return
         */
        List<SearchPredicate> getActions();

        /**
         * Pushes a lexical clause onto the clause stack.
         * @param operator SearchOperator prefix for this clause.
         * @return LexicalClause instance.
         */
        LexicalClause push(SearchOperator operator);

        /**
         * Pops a lexical clause off of the clause stack.
         * @return parent clause.
         */
        LexicalClause pop();

        /**
         * Finds if the clause can pop.
         * @return true if the clause can pop, false otherwise.
         */
        boolean canPop();
    }

    private static class PushedLexicalClause extends SearchPredicate implements LexicalClause {
        private final List<SearchPredicate> actions = new LinkedList<>();
        private final LexicalClause parent;
        private final SearchOperator operator;

        PushedLexicalClause(LexicalClause parent, SearchOperator operator) {
            super(null);

            this.parent = parent;
            this.operator = operator;
        }

        public LexicalClause push(PushedLexicalClause clause) {
            addPredicate(clause);
            return clause;
        }

        public LexicalClause push(SearchOperator operator) {
            return push(new PushedLexicalClause(parent, operator));
        }

        @Override
        public LexicalClause pop() {
            if (parent == null) throw new IllegalArgumentException("Cannot pop clause: no parent");
            return parent;
        }

        @Override
        public boolean canPop() {
            return parent != null;
        }

        public void addPredicate(SearchPredicate predicate) {
            actions.add(predicate);
        }

        @Override
        public void handle(SearchHandler.Clause clause) throws IllegalArgumentException {
            SearchHandler.Clause searchClause = clause.push(operator);
            for (SearchPredicate action : actions) action.handle(searchClause);
            searchClause.pop();
        }

        @Override
        public List<SearchPredicate> getActions() {
            return actions;
        }

        SearchPredicate getLastAction() {
            return getActions().stream().reduce((first, second) -> second).orElse(null);
        }
    }

    public static class Builder extends PushedLexicalClause implements LexicalClause {
        private int page = 1; // default page is 1, of course
        private Collection<Order> orders = new LinkedList<>();

        public Builder() {
            super(null, SearchOperator.UNSPECIFIED);
        }

        public Search build() {
            // Simply return a new Search object around the root lexical clause (this)
            return new Search(orders, this, page);
        }

        @Override
        public LexicalClause push(SearchOperator operator) {
            return push(new PushedLexicalClause(this, operator));
        }

        @Override
        public LexicalClause pop() {
            throw new IllegalArgumentException("Cannot pop root clause");
        }

        @Override
        public boolean canPop() {
            return false;
        }

        public int getPage() {
            return page;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Collection<Order> getOrders() {
            return orders;
        }

        public Builder order(Order order) {
            this.orders.add(order);
            return this;
        }
    }

    private interface LexicalParser {
        /**
         * Attempts to complete the parser, such as when a query string has ended.
         */
        void complete() throws IllegalArgumentException;

        /**
         * Interprets a single character.
         * @param c Character to interpret.
         * @return true if further parsing is needed, false otherwise.
         * @throws IllegalArgumentException if there was a problem interpreting the given character.
         */
        boolean interpret(char c) throws IllegalArgumentException;
    }

    private static abstract class AbstractParser implements LexicalParser {
        private final SearchOperator operator;
        private final LexicalClause clause;

        protected AbstractParser(SearchOperator operator, LexicalClause clause) {
            this.operator = operator;
            this.clause = clause;
        }

        protected SearchOperator getOperator() {
            return operator;
        }

        protected LexicalClause getClause() {
            return clause;
        }
    }

    private static class StringParser extends AbstractParser {
        static final char ASSOCIATED_CHARACTER = '"';
        private static final char escapeCharacter = '\\';

        private final StringBuilder builder = new StringBuilder();
        private boolean escape = false;

        protected StringParser(SearchOperator operator, LexicalClause clause) {
            super(operator, clause);
        }

        @Override
        public void complete() {
            throw new IllegalArgumentException("Unexpected end of string");
        }

        @Override
        public boolean interpret(char c) throws IllegalArgumentException {
            if (!escape && c == ASSOCIATED_CHARACTER) {
                getClause().addPredicate(new SearchPredicateString(new SearchArgument(
                        getOperator(),
                        builder.toString()
                )));

                return false; // done
            }

            if (c == escapeCharacter) {
                if (escape) {
                    builder.append(c);
                } else {
                    escape = true;
                }
            } else {
                builder.append(c);
                escape = false;
            }

            return true; // need more
        }
    }

    private static class CommandParser extends AbstractParser {
        private final StringBuilder builder = new StringBuilder();

        protected CommandParser(SearchOperator operator, LexicalClause clause) {
            super(operator, clause);
        }

        @Override
        public void complete() {
            // Don't argue.
            getClause().addPredicate(new SearchPredicateArgument(
                    new SearchArgument(getOperator(), builder.toString())
            ));
        }

        @Override
        public boolean interpret(char c) throws IllegalArgumentException {
            if (Character.isWhitespace(c)) {
                complete();
                return false;
            } else {
                builder.append(c);
                return true;
            }
        }
    }

    /**
     * Introspective self-parsing clause parser
     */
    private static class ClauseParser extends AbstractParser {
        static final char ASSOCIATED_CHARACTER = '(';
        private static final char closingCharacter = ')';
        private final boolean requireClosingToken;

        /**
         * Current lexical parser instance.  When null, the <b>ClauseParser</b> seeks for the next character which
         * indicates the type of parser that should be constructed.
         */
        private LexicalParser parser = null;

        /**
         * Operator to apply to the next <b>LexicalParser</b> instantiated.
         */
        private SearchOperator nextOperator = SearchOperator.UNSPECIFIED; // otherwise will throw exception

        /**
         * Count of handled parsers.  Used to know if we should allow a search operator token or not.
         */
        private int handled = 0;

        protected ClauseParser(SearchOperator operator, LexicalClause clause, boolean requireClosingToken) {
            super(SearchOperator.UNSPECIFIED, clause.push(operator));

            this.requireClosingToken = requireClosingToken;
        }

        private void completeIntl() {
            // Attempt to end the current parser.
            if (parser != null) parser.complete();

            // Pop current clause
            getClause().pop();
        }

        @Override
        public void complete() {
            if (requireClosingToken) throw new IllegalArgumentException("Unexpected end of clause");

            completeIntl();
        }

        @Override
        public boolean interpret(char c) throws IllegalArgumentException {
            boolean parseExternally = parser == null || (parser instanceof CommandParser && c == closingCharacter);
            if (parseExternally) {
                // Skip whitespaces
                if (Character.isWhitespace(c)) return true; // continue parsing

                switch (c) {
                    case StringParser.ASSOCIATED_CHARACTER: // Open string parser
                        parser = new StringParser(nextOperator, getClause());
                        nextOperator = SearchOperator.getDefault();
                        break;
                    case '~':
                        if (handled <= 0)
                            throw new IllegalArgumentException("Unexpected token: " + c);
                        nextOperator = SearchOperator.INCLUDE;
                        break;
                    case '+':
                        if (handled <= 0)
                            throw new IllegalArgumentException("Unexpected token: " + c);
                        nextOperator = SearchOperator.MERGE;
                        break;
                    case '-':
                        if (handled <= 0)
                            throw new IllegalArgumentException("Unexpected token: " + c);
                        nextOperator = SearchOperator.EXCLUDE;
                        break;
                    case ASSOCIATED_CHARACTER: // Open new clause parser
                        parser = new ClauseParser(nextOperator, getClause(), true);
                        break;
                    case closingCharacter: // Close current clause parser
                        if (!requireClosingToken) throw new IllegalArgumentException("Unexpected end of clause");
                        completeIntl();
                        return false;
                    default:
                        parser = new CommandParser(nextOperator, getClause());
                        parser.interpret(c); // must be done, or we lose this character.
                        break;
                }

                return true; // continue
            } else {
                boolean completed = !parser.interpret(c);
                if (completed) {
                    parser = null;
                    nextOperator = SearchOperator.getDefault();
                    handled ++;
                }

                return true; // continue
            }
        }
    }

    /**
     * Lexically parses a <i>queryString</i> and forms a machine-readable clause structure from the input.
     *
     * Most of the processing logical processing itself occurs in private class <b>ClauseParser</b>, as the
     * string itself is a clause.
     *
     * @param queryString query string to parse.
     * @return Search object, encapsulating the Clause structure.
     * @throws IllegalArgumentException if there was a problem interpreting the given query string.
     */
    public static Search parse(String queryString) throws IllegalArgumentException {
        Search.Builder builder = new Search.Builder();
        LexicalParser parser = new ClauseParser(SearchOperator.UNSPECIFIED, builder, false);

        // Iterate over all characters in the query string.
        for (char c : queryString.toCharArray())
            if (!parser.interpret(c)) throw new IllegalArgumentException("Unexpected end of query");

        // Attempt completion
        parser.complete();

        // Bit of a hack... search for "page" argument at the very end of the parser and "sort" as well.
        PushedLexicalClause thisClause = builder;
        while (true) {
            SearchPredicate nextClause = thisClause.getLastAction();
            if ((nextClause instanceof PushedLexicalClause))
                thisClause = (PushedLexicalClause) nextClause;
            else
                break;
        }

        ListIterator<SearchPredicate> iterator = thisClause.getActions().listIterator(thisClause.getActions().size());
        List<Order> orders = new ArrayList<>();
        boolean first = true;

        while (iterator.hasPrevious()) {
            SearchPredicate predicate = iterator.previous();

            if (predicate instanceof SearchPredicateArgument) {
                String text = predicate.getArgument().getValue();
                if (text.startsWith("page:")) {
                    if (!first) throw new IllegalStateException(
                            "Page argument must be last in argument list: \"" + text + "\"");
                    builder.page(Integer.parseInt(text.substring(5)));
                    iterator.remove();
                } else if (text.startsWith("p:")) {
                    if (!first) throw new IllegalStateException(
                            "Page argument must be last in argument list: \"" + text + "\"");
                    builder.page(Integer.parseInt(text.substring(2)));
                    iterator.remove();
                } else if (text.startsWith("sort:")) {
                    String[] parts = text.split("\\:", 3);

                    String key = parts[1].trim();
                    String orderString = parts.length >= 3 ? parts[2].trim().toLowerCase() : "asc";
                    SortOrder order;
                    switch (orderString) {
                        case "a":
                        case "asc":
                        case "ascending":
                        case "ascend":
                            order = SortOrder.ASCENDING;
                            break;
                        case "d":
                        case "desc":
                        case "descending":
                        case "descend":
                            order = SortOrder.DESCENDING;
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown sort order: \"" + orderString + "\"");
                    }

                    orders.add(new DefaultOrder(key, order));
                    iterator.remove();
                } else {
                    break;
                }
            }

            first = false;
        }

        // We traversed in reverse
        Collections.reverse(orders);
        orders.forEach(builder::order);

        // Build Search object
        return builder.build();
    }
}