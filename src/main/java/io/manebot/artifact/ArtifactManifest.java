package io.manebot.artifact;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Stream;

public interface ArtifactManifest {

    /**
     * Gets the manifest's qualifier.
     * @return ArtifactQualifier instance.
     */
    ManifestIdentifier getIdentifier();

    /**
     * Gets the artifact's ID.
     *
     * @return Artifact ID.
     */
    default String getArtifactId() {
        return getIdentifier().getArtifactId();
    }

    /**
     * Gets the artifact's package ID.
     *
     * @return Artifact package ID.
     */
    default String getPackageId() {
        return getIdentifier().getPackageId();
    }

    /**
     * Gets the latest version of this plugin.
     *
     * @return Version instance representing the latest version of this plugin.
     */
    ArtifactIdentifier getLatestVersion();

    /**
     * Finds if an update exists for a specific plugin's version.
     * @param version Version to check for an update to.
     * @return true if an update exists, false otherwise.
     */
    default boolean doesUpdateExist(String version) {
        return !getLatestVersion().getVersion().equals(version);
    }

    /**
     * Gets a plugin for a specific version associated with this manifest.
     * @param version version to get.
     * @return Artifact instance corresponding to the version requested.
     */
    Artifact getArtifact(String version) throws ArtifactNotFoundException;

    /**
     * Gets the versions this plugin has.
     * @return Version collection representing the versions for this plugin.
     */
    Collection<String> getVersions();

    /**
     * Gets the repository this plugin was obtained from.
     * @return Artifact repository.
     */
    ArtifactRepository getRepository();

    /**
     * Gets this plugin's base URI.  This could be on the web as a GitHub repository, or on disk as a JARfile.
     * @return plugin resource URI.
     */
    URI getUri();

}
