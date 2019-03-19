package com.github.manevolent.jbot.platform;

import com.github.manevolent.jbot.plugin.Plugin;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface PlatformManager {

    /**
     * Registers a platform in the system, and automatically connects to it.
     * Registration may only occur if a system platform has not yet been assigned.
     *
     * @param function Platform building function connection to call for registration.
     * @return Platform instance.
     */
    default PlatformRegistration registerPlatform(Function<Builder, PlatformRegistration> function)
            throws IllegalStateException {
        PlatformRegistration registration = function.apply(buildPlatform());

        PlatformConnection connection = registration.getConnection();
        if (connection != null)
            connection.connect();

        return registration;
    }

    /**
     * Constructs a new builder for a Platform.
     *
     * @return Builder instance to construct a Platform.
     */
    Builder buildPlatform();

    /**
     * Unregisters a platform registration from the system.
     * @param registration Platform registration instance to unregister.
     */
    void unregisterPlatform(PlatformRegistration registration);

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
        return getPlatforms().stream().filter(platform -> {
            PlatformRegistration registration = platform.getRegistration();
            if (registration == null) return false;
            return registration.getName().equals(name);
        }).findFirst().orElse(null);
    }

    /**
     * Gets all platforms assigned to a specific Plugin instance.
     * @param plugin Plugin instance to search for.
     * @return Platform instances assigned ot the specified Plugin instance.
     */
    default Collection<Platform> getPlatformsByPlugin(Plugin plugin) {
        return getPlatforms().stream().filter(platform ->
                platform.getPlugin() != null
        ).collect(Collectors.toList());
    }

    abstract class Builder {
        private String id, name;
        private PlatformConnection connection;

        public String getId() {
            return id;
        }

        public Builder id(String id) {
            this.id = id;

            if (this.name == null) this.name = id;

            return this;
        }

        public Platform platform(String id) {
            id(id);
            return getPlatform();
        }

        public String getName() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;

            return this;
        }

        /**
         * Gets the plugin associated with the Builder's <b>id</b>, as previous set.
         * @return
         */
        public abstract Platform getPlatform();

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
        public abstract PlatformRegistration register(Plugin plugin);
    }

}
