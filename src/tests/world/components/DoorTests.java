package tests.world.components;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import world.components.Door;
import world.components.Key;

/**
 * Tests for the GameToken class
 * @author Kalo Pilato - ID:300313803
 *
 */
public class DoorTests {

	/**
	 * A Door that is created as not lockable should have state isLockable() = false
	 */
	@Test public void testValidDoor1(){
		Door d = new Door(false);
		assertTrue(d.isLockable() == false);
	}
	
	/**
	 * A Door that is created as not lockable should not have a Key
	 */
	@Test public void testValidDoor2(){
		Door d = new Door(false);
		assertTrue(d.getKey() == null);
	}
	
	/**
	 * A Door that is created as not lockable should not be set to locked
	 */
	@Test public void testInValidLocked(){
		Door d = new Door(false);
		assertTrue(!d.setLocked(true));
	}
	
	/**
	 * A Door that is not lockable should not allow a Key to be set
	 */
	@Test public void testInvalidSetKey(){
		Door d = new Door(false);
		assertTrue(!d.setKey(new Key("Key", Color.BLACK)));
	}
	
	/**
	 * A Door with should return the same Key as was last set to the Door
	 */
	@Test public void testGetKey(){
		Door d = new Door(true);
		Key key = new Key("Key", Color.BLACK);
		d.setKey(key);
		assert(d.getKey().equals(key));
	}
	
	/**
	 * A Door that is lockable, when set to locked, should return isLocked = true
	 */
	@Test public void testLocked1(){
		Door d = new Door(true);
		d.setLocked(true);
		assert(d.isLocked());
	}
	
	/**
	 * A Door that is not lockable should not be set to locked
	 */
	@Test public void testLocked2(){
		Door d = new Door(false);
		assertTrue(!d.setLocked(true));
	}
}
