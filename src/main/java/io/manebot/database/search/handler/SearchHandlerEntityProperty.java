package io.manebot.database.search.handler;

import io.manebot.database.search.SearchArgument;
import io.manebot.database.search.SearchArgumentHandler;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Function;

public abstract class SearchHandlerEntityProperty implements SearchArgumentHandler {
    private final Function<Root, Path> pathFinder; // Path of Exile is a pretty good game

    public SearchHandlerEntityProperty(Function<Root, Path> pathFinder) {
        this.pathFinder = pathFinder;
    }

    public SearchHandlerEntityProperty(String property) {
        this.pathFinder = (root) -> root.get(property);
    }

    protected abstract Predicate handle(Path path, CriteriaBuilder criteriaBuilder, SearchArgument value);

    @Override
    public Predicate handle(Root root, CriteriaBuilder criteriaBuilder, SearchArgument value) {
        return handle(pathFinder.apply(root), criteriaBuilder, value);
    }
}
