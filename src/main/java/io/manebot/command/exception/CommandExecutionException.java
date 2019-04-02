package io.manebot.command.exception;

import io.manebot.chat.exception.ChatException;

public class CommandExecutionException extends ChatException {
    private final String message;

    public CommandExecutionException(Throwable cause) {
        super(cause);

        if (cause != null)
            if (cause instanceof CommandExecutionException) {
                this.message = cause.getMessage();
            } else {
                this.message = "\u26A0" + ' ' + "There was an unexpected problem executing the command.";
            }
        else
            this.message = "null";
    }

    public CommandExecutionException(String message, Throwable ex) {
        super(message, ex);

        this.message = "\u26A0" + ' ' +message;
    }


    public CommandExecutionException(String message) {
        super(message);

        this.message = "\u26A0" + ' ' +message;
    }

    protected CommandExecutionException(boolean icon, String message, Throwable ex) {
        super(message, ex);

        this.message = (icon ? ("\u26A0" + ' ') : "") + message;
    }

    protected CommandExecutionException(boolean icon, String message) {
        super(message);

        this.message = (icon ? ("\u26A0" + ' ') : "") + message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }
}
