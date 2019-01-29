package com.github.manevolent.jbot.artifact;

/**
 * Describes an <b>Artifact</b> identifier, a case-insensitive identification scheme for <b>Artifact</b>s.
 *
 * Format: <i>packageId:artifactId:version</i>
 *      Where "version" corresponds to the format described in <b>com.github.manevolent.jbot.artifact.Version</b>.
 *
 * Example: <i>com.github.manevolent:ts3j:1.0.0-ba938fd</i>
 *
 * <b>ArtifactIdentifier</b> has a method (fromString) which allows you to parse well-formatted artifact identifiers.
 *
 * Artifact identifiers have three levels of identification:
 *
 *  - packageId, a global-scope specifier which isolates <b>Artifact</b>s to specific packages.
 *  - artifactId, a package-scope specifier which identifies a particular artifact within a package.
 *  - version, a version number associated with the artifact, occasionally containing hexadecimal build fingerprints as
 *    well.
 *
 * Artifact identifiers are case-insensitive by nature, and all overloaded <b>Object</b> method adhere to this
 * specification.  Due to this, it is safe to use separate but logically equal instances of <b>ArtifactIdentifier</b> in
 * a <b>Map</b>, or dictionary, utilizing hashed keys.
 *
 */
public class ArtifactIdentifier {
    private final String packageId;
    private final String artifactId;
    private final Version version;

    /**
     * Constructs a new artifact identifier.
     *
     * @param packageId Package ID of the artifact, must not be null.
     * @param artifactId Artifact ID of the artifact, must not be null.
     * @param version Version of the artifact, may be null.
     */
    public ArtifactIdentifier(String packageId, String artifactId, Version version) {
        if (packageId == null) throw new NullPointerException("packageId");
        assertLowercase(packageId);
        this.packageId = packageId;

        if (artifactId == null) throw new NullPointerException("artifactId");
        assertLowercase(artifactId);
        this.artifactId = artifactId;

        if (version != null)
            assertLowercase(version.getBuildId());
        this.version = version;
    }

    /**
     * Gets the artifactId associated with this artifact identifier.
     * @return artifactId.
     */
    public final String getArtifactId() {
        return artifactId;
    }

    /**
     * Gets the packageId associated with this artifact identifier.
     * @return packageId.
     */
    public final String getPackageId() {
        return packageId;
    }

    /**
     * Gets the version associated with this artifact.  May be null.
     * @return version.
     */
    public final Version getVersion() {
        return version;
    }

    /**
     * Converts this artifact identifier to a string.
     *
     * There are two possible formats, depending on the value of <b>version</b> (getVersion()):
     *
     * Version is null: packageId:artifactId
     * Version is not null: packageId:artifactId:version
     *
     * @return the ArtifactIdentifier's string representation.
     */
    @Override
    public String toString() {
        return version != null ?
                packageId + ":" + artifactId + ":" + version.toString() :
                packageId + ":" + artifactId;
    }

    @Override
    public int hashCode() {
        return version != null ?
                packageId.hashCode() ^ artifactId.hashCode() ^ version.hashCode() :
                packageId.hashCode() ^ artifactId.hashCode();
    }

    @Override
    public boolean equals(Object b) {
        return b != null && b instanceof ArtifactIdentifier && equals((ArtifactIdentifier) b);
    }

    /**
     * Checks for equality between two ArtifactIdentifiers.
     *
     * When comparing versions, two ArtifactIdentifiers each with a null <b>version</b> are considered equal.
     *
     * @param b artifact to check for equality against this artifact identifier.
     * @return true if the artifact identifiers are equal, false otherwise.
     */
    public boolean equals(ArtifactIdentifier b) {
        return packageId.equals(b.packageId) && artifactId.equals(b.artifactId) &&
                (
                        (version != null && b.version != null && version.equals(b.version)) ||
                        version == null && b.version == null
                );
    }

    private static void assertLowercase(String string) {
        if (string == null) return;

        for (int idx = 0; idx < string.length(); idx ++) {
            if (Character.isUpperCase(string.charAt(idx)))
                throw new IllegalArgumentException("string is not lowercase");
        }
    }

    /**
     * Parses the specified string and converts it into a well-formatted Artifact identifier.
     *
     * @param string string to parse.
     * @return ArtifactIdentifier instance representing the specified string.
     */
    public static ArtifactIdentifier fromString(String string) {
        String[] parts = string.split("\\:", 3);
        if (parts.length < 2)
            throw new IllegalArgumentException(
                    "artifact identifier does not have at least a " +
                            "package identifier and an artifact identifier (such as, packageId:artifactId): " +
                            string
            );

        if (parts.length == 2) {
            return new ArtifactIdentifier(
                    parts[0],
                    parts[1],
                    null
            );
        } else if (parts.length == 3) {
            return new ArtifactIdentifier(
                    parts[0],
                    parts[1],
                    Version.fromString(parts[2])
            );
        } else
            throw new IllegalArgumentException("unexpected artifact identifier: " + string);
    }
}
