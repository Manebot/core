package com.github.manevolent.jbot.chat;

import com.github.manevolent.jbot.chat.exception.ChatException;

public interface ChatDispatcher {

    void execute(ChatMessage chatMessage) throws ChatException;

}
