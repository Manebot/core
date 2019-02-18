package com.github.manevolent.jbot.database;

import java.util.Collection;

public interface DatabaseManager {

    default boolean hasDatabase(String name) {
        return getDatabase(name) != null;
    }

    Collection<Database> getDatabases();

    default Database getDatabase(String name) {
        return getDatabases().stream().filter(db -> db.getName().equals(name)).findFirst().orElse(null);
    }

    Database createDatabase(String name);
    Database createDatabase(String name, DatabaseInitializer initializer);

}
