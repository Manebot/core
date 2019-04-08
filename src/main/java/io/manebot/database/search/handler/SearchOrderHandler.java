package io.manebot.database.search.handler;

import io.manebot.database.search.SortOrder;

import javax.persistence.criteria.CriteriaBuilder;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public interface SearchOrderHandler {
    Order handle(Root root, CriteriaBuilder criteriaBuilder, SortOrder order);

    default SearchOrderHandler reverse() {
        return (root, criteriaBuilder, order) -> SearchOrderHandler.this.handle(root, criteriaBuilder, order).reverse();
    }
}
