package com.github.manevolent.jbot.security;

import com.google.common.collect.MapMaker;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

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
}
