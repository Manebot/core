package com.github.manevolent.jbot.artifact;

import com.github.manevolent.jbot.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

public interface RemoteArtifact extends Artifact {

    /**
     * Gets the remote URL associated with this artifact.
     * @return Artifact file.
     */
    URL getURL();

    // Overload
    default URI getUri() {
        try {
            return getURL().toURI();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Loads this artifact as a plugin, if it has not been loaded already.
     * @return JavaPlugin instance of this plugin.
     */
    default JavaPlugin loadAsPlugin() {
        throw new UnsupportedOperationException("cannot load remote artifact; use obtain() to obtain an artifact");
    }

}
