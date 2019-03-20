package com.github.manevolent.jbot.artifact;

public interface ArtifactRepository {

    /**
     * Finds if this repository is a local repository.
     * @return true if the repository is local, false otherwise.
     */
    boolean isLocal();

    /**
     * Queries an artifact manifest on this repository.
     * @param identifier ManifestIdentifier to query.
     * @return Artifact manifest.
     * @throws ArtifactRepositoryException if an issue occurs while accessing the repository
     * @throws ArtifactNotFoundException if the manifest cannot be found.
     */
    default ArtifactManifest getManifest(ManifestIdentifier identifier)
            throws ArtifactRepositoryException, ArtifactNotFoundException {
        return getManifest(identifier.getPackageId(), identifier.getArtifactId());
    }

    /**
     * Queries an artifact manifest on this repository.
     * @param packageId Package ID to query.
     * @param artifactId Artifact ID to query.
     * @return Artifact manifest.
     * @throws ArtifactRepositoryException if an issue occurs while accessing the repository
     * @throws ArtifactNotFoundException if the manifest cannot be found.
     */
    ArtifactManifest getManifest(String packageId, String artifactId)
            throws ArtifactRepositoryException, ArtifactNotFoundException;

    /**
     * Gets an artifact by its identifier.
     * @param identifier Artifact identifier.
     * @return Artifact definition instance.
     * @throws ArtifactRepositoryException if an issue occurs while accessing the repository
     * @throws ArtifactNotFoundException if the manifest cannot be found.
     */
    default Artifact getArtifact(ArtifactIdentifier identifier)
            throws ArtifactRepositoryException, ArtifactNotFoundException {
        return getArtifact(identifier.getPackageId(), identifier.getArtifactId(), identifier.getVersion());
    }

    /**
     * Gets an artifact by its identifier.
     * @param packageId Package ID to query.
     * @param artifactId Artifact ID to query.
     * @param version Artifact version to query.
     * @return Artifact definition instance.
     * @throws ArtifactRepositoryException if an issue occurs while accessing the repository
     * @throws ArtifactNotFoundException if the manifest cannot be found.
     */
    default Artifact getArtifact(String packageId, String artifactId, String version)
            throws ArtifactRepositoryException, ArtifactNotFoundException {
        return getManifest(packageId, artifactId).getArtifact(version);
    }

}
