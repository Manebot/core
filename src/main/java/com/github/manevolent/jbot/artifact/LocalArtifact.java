package com.github.manevolent.jbot.artifact;

import java.io.File;
import java.net.URI;

public interface LocalArtifact extends Artifact {

    /**
     * Gets the file (i.e. JAR) associated with this artifact.
     * @return Artifact file.
     */
    File getFile();

    @Override
    default boolean hasObtained() {
        return true;
    }

    // Overload
    @Override
    default URI getUri() {
        return getFile().toURI();
    }

}
