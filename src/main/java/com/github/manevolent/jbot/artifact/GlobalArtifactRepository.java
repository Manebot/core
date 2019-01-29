package com.github.manevolent.jbot.artifact;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class GlobalArtifactRepository extends AggregateArtifactRepository {
    private final List<ArtifactRepository> repositories = new LinkedList<>();

    public GlobalArtifactRepository() {

    }

    public GlobalArtifactRepository(Collection<ArtifactRepository> artifactRepositories) {
        repositories.addAll(artifactRepositories);
    }

    /**
     * Adds a repository to this global artifact repository.
     * @param repository repository to add.
     */
    public void addRepository(ArtifactRepository repository) {
        this.repositories.add(repository);
    }

    /**
     * Removes a repository from this global artifact repository.
     * @param repository repository to remove.
     */
    public void removeRepository(ArtifactRepository repository) {
        this.repositories.remove(repository);
    }

    @Override
    protected Collection<ArtifactRepository> getRepositories() {
        return Collections.unmodifiableCollection(repositories);
    }
}
