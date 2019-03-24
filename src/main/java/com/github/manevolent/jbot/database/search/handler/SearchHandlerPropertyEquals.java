package com.github.manevolent.jbot.database.search.handler;

import com.github.manevolent.jbot.database.search.SearchArgument;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Function;

public class SearchHandlerPropertyEquals extends SearchHandlerEntityProperty {
    public SearchHandlerPropertyEquals(Function<Root, Path> pathFinder) {
        super(pathFinder);
    }

    public SearchHandlerPropertyEquals(String property) {
        super(property);
    }

    @Override
    protected Predicate handle(Path path, CriteriaBuilder criteriaBuilder, SearchArgument value) {
        return criteriaBuilder.equal(path, value.getValue());
    }
}
