package io.manebot.lambda;

import java.util.function.Function;

public interface ThrowingFunction<T, R, E extends Exception> extends Function<T, R> {

    R applyChecked(T t) throws E;

    default R apply(T t) {
        try {
            return applyChecked(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
