package com.github.manevolent.jbot.artifact;

import java.util.Collection;

public abstract class AggregateArtifactRepository implements ArtifactRepository {
    protected abstract Collection<ArtifactRepository> getRepositories();

    @Override
    public boolean isLocal() {
        return getRepositories().stream().allMatch(ArtifactRepository::isLocal);
    }

    @Override
    public ArtifactManifest getManifest(String packageId, String artifactId) throws ArtifactNotFoundException {
        for (ArtifactRepository repository : getRepositories()) {
            try {
                return repository.getManifest(packageId, artifactId);
            } catch (ArtifactRepositoryException ex) {
                // Ignore
            }
        }

        throw new ArtifactNotFoundException(packageId + ":" + artifactId);
    }
}
