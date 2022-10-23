package lawnlayer;

import java.util.TreeMap;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;

public class App extends PApplet {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int TOPBAR = 80;

    public static final int FPS = 60;

    public String configPath;
	
	public PImage grass;
    public PImage concrete;
    public PImage worm;
    public PImage beetle;
    public PImage ball;
    private LawnLayer lawnLayer;
    private TreeMap<String, PImage> treeMap=new TreeMap<>();
    
    /**
    * Import the game states and draw the map
    */ 
    public App() {
        this.configPath = "config.json";
        lawnLayer=new LawnLayer(new IDrawer() {
			
			@Override
			public void draw(int color, int x, int y) {
				fill(color);
				rect(x, y, 20, 20);
			}
			
			@Override
			public void draw(String obj, int x, int y) {
				if (treeMap.containsKey(obj)) {
					image(treeMap.get(obj), x, y);
				}else {
					fill(0xffffffff);
					text(obj, x, y);
				}
				
			}
		});
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);
        textSize(40);

        /**
         * Load images during setup 
        */ 
		this.grass = loadImage(this.getClass().getResource("grass.png").getPath());
        this.concrete = loadImage(this.getClass().getResource("concrete_tile.png").getPath());
        this.worm = loadImage(this.getClass().getResource("worm.png").getPath());
        this.beetle = loadImage(this.getClass().getResource("beetle.png").getPath());
        this.ball = loadImage(this.getClass().getResource("ball.png").getPath());
        
        treeMap.put("grass", grass);
        treeMap.put("wall", concrete);
        treeMap.put("worm", worm);
        treeMap.put("beetle", beetle);
        treeMap.put("player", ball);
    }
	
    /**
     * Draw all elements in the game by current frame. 
     */
    public void draw() {
    	background(100, 60, 0);
    	lawnLayer.update();
    	lawnLayer.render();

    }
    /**
     * Check which key has uses pressed
     */
    @Override
    public void keyPressed(KeyEvent event) {
    	super.keyPressed(event);
    	switch (event.getKeyCode()) {
		case UP:
			lawnLayer.up();
			break;
		case DOWN:
			lawnLayer.down();
			break;
		case LEFT:
			lawnLayer.left();
			break;
		case RIGHT:
			lawnLayer.right();
			break;

		default:
			break;
		}
    }
    
    /**
     * Check whether user releases the key  
     */ 
    @Override
    public void keyReleased() {
    	super.keyReleased();
    	lawnLayer.keyReleased();
    }
    
    
    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
