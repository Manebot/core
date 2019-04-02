package io.manebot.database.search.handler;

import io.manebot.database.expressions.ExtendedExpressions;
import io.manebot.database.expressions.MatchMode;
import io.manebot.database.search.SearchArgument;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Function;

public class SearchHandlerPropertyContains extends SearchHandlerEntityProperty {
    public SearchHandlerPropertyContains(Function<Root, Path> pathFinder) {
        super(pathFinder);
    }

    public SearchHandlerPropertyContains(String property) {
        super(property);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Predicate handle(Path path, CriteriaBuilder criteriaBuilder, SearchArgument value) {
        return ExtendedExpressions.escapedLike(
                criteriaBuilder,
                path,
                value.getValue(),
                MatchMode.ANYWHERE
        );
    }
}
