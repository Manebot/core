package io.manebot.database.search;

/**
 * Search predicates are lexically-parsed tokens that describe individual actions taken to build a JPA query clause.
 * These are defined explicitly within the API itself, and are not a component of query execution itself.
 */
public abstract class SearchPredicate {
    private final SearchArgument argument;

    SearchPredicate(SearchArgument argument) {
        this.argument = argument;
    }

    public SearchArgument getArgument() {
        return argument;
    }

    public abstract void handle(SearchHandler.Clause clause) throws IllegalArgumentException;
}
