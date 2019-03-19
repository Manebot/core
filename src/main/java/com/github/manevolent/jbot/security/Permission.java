package com.github.manevolent.jbot.security;

import com.github.manevolent.jbot.command.exception.CommandAccessException;
import com.github.manevolent.jbot.virtual.Virtual;
import com.github.manevolent.jbot.virtual.VirtualProcess;
import com.google.common.collect.MapMaker;

import java.util.Map;

public final class Permission {
    private static final Map<String, Permission> referenceMap = new MapMaker().weakValues().makeMap();
    private final String node;

    private Permission(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    @Override
    public String toString() {
        return node;
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    @Override
    public boolean equals(Object b) {
        return b != null && b instanceof Permission && node.equals(((Permission) b).node);
    }

    /**
     * Gets or creates a permission in the permission node tree.
     * @param node node to create.
     * @return Permission instance.
     */
    public static Permission get(String node) {
        synchronized (referenceMap) {
            return referenceMap.computeIfAbsent(node, Permission::new);
        }
    }

    public static void checkPermission(String node) throws IllegalStateException, CommandAccessException {
        checkPermission(get(node));
    }

    public static void checkPermission(Permission permission) throws IllegalStateException, CommandAccessException {
        VirtualProcess currentProcess = Virtual.getInstance().currentProcess();
        if (currentProcess == null)
            throw new IllegalStateException(
                    Thread.currentThread().toString() +
                    " is not member of virtual subsystem"
            );

        if (currentProcess.getUser() == null)
            throw new IllegalStateException(
                    Thread.currentThread().toString() +
                    " is member of virtual subsystem, but is not logged in"
            );

        currentProcess.getUser().checkPermission(permission);
    }
}
