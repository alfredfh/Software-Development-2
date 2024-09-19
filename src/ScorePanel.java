import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScorePanel extends JPanel implements ScoreObserver {
   
	private static final long serialVersionUID = 1L;
	private JLabel scoreLabel;
    private JLabel shipsDestroyedLabel;

    public ScorePanel() {
        scoreLabel = new JLabel("Score: 0");
        shipsDestroyedLabel = new JLabel("Ships destroyed: 0");

        add(scoreLabel);
        add(shipsDestroyedLabel);
    }
    
    //the implementation of the score observer interface in the scorepanel displayed in the game
    //allowing the game to update the score when the game is running
    @Override
    public void updateScore(int score, int shipsDestroyed) {
        scoreLabel.setText("Score: " + score);
        shipsDestroyedLabel.setText("Ships destroyed: " + shipsDestroyed);
    }
}
