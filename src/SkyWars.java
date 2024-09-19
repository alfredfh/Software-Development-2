import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class SkyWars{
	
	
	private static final String HIGH_SCORES_FILE = "high_scores2.txt";
	static List<EnemySpaceShip> enemySpaceShips;
	
	private static String username;
	
	private static List<ScoreObserver> scoreObservers = new ArrayList<>();

	public static JFrame frame;
	private ScorePanel scorePanel;
	private static Sky sky;
	public static MasterSpaceShip masterSpaceShip;

	static JButton startButton;
	static JButton moveButton;
	static JButton exitButton;
	static JButton saveGameButton;
	
	public static GridPanel gridPanel;
    
	public static int score;
	public static int shipsDestroyed;
	private static boolean startButtonPressed = false;
	
   
    public SkyWars(String username) {
    	SkyWars.username = username;
        createAndShowGUI();
        registerScoreObserver(scorePanel);
    }
    
    //saving the game state to a hashmap .dat file 
    void saveGameState(String username) {
        HashMap<String, Object> gameState = new HashMap<>();
        gameState.put("score", score);
        gameState.put("shipsDestroyed", shipsDestroyed);
        gameState.put("masterSpaceShip", masterSpaceShip);
        gameState.put("enemySpaceShips", enemySpaceShips);
        
        String filePath = username + "_game_state_new.dat";
        Path gameStatePath = Paths.get(filePath);

        // Delete the existing file if it exists to save the newest one
        if (Files.exists(gameStatePath)) {
            try {
                Files.delete(gameStatePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //writing file output stream
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(username + "_game_state_new.dat"))) {
            oos.writeObject(gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
     
    }
    
    //loading game boolean method that takes the username entered
//    static boolean loadGameState(String username) {
//        Path gameStatePath = Paths.get(username + "_game_state_new.dat");
//
//        if (!Files.exists(gameStatePath)) {
//            return false;
//        }
//
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(username + "_game_state_new.dat"))) {
//            @SuppressWarnings("unchecked")
//            HashMap<String, Object> gameState = (HashMap<String, Object>) ois.readObject();
//         // Create a new SkyWars object and initialise the GUI
//            SkyWars newSkyWars = new SkyWars(username);
//            
//            newSkyWars.score = (int) gameState.get("score");
//            newSkyWars.shipsDestroyed = (int) gameState.get("shipsDestroyed");
//            newSkyWars.masterSpaceShip = (MasterSpaceShip) gameState.get("masterSpaceShip");
//            newSkyWars.enemySpaceShips = (List<EnemySpaceShip>) gameState.get("enemySpaceShips");
//            
//            newSkyWars.gridPanel.setEnemySpaceShips(enemySpaceShips);
//            newSkyWars.gridPanel.repaint();
//            notifyScoreObservers();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return true;
//    }
    
    
    static boolean loadGameState(String username) {
        Path gameStatePath = Paths.get(username + "_game_state_new.dat");

        if (!Files.exists(gameStatePath)) {
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(username + "_game_state_new.dat"))) {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> gameState = (HashMap<String, Object>) ois.readObject();
            
            // Create a new SkyWars object and initialize the GUI
            SkyWars newSkyWars = new SkyWars(username);
            
            newSkyWars.score = (int) gameState.get("score");
            newSkyWars.shipsDestroyed = (int) gameState.get("shipsDestroyed");
            newSkyWars.masterSpaceShip = (MasterSpaceShip) gameState.get("masterSpaceShip");
            newSkyWars.enemySpaceShips = (List<EnemySpaceShip>) gameState.get("enemySpaceShips");

            // Load images from the game state
            String masterShipImagePath = (String) gameState.get("masterShipImagePath");
            String enemyShooterImagePath = (String) gameState.get("enemyShooterImagePath");
            String enemyCruiserImagePath = (String) gameState.get("enemyCruiserImagePath");
            String enemyStarImagePath = (String) gameState.get("enemyStarImagePath");

            newSkyWars.gridPanel.loadImages();
            newSkyWars.gridPanel.setEnemySpaceShips(newSkyWars.enemySpaceShips);
            newSkyWars.gridPanel.repaint();
            newSkyWars.notifyScoreObservers();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return true;
    }

    
    
    
    


  private static final String GREEN_COLOR = "<html><font color='green'>";
  private static final String BLACK_COLOR = "<html><font color='black'>";
  private static final String RED_COLOR = "<html><font color='red'>";
    
  static class WelcomeFrame extends JFrame {
       
		private static final long serialVersionUID = 1L;
		private JPanel cards;
        private JTextField usernameField;
        
        public WelcomeFrame() {
        	//displaying a welcome frame 
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(300, 300);
            setTitle("Welcome");

            cards = new JPanel(new CardLayout());

            JPanel welcomePanel = createWelcomePanel();
            JPanel mainMenuPanel = createMainMenuPanel();

            cards.add(welcomePanel, "welcome");
            cards.add(mainMenuPanel, "mainMenu");

            getContentPane().add(cards);

            setVisible(true);
        }

        private JPanel createWelcomePanel() {
        	//creating a welcome frame for user to input name
            JPanel welcomePanel = new JPanel();
            welcomePanel.setLayout(new BorderLayout());

            JPanel labelPanel = new JPanel();
            labelPanel.add(new JLabel("Enter your username:"));
            welcomePanel.add(labelPanel, BorderLayout.NORTH);

            JPanel fieldPanel = new JPanel();
            
            usernameField = new JTextField(15);
            fieldPanel.add(usernameField);
            welcomePanel.add(fieldPanel, BorderLayout.CENTER);

            
            JPanel buttonPanel = new JPanel();
            JButton submitButton = new JButton("Submit");
            
            submitButton.addActionListener(e -> showMainMenu());
            buttonPanel.add(submitButton);
            
           
            welcomePanel.add(buttonPanel, BorderLayout.SOUTH);
            
            
			return welcomePanel;
        
            
        }
  

        private JPanel createMainMenuPanel() {
        	//creating main menu that allows the user to start a game, view high scores, or load a game
            JPanel mainMenuPanel = new JPanel();
            mainMenuPanel.setLayout(new GridLayout(3, 1));

            JButton startGameButton = new JButton("Start Game");
            startGameButton.addActionListener(e -> {
            	String username = usernameField.getText();
            	 if (username.trim().isEmpty()) {
            	        JOptionPane.showMessageDialog(this, "Please enter a username");
            	        System.exit(0);
            	    } else {
                   SkyWars skyWars = new SkyWars(username);
                skyWars.setVisible(true);
                setVisible(false);
                dispose();
                username = usernameField.getText();
            	}
            });

            JButton highScoreButton = new JButton("High Scores");
            highScoreButton.addActionListener(e -> {
                List<String> highScores = getHighScores();
                String highScoresMessage;
                //validating the high score file and if any high scores exist
                if (highScores.isEmpty()) {
                    highScoresMessage = "No high scores yet.";
                } else {
                    highScoresMessage = highScores.stream()
                        .map(line -> {
                            String[] parts = line.split(",");
                            //splitting the output to display more clearly
                            return GREEN_COLOR + parts[0] + BLACK_COLOR + " | No. of ships destroyed: " + RED_COLOR + parts[1] + BLACK_COLOR + " | Total Score: " + RED_COLOR + parts[2];
                        })
                        .collect(Collectors.joining("\n"));
                }
                JOptionPane.showMessageDialog(this, "High Scores:\n\n" + highScoresMessage);
            });
            
            //loading the game through the button and having an action listener for clicking the button
            JButton loadGameButton = new JButton("Load Game");
            loadGameButton.addActionListener(e -> {
            	
            	String username = usernameField.getText();
                if (loadGameState(username)) {
                	
                    this.setVisible(true);
                    setVisible(false);
                    dispose();
                    //setting the start button so that it is pressed and no error message shows up
                    //due to the nature of the game this was validated in the start and move button listeners in the code below
                    startButtonPressed = true;
                } else {
                    JOptionPane.showMessageDialog(this, "No saved game found for: " + username);
                }
            });

            mainMenuPanel.add(startGameButton);
            mainMenuPanel.add(highScoreButton);
            mainMenuPanel.add(loadGameButton);

            return mainMenuPanel;
        }

   

		private void showMainMenu() {
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "mainMenu");
            setTitle("Welcome " + usernameField.getText());
        }
    }
  
  
  	//creating the GUI for the game with the various buttons
    private void createAndShowGUI() {
        sky = new Sky(4, 4);

        frame = new JFrame("Sky Wars");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,650);
        frame.setLayout(new BorderLayout());
        
        scorePanel = new ScorePanel();
        frame.add(scorePanel, BorderLayout.NORTH);

        gridPanel = new GridPanel();
        gridPanel.setLayout(new GridLayout(sky.width, sky.height));
        

        startButton = new JButton("Start");
        moveButton = new JButton("Move");
        saveGameButton = new JButton("Save");
        exitButton = new JButton("Exit");
       
        

        startButton.addActionListener(new StartButtonListener());
        moveButton.addActionListener(new MoveButtonListener());
        
        exitButton.addActionListener(e -> System.exit(0));
       
        //switching to the two modes in the advanced features
        //using the defensiveMode method implemented in the mastership
        JButton modeButton = new JButton("Switch to Attack Mode");
        modeButton.addActionListener(e -> {
            if (masterSpaceShip != null && gridPanel != null) { // check if game has started
                masterSpaceShip.setDefensiveMode(!masterSpaceShip.isDefensiveMode());
                // update button text to reflect new mode
                if (masterSpaceShip.isDefensiveMode()) {
                    modeButton.setText("You are in Defence: Switch to Attack Mode");
                } else {
                    modeButton.setText("You are in Attack: Switch to Defence Mode");
                }
                gridPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Start the game first");
            }
        });
        
        //saving the game and exiting by calling the saveGameState and then system.exit
        JButton saveGameButton = new JButton("Save Game");
        saveGameButton.addActionListener(e -> {
             SkyWars.this.saveGameState(username);
             JOptionPane.showMessageDialog(null, "Game saved " + username);
             System.exit(0);
        });
        
       

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(moveButton);
        buttonPanel.add(modeButton);
        buttonPanel.add(saveGameButton);
        buttonPanel.add(exitButton);
        

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        
        frame.setVisible(true);
     
    }
    
    //methods to register, remove and notify the scoreObserver of the changes
    public void registerScoreObserver(ScoreObserver observer) {
        scoreObservers.add(observer);
    }

    public void removeScoreObserver(ScoreObserver observer) {
        scoreObservers.remove(observer);
    }

    public static void notifyScoreObservers() {
        for (ScoreObserver observer : scoreObservers) {
            observer.updateScore(score, shipsDestroyed);
        }
    }
    
   //class to start the game 
    public static class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Random random = new Random();
            //random positing of the mastership on clicking the start button
            int x = random.nextInt(sky.width);
            int y = random.nextInt(sky.height);
            masterSpaceShip = new MasterSpaceShip(x, y);
            enemySpaceShips = new ArrayList<>();
            
            //random spawning of enemy ship on top left corner in 1/3 chance and 1/3 type of ship
            // and adding it to the arraylist
            if (random.nextInt(3) == 0) {
                int enemyX = 0; // Always spawn at the top-left corner
                int enemyY = 0; // Always spawn at the top-left corner
                int shipType = random.nextInt(3);
                EnemySpaceShip initialEnemyShip;

                switch (shipType) {
                    case 0:
                    	initialEnemyShip = new BattleStar(enemyX, enemyY);
                        break;
                    case 1:
                    	initialEnemyShip = new BattleCruiser(enemyX, enemyY);
                        break;
                    default:
                    	initialEnemyShip = new BattleShooter(enemyX, enemyY);
                        break;
                }
            enemySpaceShips.add(initialEnemyShip);
            }
            
           
            

            gridPanel.setEnemySpaceShips(enemySpaceShips);
            //sets the start button to pressed as the player cannot move the ships if the game has not started
            startButtonPressed = true;
            gridPanel.loadImages();
            gridPanel.repaint();
        }
    }
    
    //class for moving the ships
    class MoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	//validating that the game has started
        	 if (!startButtonPressed) {
                 JOptionPane.showMessageDialog(null, "Press Start button first", "Error", JOptionPane.ERROR_MESSAGE);
             }else {
            //moving the ships by calling the move() method
            masterSpaceShip.move(sky.width, sky.height);
            enemySpaceShips.forEach(enemySpaceShip -> enemySpaceShip.move(sky.width, sky.height));

            //randomly spawning more enemyships
            Random random = new Random();
            if (random.nextInt(3) == 0) {
                int x = 0; // Always spawn at the top-left corner
                int y = 0; // Always spawn at the top-left corner
                int shipType = random.nextInt(3);
                EnemySpaceShip newEnemy;

                switch (shipType) {
                    case 0:
                        newEnemy = new BattleStar(x, y);
                        break;
                    case 1:
                        newEnemy = new BattleCruiser(x, y);
                        break;
                    default:
                        newEnemy = new BattleShooter(x, y);
                        break;
                }

                enemySpaceShips.add(newEnemy);
            }

            gridPanel.repaint();
            
            //checking the collision with enemy ships and removing them from the game 
            //by adding them to a new arraylist called collidedships
            List<EnemySpaceShip> collidedShips = new ArrayList<>();
            StringBuilder shipNamesBuilder = new StringBuilder();
            for (EnemySpaceShip enemySpaceShip : enemySpaceShips) {
                if (masterSpaceShip.hasCollided(enemySpaceShip)) {
                    collidedShips.add(enemySpaceShip);
                    //getting the name of the ship that was destroyed
                    shipNamesBuilder.append(enemySpaceShip.getShipName()).append(" ");
                }
            }
            //extra feature added to help implement the observer design pattern by adding a score board
            for (EnemySpaceShip enemySpaceShip : collidedShips) {
                if (enemySpaceShip instanceof BattleStar) {
                    score += 1;
                } else if (enemySpaceShip instanceof BattleCruiser) {
                    score += 2;
                } else if (enemySpaceShip instanceof BattleShooter) {
                    score += 3;
                }
                shipsDestroyed++;
            }

            enemySpaceShips.removeAll(collidedShips);
            //updating the score board
            notifyScoreObservers();
            
            //accounting for the collided ships, game mode and saving the high score of a player
            if (collidedShips.size() == 1) {
                JOptionPane.showMessageDialog(frame, shipNamesBuilder.toString() + "destroyed!");
                
            }
            if (collidedShips.size() == 2 && !masterSpaceShip.isDefensiveMode()) {
            	JOptionPane.showMessageDialog(frame, shipNamesBuilder.toString()+ "Two enemy ships destroyed as you are in attack mode!");
            }
            
            // When the game ends, display the total score and ships destroyed
            if (collidedShips.size() >= 2 && masterSpaceShip.isDefensiveMode()) {
                JOptionPane.showMessageDialog(frame, "Master ship destroyed! \n" +
                        "Username: " + username + "\n" +
                        "Total ships destroyed: " + shipsDestroyed + "\n" +
                        "Total score: " + score);
                saveHighScore(username, shipsDestroyed, score);
                
                frame.dispose();
                System.exit(0);
            }
            }
        }
    }
       
    // Methods to read and write high scores to a text file 

    void saveHighScore(String usernameField, int shipsDestroyed, int score) {
        List<String> highScores = getHighScores();
        

        // Check if a high score for the given username already exists
        boolean existingScoreFound = false;
        for (int i = 0; i < highScores.size(); i++) {
            String[] parts = highScores.get(i).split(",");
            if (parts[0].equals(usernameField)) {
                int existingShipsDestroyed = Integer.parseInt(parts[1]);
                int existingScore = Integer.parseInt(parts[2]);
                if (shipsDestroyed > existingShipsDestroyed || (shipsDestroyed == existingShipsDestroyed && score > existingScore)) {
                    // New high score is higher than the existing one, so update the entry in the list
                    highScores.set(i, usernameField + "," + shipsDestroyed + "," + score);
                }
                existingScoreFound = true;
                break;
            }
        }

        // If a high score for the given username does not already exist, add it to the list
        if (!existingScoreFound) {
            highScores.add(usernameField + "," + shipsDestroyed + "," + score);
        }

        // Sort the high scores by total score in descending order
        Collections.sort(highScores, (s1, s2) -> {
            String[] parts1 = s1.split(",");
            String[] parts2 = s2.split(",");
            int score1 = Integer.parseInt(parts1[2]);
            int score2 = Integer.parseInt(parts2[2]);
            return Integer.compare(score2, score1);
        });

        // Write the updated high scores list to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(HIGH_SCORES_FILE))) {
            for (String highScore : highScores) {
                bw.write(highScore);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //getting the high scores and displaying it
    static List<String> getHighScores() {
        try {
            if (!Files.exists(Paths.get(HIGH_SCORES_FILE))) {
                Files.createFile(Paths.get(HIGH_SCORES_FILE));
            }
            return Files.readAllLines(Paths.get(HIGH_SCORES_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    
    
   
    static class GridPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private List<EnemySpaceShip> enemySpaceShips;
        private BufferedImage masterShipImage;
        private BufferedImage enemyShooterImage;
        private BufferedImage enemyCruiserImage;
        private BufferedImage enemyStarImage;

        public void setEnemySpaceShips(List<EnemySpaceShip> enemySpaceShips) {
            this.enemySpaceShips = enemySpaceShips;
        }

        public void loadImages() {
            // Load master spaceship image
            try {
                masterShipImage = ImageIO.read(new File("src/images/master_ship.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Load enemy spaceship images
            try {
                enemyShooterImage = ImageIO.read(new File("src/images/enemy_shooter.png"));
                enemyCruiserImage = ImageIO.read(new File("src/images/enemy_cruiser.png"));
                enemyStarImage = ImageIO.read(new File("src/images/enemy_star.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int cellWidth = getWidth() / sky.width;
            int cellHeight = getHeight() / sky.height;

            for (int i = 0; i < sky.width; i++) {
                for (int j = 0; j < sky.height; j++) {
                    g.drawRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
                }
            }

            if (masterSpaceShip != null && masterShipImage != null) {
                g.drawImage(masterShipImage, masterSpaceShip.x * cellWidth, masterSpaceShip.y * cellHeight, cellWidth, cellHeight, null);
                g.setColor(Color.BLACK);
//                g.drawString("Master", masterSpaceShip.x * cellWidth + 5, masterSpaceShip.y * cellHeight + cellHeight / 2);
            }

            if (enemySpaceShips != null) {
                for (EnemySpaceShip enemySpaceShip : enemySpaceShips) {
                    int shipX = enemySpaceShip.x * cellWidth;
                    int shipY = enemySpaceShip.y * cellHeight;

                    BufferedImage shipImage;

                    // Load the corresponding image based on the ship type
                    switch (enemySpaceShip.getShipName()) {
                        case "BattleShooter":
                            shipImage = enemyShooterImage;
                            break;
                        case "BattleCruiser":
                            shipImage = enemyCruiserImage;
                            break;
                        case "BattleStar":
                            shipImage = enemyStarImage;
                            break;
                        default:
                            // Use a default image or handle the case as needed
                            shipImage = null;
                            break;
                    }

                    if (shipImage != null) {
                        g.drawImage(shipImage, shipX, shipY, cellWidth, cellHeight, null);
                        g.setColor(Color.BLACK);
//                        g.drawString(enemySpaceShip.getShipType(), shipX + 5, shipY + cellHeight / 2);
                    }
                }
            }
        }
    
    }
    
//    
//    //grid panel class that displays the ships on the grid using Graphics to colour in the ships
//    static class GridPanel extends JPanel {
//    	
//    	
//    	
//    	
//		private static final long serialVersionUID = 1L;
//		
//		private List<EnemySpaceShip> enemySpaceShips;
//		
//				
//    	public void setEnemySpaceShips(List<EnemySpaceShip> enemySpaceShips) {
//            this.enemySpaceShips = enemySpaceShips;
//        }
//    	
//    	
//    	
//    	
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//
//            int cellWidth = getWidth() / sky.width;
//            int cellHeight = getHeight() / sky.height;
//
//            for (int i = 0; i < sky.width; i++) {
//                for (int j = 0; j < sky.height; j++) {
//                    g.drawRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
//                }
//            }
//
//            if (masterSpaceShip != null) {
//                g.setColor(Color.GREEN);
//                g.fillRect(masterSpaceShip.x * cellWidth, masterSpaceShip.y * cellHeight, cellWidth, cellHeight);
//                g.setColor(Color.BLACK);
//                g.drawString("Master", masterSpaceShip.x * cellWidth + 5, masterSpaceShip.y * cellHeight + cellHeight / 2);
//            }
//
//            if (enemySpaceShips != null) {
//                for (EnemySpaceShip enemySpaceShip : enemySpaceShips) {
//                    g.setColor(Color.RED);
//                    g.fillRect(enemySpaceShip.x * cellWidth, enemySpaceShip.y * cellHeight, cellWidth, cellHeight);
//                    g.setColor(Color.BLACK);
//                    g.drawString(enemySpaceShip.getClass().getSimpleName(), enemySpaceShip.x * cellWidth + 5, enemySpaceShip.y * cellHeight + cellHeight / 2);
//                }
//             }
//        }
//    }


    
	public void dispose() {
				
	}

	//setting the boolean for the start game
	public void setVisible(boolean b) {
				
	}
}
