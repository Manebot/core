package io.manebot.event;

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

}
