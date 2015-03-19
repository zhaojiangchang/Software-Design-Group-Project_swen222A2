package world.components;

/**
 * A Torch for lighting up dark rooms.  Can be turned on and off.
 * @author Kalo Pilato - ID: 300313803
 *
 */
public class Torch implements MoveableObject, java.io.Serializable{

	private boolean lit = false;
	
	/**
	 * Constructor - creates a Torch which is turned off by default.
	 */
	public Torch(){
	}
	
	/**
	 * Turns this Torch on
	 */
	public void turnOn(){
		lit = true;
	}
	
	/**
	 * Turns this Torch off
	 */
	public void turnOff(){
		lit = false;
	}
	
	/**
	 * Returns the lit state of this Torch
	 * @return true if lit
	 */
	public boolean isLit(){
		return lit;
	}
	
	/**
	 * Returns a String representation of this Torch
	 */
	public String toString(){
		return "torch";
	}
}
