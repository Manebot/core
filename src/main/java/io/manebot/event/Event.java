package io.manebot.event;

public abstract class Event {
    private final Object sender;

    public Event(Object sender) {
        this.sender = sender;
    }

    public Object getSender() {
        return sender;
    }
}
