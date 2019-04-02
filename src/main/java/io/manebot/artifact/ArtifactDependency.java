package io.manebot.artifact;

public interface ArtifactDependency {

    /**
     * Gets the parent artifact in this relationship.
     * @return Parent artifact.
     */
    Artifact getParent();

    /**
     * Gets the dependent artifact in this relationship.
     * @return Dependent artifact.
     */
    Artifact getChild();

    /**
     * Gets the type of dependency this artifact dependency requires.
     * @return dependency type.
     */
    ArtifactDependencyLevel getType();

    /**
     * Finds if this is a required dependency.
     * @return true if the dependency is required, false otherwise.
     */
    boolean isRequired();

}
