package io.manebot.database.search;

/**
 * Search operators are lexically-parsed tokens that indicate the type of operation (include, exclude, or merge) that
 * should take place on a given token in a search string.
 */
public enum SearchOperator {
    INCLUDE('~', true), // OR
    EXCLUDE('-', true), // AND NOT
    MERGE('+', true),   // AND
    UNSPECIFIED('?', false);

    private final char character;
    private final boolean canSpecify;

    SearchOperator(char character, boolean canSpecify) {
        this.character = character;
        this.canSpecify = canSpecify;
    }

    public static SearchOperator getDefault() {
        return INCLUDE;
    }

    public char getCharacter() {
        return character;
    }

    public boolean canSpecify() {
        return canSpecify;
    }
}