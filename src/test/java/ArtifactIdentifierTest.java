import com.github.manevolent.jbot.artifact.ArtifactIdentifier;
import junit.framework.TestCase;

public class ArtifactIdentifierTest extends TestCase {

    public static void main(String[] args) throws Exception {
        new ArtifactIdentifierTest().testParser();
    }

    public void testParser() throws Exception {
        assertEquals(
                "jbot",
                ArtifactIdentifier.fromString("com.github.manevolent:jbot:1.0.0").getArtifactId()
        );

        assertEquals(
                "com.github.manevolent",
                ArtifactIdentifier.fromString("com.github.manevolent:jbot:1.0.0").getPackageId()
        );

        assertEquals(
                "1.0.0",
                ArtifactIdentifier.fromString("com.github.manevolent:jbot:1.0.0").getVersion().toString()
        );

        assertEquals(
                "com.github.manevolent:jbot:1.0.0",
                ArtifactIdentifier.fromString("com.github.manevolent:jbot:1.0.0").toString()
        );
    }

}
