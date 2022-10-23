package lawnlayer;

public class Grass extends GameObject {
	/**
	 * Initialise the grasse
	 * @param lawnLayer
	 * @param cell
	 */
	public Grass(LawnLayer lawnLayer,Cell cell) {
		super(lawnLayer);
		type="grass";
		this.cell=cell;
		position=cell.getV2();
	}
	
	

}
