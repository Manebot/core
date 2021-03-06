package io.manebot.database.search;

import io.manebot.database.search.handler.SearchArgumentHandler;

/**
 * Describes a lexically-parsed search argument, which is used to build a JPA query clause around an argument definition.
 */
public class SearchPredicateArgument extends SearchPredicate {
    public SearchPredicateArgument(SearchArgument argument) {
        super(argument);
    }

    @Override
    public void handle(SearchHandler.Clause clause) throws IllegalArgumentException {
        SearchArgument argument = getArgument();
        if (argument.getValue().contains(":")) {
            String[] args = argument.getValue().split("\\:", 2);
            String name = args[0];

            SearchArgumentHandler handler = clause.getSearchHandler().getArgumentHandler(name);
            if (handler == null) throw new IllegalArgumentException("Unexpected argument: \"" + name + "\".");

            clause.addPredicate(
                    getArgument().getOperator(),
                    handler.handle(
                            clause.getRoot(),
                            clause.getCriteriaBuilder(),
                            new SearchArgument(
                                    argument.getOperator(),
                                    args[1]
                            ))
            );
        } else {
            String commandName = argument.getValue().toLowerCase();

            SearchArgumentHandler handler = clause.getSearchHandler().getCommandHandler(commandName);
            if (handler == null) throw new IllegalArgumentException("Unexpected command: \"" + commandName + "\".");

            clause.addPredicate(
                    getArgument().getOperator(),
                    handler.handle(
                            clause.getRoot(),
                            clause.getCriteriaBuilder(),
                            new SearchArgument(
                                    argument.getOperator(), commandName
                            ))
            );
        }
    }
}
