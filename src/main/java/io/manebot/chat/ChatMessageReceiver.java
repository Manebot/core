package io.manebot.chat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface ChatMessageReceiver {

    /**
     * Sends a message to the receiver.
     * @param function function to use to build the chat message.
     */
    Collection<ChatMessage> sendMessage(Consumer<ChatMessage.Builder> function);

    /**
     * Sends a raw text message to the receiver.
     * @param message message to send.
     * @return ChatMessage instance.
     */
    default Collection<ChatMessage> sendRawMessage(String message) {
        return sendFormattedMessage(format -> format.appendRaw(message));
    }

    /**
     * Sends an unformatted message to the receiver.
     * @param message message to add.
     */
    default Collection<ChatMessage> sendMessage(String message) {
        return sendFormattedMessage(format -> format.append(message));
    }

    /**
     * Sends a formatted message to the receiver.
     * @param function function to provide a formatted message.
     */
    default Collection<ChatMessage> sendFormattedMessage(Consumer<TextBuilder> function) {
        return sendMessage(builder -> builder.message(function));
    }

    /**
     * Sends several raw messages to the receiver.
     * @param messages messages to send.
     */
    default Collection<ChatMessage> sendRawMessages(String... messages) {
        return Arrays.stream(messages)
                .flatMap(message -> sendRawMessage(message).stream()
                        .filter(Objects::nonNull)
                ).collect(Collectors.toList());
    }

    /**
     * Sends several messages to the receiver.
     * @param messages messages to send.
     */
    default Collection<ChatMessage> sendMessages(String... messages) {
        return Arrays.stream(messages)
                .flatMap(message -> sendMessage(message).stream()
                        .filter(Objects::nonNull)
                ).collect(Collectors.toList());
    }

    /**
     * Sends several messages to the receiver.
     * @param functions messages to send.
     */
    default Collection<ChatMessage> sendFormattedMessages(Consumer<TextBuilder>... functions) {
        return Arrays.stream(functions)
                .flatMap(function -> sendFormattedMessage(function).stream().filter(Objects::nonNull)
                ).collect(Collectors.toList());
    }

    /**
     * Sends several messages to the receiver.
     * @param builders messages to send.
     */
    default Collection<ChatMessage> sendMessages(Consumer<ChatMessage.Builder>... builders) {
        return Arrays.stream(builders)
                .flatMap(builder -> sendMessage(builder).stream().filter(Objects::nonNull)
                ).collect(Collectors.toList());
    }

    /**
     * Finds if this receiver can send receive embedded messages in this conversation.
     * @return true if the bot can send embedded messages to this receiver, false otherwise.
     */
    boolean canSendEmbeds();

}
