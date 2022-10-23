package lawnlayer;

import java.util.ArrayList;

public class Route extends GameObject {

	private ArrayList<RCell> rCells=new ArrayList<>();
	private int counter=0;
	
	public Route(LawnLayer lawnLayer) {
		super(lawnLayer);
	}
	/**
	 * update route
	 */
	@Override
	public void update() {
		if (rCells.size()>0&&counter>0) {
			
			counter--;
			if (counter<=0) {
				rCells.clear();
				lawnLayer.playerLoseLive();
			}
		}
		
		
		
		
	}
	/**
	 * draw route
	 */
	@Override
	public void render() {
		for (RCell rCell : rCells) {
			rCell.render();
		}
	}
	/**
	 * add cell of route
	 * @param cell
	 */
	public void addRCell(Cell cell) {
		if (counter>0) {
			return;
		}
		RCell rCell=new RCell(lawnLayer, cell);
		if (rCells.contains(rCell)) {
			return;
		}
		rCells.add(rCell);
	}
	
	public ArrayList<RCell> getrCells() {
		return rCells;
	}
	/**
	 * collision about route
	 */
	@Override
	public boolean collision(GameObject gameObject) {
		if (counter>0) {
			return false;
		}
		int n=rCells.size();
		if (gameObject instanceof Player) {
			n-=3;
		}
		for (int i = 0; i <n; i++) {
			RCell rCell=rCells.get(i);
			if (rCell.collision(gameObject)) {
				for (int j = 0; j <=i; j++) {
					rCells.get(j).color=0xffff0000;
				}
				counter=15;
				lawnLayer.getPlayer().setLive(false);
				return true;
			}
		}
		
		return false;
		
	}
	
	

}
