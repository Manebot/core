package com.github.manevolent.jbot.command.executor.chained;

public enum ChainPriority {
    NONE(-1),
    LOW(1),
    HIGH(2);

    private int ordinal;
    ChainPriority(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getOrdinal() {
        return ordinal;
    }
}
