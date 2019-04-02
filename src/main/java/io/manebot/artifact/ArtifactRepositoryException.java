package io.manebot.artifact;

public class ArtifactRepositoryException extends ArtifactException {
    public ArtifactRepositoryException(String message) {
        super(message);
    }
    public ArtifactRepositoryException(String message, Exception cause) {
        super(message, cause);
    }
    public ArtifactRepositoryException(Exception cause) {
        super(cause);
    }
}
