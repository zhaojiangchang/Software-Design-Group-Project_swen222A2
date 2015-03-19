package world.components;

import java.awt.Color;

/**
 * A Key to unlock a Door.  A Key has a Color (to distinguish between Keys when drawing) and a name.
 * @author Kalo Pilato - ID: 300313803
 *
 */
public class Key implements MoveableObject, java.io.Serializable{

	private final String name;
	private final Color color;
	
	/**
	 * Constructor - creates a Key to a Door
	 * @param name the name of this key
	 * @param color the colour of this key
	 */
	public Key(String name, Color color){
		this.name = name;
		this.color = color;
	}

	/**
	 * Returns the Color of this Key
	 * @return the Color of this Key
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Returns the Name of this Key
	 * @return the Name of this Key
	 */
	public String getName(){
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Key other = (Key) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	/**
	 * Returns a String representation of this Key
	 * @return the name of this Key
	 */
	public String toString(){
		return name;
	}
	
	
}
