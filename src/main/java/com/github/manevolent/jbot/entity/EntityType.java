package com.github.manevolent.jbot.entity;

import com.github.manevolent.jbot.command.exception.CommandAccessException;
import com.github.manevolent.jbot.security.Grant;
import com.github.manevolent.jbot.security.GrantedPermission;
import com.github.manevolent.jbot.security.Permission;

import static com.github.manevolent.jbot.security.Grant.ALLOW;
import static com.github.manevolent.jbot.security.Grant.DENY;

public interface EntityType {

    /**
     * Gets the entity associated with this object.
     * @return Associated entity.
     */
    Entity getEntity();


    /**
     * Finds if this entity has a specific permission node
     * @param node permission node to check for.
     * @return true if the permission node is granted, false otherwise.
     */
    default boolean hasPermission(String node) {
        return hasPermission(Permission.get(node), DENY);
    }


    /**
     * Finds if this entity has a specific permission
     * @param permission Permission to check for.
     * @return true if the permission is granted, false otherwise.
     */
    default boolean hasPermission(Permission permission) {
        return hasPermission(permission, DENY);
    }

    /**
     * Finds if this entity has a specific permission
     * @param node Permission node to check for.
     * @return true if the permission is granted, false otherwise.
     */
    default boolean hasPermission(String node, Grant fallback) {
        return hasPermission(Permission.get(node), fallback);
    }

    /**
     * Finds if this entity has a specific permission
     * @param permission Permission to check for.
     * @return true if the permission is granted, false otherwise.
     */
    default boolean hasPermission(Permission permission, Grant fallback) {
        GrantedPermission grantedPermission = getEntity().getPermission(permission);
        if (grantedPermission == null) return false;

        Grant grant = grantedPermission.getGrant();

        // grant != null may be superfluous
        if (grant == null)
            grant = fallback;

        return grant == ALLOW;
    }

    default void checkPermission(Permission permission) throws CommandAccessException {
        checkPermission(permission, DENY);
    }

    default void checkPermission(String node) throws CommandAccessException {
        checkPermission(Permission.get(node), DENY);
    }

    default void checkPermission(Permission permission, Grant fallback) throws CommandAccessException {
        if (!hasPermission(permission, fallback)) throw new CommandAccessException(permission.getNode());
    }

    default void checkPermission(String node, Grant fallback) throws CommandAccessException {
        if (!hasPermission(node, fallback)) throw new CommandAccessException(node);
    }

}
