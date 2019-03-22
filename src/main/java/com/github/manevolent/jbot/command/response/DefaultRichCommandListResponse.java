package com.github.manevolent.jbot.command.response;

import com.github.manevolent.jbot.chat.ChatSender;
import com.github.manevolent.jbot.chat.DefaultRichChatMessage;
import com.github.manevolent.jbot.chat.RichChatMessage;
import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;

import java.util.Collection;
import java.util.LinkedList;

public class DefaultRichCommandListResponse<T> extends CommandListResponse<T> {
    public DefaultRichCommandListResponse(ChatSender sender, int actualTotal, int page,
                                          int elementsPerPage, ListAccessor<T> accessor,
                                          ListElementFormatter<T> responder) {
        super(sender, actualTotal, page, elementsPerPage, accessor, responder);
    }

    @Override
    public void send() throws CommandExecutionException {
        int totalPages = (int) Math.ceil((double)getTotalElements() / (double)getElementsPerPage());
        int elements = Math.min(getAccessor().size(), getElementsPerPage());

        if (elements < 0) throw new CommandExecutionException("Invalid page (" + totalPages + " pages).");
        else if (elements == 0) throw new CommandExecutionException("No results found.");

        DefaultRichChatMessage.Builder builder =
                DefaultRichChatMessage.builder()
                .title("Discovered " + getTotalElements() + " " + (getTotalElements() == 1 ? "item" : "items"))
                .footer("showing " + elements + ", page " + getPage() + " of " + totalPages + ")");

        Collection<RichChatMessage.Element> body = new LinkedList<>();

        for (int i = 0; i < elements; i ++)
            body.add(new RichChatMessage.PlainText(getResponder().line(getSender(), getAccessor().get(i)).trim()));

        builder.body(body);

        getSender().sendMessage(builder.build());
    }
}