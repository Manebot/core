package com.github.manevolent.jbot.user;

import com.github.manevolent.jbot.platform.Platform;

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
     * Removes this User association fromm the Platform.
     */
    void remove();

}
