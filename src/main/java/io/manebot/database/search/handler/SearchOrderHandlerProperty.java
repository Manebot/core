package io.manebot.database.search.handler;

import javax.persistence.criteria.*;
import java.util.function.Function;

public class SearchOrderHandlerProperty extends AbstractSearchOrderHandler {
    private final Function<Root, Path> pathFinder; // Path of Exile is a pretty good game

    public SearchOrderHandlerProperty(Function<Root, Path> pathFinder) {
        this.pathFinder = pathFinder;
    }

    public SearchOrderHandlerProperty(String property) {
        this.pathFinder = (root) -> root.get(property);
    }

    @Override
    public Expression<?> handle(Root root, CriteriaBuilder criteriaBuilder) {
        return pathFinder.apply(root);
    }
}
