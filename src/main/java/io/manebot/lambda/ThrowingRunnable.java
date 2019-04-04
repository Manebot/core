package io.manebot.lambda;

public interface ThrowingRunnable<E extends Exception> extends Runnable {

    void runChecked() throws E;

    @Override
    default void run() {
        try {
            runChecked();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}