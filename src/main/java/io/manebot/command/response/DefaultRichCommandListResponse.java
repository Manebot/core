package io.manebot.command.response;

import io.manebot.chat.ChatMessage;
import io.manebot.chat.ChatSender;
import io.manebot.command.exception.CommandExecutionException;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultRichCommandListResponse<T> extends CommandListResponse<T> {
    public DefaultRichCommandListResponse(ChatSender sender, long actualTotal, long page,
                                          int elementsPerPage, ListAccessor<T> accessor,
                                          ListElementFormatter<T> responder) {
        super(sender, actualTotal, page, elementsPerPage, accessor, responder);
    }

    @Override
    public Collection<ChatMessage> send() throws CommandExecutionException {
        int totalPages = (int) Math.ceil((double)getTotalElements() / (double)getElementsPerPage());
        long elements = Math.min(getAccessor().size(), getElementsPerPage());

        if (elements < 0) throw new CommandExecutionException("Invalid page (" + totalPages + " pages).");
        else if (elements == 0) throw new CommandExecutionException("No results found.");

        return getSender().sendMessage(
                builder -> {
                    if (builder.getChat().getFormat().shouldMention(getSender().getPlatformUser()))
                        builder.message(textBuilder -> textBuilder.appendMention(getSender().getPlatformUser()));

                    builder.embed(embedBuilder -> {
                        embedBuilder.title(
                                "Discovered " +
                                        getTotalElements() + " " +
                                        (getTotalElements() == 1 ? "item" : "items")
                        );

                        embedBuilder.description(textBuilder -> {
                            for (int i = 0; i < elements; i++) {
                                if (i > 0) textBuilder.newLine();
                                textBuilder.append("- ");
                                getResponder().line(textBuilder, getAccessor().get(i));
                            }
                        });

                        embedBuilder.footer("showing " + elements + ", page " + getPage() + " of " + totalPages);
                    });
                }
        );
    }
}