package com.github.manevolent.jbot.command.response;

import com.github.manevolent.jbot.chat.ChatSender;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

public class DefaultBasicCommandListResponse<T> extends CommandListResponse<T> {
    public DefaultBasicCommandListResponse(ChatSender sender,
                                           int actualTotal,
                                           int page,
                                           int elementsPerPage,
                                           ListAccessor<T> accessor,
                                           ListElementFormatter<T> responder) {
        super(sender, actualTotal, page, elementsPerPage, accessor, responder);
    }

    @Override
    public void send() throws CommandExecutionException {
        int totalPages = (int) Math.ceil((double)getTotalElements() / (double)getElementsPerPage());
        int elements = Math.min(getAccessor().size(), getElementsPerPage());

        if (elements < 0) throw new CommandExecutionException("Invalid page (" + totalPages + " pages).");
        else if (elements == 0) throw new CommandExecutionException("No results found.");

        getSender().sendMessage("Discovered " + getTotalElements() + " " + (getTotalElements() == 1 ? "item" : "items")
                + " (showing " + elements + ", page " + getPage() + " of " + totalPages + "):");

        for (int i = 0; i < elements; i ++)
            getSender().sendMessage(" - " + getResponder().line(getSender(), getAccessor().get(i)).trim());
    }
}