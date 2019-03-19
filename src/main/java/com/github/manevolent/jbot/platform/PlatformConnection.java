package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.plugin.Plugin;
import com.github.manevolent.jbot.user.User;
import com.github.manevolent.jbot.user.UserAssociation;

import java.util.Collection;
import java.util.stream.Collectors;

public interface PlatformConnection {

    /**
     * Gets the plugin associated with the ownership of this platform.
     * @return Plugin instance.
     */
    Plugin getPlugin();

    /**
     * Gets a list of user associations for this platform.
     *
     * @return collection of user associations.
     */
    Collection<UserAssociation> getUserAssociations();

    /**
     * Gets a specific assocation for this platform.
     *
     * @param id Platform-specific ID to search for.
     * @return user association if found on this platform, null otherwise.
     */
    default UserAssociation getUserAssocation(String id) {
        return getUserAssociations().stream().filter(x -> x.getPlatformId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Gets a set of user associations for the specified user.
     *
     * @param user User to search for.
     * @return collection of user associations for the specified uesr.
     */
    default Collection<UserAssociation> getUserAssociations(User user) {
        return getUserAssociations().stream().filter(x -> x.getUser().equals(user)).collect(Collectors.toList());
    }

}
