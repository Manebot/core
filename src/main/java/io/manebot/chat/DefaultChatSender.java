package io.manebot.chat;

import io.manebot.command.response.*;
import io.manebot.platform.PlatformUser;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class DefaultChatSender implements ChatSender {
    private final Object bufferLock = new Object();
    private TextBuilder buffer;

    private final PlatformUser user;
    private final Chat chat;

    public DefaultChatSender(PlatformUser user, Chat chat) {
        this.user = user;
        this.chat = chat;
    }

    @Override
    public PlatformUser getPlatformUser() {
        return user;
    }

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public <T> CommandListResponse<T> list(
            Class<T> type,
            Function<CommandListResponse.Builder<T>, CommandListResponse<T>> function
    ) {
        CommandListResponse.Builder<T> builder;

        if (canSendEmbeds()) {
            builder = new CommandListResponse.Builder<T>() {
                @Override
                public CommandListResponse<T> build() {
                    return new DefaultRichCommandListResponse<>(
                            DefaultChatSender.this,
                            getTotalElements(),
                            getPage(),
                            getElementsPerPage(),
                            createListAccessor(),
                            getResponder()
                    );
                }
            };
        } else {
            builder = new CommandListResponse.Builder<T>() {
                @Override
                public CommandListResponse<T> build() {
                    return new DefaultBasicCommandListResponse<>(
                            DefaultChatSender.this,
                            getTotalElements(),
                            getPage(),
                            getElementsPerPage(),
                            createListAccessor(),
                            getResponder()
                    );
                }
            };
        }

        return function.apply(builder);
    }

    @Override
    public CommandDetailsResponse details(Function<CommandDetailsResponse.Builder, CommandDetailsResponse> function) {
        CommandDetailsResponse.Builder builder;

        if (canSendEmbeds()) {
            builder = new CommandDetailsResponse.Builder() {
                @Override
                public CommandDetailsResponse build() {
                    return new DefaultRichCommandDetailsResponse(
                            DefaultChatSender.this,
                            getName(),
                            getKey(),
                            getItems()
                    );
                }
            };
        } else {
            builder = new CommandDetailsResponse.Builder() {
                @Override
                public CommandDetailsResponse build() {
                    return new DefaultBasicCommandDetailsResponse(
                            DefaultChatSender.this,
                            getName(),
                            getKey(),
                            getItems()
                    );
                }
            };
        }

        return function.apply(builder);
    }

    /**
     * Opens the command buffer.
     * @return true if the buffer was opened, false if no changes were made.
     */
    public boolean begin() {
        synchronized (bufferLock) {
            if (buffer != null)
                return false;
            else {
                buffer = getChat().text();
                return true;
            }
        }
    }

    @Override
    public Collection<ChatMessage> sendMessage(String message) {
        String[] split = message.replace("\r", "").split("\n");
        Collection<ChatMessage> chatMessages = new LinkedList<>();
        for (String single : split)
            chatMessages.addAll(sendFormattedMessage(textBuilder -> textBuilder.append(single)));
        return chatMessages;
    }

    @Override
    public Collection<ChatMessage> sendFormattedMessage(Consumer<TextBuilder> function) {
        Consumer<TextBuilder> consumer = textBuilder -> {
            textBuilder =
                    (textBuilder.getFormat().shouldMention(user) ?
                        textBuilder.appendMention(user) :
                        textBuilder.append(getDisplayName().trim()))
                        .append(" -> ");

            function.accept(textBuilder);
        };

        synchronized (bufferLock) {
            if (buffer != null) {
                if (buffer.hasContent()) buffer.newLine();
                consumer.accept(buffer);
                return Collections.emptyList();
            } else {
                return getChat().sendFormattedMessage(consumer);
            }
        }
    }

    @Override
    public Collection<ChatMessage> sendMessages(String... messages) {
        Collection<ChatMessage> chatMessages = new ArrayList<>(messages.length);
        for (String s : messages)
            chatMessages.addAll(sendMessage(s));
        return chatMessages;
    }

    @Override
    public boolean canSendEmbeds() {
        return getChat().canSendEmbeds();
    }

    /**
     * Ends the command buffer.
     * @return number of flushes accomplished.
     */
    public Collection<ChatMessage> end() {
        synchronized (bufferLock) {
            if (buffer != null && buffer.hasContent()) {
                try {
                    return getChat().sendRawMessage(buffer.build());
                } finally {
                    buffer = null;
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Flushes the buffer.
     */
    public Collection<ChatMessage> flush() {
        synchronized (bufferLock) {
            if (buffer != null) {
                try {
                    return end();
                } finally {
                    begin();
                }
            } else {
                return Collections.emptyList();
            }
        }
    }
}
