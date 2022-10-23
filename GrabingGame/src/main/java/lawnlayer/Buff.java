package lawnlayer;

import java.util.ArrayList;
import java.util.Random;

public class Buff extends GameObject {

	private boolean has=false;
	/**
	 * Buff will be presented in 600 frames (10seconds)
	 * @param lawnLayer
	 */ 
	private int span=600;
	public Buff(LawnLayer lawnLayer) {
		super(lawnLayer);
	}

	/**
	 * Update buff after 600 frames
	 */
	@Override
	public void update() {
		if (!has&&span>0) {
			span--;
			/**
			 * randomly generate the buff cell
			 */
			if (span==0) {
				has=true;
				span=600;
				ArrayList<Cell> arrayList=lawnLayer.getGrid().getLandCells();
				Random random=new Random();
				setCell(arrayList.get(random.nextInt(arrayList.size())));
				has=true;
			}
		}
	}
	
	/**
	 * Draw the buff on the map as yellow cell
	 */
	@Override
	public void render() {
		if (!has) {
			return;
		}
		lawnLayer.getDrawer().draw(0xffffff00, (int)position.getX(), (int)position.getY());
	}
	
	/**
	 * If play collised with buff, player's speed increse 
	 */ 
	@Override
	public boolean collision(GameObject gameObject) {
		if (!has) {
			return false;
		}
		boolean flag=super.collision(gameObject);
		if (flag) {
			if (gameObject instanceof Player) {
				Player player=(Player)gameObject;
				player.speedPowerUp();
			}
			has=false;
		}
		return flag;
	}

	

}
