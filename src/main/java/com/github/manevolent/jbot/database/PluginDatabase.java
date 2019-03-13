package com.github.manevolent.jbot.database;

import com.github.manevolent.jbot.artifact.ArtifactIdentifier;

public interface PluginDatabase extends Database {

    /**
     * Gets the plugin associated with this database.
     * @return Plugin artifact identifier.
     */
    ArtifactIdentifier getPluginIdentifier();

}
