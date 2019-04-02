package io.manebot.artifact;

/**
 * Describes an <b>Manifest</b> identifier, a case-insensitive identification scheme for <b>ArtifactManifest</b>s.
 *
 * Format: <i>packageId:artifactId</i>
 *
 * Example: <i>com.github.manevolent:ts3j</i>
 *
 * <b>ManifestIdentifier</b> has a method (fromString) which allows you to parse well-formatted manifest qualifiers.
 *
 * Manifest identifiers have two levels of identification:
 *
 *  - packageId, a global-scope specifier which isolates <b>Artifact</b>s to specific packages.
 *  - artifactId, a package-scope specifier which identifies a particular artifact within a package.
 *
 * Manifest identifiers are case-insensitive by nature, and all overloaded <b>Object</b> method adhere to this
 * specification.  Due to this, it is safe to use separate but logically equal instances of <b>ManifestIdentifier</b> in
 * a <b>Map</b>, or dictionary, utilizing hashed keys.
 *
 */
public class ManifestIdentifier {
    private final String packageId;
    private final String artifactId;

    /**
     * Constructs a new artifact identifier.
     *
     * @param packageId Package ID of the artifact, must not be null.
     * @param artifactId Artifact ID of the artifact, must not be null.
     */
    public ManifestIdentifier(String packageId, String artifactId) {
        if (packageId == null) throw new NullPointerException("packageId");
        this.packageId = packageId;

        if (artifactId == null) throw new NullPointerException("artifactId");
        this.artifactId = artifactId;
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
        return packageId + ":" + artifactId;

    }

    @Override
    public int hashCode() {
        return packageId.hashCode() ^ artifactId.hashCode();

    }

    @Override
    public boolean equals(Object b) {
        return b != null && b instanceof ManifestIdentifier && equals((ManifestIdentifier) b);
    }

    /**
     * Checks for equality between two ArtifactIdentifiers.
     *
     * When comparing versions, two ArtifactIdentifiers each with a null <b>version</b> are considered equal.
     *
     * @param b artifact to check for equality against this artifact identifier.
     * @return true if the artifact identifiers are equal, false otherwise.
     */
    public boolean equals(ManifestIdentifier b) {
        return packageId.equalsIgnoreCase(b.packageId) && artifactId.equalsIgnoreCase(b.artifactId);
    }

    /**
     * Parses the specified string and converts it into a well-formatted Artifact identifier.
     *
     * @param string string to parse.
     * @return ArtifactIdentifier instance representing the specified string.
     */
    public static ManifestIdentifier fromString(String string) {
        String[] parts = string.split("\\:", 3);
        if (parts.length < 2)
            throw new IllegalArgumentException(
                    "artifact identifier does not have at least a " +
                            "package identifier and an artifact identifier (such as, packageId:artifactId): " +
                            string
            );

        if (parts.length == 2) {
            return new ManifestIdentifier(
                    parts[0],
                    parts[1]
            );
        } else
            throw new IllegalArgumentException("unexpected artifact identifier: " + string);
    }

    /**
     * Constructs an anonymized ArtifactIdentifier instance, without the version component.
     * @return ArtifactIdentifier without a version component.
     */
    public ArtifactIdentifier withVersion(String version) {
        return new ArtifactIdentifier(packageId, artifactId, version);
    }
}
