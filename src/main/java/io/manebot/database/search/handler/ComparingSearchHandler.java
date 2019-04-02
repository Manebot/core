package io.manebot.database.search.handler;

import io.manebot.database.search.SearchArgument;
import io.manebot.database.search.SearchArgumentHandler;
import io.manebot.database.search.SearchOperator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ComparingSearchHandler implements SearchArgumentHandler {
    private final SearchOperator operator;
    private final SearchArgumentHandler a, b;

    public ComparingSearchHandler(SearchArgumentHandler a, SearchArgumentHandler b, SearchOperator operator) {
        this.operator = operator;
        this.a = a;
        this.b = b;
    }

    @Override
    public Predicate handle(Root root, CriteriaBuilder criteriaBuilder, SearchArgument value) {
        Predicate first = a.handle(root, criteriaBuilder, value);
        Predicate second = b.handle(root, criteriaBuilder, value);

        Predicate result;

        switch (operator) {
            case MERGE:
                result = criteriaBuilder.and(first, second);
                break;
            case INCLUDE:
                result = criteriaBuilder.or(first, second);
                break;
            case EXCLUDE:
                result = criteriaBuilder.and(first, second.not());
                break;
            default:
                throw new IllegalArgumentException("Illegal compound predicate operator: " + operator);
        }

        return result;
    }
}
