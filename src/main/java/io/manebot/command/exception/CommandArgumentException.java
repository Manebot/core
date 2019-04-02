package io.manebot.command.exception;

public class CommandArgumentException extends CommandExecutionException {

    public CommandArgumentException(Throwable cause) {
        super(false, getMessage(cause));
    }

    public CommandArgumentException(String message) {
        super(false, "\u261B" + ' ' + message);
    }

    public CommandArgumentException(String message, Throwable cause) {
        super(false, "\u261B" + ' ' + message, cause);
    }

    private static String getMessage(Throwable cause) {
        if (cause != null)
            if (cause instanceof CommandExecutionException) {
                return cause.getMessage();
            } else {
                return "\u261B" + ' ' + cause.getMessage();
            }
        else return null;
    }

}
