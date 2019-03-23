package com.github.manevolent.jbot.command.search;

import com.github.manevolent.jbot.command.exception.CommandArgumentException;

/**
 * Describes a lexically-parsed search argument, which is used to build a JPA query clause around an argument definition.
 */
public class SearchPredicateArgument extends SearchPredicate {
    SearchPredicateArgument(SearchArgument argument) {
        super(argument);
    }

    @Override
    public void handle(SearchHandler.Clause clause) throws CommandArgumentException {
        SearchArgument argument = getArgument();
        if (argument.getValue().contains(":")) {
            String[] args = argument.getValue().split("\\:", 2);
            String name = args[0];

            SearchArgumentHandler handler = clause.getSearchHandler().getArgumentHandler(name);
            if (handler == null) throw new CommandArgumentException("Unexpected argument: \"" + name + "\".");

            clause.addExpression(
                    getArgument().getOperator(),
                    handler.handle(
                            clause.getCriteriaBuilder(),
                            new SearchArgument(
                                    argument.getOperator(),
                                    args[1]
                            ))
            );
        } else {
            String commandName = argument.getValue().toLowerCase();

            SearchArgumentHandler handler = clause.getSearchHandler().getCommandHandler(commandName);
            if (handler == null) throw new CommandArgumentException("Unexpected command: \"" + commandName + "\".");

            clause.addExpression(
                    getArgument().getOperator(),
                    handler.handle(
                            clause.getCriteriaBuilder(),
                            new SearchArgument(
                                    argument.getOperator(), commandName
                            ))
            );
        }
    }
}
