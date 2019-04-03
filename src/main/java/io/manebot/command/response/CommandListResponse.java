package io.manebot.command.response;

import io.manebot.chat.ChatSender;
import io.manebot.chat.TextBuilder;

import java.util.List;
import java.util.function.Supplier;

public abstract class CommandListResponse<T> extends CommandResponse {
    private final long page;
    private final long totalElements;
    private final int elementsPerPage;

    private final ListAccessor<T> accessor;
    private final ListElementFormatter<T> responder;

    public CommandListResponse(ChatSender sender,
                               long actualTotal,
                               long page,
                               int elementsPerPage,
                               ListAccessor<T> accessor,
                               ListElementFormatter<T> responder) {
        super(sender);

        this.page = page;
        this.totalElements = actualTotal;
        this.elementsPerPage = elementsPerPage;

        this.accessor = accessor;
        this.responder = responder;
    }

    public long getPage() {
        return page;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getElementsPerPage() {
        return elementsPerPage;
    }

    public ListAccessor<T> getAccessor() {
        return accessor;
    }

    public ListElementFormatter<T> getResponder() {
        return responder;
    }

    public interface ListElementFormatter<T> {
        void line(TextBuilder builder, T o);
    }

    public static class DefaultListElementFormatter<T> implements ListElementFormatter<T> {
        @Override
        public void line(TextBuilder builder, T o) {
            builder.append(o.toString());
        }
    }

    public interface ListAccessor<T> {
        T get(int resultOffset);
        long size();
    }

    private static final class DirectListAccessor<T> implements ListAccessor<T> {
        private final long offset;
        private final List<T> baseList;

        private DirectListAccessor(List<T> baseList, long offset) {
            this.baseList = baseList;
            this.offset = offset;
        }

        @Override
        public T get(int resultOffset) {
            return baseList.get((int) (offset + resultOffset));
        }

        @Override
        public long size() {
            return baseList.size() - offset;
        }
    }

    private static final class VirtualListAccessor<T> implements ListAccessor<T> {
        private final List<T> baseList;

        private VirtualListAccessor(List<T> baseList) {
            this.baseList = baseList;
        }

        @Override
        public T get(int resultOffset) {
            return baseList.get(resultOffset);
        }

        @Override
        public long size() {
            return baseList.size();
        }
    }

    public static abstract class Builder<T> {
        private long page = 1;
        private long totalElements;
        private int elementsPerPage = 6;

        private ListElementFormatter<T> responder = (sender, o) -> o.toString();

        private Supplier<ListAccessor<T>> accessorSupplier;

        public Builder() { }

        public long getPage() {
            return page;
        }

        public Builder<T> page(long page) {
            this.page = page;
            return this;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public Builder<T> totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public int getElementsPerPage() {
            return elementsPerPage;
        }

        public Builder<T> elementsPerPage(int elementsPerPage) {
            this.elementsPerPage = elementsPerPage;
            return this;
        }

        public Builder<T> direct(List<T> list) {
            this.accessorSupplier = () -> new DirectListAccessor<>(list, (page-1L) * elementsPerPage);
            this.totalElements = list.size();
            return this;
        }

        public Builder<T> virtual(List<T> list) {
            this.accessorSupplier = () -> new VirtualListAccessor<>(list);
            return this;
        }

        public ListElementFormatter<T> getResponder() {
            return responder;
        }

        public Builder<T> responder(ListElementFormatter<T> responder) {
            this.responder = responder;
            return this;
        }

        public ListAccessor<T> createListAccessor() {
            return accessorSupplier.get();
        }

        public abstract CommandListResponse<T> build();
    }
}
