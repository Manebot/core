package com.github.manevolent.jbot.command.executor.chained.argument.search;

import com.github.manevolent.jbot.command.exception.CommandArgumentException;

/**
 * Describes a lexically-parsed search argument, which is used to build a JPA query clause around a string text
 * definition.
 */
public class SearchPredicateString extends SearchPredicate {
    SearchPredicateString(SearchArgument argument) {
        super(argument);
    }

    @Override
    public void handle(SearchHandler.Clause clause) throws CommandArgumentException {
        SearchArgumentHandler handler = clause.getSearchHandler().getStringHandler();
        if (handler == null) throw new CommandArgumentException("This search does not handle string arguments.");

        clause.addExpression(
                getArgument().getOperator(),
                handler.handle(clause.getCriteriaBuilder(), getArgument())
        );
    }
}
