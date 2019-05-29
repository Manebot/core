package io.manebot.database.search;

import io.manebot.database.Database;
import io.manebot.database.search.handler.SearchArgumentHandler;
import io.manebot.database.search.handler.SearchOrderHandler;
import io.manebot.database.search.handler.SearchOrderHandlerProperty;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * SearchHandlers are used to handle <b>Search</b> queries, which are in turn constructed by command arguments passed
 * to commands with the CommandArgumentSearch type.
 *
 * A SearchHandler takes a raw query from the aforementioned argument type, validates it, and executes it against the
 * database.
 *
 * There are three search component types, all parsed by the <b>Search</b> class, and passed to a SearchHandler:
 * 1.   Argument
 *  Example: <i>name:value</i>
 * 2.   Command
 *  Example: <i>command</i>
 * 3.   String
 *  Example: <i>"multi word string"</i>
 *
 *  These can be combined together with operators, such as '+', '-', and '~', denoting AND, AND NOT, and OR respectively.
 *  OR (~) is implied, and is assumed when no operator is explicitly defined.  Operators at the beginning a query are
 *  not allowed.
 *
 *  "title" -banned +by:user
 *
 * @param <T> search common entity class.  Should be mapped to a database.
 */
public interface SearchHandler<T> {

    /**
     * Gets the entity class associated with this search handler.
     * @return entity class.
     */
    Class<T> getEntityClass();

    /**
     * Gets the database associated with this search handler.
     * @return Database instance.
     */
    Database getDatabase();

    /**
     * Gets the argument handler for a given argument.
     * @param name Argument name to check.
     * @return SearchArgumentHandler instance if found, or null if none was found.
     */
    SearchArgumentHandler getArgumentHandler(String name);

    /**
     * Gets the command handler for a given command label.
     * @param name Command name to check.
     * @return SearchArgumentHandler instance if found, or null if none was found.
     */
    SearchArgumentHandler getCommandHandler(String name);

    /**
     * Gets the default string handler for the search handler.
     * @return Default SearchArgumentHandler for string arguments, or null if none was found.
     */
    SearchArgumentHandler getStringHandler();

    /**
     * Executes a search on a specific search object.
     * @param search Search object to parse, containing predicates to filter down the specified entity list.
     * @param maxResults Maximum page result count
     * @return Immutable list of entities found by the search.
     * @throws SQLException if there was a SQL exception executing a completed or incomplete search.
     * @throws IllegalArgumentException if the arguments provided by the <i>search</i> argument were invalid.
     */
    SearchResult<T> search(Search search, int maxResults) throws SQLException, IllegalArgumentException;

    /**
     * Executes a search and returns one random row.
     * @param search Search object to parse, containing predicates to filter down the specified entity list.
     * @param maxResults Maximum page result count
     * @return Random singleton from the search
     * @throws SQLException if there was a SQL exception executing a completed or incomplete search.
     * @throws IllegalArgumentException if the arguments provided by the <i>search</i> argument were invalid.
     */
    SearchResult<T> random(Search search, int maxResults) throws SQLException, IllegalArgumentException;

    /**
     * Builds SearchHandlers.
     * @param <T> search entity type.
     */
    interface Builder<T> {

        /**
         * Binds the specified argument handler to the key described by <B>name</B>.
         * @param name name of the key to bind to (i.e.: <i>argument:value</i>)
         * @param handler handler to associate with the specified argument name.
         * @return Builder instance.
         */
        Builder<T> argument(String name, SearchArgumentHandler handler);

        /**
         * Binds the specified argument handler to the command key described by <B>name</B>.
         * @param name name of the key to bind to (i.e.: <i>command</i>)
         * @param handler handler to associate with the specified argument name.
         * @return Builder instance.
         */
        Builder<T> command(String name, SearchArgumentHandler handler);

        /**
         * Binds the specified argument handler to all un-bound command keys.
         * @param handler handler to associate with the un-specified argument names.
         * @return Builder instance.
         */
        Builder<T> command(SearchArgumentHandler handler);

        /**
         * Binds the specified argument handler to any string query used (i.e.: "test string" text in query).
         * If no string handler is specified, exceptions are thrown during search.
         * @param handler handler to associate with any quoted string query.
         * @return Builder instance.
         */
        Builder<T> string(SearchArgumentHandler handler);

        /**
         * Executes the provided execution as a filter on every search execution.
         * @param executionConsumer Search execution consumer.
         * @return Builder instance.
         */
        Builder<T> always(Consumer<Clause<T>> executionConsumer);

        /**
         * Binds the specified key to an order handler.
         * @param key key to bind to.
         * @param handler handler to associate with ordering the result list.
         * @return Builder instance.
         */
        Builder<T> sort(String key, SearchOrderHandler handler);

        /**
         * Binds the specified key to an order handler.
         * @param key key to bind to.
         * @param field field name to associate with ordering the result list.
         * @return Builder instance.
         */
        default Builder<T> sort(String key, String field) {
            return sort(key, new SearchOrderHandlerProperty(field));
        }

        /**
         * Binds the specified key to an order handler.
         * @param key key to bind to.
         * @param function function to associate with ordering the result list.
         * @return Builder instance.
         */
        default Builder<T> sort(String key, Function<Root, Path> function) {
            return sort(key, new SearchOrderHandlerProperty(function));
        }

        /**
         * Sets the specific order key as the default order handler.
         * @param key key to designate as the default order.
         * @param order default order.
         * @return Builder instance.
         */
        Builder<T> defaultSort(String key, SortOrder order);

        /**
         * Sets the specific order key as the default ascending order handler.
         * @param key key to designate as the default order.
         * @return Builder instance.
         */
        default Builder<T> defaultSort(String key) {
            return defaultSort(key, SortOrder.ASCENDING);
        }

        /**
         * Builds the search handler, capable of executing searches with the pre-formatted parameters.
         * @return SearchHandler instance.
         * @throws IllegalArgumentException if there was an argument exception while discerning parsing steps.
         */
        SearchHandler<T> build() throws IllegalArgumentException;

    }

    /**
     * Represents a WHERE clause.
     * @param <T> search entity type.
     */
    interface Clause<T> {

        /**
         * Gets the search handler associated with this execution.
         * @return search handler.
         */
        SearchHandler<T> getSearchHandler();

        /**
         * Gets the root of this clause.
         * @return root instance.
         */
        Root getRoot();

        /**
         * Gets the criteria builder instances associated with this execution.
         * @return criteria builder instance.
         */
        CriteriaBuilder getCriteriaBuilder();

        /**
         * Adds a predicate to the query.
         * @param operator operator used for this expression.
         * @param predicate predicate to push.
         */
        void addPredicate(SearchOperator operator, Predicate predicate);

        /**
         * Pushes a new clause onto the stack.
         * @param operator prefixing clause operator.
         * @return clause instance to use for this clause.
         */
        Clause<T> push(SearchOperator operator);

        /**
         * Pops a clause off of the stack.
         * @return clause instance.
         * @throws IllegalArgumentException if no clause is able to be popped.
         */
        Clause<T> pop() throws IllegalArgumentException;

    }

}
