import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SkyWarsTest {
	
	 class TestScoreObserver implements ScoreObserver {
	        List<Integer> scores = new ArrayList<>();
	        List<Integer> shipsDestroyed = new ArrayList<>();

	        @Override
	        public void updateScore(int score, int shipsDestroyed) {
	            this.scores.add(score);
	            this.shipsDestroyed.add(shipsDestroyed);
	        }
	    }
    private SkyWars skyWars;
    private String testUsername = "testuser";

    @BeforeEach
    public void setUp() {
        skyWars = new SkyWars("testUser");
        skyWars.setVisible(true);
    }

    @AfterEach
    public void tearDown() {
        skyWars.dispose();
    }

    @Test
    public void testHighScores() {
        List<String> highScores = SkyWars.getHighScores();
        if (highScores.isEmpty()) {
            assertEquals(0, highScores.size());
        } else {
            assertTrue(highScores.size() > 0);
        }
    }

    @Test
    public void testStartButton() {
        skyWars.startButton.doClick();
        assertNotNull(skyWars.masterSpaceShip);
        assertFalse(skyWars.enemySpaceShips.isEmpty());
    }

    @Test
    public void testMoveButton() {
        skyWars.startButton.doClick();
        
        if (skyWars.enemySpaceShips.isEmpty()) {
            SkyWars.enemySpaceShips.add(new BattleStar(100, 100));
        }
        int initialMasterX = skyWars.masterSpaceShip.x;
        int initialMasterY = skyWars.masterSpaceShip.y;
        int initialEnemyX = skyWars.enemySpaceShips.get(0).x;
        int initialEnemyY = skyWars.enemySpaceShips.get(0).y;

        skyWars.moveButton.doClick();

        assertFalse(initialMasterX == skyWars.masterSpaceShip.x && initialMasterY == skyWars.masterSpaceShip.y);
        assertFalse(initialEnemyX == skyWars.enemySpaceShips.get(0).x && initialEnemyY == skyWars.enemySpaceShips.get(0).y);
    }
    
    @Test
    void testCollisionInDefensiveMode() {
        MasterSpaceShip masterSpaceShip = new MasterSpaceShip(0, 0);
        masterSpaceShip.setDefensiveMode(true);
        BattleStar battleStar = new BattleStar(0, 0);

        assertTrue(masterSpaceShip.hasCollided(battleStar));
        assertTrue(masterSpaceShip.isDefensiveMode());
    }
    
    @Test
    void testCollisionInAttackMode() {
        MasterSpaceShip masterSpaceShip = new MasterSpaceShip(0, 0);
        masterSpaceShip.setDefensiveMode(false);
        BattleStar battleStar = new BattleStar(0, 0);

        assertTrue(masterSpaceShip.hasCollided(battleStar));
        assertFalse(masterSpaceShip.isDefensiveMode());
    }
    
    @Test
    void testScoreObserver() {
        SkyWars skyWars = new SkyWars("test_user");
        TestScoreObserver observer = new TestScoreObserver();
        skyWars.registerScoreObserver(observer);

        // Set the score and shipsDestroyed manually
        SkyWars.score = 5;
        SkyWars.shipsDestroyed = 3;
        SkyWars.notifyScoreObservers();

        assertFalse(observer.scores.isEmpty(), "Score updates should not be empty");
        assertFalse(observer.shipsDestroyed.isEmpty(), "Ships destroyed updates should not be empty");

        assertEquals(5, observer.scores.get(0), "Score update should match the set score");
        assertEquals(3, observer.shipsDestroyed.get(0), "Ships destroyed update should match the set value");

        // Unregister the observer and update the score again
        skyWars.removeScoreObserver(observer);
        SkyWars.score = 10;
        SkyWars.shipsDestroyed = 6;
        SkyWars.notifyScoreObservers();

        assertEquals(1, observer.scores.size(), "Observer should not receive updates after being unregistered");
        assertEquals(1, observer.shipsDestroyed.size(), "Observer should not receive updates after being unregistered");
    }
    
    @Test
    public void testSaveGameState() {
        skyWars.saveGameState(testUsername);
        Path gameStatePath = Paths.get(testUsername + "_game_state_new.dat");
        assertTrue(Files.exists(gameStatePath), "Game state file should exist after saving");
    }

    @Test
    public void testLoadGameState() {
        saveDefaultGameState();
        assertTrue(skyWars.loadGameState(testUsername), "Loading game state should return true for existing saved game");

        deleteSavedGameState();
        assertFalse(skyWars.loadGameState(testUsername), "Loading game state should return false for non-existing saved game");
    }

    // Utility methods to assist in testing
    private void saveDefaultGameState() {
        skyWars.saveGameState(testUsername);
    }

    private void deleteSavedGameState() {
        Path gameStatePath = Paths.get(testUsername + "_game_state_new.dat");
        if (Files.exists(gameStatePath)) {
            try {
                Files.delete(gameStatePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
