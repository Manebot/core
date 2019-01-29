package com.github.manevolent.jbot.artifact;

import java.net.URI;

public interface Artifact {

    /**
     * Gets this artifact's identifier.
     * @return artifact identifier.
     */
    default ArtifactIdentifier getIdentifier() {
        return new ArtifactIdentifier(getManifest().getArtifactId(), getManifest().getPackageId(), getVersion());
    }

    /**
     * Gets this artifact's manifest.
     * @return Artifact manifest.
     */
    ArtifactManifest getManifest();

    /**
     * Finds if this artifact is out-of-date (i.e. a newer version exists)
     * @return true if this artifact is out-of-date, false otherwise.
     */
    default boolean isOutOfDate() {
        return getManifest().doesUpdateExist(getVersion());
    }

    /**
     * Gets this plugin's version.
     * @return Version.
     */
    Version getVersion();

    /**
     * Gets this artifact's root resource URI.
     * @return Artifact root resource URI.
     */
    URI getUri();

    /**
     * Obtains this artifact locally.
     * @return Local artifact.
     */
    LocalArtifact obtain() throws ArtifactRepositoryException, ArtifactNotFoundException;

}
