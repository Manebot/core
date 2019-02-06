package com.github.manevolent.jbot.command.exception;


public class CommandArgumentCastException extends CommandArgumentException {
    private final Level level;

    public CommandArgumentCastException(String message, Level level) {
        super(message);
        this.level = level;
    }

    public CommandArgumentCastException(Exception cause, Level level) {
        super(cause);
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public enum Level {
        ROUTER,
        CAST
    }
}
