package world.components;

import java.io.Serializable;
import java.util.ArrayList;

import world.components.MoveableObject;

/**
 * A GameObject that can contain another GameObject.  These are represented in the world by such items as a Briefcase, Chest and Drawers
 * @author Kalo Pilato - ID: 300313803
 *
 */
public class Container implements GameObject, Serializable{

	private ArrayList<MoveableObject> contents;
	private CellType type;
	private Direction facing;
	private int maxSize;
	
	/**
	 * Constructor - creates a Container from a given CellType
	 * @param type the type of Container to create
	 */
	public Container(CellType type, Direction facing){
		contents = new ArrayList<MoveableObject>();
		this.type = type;
		this.facing = facing;
		if(type == CellType.BRIEFCASE || type == CellType.CHEST) maxSize = 1;
		if(type == CellType.DRAWERS) maxSize = 3;
	}
	
	/**
	 * Sets the contents of this Container to a given MoveableObject
	 * @param object the MoveableObject to place in this Container
	 * @return true if the object was successfully placed in this Container
	 */
	public boolean setContents(MoveableObject object){
		if(contents.size() < maxSize){
			contents.add(object);
			return true;
		}
		return false;
	}
	
	/**
	 * Removes and returns the MoveableObject in this Container.  In the case of a Container that can contain multiple objects 
	 * (e.g. Drawers) the first item is removed and returned
	 * @return the first object in this Container, returns null if the Container is empty
	 */
	public MoveableObject getContents(){
		if(!contents.isEmpty()) return contents.remove(0);
		else return null;
	}
	
	/**
	 * Returns the CellType of this Container
	 * @return the CellType of this Container
	 */
	public CellType getType(){
		return type;
	}
	
	/**
	 * Returns the Direction this Container is facing
	 * @return the Direction this Container is facing
	 */
	public Direction getFacing(){
		return facing;
	}
	
	/**
	 * Returns a String representation of this Container
	 */
	public String toString(){
		return type.toString();
	}
}
