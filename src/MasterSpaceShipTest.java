import org.junit.Test;
import static org.junit.Assert.*;

public class MasterSpaceShipTest {
    
    @Test
    public void testMove() {
        // Create a MasterSpaceShip at (0,0)
        MasterSpaceShip ship = new MasterSpaceShip(0, 0);
        
        // Test that the ship moves within bounds
        for (int i = 0; i < 100; i++) {
            ship.move(5, 5);
            assertTrue(ship.x >= 0 && ship.x <= 4);
            assertTrue(ship.y >= 0 && ship.y <= 4);
        }
    }
}
