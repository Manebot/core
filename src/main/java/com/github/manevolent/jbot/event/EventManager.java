package com.github.manevolent.jbot.event;

public interface EventManager {

    /**
     * Registers all compatible events from the listener object.
     * @param listener Event listener object.
     */
    void registerListener(EventListener listener);

    /**
     * Unregisters all stored events from the manager.
     * @param listener Event listener object.
     */
    void unregisterListener(EventListener listener);

    /**
     * Checks an event given from the global system against the cached event system.
     * @param event Event to check.
     * @return A checked event.
     */
    Event fire(Event event) throws EventExecutionException;

}
