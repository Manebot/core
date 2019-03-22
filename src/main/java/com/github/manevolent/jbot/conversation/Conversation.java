package com.github.manevolent.jbot.conversation;

import com.github.manevolent.jbot.chat.Chat;
import com.github.manevolent.jbot.entity.EntityType;
import com.github.manevolent.jbot.platform.Platform;

public interface Conversation extends EntityType {

    /**
     * Gets the platform associated with this conversation.
     * @return Platform instance.
     */
    Platform getPlatform();

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

    /**
     * Finds if this conversation is connected to a chat object.
     *
     * If this is true, getChat() should not be null.
     *
     * @return true if getChat() is not null, and is reporting connectivity such that getChat().isConnected() == true.
     */
    default boolean isConnected() {
        Chat chat = getChat();
        return chat != null && chat.isConnected();
    }

}
