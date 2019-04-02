package io.manebot.database.search;

/**
 * Describes a lexically-parsed search argument, which is used to build a JPA query clause around a string text
 * definition.
 */
public class SearchPredicateString extends SearchPredicate {
    SearchPredicateString(SearchArgument argument) {
        super(argument);
    }

    @Override
    public void handle(SearchHandler.Clause clause) throws IllegalArgumentException {
        SearchArgumentHandler handler = clause.getSearchHandler().getStringHandler();
        if (handler == null) throw new IllegalArgumentException("This search does not handle string arguments.");

        clause.addPredicate(
                getArgument().getOperator(),
                handler.handle(clause.getRoot(), clause.getCriteriaBuilder(), getArgument())
        );
    }
}
