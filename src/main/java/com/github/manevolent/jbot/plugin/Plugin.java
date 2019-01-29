package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.artifact.Artifact;

public interface Plugin {

    /**
     * Gets the artifact associated with this plugin.
     * @return associated Artifact instance.
     */
    Artifact getArtifact();

    /**
     * Gets this plugin's name.  This is typically the <b>artifactId</b> of the plugin.
     * @return Plugin's name.
     */
    String getName();

    /**
     * Sets this artifact's enabled state.
     * @param enabled enabled state
     * @return true if the artifact's state was changed, false otherwise.
     */
    boolean setEnabled(boolean enabled);

    /**
     * Finds if this plugin is enabled.
     * @return true if the plugin is enabled, false otherwise.
     */
    boolean isEnabled();

}
