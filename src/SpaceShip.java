import java.io.Serializable;

public abstract class SpaceShip implements Serializable {
    
	private static final long serialVersionUID = 1L;
	int x;
    int y;

    public SpaceShip(int x, int y) {
        this.x = x;
        this.y = y;
    }
    //moving the ships in the game
    public abstract void move(int maxX, int maxY);
    
    //checking if ships have collided 
    public boolean hasCollided(SpaceShip other) {
        return this.x == other.x && this.y == other.y;
    }
    
}
