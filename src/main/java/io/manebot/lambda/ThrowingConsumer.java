package io.manebot.lambda;

import java.util.function.Consumer;

public interface ThrowingConsumer<V, E extends Exception> extends Consumer<V> {

    void acceptChecked(V v) throws E;

    @Override
    default void accept(V v) {
        try {
            acceptChecked(v);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
