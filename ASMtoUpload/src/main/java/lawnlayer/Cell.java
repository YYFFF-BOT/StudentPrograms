package lawnlayer;

import java.util.ArrayList;

public class Cell {
	
	private int row;
	private int col;
	
	
	public Cell(int row,int col) {
		this.row=row;
		this.col=col;
		
	}
	
	public int getCol() {
		return col;
	}
	
	public int getRow() {
		return row;
	}
	/**
	 * get position from cell
	 * @return
	 */
	public V2 getV2() {
		return new V2(col*20, row*20+App.TOPBAR);
	}
	/**
	 * Get all the neibors of the cell
	 * @return
	 */
	public ArrayList<Cell> neibor() {
		ArrayList<Cell> arrayList=new ArrayList<>();
		arrayList.add(new Cell(row, col-1));
		arrayList.add(new Cell(row, col+1));
		arrayList.add(new Cell(row-1, col));
		arrayList.add(new Cell(row+1, col));
		return arrayList;
	}
	
	
	/**
	*Check whether two cells equal  
	*/ 
	@Override
	public boolean equals(Object obj) {
		Cell cell=(Cell)obj;
		return cell.row==row&&cell.col==col;
	}
	
	/**
	* Get the position in digital form 
	*/ 
	@Override
	public String toString() {
		return String.format("(%d,%d)", row,col);
	}

}
