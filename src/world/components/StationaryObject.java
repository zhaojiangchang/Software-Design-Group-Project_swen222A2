package world.components;

import java.awt.Point;
import java.util.ArrayList;

/**
 * The interface for stationary objects in this game world
 * @author Kalo Pilato - ID: 300313803
 *
 */
public interface StationaryObject extends GameObject {

	/**
	 * Returns the Direction that this StationaryObject is facing
	 * Used by the renderer for determining furniture orientation for drawing
	 * @return the Directino that this StationaryObject is facing
	 */
	public Direction getFacing(); 
	
	/**
	 * Returns an ArrayList of the Points occupied by this StationaryObject
	 * @return an ArrayList of the Points occupied by this StationaryObject
	 */
	public ArrayList<Point> getPoints();
}
