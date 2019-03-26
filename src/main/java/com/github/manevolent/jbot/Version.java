package com.github.manevolent.jbot;

import java.util.Arrays;
import java.util.stream.Collectors;

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
    private final int[] components;
    private final String buildId;

    public Version(int[] components, String buildId) {
        this.components = components;
        this.buildId = buildId;
    }

    public int getComponent(int index) {
        if (components.length <= index) return 0;
        else return components[index];
    }

    @Override
    public int compareTo(Version o) {
        int len = Math.max(o.components.length, components.length);
        for (int componentIndex = 0; componentIndex < len; componentIndex ++) {
            int c = Integer.compare(getComponent(componentIndex), o.getComponent(componentIndex));
            if (c != 0) return c;
        }

        return 0;
    }

    @Override
    public String toString() {
        return String.join(".", Arrays.stream(components).mapToObj(String::valueOf).collect(Collectors.toList()))
                + (buildId != null ? ("-" + buildId) : "");
    }

    @Override
    public boolean equals(Object b) {
        return b instanceof Version && equals((Version) b);
    }

    public boolean equals(Version b) {
        return b != null && Arrays.equals(components, b.components) && buildId.equals(b.buildId);
    }

    public static Version fromString(String versionString) {
        String[] parts = versionString.split("\\-", 2);
        String buildId = parts.length == 2 ? parts[1] : null;
        String[] componentStrings = parts[0].split("\\.");
        int[] components = Arrays.stream(componentStrings).mapToInt(Integer::parseInt).toArray();
        return new Version(components, buildId);
    }

}
