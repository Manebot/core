package com.github.manevolent.jbot.chat;

import com.github.manevolent.jbot.database.search.SearchResult;
import com.github.manevolent.jbot.command.response.CommandDetailsResponse;
import com.github.manevolent.jbot.command.response.CommandListResponse;
import com.github.manevolent.jbot.platform.PlatformUser;

import java.util.function.Function;

public interface ChatSender {

    /**
     * Gets the platform user that is associated with this chat sender.
     * @return PlatformUser instance.
     */
    PlatformUser getPlatformUser();

    /**
     * Gets the username of the command sender.
     */
    default String getUsername() {
        return getPlatformUser().getId();
    }

    /**
     * Gets the display name of the command sender.
     */
    default String getDisplayName() {
        return getPlatformUser().getNickname();
    }

    /**
     * Gets the chat the message was sent in.
     * @return Chat instance.
     */
    Chat getChat();

    /**
     * Opens the command buffer.
     * @return true if the buffer was opened, false if no changes were made.
     */
    boolean begin();

    /**
     * Adds a message to the command buffer or sends a message.
     * @param message message to add.
     */
    void sendMessage(String message);
    /**
     * Sends a text message to the sender.
     * @param message PlainText message to send.
     */
    default void sendMessage(ChatMessage message) {
        sendMessage(message.getMessage());
    }

    /**
     * Sends a rich message to the sender.
     * @param message Rich message to send.
     * @throws UnsupportedOperationException if the conversation does not support rich messaging.
     */
    default void sendMessage(RichChatMessage message) throws UnsupportedOperationException {
        getChat().sendMessage(message);
    }

    /**
     * Ends the command buffer.
     * @return number of lines sent.
     */
    int end();

    /**
     * Sends several messages to the remote.
     * @param messages Messages to send.
     */
    void sendMessage(String... messages);

    /**
     * Flushes the buffer.
     * @return number of lines sent.
     */
    int flush();


    /**
     * Creates a list response to send to the command sender, opportunistically formatting as rich content.
     * @param function Function providing a command response object from a builder.
     * @param <T> List item type.
     * @return CommandResponse object corresponding to the desired message; contains <b>send()</b> method to dispatch.
     */
    <T> CommandListResponse<T> list(Class<T> type,
                                    Function<CommandListResponse.Builder<T>, CommandListResponse<T>> function);

    /**
     * Constructs a list response on the given search object and associated chat sender.
     *
     * Has a default implementation for ease-of-use, via the <b>virtual</b> list accessor system, used for paged
     * results where more results exist virtually outside the application (such as, in the case of a search). This
     * is used to improve performance and lower overhead when searching over millions of rows, for example.
     *
     * @param type List item type.
     * @param result Search result object, obtained from a <B>SearchHandler</B> class.
     * @param <T> List item type.
     * @return  CommandResponse object corresponding to the desired message; contains <b>send()</b> method to dispatch.
     */
    default <T> CommandListResponse<T> list(Class<T> type,
                                            SearchResult<T> result,
                                            CommandListResponse.ListElementFormatter<T> formatter) {
        return list(
                type,
                builder -> builder
                        .virtual(result.getResults())
                        .responder(formatter)
                        .elementsPerPage(result.getPageSize())
                        .totalElements(result.getTotalResults())
                        .page(result.getPage())
                        .build()
        );
    }

    /**
     * Creates a details response to send to the command sender, opportunistically formatting as rich content.
     *
     * Details are formatted as such:
     *
     *      ObjectName "ObjectKey" details:
     *       Key: value
     *       Key: [value1,value2,value3]
     *
     * @param function Function providing a command response object from a builder.
     * @return CommandResponse object corresponding to the desired message; contains <b>send()</b> method to dispatch.
     */
    CommandDetailsResponse details(
            Function<CommandDetailsResponse.Builder, CommandDetailsResponse> function
    );

}
