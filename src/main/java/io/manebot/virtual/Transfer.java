package io.manebot.virtual;

import java.util.function.Function;

public abstract class Transfer<C, S> implements Function<C, S> {
    private final Function<C, S> function;

    public Transfer(Function<C, S> function) {
        this.function = function;
    }

    @Override
    public S apply(C c) {
        return function.apply(c);
    }
}
