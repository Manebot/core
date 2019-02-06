package com.github.manevolent.jbot.entity;

import com.github.manevolent.jbot.property.Property;
import com.github.manevolent.jbot.security.Grant;
import com.github.manevolent.jbot.security.GrantedPermission;
import com.github.manevolent.jbot.security.Permission;

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
    Grant getGrant(String node);

    /**
     * Gets a specific pre-existing grant for this entity.
     * @param permission Permission to check for.
     * @return ALLOW or DENY, null if no permission grant is explicitly defined.
     */
    default Grant getGrant(Permission permission) {
        return getGrant(permission.getNode());
    }

    /**
     * Finds if this entity has a specific permission node
     * @param node permission node to check for.
     * @return true if the permission node is granted, false otherwise.
     */
    default boolean hasPermission(String node) {
        Grant grant = getGrant(node);

        // grant != null may be superfluous
        return grant != null && grant == Grant.ALLOW;
    }


    /**
     * Finds if this entity has a specific permission
     * @param permission Permission to check for.
     * @return true if the permission is granted, false otherwise.
     */
    default boolean hasPermission(Permission permission) {
        Grant grant = getGrant(permission);

        // grant != null may be superfluous
        return grant != null && grant == Grant.ALLOW;
    }

    /**
     * Gets the explicitly granted permissions for this entity.
     * @return permission grants.
     */
    Collection<GrantedPermission> getPermissions();

}
