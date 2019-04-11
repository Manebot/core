package io.manebot.virtual;

import io.manebot.user.User;

import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

public interface VirtualProcess {

    void interrupt();

    void kill() throws InterruptedException;

    long getId();

    void setDescription(String description);

    String getDescription();

    String getName();

    boolean isRoot();

    boolean isCallerSelf();

    User getUser();

    void start();

    void changeUser(User user) throws SecurityException;

    VirtualProcess getParent();

    Logger getLogger();

    boolean canControl();

    boolean isRunning();

    Profiler getProfiler();

    ThreadFactory newThreadFactory() throws SecurityException;

}
