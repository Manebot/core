package io.manebot.database.search;

public enum SortOrder {
    ASCENDING("a", "asc", "ascending", "ascend"),
    DESCENDING("d", "desc", "descending", "descend");

    public static final SortOrder DEFAULT = ASCENDING;
    private final String[] keys;

    SortOrder(String... keys) {
        this.keys = keys;
    }

    public String[] getKeys() {
        return keys;
    }
}
