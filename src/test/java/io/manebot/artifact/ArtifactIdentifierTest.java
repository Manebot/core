package io.manebot.artifact;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ArtifactIdentifierTest {

    @Test
    public void testFromString_Valid() throws Exception {
        assertEquals(
                "manebot",
                ArtifactIdentifier.fromString("io.manebot:manebot:1.0.0").getArtifactId()
        );

        assertEquals(
                "io.manebot",
                ArtifactIdentifier.fromString("io.manebot:manebot:1.0.0").getPackageId()
        );

        assertEquals(
                "1.0.0",
                ArtifactIdentifier.fromString("io.manebot:manebot:1.0.0").getVersion()
        );

        assertEquals(
                "io.manebot:manebot:1.0.0",
                ArtifactIdentifier.fromString("io.manebot:manebot:1.0.0").toString()
        );
    }

    @Test
    public void testFromString_Invalid() throws Exception {
        try {
            ArtifactIdentifier.fromString("io.manebot_invalid");

            throw new AssertionError("fromString did not throw expected exception");
        } catch (IllegalArgumentException expected) {

        }
    }

}
