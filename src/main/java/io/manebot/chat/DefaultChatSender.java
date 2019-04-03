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

        if (getChat().canSendEmbeds()) {
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

        if (getChat().canSendEmbeds()) {
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
            if (buffer != null) return false;
            else {
                buffer = chat.text();
                return true;
            }
        }
    }

    /**
     * Adds a message to the command buffer or sends a message.
     * @param message text to add.
     */
    public Collection<ChatMessage> sendMessage(String message) {
        String[] split = message.replace("\r", "").split("\n");
        Collection<ChatMessage> chatMessages = new LinkedList<>();
        for (String single : split) {
            ChatMessage chatMessage = sendFormattedMessage(textBuilder -> textBuilder.append(single));
            if (chatMessage != null) chatMessages.add(chatMessage);
        }
        return chatMessages;
    }

    @Override
    public ChatMessage sendFormattedMessage(Consumer<TextBuilder> function) {
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
                return null;
            } else {
                return getChat().sendFormattedMessage(consumer);
            }
        }
    }

    /**
     * Ends the command buffer.
     * @return number of flushes accomplished.
     */
    public ChatMessage end() {
        synchronized (bufferLock) {
            if (buffer != null && buffer.hasContent()) {
                ChatMessage chatMessage = getChat().sendRawMessage(buffer.build());
                buffer = null;
                return chatMessage;
            }
        }

        return null;
    }

    /**
     * Sends several messages to the remote.
     * @param messages Messages to get.
     * @return last ChatMessage genearted.
     */
    public Collection<ChatMessage> sendMessage(String... messages) {
        Collection<ChatMessage> chatMessages = new ArrayList<>(messages.length);
        for (String s : messages)
            chatMessages.addAll(sendMessage(s));
        return chatMessages;
    }

    /**
     * Flushes the buffer.
     */
    public ChatMessage flush() {
        synchronized (bufferLock) {
            if (buffer != null) {
                ChatMessage chatMessage = end();
                begin();
                return chatMessage;
            } else {
                return null;
            }
        }
    }
}
