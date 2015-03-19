package tests.world.components;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import world.components.TokenType;
import world.game.TokenList;

/**
 * Tests for the TokenList class
 * @author Kalo Pilato - ID: 300313803
 *
 */
public class TokenListTests {

	/**
	 * A newly created TokenList should be of size 5
	 */
	@Test public void testConstructor1(){
		TokenList tokens = new TokenList(TokenType.CUBE);
		assertTrue(tokens.size() == 5);
	}
	
	/**
	 * A newly created TokenList should contain only GameTokens of the same type
	 */
	@Test public void testConstructor2(){
		TokenList tokens = new TokenList(TokenType.CUBE);
		boolean allMatch = true;
		for(int i = 0; i < tokens.size(); i++){
			if(!tokens.get(i).getType().equals(TokenType.CUBE)) allMatch = false;
		}
		assertTrue(allMatch);
	}
	
	/**
	 * A newly created TokenList should contain no equal Tokens
	 */
	@Test public void testConstructor3(){
		TokenList tokens = new TokenList(TokenType.CUBE);
		boolean tokensMatch = false;
		for(int i = 0; i < tokens.size() - 1; i++){
			for(int j = i + 1; j < tokens.size(); j++){
				if(tokens.get(i).equals(tokens.get(j))) tokensMatch = true;
			}
		}
		assertTrue(!tokensMatch);
	}
}
