package com.github.manevolent.jbot.user;

import java.util.Collection;

public interface UserManager {

    /**
     * Creates a new user.
     * @param username username of the new user.
     * @return User instance representing the new user.
     */
    User createUser(String username);

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
     * Gets all users on the system.
     * @return Collection representing all users on the system.
     */
    Collection<User> getUsers();

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

}
