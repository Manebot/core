package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.artifact.Artifact;
import com.github.manevolent.jbot.platform.Platform;

import java.util.Collection;

public interface Plugin {

    /**
     * Gets the artifact associated with this plugin.
     * @return associated Artifact instance.
     */
    Artifact getArtifact();

    /**
     * Gets a list of platforms registered by this plugin.
     * @return associated Platform instances.
     */
    Collection<Platform> getPlatforms();

    /**
     * Gets the registered command labels for this plugin.
     * @return registered commands.
     */
    Collection<String> getCommands();

    /**
     * Gets this plugin's name.  This is typically the lowercase <b>artifactId</b> of the plugin.
     * @return Plugin's name.
     */
    String getName();

    /**
     * Sets this artifact's enabled state.
     * @param enabled enabled state
     * @return true if the artifact's state was changed, false otherwise.
     */
    boolean setEnabled(boolean enabled) throws PluginException;

    /**
     * Finds if this plugin is enabled.
     * @return true if the plugin is enabled, false otherwise.
     */
    boolean isEnabled();

}
