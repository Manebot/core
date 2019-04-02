package io.manebot.event;

public interface EventExecutor {

    /**
     * Executes the listener on the executor.
     * @param event Event to execute.
     */
    void fire(Event event) throws EventExecutionException;

    /**
     * Gets the event listener associated with this executor.
     * @return Listener.
     */
    EventListener getListener();

    /**
     * Gets the priority for this executor.
     * @return executor priority.
     */
    EventPriority getPriority();

}