package io.manebot.entity;

import io.manebot.property.Property;
import io.manebot.security.Grant;
import io.manebot.security.GrantedPermission;
import io.manebot.security.Permission;

import java.util.Collection;

public interface Entity {

    /**
     * Gets this entity's name.
     * @return entity name.
     */
    String getName();

    /**
     * Gets a collection of this entity's properties.
     * @return entity properties.
     */
    Collection<Property> getProperties();

    /**
     * Gets a specific property by its identitifer,
     * @param id entity property identitfier.
     * @return Property instance if a property exists by that ID, and a null-valued Property otherwise.
     */
    Property getPropery(String id);

    /**
     * Gets a specific pre-existing grant for this entity.
     * @param node permission node to check for.
     * @return ALLOW or DENY, null if no permission grant is explicitly defined.
     */
    GrantedPermission getPermission(String node);

    /**
     * Gets a specific pre-existing grant for this entity.
     * @param permission Permission to check for.
     * @return ALLOW or DENY, null if no permission grant is explicitly defined.
     */
    default GrantedPermission getPermission(Permission permission) {
        return getPermission(permission.getNode());
    }

    /**
     * Grants a specific permission to this entity.
     * @param node permission node to grant.
     * @param type type of grant to supply.
     */
    GrantedPermission setPermission(String node, Grant type) throws SecurityException;

    /**
     * Removes a specific permission from this entity.
     * @param node permission node to grant.
     */
    void removePermission(String node);

    /**
     * Gets the explicitly granted permissions for this entity.
     * @return permission grants.
     */
    Collection<GrantedPermission> getPermissions();

}
