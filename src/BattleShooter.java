
//class extending the enemyspaceship
public class BattleShooter extends EnemySpaceShip {
    
	private static final long serialVersionUID = 1L;

	public BattleShooter(int x, int y) {
        super(x, y);
    }
	
	 @Override
	    public String getShipName() {
	        return "BattleShooter";
	    }
}
