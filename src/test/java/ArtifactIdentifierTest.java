import io.manebot.artifact.ArtifactIdentifier;
import junit.framework.TestCase;

public class ArtifactIdentifierTest extends TestCase {

    public static void main(String[] args) throws Exception {
        new ArtifactIdentifierTest().testParser();
    }

    public void testParser() throws Exception {
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

}
