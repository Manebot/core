package com.github.manevolent.jbot.virtual;

import java.util.Collection;

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
     * Finds all virtual processes.
     * @return Processes.
     */
    public abstract Collection<VirtualProcess> getProcesses();

    /**
     * Creates a new process
     * @param runnable runnable for process to execute.
     * @return VirtualProcess instance.
     */
    public abstract VirtualProcess create(Runnable runnable) throws SecurityException;
}
