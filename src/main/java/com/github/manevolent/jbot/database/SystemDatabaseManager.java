package com.github.manevolent.jbot.database;

import com.github.manevolent.jbot.artifact.ArtifactIdentifier;

import java.util.Collection;
import java.util.stream.Collectors;

public interface SystemDatabaseManager extends DatabaseManager {
    ArtifactIdentifier SYSTEM_IDENTIFIER = new ArtifactIdentifier("com.github.manevolent", "jbot");

    default boolean hasSystemDatabase(String name) {
        return getSystemDatabase(name) != null;
    }

    default boolean hasDatabase(ArtifactIdentifier identifier, String name) {
        return getDatabase(identifier, name) != null;
    }

    default Database getSystemDatabase(String name) {
        return getSystemDatabases().stream().filter(db -> db.getName().equals(name)).findFirst().orElse(null);
    }

    default Database getDatabase(ArtifactIdentifier identifier, String name) {
        return getDatabases().stream().filter(db -> db.getSubject().equals(identifier) && db.getName().equals(name))
                .findFirst().orElse(null);
    }

    default Collection<Database> getDatabases(ArtifactIdentifier identifier) {
        return getDatabases().stream().filter(db -> db.getSubject().equals(identifier)).collect(Collectors.toList());
    }

    default Collection<Database> getSystemDatabases() {
        return getDatabases(SYSTEM_IDENTIFIER);
    }

    Collection<Database> getDatabases();

    default Database createDatabase(ArtifactIdentifier identifier, String name) {
        return createDatabase(identifier, name);
    }

    default Database createSystemDatabase(String name) {
        return createDatabase(SYSTEM_IDENTIFIER, name);
    }

}
