package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.plugin.Plugin;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface PlatformManager {

    /**
     * Registers a platform in the system.  Registration may only occur if a system platform has not yet been assigned.
     *
     * @param function Platform building function connection to call for registration.
     * @return Platform instance.
     */
    Platform registerPlatform(Function<Builder, Platform> function) throws IllegalStateException;

    /**
     * Unregisters a platform from the system.
     * @param platform Platform instance to unregister.
     */
    void unregisterPlatform(Platform platform);

    /**
     * Gets a list of system platforms in the system.
     * @return Immutable list of system platforms.
     */
    Collection<Platform> getPlatforms();

    /**
     * Gets a platform by its ID.
     * @param id Platform ID.
     * @return Platform instance if found, null otherwise.
     */
    default Platform getPlatformById(String id) {
        return getPlatforms().stream().filter(platform -> platform.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Gets a platform by its name.
     * @param name Platform name.
     * @return Platform instance if found, null otherwise.
     */
    default Platform getPlatformByName(String name) {
        return getPlatforms().stream().filter(platform -> platform.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Gets all platforms assigned to a specific Plugin instance.
     * @param plugin Plugin instance to search for.
     * @return Platform instances assigned ot the specified Plugin instance.
     */
    default Collection<Platform> getPlatformsByPlugin(Plugin plugin) {
        return getPlatforms().stream().filter(platform ->
                platform.getConnection() != null &&
                platform.getConnection().getPlugin().equals(plugin)
        ).collect(Collectors.toList());
    }

    abstract class Builder {
        private String name, id;
        private PlatformConnection connection;

        public String getId() {
            return id;
        }

        public Builder id(String id) {
            this.id = id;

            return this;
        }

        public String getName() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;

            return this;
        }

        public PlatformConnection getConnection() {
            return connection;
        }

        public Builder withConnection(PlatformConnection connection) {
            this.connection = connection;

            return this;
        }

        /**
         * Registers this platform, returning an assigned platform instance capable of managing the platform assignment.
         * @param plugin Plugin to assign this platform to.
         * @return AssignedPlatform instance.
         */
        abstract AssignedPlatform register(Plugin plugin);
    }

}
