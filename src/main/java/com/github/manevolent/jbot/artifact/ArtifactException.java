package com.github.manevolent.jbot.artifact;

public class ArtifactException extends Exception {
    public ArtifactException(String message) {
        super(message);
    }
    public ArtifactException(String message, Throwable ex) {
        super(message, ex);
    }
    public ArtifactException(Throwable ex) {
        super(ex);
    }
}
