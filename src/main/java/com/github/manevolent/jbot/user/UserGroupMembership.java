package com.github.manevolent.jbot.user;

import java.util.Date;

public interface UserGroupMembership {

    /**
     * Gets the user group in this membership.
     * @return UserGroup instance.
     */
    UserGroup getGroup();

    /**
     * Gets the user in this relationship.
     * @return User instance.
     */
    User getUser();

    /**
     * Gets the user that added this membership.
     * @return User instance.
     */
    User getAddingUser();

    /**
     * Gets the date the user was added by the adding user.
     * @return Date instance.
     */
    Date getAddedDate();

    /**
     * Removes this membership, removing the user from the group in the membership.
     * @throws SecurityException if there was a security violation removing the user from the group.
     */
    void remove() throws SecurityException;

}
