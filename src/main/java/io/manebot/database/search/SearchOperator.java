package io.manebot.database.search;

/**
 * Search operators are lexically-parsed tokens that indicate the type of operation (include, exclude, or merge) that
 * should take place on a given token in a search string.
 */
public enum SearchOperator {
    INCLUDE('~'), // OR
    EXCLUDE('-'), // AND NOT
    MERGE('+'),   // AND
    UNSPECIFIED('?');

    private final char character;

    SearchOperator(char character) {
        this.character = character;
    }

    public static SearchOperator getDefault() {
        return INCLUDE;
    }

    public char getCharacter() {
        return character;
    }
}