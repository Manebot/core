package com.github.manevolent.jbot.database;

import com.github.manevolent.jbot.artifact.ArtifactIdentifier;

public interface Database {
    String getName();
    ArtifactIdentifier getSubject();
}
