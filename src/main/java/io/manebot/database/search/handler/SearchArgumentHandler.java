package io.manebot.database.search.handler;

import io.manebot.database.search.SearchArgument;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Expresses a search argument handler.  Search argument handlers are the JPA-level token constructors that build
 * a JPA query from lexically-parsed <b>SearchArguments</b>.  These are called by a SearchHandler implementation.
 */
public interface SearchArgumentHandler {
    Predicate handle(Root root, CriteriaBuilder criteriaBuilder, SearchArgument value);

    default SearchArgumentHandler not() {
        return (root, criteriaBuilder, value) -> SearchArgumentHandler.this.handle(root, criteriaBuilder, value).not();
    }
}
