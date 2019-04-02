package io.manebot.artifact;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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

}
