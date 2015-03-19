package tests.world.components;
import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import world.components.Torch;

/**
 * Tests for the Torch class
 * @author Kalo Pilato - ID:300313803
 *
 */
public class TorchTests {

	/**
	 * A newly constructed Torch should not be lit
	 */
	@Test public void validConstructor1(){
		Torch t = new Torch();
		assertTrue(!t.isLit());
	}
	
	/**
	 * A Torch should be lit after being turned on
	 */
	@Test public void validLit(){
		Torch t = new Torch();
		t.turnOn();
		assertTrue(t.isLit());
	}
	
	/**
	 * A Torch should not be lit after being turned off
	 */
	@Test public void validUnlit(){
		Torch t = new Torch();
		t.turnOn();
		t.turnOff();
		assertTrue(!t.isLit());
	}
}
