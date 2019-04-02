package io.manebot.chat.exception;

public class ChatException extends Exception {
    public ChatException(Throwable cause) {
        super(cause);
    }

    public ChatException(String message, Throwable ex) {
        super(message, ex);
    }

    public ChatException(String message) {
        super(message);
    }
}
