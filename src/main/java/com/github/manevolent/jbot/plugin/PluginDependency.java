package com.github.manevolent.jbot.plugin;

import com.github.manevolent.jbot.artifact.ArtifactIdentifier;

public interface PluginDependency {

    /**
     * Gets the plugin parent artifact identifier in this dependency relationship.
     * @return parent identifier.
     */
    ArtifactIdentifier getIdentifier();

    /**
     * Gets the depended plugin artifact identifier in this plugin dependency relationship.
     * @return dependency identifier.
     */
    ArtifactIdentifier getDependency();

    /**
     * Finds if this dependency is explicitly required.
     * @return true if the dependency is required, false otherwise.
     */
    boolean isRequired();

}
