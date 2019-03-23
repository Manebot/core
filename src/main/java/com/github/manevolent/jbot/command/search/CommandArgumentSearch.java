package com.github.manevolent.jbot.command.search;

import com.github.manevolent.jbot.command.executor.chained.AnnotatedCommandExecutor;
import com.github.manevolent.jbot.command.executor.chained.ChainPriority;
import com.github.manevolent.jbot.command.executor.chained.ChainState;
import com.github.manevolent.jbot.command.executor.chained.argument.CommandArgument;
import com.github.manevolent.jbot.database.search.Search;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The search command argument type.  Builds <b>Search</b> objects for use by a <b>SearchHandler</b>.
 */
public class CommandArgumentSearch extends CommandArgument {
    private static final char STRING_CHARACTER = '"';
    private static final char ESCAPE_CHARACTER = '\\';

    public CommandArgumentSearch() { }
    public CommandArgumentSearch(Argument argument) { }

    @Override
    public String getHelpString() {
        return "<query...>";
    }

    /**
     * Parses a search lexically; does NOT execute any search or validate arguments.
     *
     * This behavior defaults to the lexical parser statically defined in the <b>Search</b> class, on the <i>parse</i>
     * method.
     *
     * @param state State to parse.  Since this argument parses multiple lines, it is entirely consumed.
     * @return ChainPriority instance that was determined.
     * @throws IllegalArgumentException if there was a problem <i>lexically</i> parsing the chain state.
     */
    @Override
    public ChainPriority cast(ChainState state)  {
        if (state.size() <= 0) return ChainPriority.NONE;
        String queryString = String.join(" ", state.getArguments());
        state.extend(state.size(), Search.parse(queryString));
        return ChainPriority.LOW;
    }

    @Override
    public boolean canExtend(CommandArgument b) {
        return false; // nothing can extend this, we're like a "Following" argument type.
    }

    @Override
    public boolean canCoexist(CommandArgument b) {
        return (!(b instanceof CommandArgumentSearch));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AnnotatedCommandExecutor.Argument(type = CommandArgumentSearch.class)
    public @interface Argument {
    }
}
