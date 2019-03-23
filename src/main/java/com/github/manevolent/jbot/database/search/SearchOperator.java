package com.github.manevolent.jbot.database.search;

/**
 * Search operators are lexically-parsed tokens that indicate the type of operation (include, exclude, or merge) that
 * should take place on a given token in a search string.
 */
public enum SearchOperator {
    INCLUDE('~'), // OR
    EXCLUDE('-'), // AND NOT
    MERGE('+');   // AND

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

    /**
     * Finds a search operator by its character.
     * @param c Search operator character.
     * @return Search operator instance.
     */
    public static SearchOperator fromOperator(char c) {
        for (SearchOperator operator : values())
            if (operator.getCharacter() == c) return operator;

        throw new IllegalArgumentException("Unexpected search operator: " +c);
    }
}