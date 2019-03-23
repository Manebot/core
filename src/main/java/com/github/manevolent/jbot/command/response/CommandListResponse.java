package com.github.manevolent.jbot.command.response;

import com.github.manevolent.jbot.chat.ChatSender;
import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandArgumentException;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public abstract class CommandListResponse<T> extends CommandResponse {
    private final int page;
    private final int totalElements;
    private final int elementsPerPage;

    private final ListAccessor<T> accessor;
    private final ListElementFormatter<T> responder;

    public CommandListResponse(ChatSender sender,
                               int actualTotal,
                               int page,
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

    public int getPage() {
        return page;
    }

    public int getTotalElements() {
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
        String line(ChatSender sender, T o);
    }

    public interface ListAccessor<T> {
        T get(int resultOffset);
        int size();
    }

    private static final class DirectListAccessor<T> implements ListAccessor<T> {
        private final int offset;
        private final List<T> baseList;

        private DirectListAccessor(List<T> baseList, int offset) {
            this.baseList = baseList;
            this.offset = offset;
        }

        @Override
        public T get(int resultOffset) {
            return baseList.get(offset + resultOffset);
        }

        @Override
        public int size() {
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
        public int size() {
            return baseList.size();
        }
    }

    public static abstract class Builder<T> {
        private int page = 1;
        private int totalElements;
        private int elementsPerPage = 6;

        private ListElementFormatter<T> responder = (sender, o) -> o.toString();

        private Supplier<ListAccessor<T>> accessorSupplier;

        public Builder() { }

        public int getPage() {
            return page;
        }

        public Builder<T> page(int page) {
            this.page = page;
            return this;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public Builder<T> totalElements(int totalElements) {
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
            this.accessorSupplier = () -> new DirectListAccessor<>(list, (page-1) * elementsPerPage);
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
