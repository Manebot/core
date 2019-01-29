package com.github.manevolent.jbot.virtual;

public abstract class Virtual {
    private static final Object instanceLock = new Object();
    private static Virtual instance;
    public static Virtual getInstance() {
        return instance;
    }
    public static void setInstance(Virtual instance) throws SecurityException {
        synchronized (instanceLock) {
            if (Virtual.instance != null) throw new SecurityException("instance is already set");
            Virtual.instance = instance;
        }
    }

    /**
     * Creates a new process
     * @param runnable runnable for process to execute.
     * @return VirtualProcess instance.
     */
    public abstract VirtualProcess create(Runnable runnable) throws SecurityException;
}
