package lawnlayer;

public class Player extends GameObject {

	
	private boolean pressed;
	private boolean live=true;
	private int speedUp=0;
	public Player(LawnLayer lawnLayer,Cell cell) {
		super(lawnLayer);
		this.cell=cell;
		position=cell.getV2();
		type="player";
		
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	@Override
	public void update() {
		/**
		 * Player wont continue moveing when on the wall
		 */
		if (!lawnLayer.getGrid().inGrid(position.add(v.getX(), v.getY()))||!live) {
			return;
		}
		
		/**
		 * Timer for the powerup
		 */
		if (speedUp>0) {
			speedUp--;
				v.setX(v.getX()/2);
				v.setY(v.getY()/2);
			}
		
		/**
		 * If player in the grip, continue moving, generate route cell 
		 */
		if (lawnLayer.getGrid().inLand(position)||pressed&&!lawnLayer.getGrid().inLand(position)||
				!lawnLayer.getGrid().inLand(position)&&!cell.getV2().equals(position)) {
			super.update();
			if (lawnLayer.getGrid().inLand(position)) {
				lawnLayer.getRoute().addRCell(position.getCell());
			}
		}
		
		/**
		 * If route closed, fill the surrunded area
		 */
		if (!lawnLayer.getGrid().inLand(position)&&lawnLayer.getRoute().getrCells().size()>0) {
			lawnLayer.getGrid().fill(lawnLayer.getRoute().getrCells());
		}
	}
	
	public int getSpeedUp() {
		return speedUp;
	}
	/**
	 * speed up
	 */
	public void speedPowerUp() {
		v.setX(v.getX()*2);
		v.setY(v.getY()*2);
		speedUp=600;
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}
	/**
	 * press up key
	 */
	public void up() {
		pressed=true;
		
		v.setXY(0, -v.getLen());
	}
	/**
	 * press down key
	 */
	public void down() {
		pressed=true;
		
		v.setXY(0, v.getLen());
	}
	/**
	 * press left key
	 */
	public void left() {
		pressed=true;
		
		v.setXY(-v.getLen(), 0);
	}
	/**
	 * press right key
	 */
	public void right() {
		pressed=true;
		
		v.setXY(v.getLen(), 0);
	}
	/**
	 * key release
	 */
	public void keyReleased() {
		pressed=false;
		
		position.setXY(Math.round(position.getX()/20)*20, Math.round(position.getY()/20)*20);
	}

}
