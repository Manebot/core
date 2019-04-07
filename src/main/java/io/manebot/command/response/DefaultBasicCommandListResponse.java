package io.manebot.command.response;

import io.manebot.chat.ChatMessage;
import io.manebot.chat.ChatSender;
import io.manebot.command.exception.CommandExecutionException;

import java.util.Collection;
import java.util.LinkedList;

public class DefaultBasicCommandListResponse<T> extends CommandListResponse<T> {
    public DefaultBasicCommandListResponse(ChatSender sender,
                                           long actualTotal,
                                           long page,
                                           int elementsPerPage,
                                           ListAccessor<T> accessor,
                                           ListElementFormatter<T> responder) {
        super(sender, actualTotal, page, elementsPerPage, accessor, responder);
    }

    @Override
    public Collection<ChatMessage> send() throws CommandExecutionException {
        long totalPages = (long) Math.ceil((double)getTotalElements() / (double)getElementsPerPage());
        long elements = Math.min(getAccessor().size(), getElementsPerPage());

        if (elements < 0) throw new CommandExecutionException("Invalid page (" + totalPages + " pages).");
        else if (elements == 0) throw new CommandExecutionException("No results found.");

        getSender().sendMessage(
                "Discovered " + getTotalElements() + " " + (getTotalElements() == 1 ? "item" : "items")
                        + " (showing " + elements + ", page " + getPage() + " of " + totalPages + ")" + ":"
        );

        Collection<ChatMessage> chatMessages = new LinkedList<>();

        for (int i = 0; i < elements; i ++) {
            int finalI = i;

            chatMessages.addAll(getSender().sendFormattedMessage(builder -> {
                builder.append(" - ");
                getResponder().line(builder, getAccessor().get(finalI));
            }));
        }

        return chatMessages;
    }
}