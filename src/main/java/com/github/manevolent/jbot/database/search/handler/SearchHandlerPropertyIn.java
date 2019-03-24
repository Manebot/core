package com.github.manevolent.jbot.database.search.handler;

import com.github.manevolent.jbot.database.search.SearchArgument;
import com.github.manevolent.jbot.database.search.SearchArgumentHandler;

import javax.persistence.criteria.*;
import java.util.function.Function;

public class SearchHandlerPropertyIn extends SearchHandlerEntityProperty {
    private final Class<?> joiningType;
    private final SearchArgumentHandler handler;
    private final Function<Root, Path> subPathFinder;

    public SearchHandlerPropertyIn(Function<Root, Path> pathFinder,
                                   Class<?> joiningType,
                                   Function<Root, Path> subPathFinder,
                                   SearchArgumentHandler handler) {
        super(pathFinder);

        this.subPathFinder = subPathFinder;
        this.joiningType = joiningType;
        this.handler = handler;
    }

    public SearchHandlerPropertyIn(String property,
                                   Class<?> joiningType,
                                   Function<Root, Path> subPathFinder,
                                   SearchArgumentHandler handler) {
        super(property);

        this.subPathFinder = subPathFinder;
        this.joiningType = joiningType;
        this.handler = handler;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Predicate handle(Path path, CriteriaBuilder criteriaBuilder, SearchArgument value) {
        Subquery criteriaQuery = criteriaBuilder.createQuery(joiningType).subquery(joiningType);
        Root root = criteriaQuery.from(joiningType);
        return path.in(criteriaQuery
                        .select(subPathFinder.apply(root))
                        .where(handler.handle(root, criteriaBuilder, value)));
    }
}
