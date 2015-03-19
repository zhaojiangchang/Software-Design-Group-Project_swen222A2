package tests.world.components;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import world.components.GameToken;
import world.components.TokenType;

/**
 * Tests for the GameToken class
 * @author Kalo Pilato - ID:300313803
 *
 */
public class GameTokenTests {
	
	/**
	 * The type returned by a newly created GameToken should be equal to the type it was created with
	 */
	@Test public void testValidConstructor1(){
		GameToken testToken = new GameToken(TokenType.CUBE, Color.BLUE);
		assertTrue(testToken.getType().equals(TokenType.CUBE));
	}
	
	/**
	 * The Color returned by a newly created GameToken should be equal to the Color it was created with
	 */
	@Test public void testValidConstructor2(){
		GameToken testToken = new GameToken(TokenType.CUBE, Color.BLUE);
		assertTrue(testToken.getColor().equals(Color.BLUE));
	}
	
	/**
	 * The found state of a newly created GameToken should be false by default
	 */
	@Test public void testValidConstructor3(){
		GameToken testToken = new GameToken(TokenType.CUBE, Color.BLUE);
		assertTrue(!testToken.isFound());
	}
	
	/**
	 * The found state of a Game Token should be true once set to true
	 */
	@Test public void testFound1(){
		GameToken testToken = new GameToken(TokenType.CUBE, Color.BLUE);
		testToken.setFound(true);
		assertTrue(testToken.isFound());
	}
	
	/**
	 * GameTokens of the same type and Color should be equal
	 */
	@Test public void testValidEquals1(){
		GameToken testToken = new GameToken(TokenType.CUBE, Color.BLUE);
		assertTrue(testToken.equals(new GameToken(TokenType.CUBE, Color.BLUE)));
	}
	
	/**
	 * GameTokens of the same type and Color should be equal, even if they have different "found" states
	 */
	@Test public void testValidEquals2(){
		GameToken testToken1 = new GameToken(TokenType.CUBE, Color.BLUE);
		testToken1.setFound(false);
		GameToken testToken2 = new GameToken(TokenType.CUBE, Color.BLUE);
		testToken2.setFound(true);
		assertTrue(testToken1.equals(testToken2));
	}
	
	/**
	 * GameTokens with a different type should not be equal
	 */
	@Test public void testInvalidEquals1(){
		GameToken testToken = new GameToken(TokenType.CUBE, Color.BLUE);
		assertTrue(!testToken.equals(new GameToken(TokenType.BALL, Color.BLUE)));
	}
	
	/**
	 * GameTokens with a different Color should not be equal
	 */
	@Test public void testInvalidEquals2(){
		GameToken testToken = new GameToken(TokenType.CUBE, Color.BLUE);
		assertTrue(!testToken.equals(new GameToken(TokenType.CUBE, Color.BLACK)));
	}
	
}
