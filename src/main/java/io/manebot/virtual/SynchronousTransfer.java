package io.manebot.virtual;

import io.manebot.lambda.ThrowingFunction;

import java.util.concurrent.*;
import java.util.function.Function;

public class SynchronousTransfer<C, S, E extends Exception> extends Transfer<C, S, E> implements Runnable {
    private final Object lock = new Object();

    private final Class<E> exceptionClass;
    private final BlockingQueue<Message> serverQueue = new SynchronousQueue<>(true);

    public SynchronousTransfer(Class<E> exceptionClass, ThrowingFunction<C, S, E> function) {
        super(function);

        this.exceptionClass = exceptionClass;
    }

    /**
     * Sends a resource into the synchronous transfer.
     * @param object resource to place into the queue.
     * @return response object.
     * @throws RuntimeException if execution fails.
     */
    @SuppressWarnings("unchecked")
    @Override
    public S applyChecked(C object) throws E {
        final Message msg = new Message(object);

        try {
            serverQueue.put(msg);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            return msg.future.get();
        } catch (ExecutionException e) {
            if (e.getCause() != null && e.getCause().getClass().isAssignableFrom(exceptionClass))
                throw (E) e;
            else
                throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public S get(C object, int timeoutMillis) throws ExecutionException, TimeoutException {
        final Message msg = new Message(object);

        try {
            serverQueue.put(msg);
        } catch (InterruptedException e) {
            throw new ExecutionException(e);
        }

        try {
            return msg.future.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new ExecutionException(e);
        }
    }

    private Message next() throws InterruptedException {
        return serverQueue.take();
    }

    @Override
    public void run() {
        while (true) {
            try (Message message = next()) {
                try {
                    message.complete(SynchronousTransfer.super.applyChecked(message.getObject()));
                } catch (Throwable e) {
                    message.completeExceptionally(e);
                }
            } catch (Throwable e ) {
                throw new RuntimeException(e);
            }
        }
    }

    public class Message implements AutoCloseable {
        private final C message;
        private final CompletableFuture<S> future = new CompletableFuture<>();

        private boolean completed = false;

        private Message(C message) {
            this.message = message;
        }

        public C getObject() {
            return message;
        }

        public Future<S> getFuture() {
            return future;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void complete(S object) {
            synchronized (lock) {
                if (completed) throw new IllegalStateException("already completed");
                future.complete(object);
                completed = true;
            }
        }

        public void completeExceptionally(Throwable ex) {
            synchronized (lock) {
                if (completed) throw new IllegalStateException("already completed");
                future.completeExceptionally(ex);
                completed = true;
            }
        }

        @Override
        public void close() throws Exception {
            synchronized (lock) {
                if (!completed) {
                    completeExceptionally(new IllegalStateException("closed"));
                }
            }
        }
    }
}
