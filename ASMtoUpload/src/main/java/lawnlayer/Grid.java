package lawnlayer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 * The grid cell in the map
 * @author Administrator
 *
 */
public class Grid extends GameObject{

	public Grid(LawnLayer lawnLayer) {
		super(lawnLayer);
	}
	/**
	 * all walls
	 */
	private ArrayList<Wall> walls=new ArrayList<>();
	/**
	 * all grasses
	 */
	private ArrayList<Grass> grasses=new ArrayList<>();
	/**
	 * land cells is the cells that are not grid
	 */
	private ArrayList<Cell> landCells=new ArrayList<>();
	
	/**
	 * Draw wall and grass
	 */
	@Override
	public void render() {
		for (Wall wall : walls) {
			wall.render();
		}
		for (Grass grass : grasses) {
			grass.render();
		}
		
	}
	/**
	 * Add grass to the grasses list
	 * @param grass
	 */
	private void add(Grass grass) {
		landCells.remove(grass.getCell());
		grasses.add(grass);
	}
	/**
	 * Delete a grass
	 * @param grass
	 */
	public void remove(Grass grass) {
		landCells.add(grass.getCell());
		grasses.remove(grass);
	}
	/**
	 * Check whether the object is in the grid
	 * @param v2
	 * @return
	 */
	public boolean inGrid(V2 v2) {
		return v2.getX()>=0&&v2.getX()<=App.WIDTH-20&&
				v2.getY()>=80&&v2.getY()<=App.HEIGHT-20;
	}
	/**
	 * Check whether the object is in the grass or wall
	 * @param v2
	 * @return
	 */
	public boolean inLand(V2 v2) {
		
		Cell cell=v2.getCell();
		for (Grass grass : grasses) {
			if (grass.getCell().equals(cell)) {
				return false;
			}
		}
		
		for (Wall wall:walls) {
			if (wall.getCell().equals(cell)) {
				return false;
			}
		}
		
		return true;
		
	}
	/**
	 * This is a recursion function to find all the grid cells from the startcell and add these cells to arraylist
	 * This function wont add the cells that is grasses or wall or already been added to the arraylist
	 * @param cells
	 * @param startCell
	 */
	public void travelCell(ArrayList<Cell> cells,Cell startCell) {
		if (!landCells.contains(startCell)) {
			return;
		}
		cells.add(startCell);
		
		landCells.remove(startCell);
		for (Cell cell : startCell.neibor()) {
			
			travelCell(cells, cell);
		}
	}
	/**
	 * Fill the cells which are surrounded
	 * @param arrayList
	 */
	public void fill(ArrayList<RCell> arrayList) {
		/**
		 * Fistly update all route cells to grasses
		 */ 
		for (RCell rCell : arrayList) {
			add(new Grass(lawnLayer, rCell.cell));
		}
		/** Search all the cells from the route cells
		 *  If the route has surrened an area, there must be two areas surrened bt the route.
		 * */ 
		ArrayList< ArrayList<Cell>> cellLists=new ArrayList<>();
		for (RCell rCell : arrayList) {
			
			ArrayList<Cell> neibor=rCell.getCell().neibor();
			for (Cell cell:neibor) {
				
				ArrayList<Cell> cells=new ArrayList<>();
				travelCell(cells, cell);
				
				if (cells.size()>0) {
					cellLists.add(cells);
					if (cellLists.size()>=2) {
						break;
					}
				}
			}
			if (cellLists.size()>=2) {
				break;
			}
		}
		/**
		 * If there are two areas, Determine which area is smaller, updata this area to grass 
		*/ 
		if (cellLists.size()>=2) {
			ArrayList<Cell> cells=cellLists.get(0);
			if (cellLists.get(0).size()>cellLists.get(1).size()) {
				landCells.addAll(cells);
				cells=cellLists.get(1);
			}else {
				landCells.addAll(cellLists.get(1));
			}
			if (!lawnLayer.hasEnymy(cells)) {
				for (Cell cell : cells) {
					add(new Grass(lawnLayer, cell));
				}
			}else {
				landCells.addAll(cells);
			}
		/**
		 *If the route only surrend one area in some edge cases, update this area to grass  
		 */ 
		}else {
			if (cellLists.size()>0) {
				landCells.addAll(cellLists.get(0));
			}
			
		}
		/**
		 * clean the routecells to be ready for next determination  
		 */ 
		arrayList.clear();
	}
	/**
	 * load map from file
	 * @param file
	 */
	public void loadMap(String file) {
		try {
			BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
			String line;
			int row=0;
			while ((line=bufferedReader.readLine())!=null) {
				for (int i = 0; i <line.length(); i++) {
					char ch=line.charAt(i);
					if (ch=='X') {
						walls.add(new Wall(lawnLayer, new Cell(row, i)));
					}else {
						landCells.add(new Cell(row, i));
					}
					
				}
				row++;
				
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public ArrayList<Grass> getGrasses() {
		return grasses;
	}
	
	public ArrayList<Wall> getWalls() {
		return walls;
	}
	
	public ArrayList<Cell> getLandCells() {
		return landCells;
	}
	
	/**
	 * Get the rate of current game process, the persantage of grass area
	 * @return
	 */
	public double grassRate() {
		return 1.0*grasses.size()/(grasses.size()+landCells.size());
	}

}
