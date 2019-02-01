package com.github.manevolent.jbot.artifact;

public interface ArtifactDependency {

    Artifact getParent();

    Artifact getChild();

    /**
     * Gets the type of dependency this artifact dependency requires.
     * @return dependency type.
     */
    ArtifactDependencyLevel getType();

}
