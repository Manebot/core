package com.github.manevolent.jbot.database;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Database {

    /**
     * Gets the database's name.
     * @return Database name.
     */
    String getName();

    /**
     * Executes a function on the database, using a session and returning the session to the pool fairly.
     *
     * @param function Function to execute.
     * @param <T> Return type of the function.
     * @return Returned function.
     */
    default <T> T execute(Function<EntityManager, T> function) {
        EntityManager session = null;

        try {
            session = openSession();
            return function.apply(session);
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Executes a function on the database, using a session and returning the session to the pool fairly.
     *
     * @param function Function to execute.
     */
    default void execute(Consumer<EntityManager> function) {
       EntityManager session = null;

        try {
            session = openSession();
            function.accept(session);
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Opens a session to the database.
     *
     * @return Session instance.
     */
    EntityManager openSession();

    /**
     * Database model constructor.
     */
    interface ModelConstructor {

        /**
         * Gets the database name.
         *
         * @return Database name.
         */
        String getDatabaseName();

        /**
         * Registers an entityi n the persistence system for this database.
         *
         * @param entityClass Entity class.
         */
        void registerEntity(Class<?> entityClass);

        /**
         * Defines the database in the persistence system.
         *
         * @return Database instance.
         */
        Database define();

    }

}
