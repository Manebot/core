package com.github.manevolent.jbot.database.search.handler;

import com.github.manevolent.jbot.database.search.SearchArgument;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Function;

public class SearchHandlerPropertyEquals extends SearchHandlerEntityProperty {
    private final Function<String, ?> parser;

    public SearchHandlerPropertyEquals(Function<Root, Path> pathFinder) {
        this(pathFinder, String::toString);
    }

    public SearchHandlerPropertyEquals(String property) {
        this(property, String::toString);
    }

    public SearchHandlerPropertyEquals(Function<Root, Path> pathFinder, Function<String, ?> parser) {
        super(pathFinder);

        this.parser = parser;
    }

    public SearchHandlerPropertyEquals(String property, Function<String, ?> parser) {
        super(property);

        this.parser = parser;
    }

    @Override
    protected Predicate handle(Path path, CriteriaBuilder criteriaBuilder, SearchArgument value) {
        return criteriaBuilder.equal(path, parser.apply(value.getValue()));
    }
}
