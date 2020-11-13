package io.manebot.chat;

import io.manebot.command.exception.CommandExecutionException;
import io.manebot.database.search.SearchResult;
import io.manebot.command.response.CommandDetailsResponse;
import io.manebot.command.response.CommandListResponse;
import io.manebot.platform.PlatformUser;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ChatSender extends ChatMessageReceiver {

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
     * Gets the community the message was sent in.
     * @return Community instance.
     */
    default Community getCommunity() {
        return getChat().getCommunity();
    }

    /**
     * Opens the command buffer.
     * @return true if the buffer was opened, false if no changes were made.
     */
    boolean begin();

    /**
     * Ends the command buffer.
     * @return ChatMessage generated.
     */
    Collection<ChatMessage> end();

    /**
     * Flushes the buffer.
     * @return ChatMessage generated.
     */
    Collection<ChatMessage> flush();

    @Override
    default Collection<ChatMessage> sendMessage(Consumer<ChatMessage.Builder> function) {
        return getChat().sendMessage(function);
    }

    /**
     * Creates a list response to get to the command sender, opportunistically formatting as rich content.
     * @param function Function providing a command response object from a builder.
     * @param <T> List item type.
     * @return CommandResponse object corresponding to the desired message; contains <b>get()</b> method to dispatch.
     */
    <T> Collection<ChatMessage> sendList(
            Class<T> type,
            Consumer<CommandListResponse.Builder<T>> function) throws CommandExecutionException;

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
     * @return  CommandResponse object corresponding to the desired message; contains <b>get()</b> method to dispatch.
     */
    default <T> Collection<ChatMessage> sendList(
            Class<T> type,
            SearchResult<T> result,
            CommandListResponse.ListElementFormatter<T> formatter) throws CommandExecutionException {
        return sendList(
                type,
                builder -> builder
                        .virtual(result.getResults())
                        .responder(formatter)
                        .elementsPerPage(result.getPageSize())
                        .totalElements(result.getTotalResults())
                        .page(result.getPage())
        );
    }

    /**
     * Creates a details response to get to the command sender, opportunistically formatting as rich content.
     *
     * Details are formatted as such:
     *
     *      ObjectName "ObjectKey" details:
     *       Key: value
     *       Key: [value1,value2,value3]
     *
     * @param function Function providing a command response object from a builder.
     * @return CommandResponse object corresponding to the desired message; contains <b>get()</b> method to dispatch.
     */
    Collection<ChatMessage> sendDetails(Consumer<CommandDetailsResponse.Builder> function)
        throws CommandExecutionException;

}
