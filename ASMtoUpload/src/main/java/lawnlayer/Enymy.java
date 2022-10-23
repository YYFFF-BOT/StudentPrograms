package lawnlayer;

public class Enymy extends GameObject {

	public Enymy(LawnLayer lawnLayer,Cell cell) {
		super(lawnLayer);
		this.cell=cell;
		position=cell.getV2();
		v=new V2(Math.random()>0.5?2:-2, Math.random()>0.5?2:-2);
	}
	
	/**
	 * Check whether collision with other objects
	 */
	@Override
	public boolean collision(GameObject gameObject) {
		if (position.add(v.getX(), 0).intersects(gameObject.position)) {
			v.setX(-v.getX());
			return true;
		}else if (position.add(0, v.getY()).intersects(gameObject.position)) {
			v.setY(-v.getY());
			return true;
		}
		return false;
	}
	

}
