package com.github.manevolent.jbot.event;

public class EventExecutionException extends Exception {

    public EventExecutionException(Exception cause) {
        super(cause);
    }

    public EventExecutionException(String message) {
        super(message);
    }

    public EventExecutionException() {

    }

}
