package com.github.manevolent.jbot.event;

public enum EventPriority {

    /**
     * Events with the LOWEST priority are always executed last.
     */
    LOWEST(0),

    /**
     * Events with the LOW priority are executed after all events with the NORMAL priority have been executed.
     */
    LOW(1),

    /**
     * Events with the NORMAL priority are executed after all events with the HIGH priority have been executed.
     */
    NORMAL(2),

    /**
     * Events with the HIGH priority are executed after all events with the HIGHEST priority have been executed.
     */
    HIGH(3),

    /**
     * Events with the HIGHEST priority are always executed first.
     */
    HIGHEST(4);

    private int orderId;
    EventPriority(int orderId) {
        this.orderId = orderId;
    }

}
