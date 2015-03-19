package world.components;

import java.awt.Color;

/**
 * A collectable GameToken in the game - players are required to collect a given set of GameTokens to win the game
 * GameTokens are considered "equal" if they have the same color and type (the found state is not evaluated)
 * @author Kalo Pilato - ID: 300313803
 *
 */
public class GameToken implements java.io.Serializable, GameObject {

	private boolean found = false;
	private final Color color;
	private final TokenType type;
	
	/**
	 * Constructor - creates a Token with a given type and color, found state is set to false by default
	 * @param type the type of this Token
	 * @param color the Color of this Token
	 */
	public GameToken(TokenType type, Color color){
		this.type = type;
		this.color = color;
	}
	
	/**
	 * Returns the Color of this GameToken
	 * @return the Color of this GameToken
	 */
	public Color getColor(){
		return color;
	}
	
	/**
	 * Returns the type of this GameToken
	 * @return the type of this GameToken
	 */
	public TokenType getType(){
		return type;
	}

	/**
	 * Checks whether this Token has been found
	 * @return true if this Token has been found
	 */
	public boolean isFound() {
		return found;
	}

	/**
	 * Sets the found state of this Token
	 * @param found the new found state of this Token
	 */
	public void setFound(boolean found) {
		this.found = found;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameToken other = (GameToken) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
