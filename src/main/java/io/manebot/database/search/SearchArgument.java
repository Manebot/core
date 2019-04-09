package io.manebot.database.search;

public final class SearchArgument {
    private final SearchOperator operator;
    private final String value;

    public SearchArgument(SearchOperator operator, String value) {
        this.operator = operator;
        this.value = value;
    }

    public SearchOperator getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

}
