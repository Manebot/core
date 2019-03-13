package com.github.manevolent.jbot.database;

import java.util.Collection;
import java.util.function.Function;

public interface DatabaseManager {

    /**
     * Gets an immutable list of all defined databases in this provider.
     * @return Database collection.
     */
    Collection<Database> getDatabases();

    /**
     * Gets a pre-existing, defined database, if such a database exists.
     * @param name Database name.
     * @return Database instance if found, null otherwise.
     */
    default Database getDatabase(String name) {
        return getDatabases().stream().filter(db -> db.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Defines a database in the persistence system.
     * @param name Database name
     * @param func Function used to construct the database model.
     *
     * @return Database instance.
     */
    Database defineDatabase(String name, Function<Database.ModelConstructor, Database> func);

}
