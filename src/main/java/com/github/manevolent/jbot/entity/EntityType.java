package com.github.manevolent.jbot.entity;

import com.github.manevolent.jbot.command.exception.CommandAccessException;
import com.github.manevolent.jbot.security.Grant;
import com.github.manevolent.jbot.security.Permission;

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
        Grant grant = getEntity().getPermission(node).getGrant();

        // grant != null may be superfluous
        return grant != null && grant == Grant.ALLOW;
    }


    /**
     * Finds if this entity has a specific permission
     * @param permission Permission to check for.
     * @return true if the permission is granted, false otherwise.
     */
    default boolean hasPermission(Permission permission) {
        Grant grant = getEntity().getPermission(permission).getGrant();

        // grant != null may be superfluous
        return grant != null && grant == Grant.ALLOW;
    }

    default void checkPermission(Permission permission) throws CommandAccessException {
        if (!hasPermission(permission)) throw new CommandAccessException(permission.getNode());
    }

    default void checkPermission(String node) throws CommandAccessException {
        if (!hasPermission(node)) throw new CommandAccessException(node);
    }

}
