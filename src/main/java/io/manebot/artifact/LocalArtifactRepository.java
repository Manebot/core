package io.manebot.artifact;

import java.io.File;

public interface LocalArtifactRepository extends ArtifactRepository {

    /**
     * Gets the local directory for this repository.
     * @return Repository directory.
     */
    File getDirectory();

    /**
     * Finds if this repository is a local repository.
     * @return true
     */
    default boolean isLocal() {
        return true;
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
    LocalArtifact getArtifact(ArtifactIdentifier identifier)
            throws ArtifactRepositoryException, ArtifactNotFoundException;

}
