package io.manebot.lambda;

import java.util.concurrent.Callable;

public interface ThrowingCallable<V, E extends Exception> extends Callable {

    @Override
    V call() throws E;

}
