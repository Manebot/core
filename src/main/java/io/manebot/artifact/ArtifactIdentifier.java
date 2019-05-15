package io.manebot.artifact;

/**
 * Describes an <b>Artifact</b> identifier, a case-insensitive identification scheme for <b>Artifact</b>s.
 *
 * Format: <i>packageId:artifactId:version</i>
 *      Where "version" corresponds to the format described in <b>io.manebot.artifact.Version</b>.
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
public class ArtifactIdentifier extends ManifestIdentifier {
    private final String version;

    /**
     * Constructs a new artifact identifier.
     *
     * @param packageId Package ID of the artifact, must not be null.
     * @param artifactId Artifact ID of the artifact, must not be null.
     * @param version Version of the artifact, may be null.
     */
    public ArtifactIdentifier(String packageId, String artifactId, String version) {
        super(packageId, artifactId);

        if (version == null) throw new NullPointerException("version");
        this.version = version;
    }

    /**
     * Constructs a new artifact identifier.
     *
     * @param manifestIdentifier Manifest identifier of the artifact, must not be null.
     * @param version Version of the artifact, may be null.
     */
    public ArtifactIdentifier(ManifestIdentifier manifestIdentifier, String version) {
        super(manifestIdentifier.getPackageId(), manifestIdentifier.getArtifactId());

        if (version == null) throw new NullPointerException("version");
        this.version = version;
    }

    /**
     * Gets the version associated with this artifact.  May be null.
     * @return version.
     */
    public final String getVersion() {
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
        return super.toString() + ":" + version;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ version.hashCode();
    }

    public boolean isManifest(ManifestIdentifier manifestIdentifier) {
        return manifestIdentifier.getPackageId().equals(getPackageId()) &&
                manifestIdentifier.getArtifactId().equals(getArtifactId());
    }

    public boolean isManifest(ArtifactManifest manifest) {
        return isManifest(manifest.getIdentifier());
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
        return super.equals(b) &&
                (
                        (version != null && b.version != null && version.equals(b.version)) ||
                        version == null && b.version == null
                );
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

        if (parts.length == 3) {
            return new ArtifactIdentifier(
                    parts[0],
                    parts[1],
                    parts[2]
            );
        } else
            throw new IllegalArgumentException("unexpected artifact identifier: " + string);
    }

    /**
     * Constructs an anonymized ArtifactIdentifier instance, without the version component.
     * @return ArtifactIdentifier without a version component.
     */
    public ManifestIdentifier withoutVersion() {
        return new ManifestIdentifier(getPackageId(), getArtifactId());
    }
}
