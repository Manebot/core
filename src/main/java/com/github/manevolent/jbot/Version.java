package com.github.manevolent.jbot;

/**
 * Describes a jBot version.
 *
 * Versions follow the format: major.minor.micro-buildId
 *
 * Note that "buildId" is always ignored in comparison, is ALWAYS compared in equality, and should be considered
 * only when testing by a developer or to match JAR/Git commit versions on a plugin-by-plugin basis.
 * "buildId" must be one alpha-numeric word no longer than 32 characters.
 *
 * Valid examples:
 *
 * (1500.0.0)
 *  1500-abcdefg1234 to include buildId
 *  1500
 *
 * (1.0.0)
 *  1.0
 *  1.5-abcdefg1234 to include buildId
 *
 * (1.0.1)
 *  1.0.1
 *  1.5.15-abcdefg1234 to include buildId
 */
public class Version implements Comparable<Version> {
    private final int major, minor, micro;
    private final String buildId;

    public Version(int major, int minor, int micro, String buildId) {
        this.major = major;
        this.minor = minor;
        this.micro = micro;
        this.buildId = buildId;
    }

    /**
     * Gets this version's major version.
     *
     * major.minor.macro-buildId
     *
     * @return Major version.
     */
    public int getMajor() {
        return major;
    }


    /**
     * Gets this plugin's minor version.
     *
     * major.minor.macro-buildId
     *
     * @return Minor version.
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Gets this plugin's micro version.
     *
     * major.minor.macro-buildId
     *
     * @return Micro version.
     */
    public int getMicro() {
        return micro;
    }

    /**
     * Gets this version's build ID.
     *
     * major.minor.macro-buildId
     *
     * Note that "buildId" is always ignored in comparison, is ALWAYS compared in equality, and should be considered
     * only when testing by a developer or to match JAR/Git commit versions on a plugin-by-plugin basis.
     *
     * @return build Id.
     */
    public String getBuildId() {
        return buildId;
    }

    @Override
    public int compareTo(Version o) {
        int majorCompare = Integer.compare(getMajor(), o.getMajor());
        if (majorCompare != 0) {
            return majorCompare;
        } else {
            int minorCompare = Integer.compare(getMinor(), o.getMinor());
            if (minorCompare != 0) {
                return minorCompare;
            } else {
                int microCompare = Integer.compare(getMicro(), o.getMicro());
                if (microCompare != 0) {
                    return microCompare;
                } else {
                    // buildId is not compared
                    return 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        return Integer.toString(major) + "." + Integer.toString(minor) + "." + Integer.toString(micro) +
                (buildId != null ? ("-" + buildId) : "");
    }

    @Override
    public boolean equals(Object b) {
        return b instanceof Version && equals((Version) b);
    }

    public boolean equals(Version b) {
        return b != null &&
                major == b.major &&
                minor == b.minor &&
                micro == b.micro &&
                buildId.equals(b.buildId);
    }

    public static Version fromString(String versionString) {
        String[] parts = versionString.split("\\-", 2);
        String artifactId = parts.length == 2 ? parts[1] : null;

        String[] numericParts = parts[0].split("\\.", 3);

        switch (numericParts.length) {
            case 1:
                return new Version(
                        Integer.parseInt(numericParts[0]),
                        0,
                        0,
                        artifactId
                );
            case 2:
                return new Version(
                        Integer.parseInt(numericParts[0]),
                        Integer.parseInt(numericParts[1]),
                        0,
                        artifactId
                );
            case 3:
                return new Version(
                        Integer.parseInt(numericParts[0]),
                        Integer.parseInt(numericParts[1]),
                        Integer.parseInt(numericParts[2]),
                        artifactId
                );
            default:
                throw new IllegalArgumentException("invalid version string");
        }
    }

}
