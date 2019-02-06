package com.github.manevolent.jbot.event;

public interface EventDispatcher {

    /**
     * Fires an event
     * @param event Event to check.
     * @return A checked event.
     */
    Event fire(Event event) throws EventExecutionException;

}
