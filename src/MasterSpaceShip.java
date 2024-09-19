import java.util.Random;

//mastership extending the spaceship
public class MasterSpaceShip extends SpaceShip {
    
	private static final long serialVersionUID = 1L;
	//this is to set the mode of the mastership in the advanced features
	private boolean defensiveMode;
	
	
	public MasterSpaceShip(int x, int y) {
        super(x, y);
    }
	
	//getter and setter
	 public boolean isDefensiveMode() {
	        return defensiveMode;
	    }
	    
	 public void setDefensiveMode(boolean defensiveMode) {
	        this.defensiveMode = defensiveMode;
	    }
	
	 //how the mastership can move
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
