package com.github.manevolent.jbot.database;

import org.hibernate.Session;

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
    default <T> T execute(Function<Session, T> function) {
        try (Session session = openSession()) {
            return function.apply(session);
        }
    }

    /**
     * Executes a function on the database, using a session and returning the session to the pool fairly.
     *
     * @param function Function to execute.
     */
    default void execute(Consumer<Session> function) {
        try (Session session = openSession()) {
            function.accept(session);
        }
    }

    /**
     * Opens a session to the database.
     *
     * @return Session instance.
     */
    Session openSession();

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
