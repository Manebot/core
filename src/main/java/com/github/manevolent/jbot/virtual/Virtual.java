package com.github.manevolent.jbot.virtual;

import java.util.Collection;
import java.util.concurrent.ThreadFactory;

public abstract class Virtual implements ThreadFactory {
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
     *
     * @param runnable runnable for process to execute.
     * @return VirtualProcess instance.
     */
    public abstract VirtualProcess create(Runnable runnable) throws SecurityException;

    /**
     * Gets the current process.
     *
     * @return VirtualProcess instance if found, null otherwise.
     */
    public VirtualProcess currentProcess() {
        return getProcess(Thread.currentThread());
    }

    /**
     * Gets the process associated with a given thread.
     *
     * @param thread Thread to look for.
     * @return VirtualProcess instance if found, null otherwise.
     */
    public VirtualProcess getProcess(Thread thread) {
        long id = thread.getId();

        return getProcesses().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

}
