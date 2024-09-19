import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Component;
import javax.swing.JLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScorePanelTest {

    private ScorePanel scorePanel;

    @BeforeEach
    public void setUp() {
        scorePanel = new ScorePanel();
    }

    @Test
    public void testInitialLabels() {
        assertEquals(2, scorePanel.getComponentCount(), "There should be 2 components in the ScorePanel");

        Component scoreComponent = scorePanel.getComponent(0);
        Component shipsDestroyedComponent = scorePanel.getComponent(1);

        assertEquals(JLabel.class, scoreComponent.getClass(), "The first component should be a JLabel");
        assertEquals(JLabel.class, shipsDestroyedComponent.getClass(), "The second component should be a JLabel");

        JLabel scoreLabel = (JLabel) scoreComponent;
        JLabel shipsDestroyedLabel = (JLabel) shipsDestroyedComponent;

        assertEquals("Score: 0", scoreLabel.getText(), "Initial score text should be 'Score: 0'");
        assertEquals("Ships destroyed: 0", shipsDestroyedLabel.getText(), "Initial ships destroyed text should be 'Ships destroyed: 0'");
    }

    @Test
    public void testUpdateScore() {
        scorePanel.updateScore(15, 3);

        Component scoreComponent = scorePanel.getComponent(0);
        Component shipsDestroyedComponent = scorePanel.getComponent(1);

        JLabel scoreLabel = (JLabel) scoreComponent;
        JLabel shipsDestroyedLabel = (JLabel) shipsDestroyedComponent;

        assertEquals("Score: 15", scoreLabel.getText(), "Updated score text should be 'Score: 15'");
        assertEquals("Ships destroyed: 3", shipsDestroyedLabel.getText(), "Updated ships destroyed text should be 'Ships destroyed: 3'");
    }
}
