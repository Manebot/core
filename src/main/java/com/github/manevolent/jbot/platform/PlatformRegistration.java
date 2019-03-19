package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.plugin.Plugin;

public interface PlatformRegistration {

    /**
     * Gets the name of this registration.
     * @return name.
     */
    String getName();

    /**
     * Gets the platform connection associated with this registration.
     * @return Connection.
     */
    PlatformConnection getConnection();

    /**
     * Gets the platform associated with this runtime registration.
     * @return Platform instance.
     */
    Platform getPlatform();

    /**
     * Gets the plugin associated with the ownership of this platform.  May be null.
     * @return Plugin instance.
     */
    Plugin getPlugin();

}
