package io.manebot.user;

import io.manebot.platform.Platform;

import java.util.Collection;

public interface UserManager {

    /**
     * Creates a new user.
     * @param username username of the new user.
     * @param type user type to create
     * @return User instance representing the new user.
     */
    User createUser(String username, UserType type);

    /**
     * Gets a user by their username.
     * @param username username to look for
     * @return User instance if a user is found, null otherwise.
     */
    User getUserByName(String username);

    /**
     * Gets a user by their display name.
     * @param displayName displayName to look for
     * @return User instance if a user is found, null otherwise.
     */
    User getUserByDisplayName(String displayName);

    /**
     * Gets a collection of all user bans.
     * @return user ban collection.
     */
    Collection<UserBan> getBans();

    /**
     * Gets a collection of all current bans.
     * @return current bans.
     */
    Collection<UserBan> getCurrentBans();

    /**
     * Gets all users on the system.
     * @return Collection representing all users on the system.
     */
    Collection<User> getUsers();

    /**
     * Gets all users defined as a specific type.
     * @param type user type to search for.
     * @return Collection representing all users of a specific type on the system.
     */
    Collection<User> getUsersByType(UserType type);

    /**
     * Gets a user group by name.
     * @param name name to look for.
     * @return Group instance if a group is found, null otherwise.
     */
    UserGroup getUserGroupByName(String name);

    /**
     * Gets all user groups on the system.
     * @return Collection representing all user groups on the system.
     */
    Collection<UserGroup> getUserGroups();

    /**
     * Creates a new user group.
     * @param name name to create the new group with.
     * @return UserGroup instance representing the new group.
     */
    UserGroup createUserGroup(String name);

    /**
     * Deletes a user group.
     * @param name name of the user group to delete.
     */
    void deleteUserGroup(String name);

    /**
     * Gets a user association.
     * @param platform Platform holding the association.
     * @param id Platform-specific ID for the association.
     * @return UserAssociation instance if one exists, null otherwise.
     */
    UserAssociation getUserAssociation(Platform platform, String id);

}
