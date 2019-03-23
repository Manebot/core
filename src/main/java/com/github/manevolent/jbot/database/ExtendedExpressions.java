package com.github.manevolent.jbot.database;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public final class ExtendedExpressions {
    public static Predicate escapedLike(CriteriaBuilder criteriaBuilder, Expression<String> left, String right) {
        return criteriaBuilder.like(left, escapeLike(right), '!');
    }

    public static Predicate escapedNotLike(CriteriaBuilder criteriaBuilder, Expression<String> left, String right) {
        return criteriaBuilder.notLike(left, escapeLike(right), '!');
    }

    private static String escapeLike(String value) {
        return value
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
    }
}
