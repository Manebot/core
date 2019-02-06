package com.github.manevolent.jbot.database;

import java.util.Collection;

public interface DatabaseManager {

    default boolean hasDatabase(String name) {
        return getDatabase(name) != null;
    }

    default Database getDatabase(String name) {
        return getDatabases().stream().filter(db -> db.getName().equals(name)).findFirst().orElse(null);
    }

    Collection<Database> getDatabases();

    default Database createDatabase(String name) {
        return createDatabase(name, (database) -> { /*do nothing*/ });
    }

    Database createDatabase(String name, DatabaseInitializer initializer);

}
