package com.github.manevolent.jbot.conversation;

import com.github.manevolent.jbot.chat.Chat;
import com.github.manevolent.jbot.entity.EntityType;

public interface Conversation extends EntityType {

    /**
     * Gets the Id of this conversation.
     * @return Conversation Id.
     */
    String getId();

    /**
     * Gets the chat instance associated with this Conversation.
     * @return Chat instance.
     */
    Chat getChat();

}
