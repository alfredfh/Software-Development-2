
//class extending the enemyspaceship
public class BattleStar extends EnemySpaceShip{
    	
	private static final long serialVersionUID = 1L;

	public BattleStar(int x, int y) {
        super(x, y);
    }
	
	@Override
    public String getShipName() {
        return "BattleStar";
    }
}