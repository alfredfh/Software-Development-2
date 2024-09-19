
//class extending the enemyspaceship
public class BattleCruiser extends EnemySpaceShip {
    
	private static final long serialVersionUID = 1L;

	public BattleCruiser(int x, int y) {
        super(x, y);
    }
	@Override
    public String getShipName() {
        return "BattleCruiser";
    }
}
