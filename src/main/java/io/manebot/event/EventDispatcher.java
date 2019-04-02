package io.manebot.event;

import java.util.concurrent.Future;

public interface EventDispatcher {

    /**
     * Fires an event.
     * @param event Event to fire.
     * @param <T> Event type.
     * @return Event
     * @throws EventExecutionException if an exception occurred executing the event.
     */
    <T extends Event> T execute(T event) throws EventExecutionException;

    /**
     * Fires an event asynchronously.
     * @param event Event to fire.
     * @param <T> Event type.
     * @return Event future.
     */
    <T extends Event> Future<T> executeAsync(T event);

}
