package com.github.manevolent.jbot.user;

import com.github.manevolent.jbot.chat.Chat;
import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.entity.Entity;
import com.github.manevolent.jbot.entity.EntityType;

import java.util.Collection;
import java.util.Date;

/**
 * Users define individual jBot users.
 */
public interface User extends EntityType {

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
     * Finds the groups this user is a member of.
     * @return group collection.
     */
    Collection<UserGroup> getGroups();

    /**
     * Gets the bot's private conversation with this user.
     *
     * @return Private conversation, null if none exists.
     */
    Chat getPrivateChat();

    /**
     * Creates a command sender for the specified user.
     *
     * @param chat Chat to create a command sender for.
     * @return CommandSender instance.
     * @throws SecurityException
     */
    CommandSender createSender(Chat chat) throws SecurityException;

}
