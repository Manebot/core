package com.github.manevolent.jbot.database.search.handler;

import com.github.manevolent.jbot.database.search.SearchArgument;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Function;

public class SearchHandlerPropertyNumeric extends SearchHandlerEntityProperty {
    public SearchHandlerPropertyNumeric(Function<Root, Path> pathFinder) {
        super(pathFinder);
    }

    public SearchHandlerPropertyNumeric(String property) {
        super(property);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Predicate handle(Path path, CriteriaBuilder criteriaBuilder, SearchArgument value) {
        if (value.getValue().length() <= 0) throw new IllegalArgumentException("Numeric expression not provided.");
        char c = value.getValue().charAt(0);

        try {
            if (c == '>') {
                return criteriaBuilder.greaterThan(path, value.getValue().substring(1));
            } else if (c == '<') {
                return criteriaBuilder.lessThan(path, value.getValue().substring(1));
            } else if (c == '=') {
                return criteriaBuilder.equal(path, value.getValue().substring(1));
            } else if (c == '!') {
                return criteriaBuilder.equal(path, value.getValue().substring(1));
            } else {
                return criteriaBuilder.equal(path, value.getValue());
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(ex.getMessage()); // unboxing is necessary to propagate message
        }
    }
}
