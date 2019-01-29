package com.github.manevolent.jbot.artifact;

public class ArtifactNotFoundException extends ArtifactRepositoryException {
    public ArtifactNotFoundException(String message) {
        super(message);
    }
    public ArtifactNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
    public ArtifactNotFoundException(Exception cause) {
        super(cause);
    }
}
