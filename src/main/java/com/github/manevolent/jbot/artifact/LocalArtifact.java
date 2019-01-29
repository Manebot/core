package com.github.manevolent.jbot.artifact;

import com.github.manevolent.jbot.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URI;
import java.util.Collection;

public interface LocalArtifact extends Artifact {

    /**
     * Gets the file (i.e. JAR) associated with this artifact.
     * @return Artifact file.
     */
    File getFile();

    // Overload
    default URI getUri() {
        return getFile().toURI();
    }

}
