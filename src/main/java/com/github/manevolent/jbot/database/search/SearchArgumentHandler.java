package com.github.manevolent.jbot.database.search;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 * Expresses a search argument handler.  Search argument handlers are the JPA-level token constructors that build
 * a JPA query from lexically-parsed <b>SearchArguments</b>.  These are called by a SearchHandler implementation.
 */
public interface SearchArgumentHandler {
    Expression<Boolean> handle(Root root, CriteriaBuilder criteriaBuilder, SearchArgument value);
}
