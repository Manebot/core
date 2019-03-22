package com.github.manevolent.jbot.user;

import com.github.manevolent.jbot.platform.Platform;
import com.github.manevolent.jbot.platform.PlatformUser;

public interface UserAssociation {

    /**
     * Gets the platform connected with this association.
     * @return Platform instance.
     */
    Platform getPlatform();

    /**
     * Gets the user in this platform connection.
     * @return User instance.
     */
    User getUser();

    /**
     * Gets the ID associated with this connection.
     * @return ID.
     */
    String getPlatformId();

    /**
     * Gets the platform user instance associated with this user association.
     * @return PlatformUser instance.
     */
    default PlatformUser getPlatformUser() {
        return getPlatform().getConnection().getPlatformUser(getPlatformId());
    }

    /**
     * Removes this User association fromm the Platform.
     */
    void remove();

}
