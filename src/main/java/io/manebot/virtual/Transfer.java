package io.manebot.virtual;

import io.manebot.lambda.ThrowingFunction;

public abstract class Transfer<C, S, E extends Exception> implements ThrowingFunction<C, S, E> {
    private final ThrowingFunction<C, S, E> function;

    public Transfer(ThrowingFunction<C, S, E> function) {
        this.function = function;
    }

    @Override
    public S applyChecked(C c) throws E {
        return function.applyChecked(c);
    }
}
