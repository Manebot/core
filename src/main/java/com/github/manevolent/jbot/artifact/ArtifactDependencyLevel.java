package com.github.manevolent.jbot.artifact;

public enum ArtifactDependencyLevel {

    /**
     * Indicates an artifact dependency is only required at compile-time, and should not be included at run-time.
     *
     * These are often required for plugin co-dependencies.
     */
    COMPILE,

    /**
     * Indicates an artifact dependency is required both at compile and run-time.
     */
    RUN

}
