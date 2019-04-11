package io.manebot.security;

import io.manebot.lambda.ThrowingCallable;
import io.manebot.lambda.ThrowingFunction;
import io.manebot.lambda.ThrowingRunnable;
import io.manebot.user.User;

public interface ElevationDispatcher {

    User getElevatedUser();

    <R> R elevate(ThrowingCallable<R, Exception> callable) throws Exception;

    <T, R> R elevate(T object, ThrowingFunction<T, R, Exception> function) throws Exception;

    void elevate(ThrowingRunnable<Exception> runnable) throws Exception;

}
