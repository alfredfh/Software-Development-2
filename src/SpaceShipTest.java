import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpaceShipTest {

    @Test
    public void testMove() {
        SpaceShip spaceShip = new MasterSpaceShip(1, 1);
        spaceShip.move(4, 4);
        assertTrue(spaceShip.x >= 0 && spaceShip.x <= 4);
        assertTrue(spaceShip.y >= 0 && spaceShip.y <= 4);
    }

    @Test
    public void testHasCollided() {
        SpaceShip spaceShip1 = new MasterSpaceShip(1, 1);
        SpaceShip spaceShip2 = new BattleStar(1, 1);
        assertTrue(spaceShip1.hasCollided(spaceShip2));
        assertFalse(spaceShip1.hasCollided(new BattleCruiser(2, 2)));
    }
}
