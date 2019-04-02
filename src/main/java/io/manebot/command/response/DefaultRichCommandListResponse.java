package io.manebot.command.response;

import io.manebot.chat.ChatSender;
import io.manebot.command.exception.CommandExecutionException;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultRichCommandListResponse<T> extends CommandListResponse<T> {
    public DefaultRichCommandListResponse(ChatSender sender, long actualTotal, long page,
                                          int elementsPerPage, ListAccessor<T> accessor,
                                          ListElementFormatter<T> responder) {
        super(sender, actualTotal, page, elementsPerPage, accessor, responder);
    }

    @Override
    public void send() throws CommandExecutionException {
        int totalPages = (int) Math.ceil((double)getTotalElements() / (double)getElementsPerPage());
        long elements = Math.min(getAccessor().size(), getElementsPerPage());

        if (elements < 0) throw new CommandExecutionException("Invalid page (" + totalPages + " pages).");
        else if (elements == 0) throw new CommandExecutionException("No results found.");

        getSender().sendMessage(builder ->
            builder.embed(embedBuilder -> {
                embedBuilder
                        .title("Discovered " + getTotalElements() + " " + (getTotalElements() == 1 ? "item" : "items"))
                        .footer("showing " + elements + ", page " + getPage() + " of " + totalPages);

                embedBuilder.description(
                        String.join("\n",
                                IntStream.range(0, (int)elements).boxed()
                                        .map(i -> " - " + getResponder().line(getSender(), getAccessor().get(i)))
                                        .collect(Collectors.toList()))
                );
            })
        );
    }
}