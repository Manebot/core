package io.manebot.event;

public class EventExecutionException extends RuntimeException {

    public EventExecutionException(Throwable cause) {
        super(cause);
    }

    public EventExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventExecutionException() {

    }

}
