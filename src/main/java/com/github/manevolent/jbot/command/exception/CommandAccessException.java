package com.github.manevolent.jbot.command.exception;

public class CommandAccessException extends CommandExecutionException {
    public CommandAccessException(Throwable cause) {
        super(false, "\u26D4" + ' ' + cause.getMessage());
    }
    public CommandAccessException(String message, Throwable cause) {
        super(false, "\u26D4" + ' ' + message, cause);
    }
    public CommandAccessException(String message) {
        super(false, "\u26D4" + ' ' + message);
    }
}
