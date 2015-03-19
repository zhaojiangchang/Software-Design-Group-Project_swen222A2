package tests.world.components;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Test;

import world.components.CellType;
import world.components.Direction;
import world.components.Furniture;

/**
 * Tests for the Furniture class - simple testing only, would require extensive testing for all cases
 * @author Kalo Pilato - ID: 300313803
 *
 */
public class FurnitureTests {

	/**
	 * Newly constructed Furniture should be of the CellType it was created with
	 */
	@Test public void validConstructor1(){
		CellType type = CellType.COUCH;
		Furniture f = new Furniture(type, new Point(2, 2), Direction.NORTH);
		assertTrue(type.equals(f.getType()));
	}
	
	/**
	 * Newly constructed Furniture should be facing the Direction it was created with
	 */
	@Test public void validConstructor2(){
		Direction d = Direction.NORTH;
		Furniture f = new Furniture(CellType.COUCH, new Point(2, 2), d);
		assertTrue(d.equals(f.getFacing()));
	}
	
	/**
	 * A Newly constructed Couch should occupy two points
	 */
	@Test public void validPoints1(){
		Furniture f = new Furniture(CellType.COUCH, new Point(2, 2), Direction.NORTH);
		assertTrue(f.getPoints().size() == 2);
	}
	
	/**
	 * A Newly constructed Bed should occupy six points
	 */
	@Test public void validPoints2(){
		Furniture f = new Furniture(CellType.BED, new Point(2, 2), Direction.NORTH);
		assertTrue(f.getPoints().size() == 6);
	}
	
	/**
	 * A Newly constructed Couch should occupy two points
	 */
	@Test public void validPoints3(){
		Furniture f = new Furniture(CellType.TABLE, new Point(2, 2), Direction.NORTH);
		assertTrue(f.getPoints().size() == 2);
	}
	
	/**
	 * Checks a North facing Couch is created with the correct Points
	 */
	@Test public void northFacingPoints1(){
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(new Point(2, 2));
		points.add(new Point(3, 2));
		Furniture f = new Furniture(CellType.COUCH, new Point(2, 2), Direction.NORTH);
		assertTrue(points.equals(f.getPoints()));
	}
	
	/**
	 * Checks a North facing Bed is created with the correct Points
	 */
	@Test public void northFacingPoints2(){
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(new Point(2, 2));
		points.add(new Point(3, 2));
		points.add(new Point(4, 2));
		points.add(new Point(2, 3));
		points.add(new Point(3, 3));
		points.add(new Point(4, 3));
		Furniture f = new Furniture(CellType.BED, new Point(2, 2), Direction.NORTH);
		assertTrue(points.equals(f.getPoints()));
	}
	
	/**
	 * Checks an East facing Couch is created with the correct Points
	 */
	@Test public void eastFacingPoints1(){
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(new Point(2, 2));
		points.add(new Point(2, 3));
		Furniture f = new Furniture(CellType.COUCH, new Point(2, 2), Direction.EAST);
		assertTrue(points.equals(f.getPoints()));
	}
	
	/**
	 * Checks an East facing Bed is created with the correct Points
	 */
	@Test public void eastFacingPoints2(){
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(new Point(2, 2));
		points.add(new Point(3, 2));
		points.add(new Point(2, 3));
		points.add(new Point(3, 3));
		points.add(new Point(2, 4));
		points.add(new Point(3, 4));
		Furniture f = new Furniture(CellType.BED, new Point(2, 2), Direction.EAST);
		assertTrue(points.equals(f.getPoints()));
	}
}
