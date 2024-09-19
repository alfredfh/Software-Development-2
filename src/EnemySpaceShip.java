import java.util.Random;

//abstract class that extends the spaceship class
public abstract class EnemySpaceShip extends SpaceShip {
   
	private static final long serialVersionUID = 1L;


	public EnemySpaceShip(int x, int y) {
        super(x, y);
    }
	
	public abstract String getShipName();
	
	
	//how the enemyspaceships are allowed to move
    @Override
    public void move(int maxX, int maxY) {
        int newX = x;
        int newY = y;
        Random random = new Random();

        while (newX == x && newY == y) {
            newX = Math.min(Math.max(x + random.nextInt(3) - 1, 0), maxX - 1);
            newY = Math.min(Math.max(y + random.nextInt(3) - 1, 0), maxY - 1);
        }

        x = newX;
        y = newY;
    }


}