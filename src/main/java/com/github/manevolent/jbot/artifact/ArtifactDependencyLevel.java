package com.github.manevolent.jbot.artifact;

public enum ArtifactDependencyLevel {

    /**
     * Indicates an artifact dependency is required at compile-time, and should be packaged with an artifact locally.
     */
    COMPILE,

    /**
     * Indicates an artifact dependency is required at run-time.
     */
    RUN,

    /**
     * Indicates a dependency is required to be provided by the JRE.
     * The bot treats these as shared libraries and/or plugins.
     */
    PROVIDED,

    /**
     * Indicates a test dependency, only used for testing at run-time.
     */
    TEST

}
