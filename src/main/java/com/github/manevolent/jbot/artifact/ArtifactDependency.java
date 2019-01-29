package com.github.manevolent.jbot.artifact;

public class ArtifactDependency extends ArtifactIdentifier {
    private final ArtifactDependencyLevel dependencyType;

    public ArtifactDependency(String packageId, String artifactId, Version version,
                              ArtifactDependencyLevel dependencyType) {
        super(packageId, artifactId, version);
        this.dependencyType = dependencyType;
    }

    /**
     * Gets the type of dependency this artifact dependency requires.
     * @return dependency type.
     */
    public ArtifactDependencyLevel getType() {
        return dependencyType;
    }
}
