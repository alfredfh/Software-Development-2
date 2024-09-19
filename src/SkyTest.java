import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SkyTest {

    @Test
    public void testConstructor() {
        int width = 4;
        int height = 4;
        Sky sky = new Sky(width, height);

        assertEquals(width, sky.width);
        assertEquals(height, sky.height);
    }
}
