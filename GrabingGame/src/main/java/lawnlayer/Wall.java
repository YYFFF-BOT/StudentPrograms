package lawnlayer;

public class Wall extends GameObject {

	public Wall(LawnLayer lawnLayer,Cell cell) {
		super(lawnLayer);
		type="wall";
		this.cell=cell;
		position=cell.getV2();
	}
	

	

}
