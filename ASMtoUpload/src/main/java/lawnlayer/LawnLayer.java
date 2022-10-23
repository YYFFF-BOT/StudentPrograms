package lawnlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class LawnLayer {
	
	private IDrawer drawer;
	private ArrayList<JSONObject> leveList=new ArrayList<>();
	private int lives;
	private int levelIndex=0;
	private ArrayList<Enymy> enymies=new ArrayList<>();
	private Route route=new Route(this);
	private boolean win;
	
	private Buff buff=new Buff(this);
	
	private Player player;
	
	private Grid grid;
	
	private double goal;
	
	public LawnLayer(IDrawer drawer) {
		this.drawer=drawer;
		load();
		loadLevel();
		player=new Player(this, new Cell(0, 0));
		
	}
	
	public Route getRoute() {
		return route;
	}
	
	public ArrayList<Enymy> getEnymies() {
		return enymies;
	}
	
	public int getLives() {
		return lives;
	}
	/**
	 * Continue to next level
	 */
	private void nextLevel() {
		if (levelIndex<leveList.size()-1) {
			player.setCell(new Cell(0, 0));
			levelIndex++;
			loadLevel();
		}
	}
	/**
	 * load enemies
	 * @param jsonArray
	 */
	private void loadEnymies(JSONArray jsonArray) {
		enymies.clear();
		for (int i = 0; i <jsonArray.size(); i++) {
			JSONObject jsonObject=jsonArray.getJSONObject(i);
			int type=jsonObject.getInt("type");
			String string=jsonObject.getString("spawn");
			Cell cell=null;
			if (string.equals("random")) {
				Random random=new Random();
				ArrayList<Cell> cells=grid.getLandCells();
				cell=cells.get(random.nextInt(cells.size()));
			}
			if (type==0) {
				enymies.add(new Worm(this, cell));
			}else if (type==1) {
				enymies.add(new Beetle(this, cell));
			}
		}
		
	}
	/**
	 * load current level
	 */
	private void loadLevel() {
		JSONObject jsonObject=leveList.get(levelIndex);
		grid=new Grid(this);
		grid.loadMap(jsonObject.getString("outlay"));
		loadEnymies(jsonObject.getJSONArray("enemies"));
		goal=jsonObject.getDouble("goal");
		
	}
	/**
	 * load config file
	 */
	private void load() {
		JSONObject jsonObject=PApplet.loadJSONObject(new File("config.json"));
		JSONArray jsonArray=jsonObject.getJSONArray("levels");
		lives=jsonObject.getInt("lives");
		for (int i = 0; i < jsonArray.size(); i++) {
			leveList.add(jsonArray.getJSONObject(i));
		}
	}
	/*
	* collision with route
	*/
   private void routeCollision() {
		route.collision(player);
		for (Enymy enymy : enymies) {
			route.collision(enymy);
		}
		
   }
	/**
	 * update enymies
	 */
	private void updateEnymies() {
		for (Enymy enymy : enymies) {
			enymy.update();
		}
		enymiesCollision();
		routeCollision();
		
	}
	/**
	 * player lose a live
	 */
	public void playerLoseLive() {
		lives--;
		if (lives>0) {
			player.setLive(true);
			player.setCell(new Cell(0, 0));
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	/**
	 * collision about enymies
	 */
	private void enymiesCollision() {
		ArrayList<Wall> walls=grid.getWalls();
		ArrayList<Grass> grasses=grid.getGrasses();
		ArrayList<Grass> arrayList=new ArrayList<>();
		for (Enymy enymy : enymies) {
			for (Wall wall : walls) {
				enymy.collision(wall);
			}
			for (Grass grass : grasses) {
				if(enymy.collision(grass)&&(enymy instanceof Beetle)) {
					arrayList.add(grass);
				}
			}
		}
		for (Grass grass : arrayList) {
			grid.remove(grass);
		}
		
	}
	/**
	 * draw enemies
	 */
	private void renderEnymies() {
		for (Enymy enymy : enymies) {
			enymy.render();
		}
	}
	/**
	 * has enemy in cells?
	 * @param cells
	 * @return
	 */
	public boolean hasEnymy(ArrayList<Cell> cells) {
		for (Cell cell : cells) {
			for (Enymy enymy:enymies) {
				if (enymy.getCell().equals(cell)) {
					return true;
				}
			}
		}
		return false;
		
	}
	/**
	 * game update
	 */
	public void update() {
		if (grid.grassRate()>=goal) {
			if (levelIndex==leveList.size()-1) {
				win=true;
			}else {
				nextLevel();
			}
			
		}
		if (win||lives<=0) {
			return;
		}
		updateEnymies();
		player.update();
		route.update();
		buff.update();
		buff.collision(player);
	}
	/**
	 * game draw
	 */
	public void render() {
		grid.render();
		status();
		renderEnymies();
		route.render();
		player.render();
		buff.render();
		
	}
	/**
	 * game status
	 */
	private void status() {
		if (drawer==null) {
			return;
		}
		if (win) {
			drawer.draw("Win", 500,500);
		}
		if (lives<=0) {
			drawer.draw("Lose", 500,500);
		}
		if (player.getSpeedUp()>0) {
			drawer.draw(String.format("SpeedUp:%ds",player.getSpeedUp()/60), 200,40);
		}
		drawer.draw(String.format("lives:%d",lives), 0,40);
		drawer.draw(String.format("%%%d/%%%d",(int)(grid.grassRate()*100),(int)(goal*100)), 500,40);
		drawer.draw(String.format("level:%d", levelIndex+1), 1100, 60);
	}
	
	public IDrawer getDrawer() {
		return drawer;
	}
	
	public void up() {
		player.up();
	}
	
	public void down() {
		player.down();
	}
	
	public void left() {
		player.left();
	}
	
	public void right() {
		player.right();
	}
	
	public void keyReleased() {
		player.keyReleased();
	}
	
	public Grid getGrid() {
		return grid;
	}
}
