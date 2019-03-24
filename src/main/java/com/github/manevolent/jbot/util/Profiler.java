package com.github.manevolent.jbot.util;

import com.google.common.collect.MapMaker;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public final class Profiler implements AutoCloseable {
    private static final ConcurrentMap<Thread, Profiler> profilers = new MapMaker().weakKeys().makeMap();

    public static final Profiler get(Thread thread) {
        return profilers.get(thread);
    }

    public static final Profiler region(String name) {
        Profiler profiler = profilers.get(Thread.currentThread());

        if (profiler == null)
            profiler = new Profiler(name, null);
        else
            profiler = profiler.getOrCreateChild(name);

        profiler.begin();

        return profiler;
    }

    private final String name;
    private final Profiler parent;
    private final Object accessLock;

    /**
     * Time created, in nanoseconds
     */
    private long creationTime;

    /**
     * True if the profiler is running, false otherwise.
     */
    private boolean running = false;

    /**
     * Time the profiler was last started.
     */
    private long startTime = creationTime;

    /**
     * Total time the profiler has profiled.
     */
    private long totalTime = 0L;

    /**
     * Total execution cycles.
     */
    private long executions = 0L;

    private Map<String, Profiler> children = new LinkedHashMap<>();

    public Profiler(String name, Profiler parent) {
        this.name = name;

        this.creationTime = System.nanoTime();

        this.parent = parent;

        if (this.parent != null && this.parent.accessLock != null)
            this.accessLock = this.parent.accessLock;
        else
            this.accessLock = new Object();
    }

    public String getName() {
        return name;
    }

    public boolean isRunning() {
        return running;
    }

    public Profiler getParent() {
        return parent;
    }

    private Profiler begin() {
        if (!running) {
            synchronized (accessLock) {
                profilers.put(Thread.currentThread(), this);
                running = true;
                executions++;
                startTime = System.nanoTime();
            }
        }

        return this;
    }

    private Profiler end() {
        if (running) {
            synchronized (accessLock) {
                totalTime += System.nanoTime() - startTime;
                if (this.parent != null) profilers.put(Thread.currentThread(), this.parent);
                else profilers.put(Thread.currentThread(), this);
                running = false;
            }
        }

        return this;
    }

    /**
     * Gets the total count of executions this profiler is processng.
     */
    public long getExecutions() {
        return executions;
    }

    public long getTotalActiveNanoseconds() {
        return getTotalActiveNanoseconds(System.nanoTime());
    }

    /**
     * Gets the profiler's total active nanoseconds.  This includes child time.
     */
    public long getTotalActiveNanoseconds(long since) {
        return totalTime + (isRunning() ? (since - startTime) : 0L);
    }

    /**
     * Gets the profiler's child active nanoseconds.  This excludes own time.
     */
    public long getChildActiveNanoseconds() {
        return getChildActiveNanoseconds(System.nanoTime());
    }

    /**
     * Gets the profiler's child active nanoseconds.  This excludes own time.
     */
    public long getChildActiveNanoseconds(long since) {
        return children
                .values()
                .stream()
                .mapToLong(x -> x.getTotalActiveNanoseconds(since))
                .sum();
    }

    /**
     * Gets the profiler's own total active nanoseconds.  This excludes child time.
     */
    public long getOwnActiveNanoseconds() {
        return getOwnActiveNanoseconds(System.nanoTime());
    }
    /**
     * Gets the profiler's own total active nanoseconds.  This excludes child time.
     */
    public long getOwnActiveNanoseconds(long since) {
        return getTotalActiveNanoseconds(since) - getChildActiveNanoseconds(since);
    }

    /**
     * Gets the total lifetime of this profiler, in nanoseconds.
     */
    public long getLifetimeNanoseconds() {
        return getLifetimeNanoseconds(System.nanoTime());
    }
    /**
     * Gets the total lifetime of this profiler, in nanoseconds.
     */
    public long getLifetimeNanoseconds(long since) {
        return since - creationTime;
    }

    /**
     * Gets the ratio of this profiler region compared to the parent.
     * 0.2: 20% of time of the parent's time was spent in this profiler
     */
    public double getParentActiveRatio() {
        return getParentActiveRatio(System.nanoTime());
    }

    /**
     * Gets the ratio of this profiler region compared to the parent.
     * 0.2: 20% of time of the parent's time was spent in this profiler
     */
    public double getParentActiveRatio(long since) {
        Profiler parent = getParent();
        if (parent == null) return 1D;
        else if (parent.getLifetimeNanoseconds(since) <= 0D) return 1D;

        return (double)getTotalActiveNanoseconds(since) / (double)parent.getLifetimeNanoseconds(since);
    }

    /**
     * Gets the profiler's own active ratio.  This ratio excludes child processing.
     * @return Own active ratio
     */
    public double getOwnActiveRatio() {
        return getOwnActiveRatio(System.nanoTime());
    }

    /**
     * Gets the profiler's own active ratio.  This ratio excludes child processing.
     * @return Own active ratio
     */
    public double getOwnActiveRatio(long since) {
        if (getLifetimeNanoseconds(since) <= 0D) return 1D;
        return (double)getOwnActiveNanoseconds(since) / (double)getLifetimeNanoseconds(since);
    }

    /**
     * Gets the profiler's total active ratio.  This ratio includes child processing.
     */
    public double getTotalActiveRatio() {
        return getTotalActiveRatio(System.nanoTime());
    }

    /**
     * Gets the profiler's total active ratio.  This ratio includes child processing.
     */
    public double getTotalActiveRatio(long since) {
        long lifetimeNanoseconds = getLifetimeNanoseconds(since);
        if (lifetimeNanoseconds <= 0D) return 1D;

        return (double)getTotalActiveNanoseconds(since) / (double)lifetimeNanoseconds;
    }

    /**
     * Gets the profiler's child active ratio.  This ratio excludes own processing.
     */
    public double getChildActiveRatio() {
        return getChildActiveRatio(System.nanoTime());
    }
    /**
     * Gets the profiler's child active ratio.  This ratio excludes own processing.
     */
    public double getChildActiveRatio(long since) {
        return getTotalActiveRatio(since) - getOwnActiveRatio(since);
    }

    /**
     * Gets the average active time, in nanoseconds, per execution, including child time.
     */
    public double getTotalNanosecondsPerExecution() {
        return getTotalNanosecondsPerExecution(System.nanoTime());
    }
    /**
     * Gets the average active time, in nanoseconds, per execution, including child time.
     */
    public double getTotalNanosecondsPerExecution(long since) {
        long executions = getExecutions();
        if (executions <= 0) return 0D;
        return (double)getTotalActiveNanoseconds(since) / (double)executions;
    }

    /**
     * Gets the average active time, in nanoseconds, per execution, excluding child time.
     */
    public double getOwnNanosecondsPerExecution() {
        return getOwnNanosecondsPerExecution(System.nanoTime());
    }

    /**
     * Gets the average active time, in nanoseconds, per execution, excluding child time.
     */
    public double getOwnNanosecondsPerExecution(long since) {
        long executions = getExecutions();
        if (executions <= 0) return 0D;
        return (double)getOwnActiveNanoseconds(since) / (double)executions;
    }

    /**
     * Gets the average active time, in nanoseconds, per execution, excluding own time.
     */
    public double getChildNanosecondsPerExecution() {
        return getChildNanosecondsPerExecution(System.nanoTime());
    }
    /**
     * Gets the average active time, in nanoseconds, per execution, excluding own time.
     */
    public double getChildNanosecondsPerExecution(long since) {
        long executions = getExecutions();
        if (executions <= 0) return 0D;
        return (double)getChildActiveNanoseconds(since) / (double)executions;
    }

    /**
     * Gets the count of executions/sec of this profiler.
     * @return executions/sec
     */
    public double getExecutionsPerNanosecond() {
        return getExecutionsPerNanosecond(System.nanoTime());
    }

    /**
     * Gets the count of executions/sec of this profiler.
     * @return executions/sec
     */
    public double getExecutionsPerNanosecond(long since) {
        long lifetime = getLifetimeNanoseconds(since);
        if (lifetime <= 0) return 0D;
        return (double)executions / (double)lifetime;
    }


    /**
     * Gets a child by name.
     * @param name Profiler name.
     * @return Profiler child instance.
     */
    public Profiler getOrCreateChild(String name) {
        return children.computeIfAbsent(name, name1 -> new Profiler(name1, this));
    }

    public Profiler getChild(String name) {
        return children.get(name);
    }

    public Map<String, Profiler> get() {
        return new LinkedHashMap<>(children);
    }

    @Override
    public void close() {
        end();
    }

    public Collection<Profiler> getChildren() {
        return new LinkedList<>(children.values());
    }

    public Object getLock() {
        return accessLock;
    }
}
