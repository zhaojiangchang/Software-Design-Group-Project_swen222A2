package tests.world.components;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import world.components.Key;

/**
 * Tests for the Key class
 * @author Kalo Pilato - ID:300313803
 *
 */
public class KeyTests {

	/**
	 * A newly created Key should return the name it was created with
	 */
	@Test public void validConstructor1(){
		Key key = new Key("test key", Color.BLACK);
		assertTrue(key.getName().equals("test key"));
	}
	
	/**
	 * A newly created Key should return the Color it was created with
	 */
	@Test public void validConstructor2(){
		Key key = new Key("test key", Color.BLACK);
		assertTrue(key.getColor().equals(Color.BLACK));
	}
	
	/**
	 * Two Keys constructed with the same arguments should be equal
	 */
	@Test public void validEquals(){
		Key key = new Key("test key", Color.BLACK);
		Key compare = new Key("test key", Color.BLACK);
		assertTrue(key.equals(compare));
	}
	
	/**
	 * Two Keys constructed with different names should not be equal
	 */
	@Test public void invalidEquals1(){
		Key key = new Key("test key", Color.BLACK);
		Key compare = new Key("test ke", Color.BLACK);
		assertTrue(!key.equals(compare));
	}
	
	/**
	 * Two Keys constructed with different colours should not be equal
	 */
	@Test public void invalidEquals2(){
		Key key = new Key("test key", Color.BLUE);
		Key compare = new Key("test key", Color.BLACK);
		assertTrue(!key.equals(compare));
	}
}
