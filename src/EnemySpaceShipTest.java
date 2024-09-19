import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnemySpaceShipTest {
    
    @Test
    public void testMove() {
        int gridWidth = 4;
        int gridHeight = 4;
        EnemySpaceShip enemyShip = new BattleStar(2, 2);
        int oldX = enemyShip.x;
        int oldY = enemyShip.y;
        enemyShip.move(gridWidth, gridHeight);
        int newX = enemyShip.x;
        int newY = enemyShip.y;

        // Check that the enemy ship has moved to a different location within the grid
        assertTrue(newX >= 0 && newX < gridWidth);
        assertTrue(newY >= 0 && newY < gridHeight);
        assertTrue(oldX != newX || oldY != newY, "At least one of the coordinates should change");
    }
}
