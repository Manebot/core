package com.github.manevolent.jbot.command.response;

import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandArgumentException;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

import java.util.List;

public class CommandListResponse<T> extends CommandResponse {
    public static final int DEFAULT_ELEMENTS_PER_PAGE = 6;

    private final int page;
    private final int totalElements;
    private final int elementsPerPage;

    private final ListAccessor<T> accessor;
    private final ListElementResponder<T> responder;

    public CommandListResponse(int actualTotal,
                               int page,
                               int elementsPerPage,
                               ListAccessor<T> accessor,
                               ListElementResponder<T> responder) {
        this.page = page;
        this.totalElements = actualTotal;
        this.elementsPerPage = elementsPerPage;

        this.accessor = accessor;
        this.responder = responder;
    }

    public CommandListResponse(int actualTotal,
                               int page,
                               ListAccessor<T> accessor,
                               ListElementResponder<T> responder) {
        this(actualTotal, page, DEFAULT_ELEMENTS_PER_PAGE, accessor, responder);
    }

    @Override
    public void respond(CommandSender sender) throws CommandExecutionException {
        int totalPages = (int) Math.ceil((double)totalElements / (double)elementsPerPage);
        int elements = Math.min(accessor.size(), elementsPerPage);

        if (elements < 0) throw new CommandExecutionException("Invalid page (" + totalPages + " pages).");
        else if (elements == 0) throw new CommandExecutionException("No results found.");

        sender.sendMessage("Discovered " + totalElements + " " + (totalElements == 1 ? "item" : "items")
                        + " (showing " + elements + ", page " + page + " of " + totalPages + "):");

        for (int i = 0; i < elements; i ++)
            sender.sendMessage(" - " + responder.line(sender, accessor.get(i)).trim());
    }

    public T single() throws CommandExecutionException {
        if (accessor.size() <= 0)
            throw new CommandArgumentException("No results found.");
        else if (accessor.size() > 1)
            throw new CommandArgumentException("More than 1 result found.");

        return accessor.get(0);
    }

    public interface ListElementResponder<T> {
        String line(CommandSender sender, T o);
    }

    public interface ListAccessor<T> {
        T get(int resultOffset);
        int size();
    }

    private static final class DefaultListAccessor<T> implements ListAccessor<T> {
        private final int offset;
        private final List<T> baseList;

        private DefaultListAccessor(List<T> baseList, int offset) {
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

    private static final class ShadowListAccessor<T> implements ListAccessor<T> {
        private final List<T> baseList;

        private ShadowListAccessor(List<T> baseList) {
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

    /**
     * Creates a direct, managed listing of the list type specified.
     *
     * @param baseList Base list to page from.
     * @param elementsPerPage Elements per page.
     * @param page Current page
     * @param responder Responder for formatting
     * @param <T> Type of element to page
     * @return Response
     */
    public static <T> CommandListResponse<T> direct(List<T> baseList, int elementsPerPage, int page,
                                                  ListElementResponder<T> responder) {
        return new CommandListResponse<>(
                baseList.size(), page, elementsPerPage,
                new DefaultListAccessor<>(baseList, (page-1) * elementsPerPage),
                responder
        );
    }

    /**
     * Creates a virtual, shadowed listing of the list type specified.
     *
     * @param pageList Base list to page from.
     * @param elementsPerPage Imaginary elements per page.
     * @param page Current page
     * @param totalElements Total actual elements
     * @param responder Responder for formatting
     * @param <T> Type of element to page
     * @return Response
     */
    public static <T> CommandListResponse<T> virtual(List<T> pageList,
                                                     int elementsPerPage,
                                                     int page,
                                                     int totalElements,
                                                     ListElementResponder<T> responder) {
        return new CommandListResponse<>(
                totalElements, page, elementsPerPage,
                new ShadowListAccessor<>(pageList),
                responder
        );
    }
}
