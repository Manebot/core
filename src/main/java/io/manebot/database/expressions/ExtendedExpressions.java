package io.manebot.database.expressions;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public final class ExtendedExpressions {
    public static Predicate escapedLike(CriteriaBuilder criteriaBuilder, Expression<String> left, String right,
                                        MatchMode matchMode) {
        return criteriaBuilder.like(left, matchMode.toMatchString(escapeLike(right)), '!');
    }

    public static Predicate escapedNotLike(CriteriaBuilder criteriaBuilder, Expression<String> left, String right,
                                           MatchMode matchMode) {
        return criteriaBuilder.notLike(left, matchMode.toMatchString(escapeLike(right)), '!');
    }

    private static String escapeLike(String value) {
        return value
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
    }
}
