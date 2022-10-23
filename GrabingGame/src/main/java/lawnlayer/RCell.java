package lawnlayer;
/**
 * route cell
 * @author Administrator
 *
 */
public class RCell extends GameObject {

	protected int color=0xff00ff00;
	public RCell(LawnLayer lawnLayer,Cell cell) {
		super(lawnLayer);
		this.cell=cell;
		position=cell.getV2();
		
	}
	/**
	 * draw route cell
	 */
	@Override
	public void render() {
		lawnLayer.getDrawer().draw(color, (int)position.getX(), (int)position.getY());
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	@Override
	public boolean equals(Object obj) {
		RCell rCell=(RCell)obj;
		return rCell.getCell().equals(cell);
	}

}
