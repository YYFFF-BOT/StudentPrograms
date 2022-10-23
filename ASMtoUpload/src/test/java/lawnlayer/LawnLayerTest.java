package lawnlayer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LawnLayerTest {

	LawnLayer lawnLayer;
	@BeforeEach
	void setUp() throws Exception {
		lawnLayer=new LawnLayer(null);
	}

	@Test
	void testLawnLayer() {
		
	}

	/**
	 * Test the route 
	 */
	@Test
	void testGetRoute() {
		Route route=lawnLayer.getRoute();
		assertEquals(route.getrCells().size(), 0);
	}

	/**
	 * Test the number of lives
	 */ 
	 
	@Test
	void testPlayerLoseLive() {
		lawnLayer.playerLoseLive();
		assertEquals(lawnLayer.getLives(),2);
	}

	/**
	 * Test the current position of player
	 */
	@Test
	void testGetPlayer() {
		Player player=lawnLayer.getPlayer();
		assertEquals(player.position.getX(), 0);
	}

	/**
	 * Test whether there is enemy in the grid
	 */ 
	@Test
	void testHasEnymy() {
		assertTrue(lawnLayer.hasEnymy(lawnLayer.getGrid().getLandCells()));
	}

	/**
	 * Test the postion of enemies after 200 frams
	 */
	@Test
	void testUpdate() {
		double x= lawnLayer.getEnymies().get(0).position.getX();
		boolean flag=false;
		for (int i = 0; i <200; i++) {
			lawnLayer.update();
			double x1= lawnLayer.getEnymies().get(0).position.getX();
			if (x!=x1) {
				flag=true;
			}
		}
		
		assertTrue(flag);
	}

	@Test
	void testRender() {
		for (int i = 0; i <100; i++) {
			lawnLayer.render();
		}
	}

	@Test
	void testGetDrawer() {
		assertNull(lawnLayer.getDrawer());
	}

	/**
	 * The following 5 tests test the move keys 
	 */ 
	@Test
	void testUp() {
		assertEquals(lawnLayer.getPlayer().position.getY(), 80);
		for (int i = 0; i < 10; i++) {
			lawnLayer.up();
			lawnLayer.update();
		}
		assertEquals(lawnLayer.getPlayer().position.getY(), 80);
	}

	@Test
	void testDown() {
		assertEquals(lawnLayer.getPlayer().position.getY(), 80);
		for (int i = 0; i < 10; i++) {
			lawnLayer.down();;
			lawnLayer.update();
		}
		assertEquals(lawnLayer.getPlayer().position.getY(), 100);
	}

	@Test
	void testLeft() {
		assertEquals(lawnLayer.getPlayer().position.getX(), 0);
		for (int i = 0; i < 10; i++) {
			lawnLayer.left();
			lawnLayer.update();
		}
		assertEquals(lawnLayer.getPlayer().position.getX(), 0);
	}

	@Test
	void testRight() {
		assertEquals(lawnLayer.getPlayer().position.getX(), 0);
		for (int i = 0; i < 10; i++) {
			lawnLayer.right();
			lawnLayer.update();
		}
		assertEquals(lawnLayer.getPlayer().position.getX(), 20);
	}

	@Test
	void testKeyReleased() {
		lawnLayer.up();
		assertTrue(lawnLayer.getPlayer().isPressed());
		lawnLayer.keyReleased();
		assertFalse(lawnLayer.getPlayer().isPressed());
	}

	@Test
	/** 
	 * Test the numebr of grid cells
	 */
	void testGetGrid() {
		Grid grid=lawnLayer.getGrid();
		assertEquals(grid.getLandCells().size(), 1860);
	}

}
