package lawnlayer;

import java.awt.Rectangle;
/**
 * vector
 * @author Administrator
 *
 */
public class V2 {
	
	private double x;
	private double y;
	
	public V2(double x,double y) {
		this.x=x;
		this.y=y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	/**
	 * vector add
	 * @param x
	 * @param y
	 * @return
	 */
	public V2 add(double x,double y) {
		return new V2(x+this.x, y+this.y);
	}
	
	public void add(V2 v2) {
		x+=v2.x;
		y+=v2.y;
	}
	
	public void setXY(double x,double y) {
		this.x=x;
		this.y=y;
	}
	/**
	 * length of vector
	 * @return
	 */
	public double getLen() {
		if (Math.abs(x)>0){
			return Math.abs(x);
		}
		if (Math.abs(y)>0){
			return Math.abs(y);
		}
		return 2;
	}
	
	/**
	 * Check whether two vectors equel 
	 */
	@Override
	public boolean equals(Object obj) {
		V2 v2=(V2)obj;
		return v2.x==x&&v2.y==y;
	}
	/**
	 * Get cell by position
	 * @return
	 */
	public Cell getCell() {
		return new Cell((int)(y-70)/20, (int)(x+10)/20);
	}
	/**
	 * Collision check
	 * @param v2
	 * @return
	 */
	public boolean intersects(V2 v2) {
		Rectangle rectangle=new Rectangle((int)x, (int)y, 20, 20);
		Rectangle rectangle2=new Rectangle((int)v2.x, (int)v2.y, 20, 20);
		return rectangle.intersects(rectangle2);
	}

}
