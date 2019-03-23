package com.github.manevolent.jbot.database.search;

import com.github.manevolent.jbot.command.exception.CommandArgumentException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * <b>Search</b> is the lexical token parsing component of the search argument system.  Search is responsible for
 * interpreting textual search queries during argument parsing.
 *
 * The structures represented by the <b>Search</b> class are transformed into database-friendly clauses by a
 * SearchHandler, using its SearchArgumentHandler bindings, which are defined by implementors of the API.
 */
public class Search {
    private final LexicalClause rootLexicalClause;

    private Search(LexicalClause rootLexicalClause) {
        this.rootLexicalClause = rootLexicalClause;
    }

    /**
     * Gets the root lexical clause encapsulated by this Search instance.
     *
     * @return root lexical clause.
     */
    public LexicalClause getLexicalClause() {
        return rootLexicalClause;
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
        Collection<SearchPredicate> getActions();

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
        private final Collection<SearchPredicate> actions = new LinkedList<>();
        private final LexicalClause parent;
        private final SearchOperator operator;

        PushedLexicalClause(LexicalClause parent, SearchOperator operator) {
            super(null);
            this.parent = parent;

            this.operator = operator;
        }

        public LexicalClause push(SearchOperator operator) {
            PushedLexicalClause clause = new PushedLexicalClause(parent, operator);
            addPredicate(clause);
            return clause;
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
        public Collection<SearchPredicate> getActions() {
            return Collections.unmodifiableCollection(actions);
        }
    }

    public static class Builder implements LexicalClause {
        private final Collection<SearchPredicate> actions = new LinkedList<>();

        Builder() { }

        public Search build() {
            // Simply return a new Search object around the root lexical clause (this)
            return new Search(this);
        }

        @Override
        public void addPredicate(SearchPredicate predicate) {
            actions.add(predicate);
        }

        @Override
        public Collection<SearchPredicate> getActions() {
            return Collections.unmodifiableCollection(actions);
        }

        @Override
        public PushedLexicalClause push(SearchOperator operator) {
            PushedLexicalClause clause = new PushedLexicalClause(this, operator);
            addPredicate(clause);
            return clause;
        }

        @Override
        public LexicalClause pop() {
            throw new IllegalArgumentException("Cannot pop root clause");
        }

        @Override
        public boolean canPop() {
            return false;
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
        private SearchOperator nextOperator = SearchOperator.getDefault();

        /**
         * Count of handled parsers.  Used to know if we should allow a search operator token or not.
         */
        private int handled = 0;

        protected ClauseParser(SearchOperator operator, LexicalClause clause, boolean requireClosingToken) {
            super(null, clause.push(operator));

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
        Search.Builder builder = new Search.Builder(); // Lexical storage
        LexicalClause clause = builder; // redundant, but leaving for clarity of this unboxing
        LexicalParser parser = new ClauseParser(null, clause, false); // if not null, will throw exception

        // Iterate over all characters in the query string.
        for (char c : queryString.toCharArray())
            if (!parser.interpret(c)) throw new IllegalArgumentException("Unexpected end of query");

        // Attempt completion
        parser.complete();

        // Build Search object
        return builder.build();
    }
}