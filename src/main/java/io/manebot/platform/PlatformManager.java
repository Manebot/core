package io.manebot.platform;

import io.manebot.plugin.Plugin;
import io.manebot.plugin.PluginException;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface PlatformManager {

    /**
     * Registers a platform in the system, and automatically connects to it.
     * Registration may only occur if a system platform has not yet been assigned.
     *
     * @param consumer Platform building function connection to call for registration.
     * @return Platform instance.
     */
    PlatformRegistration registerPlatform(Consumer<Platform.Builder> consumer);

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

}
