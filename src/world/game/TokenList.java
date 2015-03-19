package world.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import world.ColourPalette;
import world.components.GameToken;
import world.components.TokenType;

/**
 * The list of GameTokens a Player is required to collect to win the game
 * @author Kalo Pilato - ID: 300313803
 *
 */
public class TokenList implements java.io.Serializable{
	
	private final List<GameToken> tokens;

	// Defines the number of tokens to be collected by each Player
	private final int tokenCount = 5;
	private int totalFound = 0;

	
	/**
	 * Constructor - creates a new TokenList, populating it with new GameTokens of a given type (Colors are assigned from the predefined COLORS array) 
	 * @param type the type of GameTokens to be used in this TokenList
	 */
	public TokenList(TokenType type){
		tokens = new ArrayList<GameToken>();
		for(int i = 0; i < tokenCount; i++){
			tokens.add(new GameToken(type, ColourPalette.get(i)));
		}
	}
	
	/**
	 * Returns the specified GameToken from this list
	 * @param token the GameToken to return
	 * @return the specified GameToken
	 */
	public GameToken get(GameToken token){
		return tokens.get(tokens.indexOf(token));
	}
	
	/**
	 * Returns the GameToken at a given index position from this list
	 * @param index the index position of the GameToken to return
	 * @return the GameToken at index
	 */
	public GameToken get(int index){
		return tokens.get(index);
	}
	
	/**
	 * Returns the size of this TokenList
	 * @return the size of this TokenList
	 */
	public int size(){
		return tokens.size();
	}
	
	/**
	 * Sets the found state of a GameToken in this TokenList to true. 
	 * @param token the GameToken to set found
	 * @return true if successfully set - returns false if the GameToken does not exist in this list
	 */
	public boolean tokenFound(GameToken token){
		if(tokens.contains(token)){
			token.setFound(true);
			totalFound++;
			return true;
		}
		return false;
	}
	
	/**
	 * Determines whether a Player has collected all of the GameTokens in their TokenList
	 * @return true if the total collected is equal to the total to collect
	 */
	public boolean collectedAll(){
		return totalFound == tokenCount;
	}
}
