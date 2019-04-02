package io.manebot.security;

import io.manebot.entity.Entity;
import io.manebot.user.User;

import java.util.Date;

public interface GrantedPermission {

    /**
     * Gets the entity that this granted permission is associated with.
     * @return Entity instance.
     */
    Entity getEntity();

    /**
     * Gets the permission node that this granted permission refers to.
     * @return
     */
    Permission getPermission();

    /**
     * Gets the grant that is explicitly defined by this permission.
     * @return Grant instance.
     */
    Grant getGrant();

    /**
     * Gets the user that granted this permission.
     * @return User instance.
     */
    User getGranter();

    /**
     * Gets the date this permission was granted.
     * @return Date instance.
     */
    Date getDate();

    /**
     * Removes this granted permission
     * @throws SecurityException ifa security violation is detected attempting to remove this permission.
     */
    void remove() throws SecurityException;

}
