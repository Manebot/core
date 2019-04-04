package io.manebot.database;

import io.manebot.database.search.SearchHandler;
import io.manebot.lambda.ThrowingConsumer;
import io.manebot.lambda.ThrowingFunction;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Function;

public interface Database extends AutoCloseable {

    /**
     * Gets the classloader used by this database to load entity model classes associated with it.
     * @return ClassLoader instance.
     */
    ClassLoader getClassLoader();

    /**
     * Gets the database manager associated with creating this database.
     * @return DatabaseManager instance.
     */
    DatabaseManager getDatabaseManager();

    /**
     * Gets the database's name.
     * @return Database name.
     */
    String getName();

    /**
     * Gets a list of this database's own registered entities.
     * @return Collection of Class objects.
     */
    Collection<Class<?>> getEntities();

    /**
     * Gets an immutable collection of this database's dependent databases.
     * @return Database collection.
     */
    Collection<Database> getDependentDatabases();

    /**
     * Finds if this database is closed.  A closed database does not hold open connections to a server.
     * @return true if the database is closed, false otherwise.
     */
    boolean isClosed();

    /**
     * Executes a function on the database, using a session and returning the session to the pool fairly.
     *
     * @param function Function to execute.
     * @param <T> Return type of the function.
     *
     * @throws E user-defined exception
     *
     * @return User-defined result.
     */
    default <T, E extends Exception> T execute(ThrowingFunction<EntityManager, T, E> function) throws E {
        EntityManager session = null;

        try {
            session = openSession();
            return function.applyChecked(session);
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Executes a function on the database, using a session and returning the session to the pool fairly.
     *
     * @throws E user-defined exception
     *
     * @param function Function to execute.
     */
    default <E extends Exception> void execute(ThrowingConsumer<EntityManager, E> function) throws E {
        EntityManager session = null;

        try {
            session = openSession();
            function.acceptChecked(session);
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Executes a function on the database, using a transactional
     * session and returning the session to the pool fairly, automatically rolling back on failure.
     *
     * @param function Function to execute.
     * @throws SQLException failure to execute <b>function</b> or transactional behavior.
     */
    default <E extends Exception> void executeTransaction(ThrowingConsumer<EntityManager, E> function)
            throws SQLException {
        EntityManager session = null;

        try {
            session = openSession();
            session.getTransaction().begin();
            function.acceptChecked(session);
            session.getTransaction().commit();
        } catch (Throwable e) {
            if (session != null && session.getTransaction().isActive())
                session.getTransaction().rollback();

            throw new SQLException("Problem executing transaction", e);
        } finally {
            if (session != null) session.close();
        }
    }
    /**
     * Executes a function on the database, using a transactional
     * session and returning the session to the pool fairly, automatically rolling back on failure.
     *
     * @param function Function to execute.
     * @return User-defined result.
     * @throws SQLException failure to execute <b>function</b> or transactional behavior.
     */
    default <T, E extends Exception> T executeTransaction(ThrowingFunction<EntityManager, T, E> function)
            throws SQLException {
        EntityManager session = null;
        T o;

        try {
            session = openSession();
            session.getTransaction().begin();
            o = function.apply(session);
            session.getTransaction().commit();
            return o;
        } catch (Throwable e) {
            if (session != null && session.getTransaction().isActive())
                session.getTransaction().rollback();

            throw new SQLException("Problem executing transaction", e);
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * Opens a session to the database.
     *
     * @return Session instance.
     */
    EntityManager openSession();

    /**
     * Creates a search, which accepts certain arguments to construct results in queryable commands.
     * @param entityClass Search entity class.
     * @param <T> search entity type
     * @return Search builder instance.
     * @throws IllegalArgumentException if the entity is not registered to this database.
     */
    <T> SearchHandler.Builder<T> createSearchHandler(Class<T> entityClass) throws IllegalArgumentException;

    /**
     * Creates a search, which accepts certain arguments to construct results in queryable commands.
     * @param entityClass Search entity class.
     * @param function Search builder function.
     * @param <T> search entity type
     * @return Search instance.
     * @throws IllegalArgumentException if the entity is not registered to this database.
     */
    default <T> SearchHandler<T> createSearchHandler(
            Class<T> entityClass,
            Function<SearchHandler.Builder<T>,
            SearchHandler<T>> function
    ) throws IllegalArgumentException {
        return function.apply(createSearchHandler(entityClass));
    }

    /**
     * Database model constructor.
     */
    interface ModelConstructor {

        /**
         * Gets the ClassLoader being used to load resources defined by this model constructor.
         * @return immutable collection of all class loaders being utilized in this session.
         */
        ClassLoader getClassLoader();

        /**
         * Applies the instance classloader of this model constructor, used to load the classes defined by it.
         * @param classLoader ClassLoader instance.
         * @return ModelConstructor instance.
         */
        ModelConstructor setClassLoader(ClassLoader classLoader);

        /**
         * Gets the database name.
         *
         * @return Database name.
         */
        String getDatabaseName();

        /**
         * Gets the system database.
         *
         * @return system database.
         */
        Database getSystemDatabase();

        /**
         * Depends on another database, associating those models with this model.
         *
         * @param database Database to depend on.
         * @return ModelConstructor instance.
         */
        ModelConstructor depend(Database database);

        /**
         * Registers an entity in the persistence system for this database.
         *
         * @param entityClass Entity class.
         * @return ModelConstructor instance.
         */
        ModelConstructor registerEntity(Class<?> entityClass);

        /**
         * Registers an entity in the persistence system for this database.
         *
         * @param virtualClass class by which this entity is also accessible.
         * @param entityClass entity class to associate this entity type to.
         * @return ModelConstructor instance.
         */
        <X,Y extends X> ModelConstructor registerEntityAssociation(Class<Y> entityClass, Class<X> virtualClass);

        /**
         * Finds if the database will update its schema for the unrecognized entities which have been registered.
         * @return true if the existing database schema will be validated.
         */
        boolean willUpdateSchema();

        /**
         * Sets the database schema update behavior flag.
         * @param updateSchema true if the schema should be validated for update, false otherwise.
         * @return ModelConstructor instance.
         */
        ModelConstructor setUpdateSchema(boolean updateSchema);

        /**
         * Defines the database in the persistence system.
         *
         * @return Database instance.
         */
        Database define();

    }

}
