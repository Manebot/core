package io.manebot.database;

import io.manebot.artifact.ArtifactIdentifier;

public interface PluginDatabase extends Database {

    /**
     * Gets the plugin associated with this database.
     * @return Plugin artifact identifier.
     */
    ArtifactIdentifier getPluginIdentifier();

}
