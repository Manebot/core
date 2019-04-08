package io.manebot.database.search.handler;

import io.manebot.database.search.SortOrder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public abstract class AbstractSearchOrderHandler implements SearchOrderHandler {
    @Override
    public Order handle(Root root, CriteriaBuilder criteriaBuilder, SortOrder order) {
        switch (order) {
            case ASCENDING:
                return criteriaBuilder.asc(handle(root, criteriaBuilder));
            case DESCENDING:
                return criteriaBuilder.desc(handle(root, criteriaBuilder));
            default:
                throw new IllegalArgumentException("Invalid search order: " + order.name());
        }
    }

    public abstract Expression<?> handle(Root root, CriteriaBuilder criteriaBuilder);
}
