package com.github.manevolent.jbot.event;

public class EventExecutionException extends Exception {

    public EventExecutionException(Throwable cause) {
        super(cause);
    }

    public EventExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventExecutionException() {

    }

}
