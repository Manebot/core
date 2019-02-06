package com.github.manevolent.jbot.command.exception;

public class CommandArgumentException extends CommandExecutionException {

    public CommandArgumentException(Exception cause) {
        super(false, getMessage(cause));
    }

    public CommandArgumentException(String message) {
        super(false, "\u261B" + ' ' + message);
    }

    private static String getMessage(Exception cause) {
        if (cause != null)
            if (cause instanceof CommandExecutionException) {
                return cause.getMessage();
            } else {
                return "\u261B" + ' ' + cause.getMessage();
            }
        else return null;
    }

}
