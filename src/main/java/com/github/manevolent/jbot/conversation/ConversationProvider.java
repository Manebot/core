package com.github.manevolent.jbot.conversation;

import com.github.manevolent.jbot.chat.Chat;

import java.util.Collection;

public interface ConversationProvider {

    /**
     * Gets a conversation by the specified Id.
     * @param id Id to search for.
     * @return Conversation associated with the specified Id.
     */
    Conversation getConversationById(String id);

    /**
     * Gets the conversation associated with a specific chat.
     * @param chat Chat instance to search for.
     * @return Conversation instance for the specified chat.
     */
    Conversation getConversationByChat(Chat chat);

    /**
     * Gets all known conversations.
     * @return immutable collection of Conversation instances.
     */
    Collection<Conversation> getConversations();

}
