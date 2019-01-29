package com.github.manevolent.jbot.user;

import com.github.manevolent.jbot.conversation.Conversation;
import com.github.manevolent.jbot.entity.Entity;

import java.util.Date;

/**
 * Users define individual jBot users.
 */
public interface User extends Entity {

    /**
     * Gets this user's username on the system.
     *
     * @return Username.
     */
    String getUsername();

    /**
     * Gets this user's username on the system.  Equivalent to <b>getUsername()</b>.
     *
     * @return Username.
     */
    default String getName() {
        return getUsername();
    }

    /**
     * Gets this user's desired display name.
     *
     * @return Display name.
     */
    String getDisplayName();

    /**
     * Gets the date this user was registered.
     *
     * @return Register date.
     */
    Date getRegisteredDate();

    /**
     * Gets the date this user was last seen.
     *
     * @return Last seen date.
     */
    Date getLastSeenDate();

    /**
     * Gets the bot's private conversation with this user.
     *
     * @return Private conversation, null if none exists.
     */
    Conversation getPrivateConversation();

}
