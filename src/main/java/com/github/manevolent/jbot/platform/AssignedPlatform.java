package com.github.manevolent.jbot.platform;

public interface AssignedPlatform extends Platform {

    /**
     * Sets the connection instance of this platform.
     * @param connection PlatformConnection instance.
     */
    void setConnection(PlatformConnection connection);

}
