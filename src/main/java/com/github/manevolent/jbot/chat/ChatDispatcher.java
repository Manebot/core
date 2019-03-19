package com.github.manevolent.jbot.chat;

import com.github.manevolent.jbot.chat.exception.ChatException;

import java.util.concurrent.Future;

public interface ChatDispatcher {

    /**
     * Dispatches a chat message
     * @param chatMessage ChatMessage instance to dispatch.
     * @throws ChatException if there was an exception executing the chat.
     */
    void execute(ChatMessage chatMessage) throws ChatException;

    /**
     * Dispatches a chat message asynchronously.
     * @param chatMessage ChatMessage instance to dispatch.
     * @return Future representing the chat future execution.
     */
    Future<?> executeAsync(ChatMessage chatMessage);

}
