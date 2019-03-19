package com.github.manevolent.jbot.user;

import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.DefaultCommandSender;
import com.github.manevolent.jbot.conversation.Conversation;
import com.github.manevolent.jbot.entity.EntityType;
import com.github.manevolent.jbot.platform.Platform;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

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
     * Sets this user's desired display name.
     *
     * @param displayName Display name.
     */
    void setDisplayName(String displayName);

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
     * Get a list of connections that associate this user with platforms.
     * @return user associations.
     */
    Collection<UserAssociation> getAssociations();

    /**
     * Gets a user association by platform and ID.
     * @param platform Platform to search for
     * @param id ID to search for.
     * @return UserAssociation instance if found, null otherwise.
     */
    default UserAssociation getUserAssociation(Platform platform, String id) {
        return getAssociations().stream()
                .filter(association -> association.getPlatform().equals(platform) && association.getPlatformId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the list of connected IDs for this user.
     * @param platform Platform association to search on.
     * @return IDs associated with the given platform for this user.
     */
    default Collection<UserAssociation> getAssociations(Platform platform) {
        return getAssociations().stream()
                .filter(association -> association.getPlatform().equals(platform))
                .collect(Collectors.toList());
    }

    /**
     * Creates a user connection for this user.
     * @param platform Platform to create the association on.
     * @param id Platform-specific ID to associate the connection to.
     * @return UserAssociation instance.
     */
    UserAssociation createAssociation(Platform platform, String id);

    /**
     * Removes a user association from this user.
     * @param association Association to remove.
     * @return true if the association was removed, false otherwise.
     */
    default boolean removeAssociation(UserAssociation association) {
        if (association.getUser() != this) throw new IllegalArgumentException("user mismatch");
        return removeAssociation(association.getPlatform(), association.getPlatformId());
    }

    /**
     * Removes a user association from this user.
     * @param platform Platform to remove an association for.
     * @param id Platform-specific id to remove an association for.
     * @return true if the association was removed, false otherwise.
     */
    boolean removeAssociation(Platform platform, String id);

    /**
     * Gets the bot's private conversation with this user.
     *
     * @return Private conversation, null if none exists.
     */
    Conversation getPrivateConversation();

    /**
     * Sets the bot's private conversation with this user.
     *
     * @param conversation private conversation, or null to unset.
     */
    void setPrivateConversation(Conversation conversation);

    /**
     * Creates a command sender for the specified user.
     *
     * @param conversation Conversation to create a command sender for.
     * @return CommandSender instance.
     * @throws SecurityException
     */
    default CommandSender createSender(Conversation conversation) throws SecurityException {
        return new DefaultCommandSender(conversation, this);
    }

    /**
     * Gets the user's system type.
     * @return UserType instance.
     */
    UserType getType();

    /**
     * Changes the user's system type.
     * @param type UserType instance to change to.
     * @return true if the type was changed.
     */
    boolean setType(UserType type);

}
