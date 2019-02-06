package com.github.manevolent.jbot.event;

public interface EventDispatcher {

    /**
     * Fires an event
     * @param event Event to fire.
     * @return Event
     */
    Event fire(Event event) throws EventExecutionException;

}
