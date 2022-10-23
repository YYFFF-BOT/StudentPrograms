package lawnlayer;

public class GameObject {
	
	protected String type;
	protected LawnLayer lawnLayer;
	protected V2 position;
	protected Cell cell;
	protected V2 v;
	
	public GameObject(LawnLayer lawnLayer) {
		this.lawnLayer=lawnLayer;
		v=new V2(0, 0);
	}
	
	public void update() {
		position.add(v);
		cell=position.getCell();
	}
	/**
	 * draw game object
	 */
	public void render() {
		if (lawnLayer.getDrawer()==null) {
			return;
		}
		lawnLayer.getDrawer().draw(type, (int)position.getX(), (int)position.getY());
	}
	
	public Cell getCell() {
		return cell;
	}
	
	public void setCell(Cell cell) {
		this.cell = cell;
		position=cell.getV2();
	}
	/**
	 * game object collision
	 * @param gameObject
	 * @return
	 */
	public boolean collision(GameObject gameObject) {
		
		return gameObject.position.intersects(position);
		
	}

}
