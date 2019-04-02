package io.manebot.entity;

import io.manebot.security.Grant;
import io.manebot.security.GrantedPermission;
import io.manebot.security.Permission;

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
        return hasPermission(Permission.get(node), Grant.DENY);
    }


    /**
     * Finds if this entity has a specific permission
     * @param permission Permission to check for.
     * @return true if the permission is granted, false otherwise.
     */
    default boolean hasPermission(Permission permission) {
        return hasPermission(permission, Grant.DENY);
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

        return grant == Grant.ALLOW;
    }

    default void checkPermission(Permission permission) throws SecurityException {
        checkPermission(permission, Grant.DENY);
    }

    default void checkPermission(String node) throws SecurityException {
        checkPermission(Permission.get(node), Grant.DENY);
    }

    default void checkPermission(Permission permission, Grant fallback) throws SecurityException {
        if (!hasPermission(permission, fallback)) throw new SecurityException("Access denied (" +permission.getNode() +")");
    }

    default void checkPermission(String node, Grant fallback) throws SecurityException {
        if (!hasPermission(node, fallback)) throw new SecurityException("Access denied (" +node +")");
    }

}
