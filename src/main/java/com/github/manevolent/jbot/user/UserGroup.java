package com.github.manevolent.jbot.user;

import com.github.manevolent.jbot.entity.EntityType;

import java.util.Collection;
import java.util.stream.Collectors;

public interface UserGroup extends EntityType {

    /**
     * Gets the name of this group.
     *
     * @return group name.
     */
    String getName();

    /**
     * Gets this group's owner.
     *
     * @return owner.
     */
    User getOwner();

    /**
     * Gets the users in this group.
     *
     * @return users in the group.
     */
    default Collection<User> getUsers() {
        return getMembership().stream()
                .map(UserGroupMembership::getUser)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Gets a collection of individual memberships, which is more definite than getUsers(), providing more specific
     * information about membership.
     *
     * @return immutable collection of user group membership records.
     */
    Collection<UserGroupMembership> getMembership();

    /**
     * Sets the owner of this group.
     *
     * @param user user to set as the new owner of this group.
     * @throws SecurityException if there is a security violation adding the user.
     */
    void setOwner(User user) throws SecurityException;

    /**
     * Finds if the specified user is a member of this group.
     *
     * @param user User to check in the group.
     * @return true if the user is a member of the group, false otherwise.
     */
    default boolean isMember(User user) {
        return getMembership(user) != null;
    }

    /**
     * Gets the membership record for a specific user.
     *
     * @param user User to search membership records for.
     * @return membership instance.
     */
    UserGroupMembership getMembership(User user);

    /**
     * Adds a user to this group.
     * @param user user to add.
     * @throws SecurityException if there is a security violation adding the user.
     */
    void addUser(User user) throws SecurityException;

    /**
     * Removes a user from this group.
     *
     * @param user user to remove.
     * @throws SecurityException if there is a security violation removing the user.
     */
    default void removeUser(User user) throws SecurityException {
        UserGroupMembership membership = getMembership(user);
        if (membership == null) throw new IllegalArgumentException(user.getUsername()
                + " is not a member of " + getName() + ".");

        membership.remove();
    }

}
