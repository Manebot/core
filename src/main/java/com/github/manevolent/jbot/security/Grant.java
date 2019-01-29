package com.github.manevolent.jbot.security;

import java.util.Arrays;

/**
 * Grants are used to control permission across the permission system.
 */
public enum Grant {
    ALLOW,
    DENY;

    /**
     * Gets a grant from the specified name.
     * @param name Name to search for.
     * @return Grant instance
     * @throws IllegalArgumentException
     */
    public static Grant fromName(String name) throws IllegalArgumentException {
        return Arrays.stream(Grant.values())
                .filter(x -> x.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(name));
    }

    public static Grant fromValue(boolean grant) {
        return grant ? ALLOW : DENY;
    }
}
