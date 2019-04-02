package io.manebot.database.search.handler;

import io.manebot.database.search.SearchArgument;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Function;

public class SearchHandlerPropertyIsNull extends SearchHandlerEntityProperty {

    public SearchHandlerPropertyIsNull(Function<Root, Path> pathFinder) {
        super(pathFinder);
    }

    public SearchHandlerPropertyIsNull(String property) {
        super(property);
    }

    @Override
    protected Predicate handle(Path path, CriteriaBuilder criteriaBuilder, SearchArgument value) {
        return criteriaBuilder.isNull(path);
    }
}
