package io.manebot.platform;

import io.manebot.plugin.Plugin;
import io.manebot.user.User;
import io.manebot.user.UserAssociation;
import io.manebot.user.UserGroup;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Platforms are the structure of the bot that represent individual chatting platforms, such as Discord, Skype,
 * and so on.
 */
public interface Platform {

    /**
     * Gets the runtime registration associated with this platform.
     * @return Platform runtime registration.
     */
    PlatformRegistration getRegistration();

    /**
     * Gets the default group assignment for users registered on this platform.
     * @return Platform default user group, or null if no default is assigned.
     */
    default UserGroup getDefaultGroup() {
        return null;
    }

    /**
     * Sets the default group assignment for users registered on this platform.
     * @param defaultGroup default user group for newly registered users.
     */
    void setDefaultGroup(UserGroup defaultGroup);

    /**
     * Gets the platform's internal unique ID.
     *
     * @return platform ID.
     */
    String getId();

    /**
     * Finds if registration is allowed.
     * @return true if registration is allowed, false otherwise.
     */
    boolean isRegistrationAllowed();

    /**
     * Sets if registration is allowed on this platform.
     * @param allowed true if registration is allowed, false otherwise.
     */
    void setRegistrationAllowed(boolean allowed);

    /**
     * Finds if the platform is currently connected.
     *
     * @return true if the platform is connected, false otherwise.
     */
    default boolean isConnected() {
        return getConnection() != null && getConnection().isConnected();
    }

    /**
     * Gets a specific assocation for this platform.
     *
     * @param id Platform-specific user ID to search for.
     * @return user association if found on this platform, null otherwise.
     */
    default UserAssociation getUserAssocation(String id) {
        return getUserAssociations().stream()
                .filter(assoc -> assoc.getPlatformId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Gets a specific assocation for this platform.
     *
     * @param user Platform-specific user to search for.
     * @return user association if found on this platform, null otherwise.
     */
    default UserAssociation getUserAssocation(PlatformUser user) {
        return getUserAssocation(user.getId());
    }

    /**
     * Gets a set of user associations for the specified user.
     *
     * @param user User to search for.
     * @return collection of user associations for the specified uesr.
     */
    default Collection<UserAssociation> getUserAssociations(User user) {
        return Collections.unmodifiableCollection(
                getUserAssociations().stream()
                        .filter(assoc -> assoc.getUser().equals(user)).collect(Collectors.toList())
        );
    }

    /**
     * Gets a list of user associations for this platform.
     *
     * @return collection of user associations.
     */
    Collection<UserAssociation> getUserAssociations();

    /**
     * Gets the current platform connection instance for this platform.
     * @return PlatformConnection instance of this platform.
     */
    default PlatformConnection getConnection() {
        PlatformRegistration registration = getRegistration();
        if (registration == null) return null;
        else return registration.getConnection();
    }

    /**
     * Gets the current plugin instance for this platform.
     * @return Plugin instance of this platform, or null if no plugin was associated with registering this Platform.
     */
    default Plugin getPlugin() {
        PlatformRegistration registration = getRegistration();
        if (registration == null) return null;
        else return registration.getPlugin();
    }

    abstract class Builder {
        private Plugin plugin;
        private String id, name;
        private PlatformConnection connection;

        public Builder() { }

        /**
         * Gets the plugin associated with the Builder's <b>id</b>, as previous set.
         * @return
         */
        public abstract Platform getPlatform();

        public String getId() {
            return id;
        }

        public Builder setId(String id) {
            if (this.id != null) throw new IllegalArgumentException("id is already set");

            this.id = id;

            return this;
        }

        public String getName() {
            return name == null ? id : name;
        }

        public Builder setName(String name) {
            if (this.name != null) throw new IllegalArgumentException("name is already set");

            this.name = name;

            return this;
        }

        public PlatformConnection getConnection() {
            return connection;
        }

        public Builder setConnection(PlatformConnection connection) {
            if (this.connection != null) throw new IllegalArgumentException("connection is already set");

            this.connection = connection;

            return this;
        }

        /**
         * Gets the instance of the plugin that this platform will be registered to.
         * @return plugin instance.
         */
        public Plugin getPlugin() {
            return plugin;
        }

        public Builder setPlugin(Plugin plugin) {
            if (this.plugin != null) throw new IllegalArgumentException("plugin", new IllegalStateException("already set"));
            this.plugin = plugin;
            return this;
        }
    }
}
