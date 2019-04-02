package io.manebot.artifact;

import java.net.URL;

public interface RemoteArtifactRepository extends ArtifactRepository {

    /**
     * Gets the remote base URL for this repository.
     * @return Repository URL.
     */
    URL getUrl();

    /**
     * Finds if this repository is a local repository.
     * @return false
     */
    default boolean isLocal() {
        return false;
    }

    /**
     * Queries an artifact manifest on this repository.
     *
     * @param packageId Package ID to query.
     * @param artifactId Artifact ID to query.
     * @return Artifact manifest.
     * @throws ArtifactRepositoryException if an issue occurs while accessing the repository
     * @throws ArtifactNotFoundException if the manifest cannot be found.
     */
    ArtifactManifest getManifest(String packageId, String artifactId)
            throws ArtifactRepositoryException, ArtifactNotFoundException;

    /**
     * Gets a local artifact by its identifier.
     *
     * @param identifier Artifact identifier.
     * @return Artifact definition instance.
     * @throws ArtifactRepositoryException if an issue occurs while accessing the repository
     * @throws ArtifactNotFoundException if the manifest cannot be found.
     */
    Artifact getArtifact(ArtifactIdentifier identifier)
            throws ArtifactRepositoryException, ArtifactNotFoundException;

}
