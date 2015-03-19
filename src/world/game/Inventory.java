package world.game;

import java.util.ArrayList;
import java.util.List;

import world.components.Key;
import world.components.MoveableObject;
import world.components.Torch;

/**
 * An inventory for a Player in the game world.  Contains MoveableObjects.
 * @author Kalo Pilato - ID: 300313803
 *
 */
public class Inventory implements java.io.Serializable{
	
	private List<MoveableObject> inventory;
	private int maxSize = 5;
	
	/**
	 * Constructor - creates an empty Inventory
	 */
	public Inventory(){
		inventory = new ArrayList<MoveableObject>();
	}
	
	/**
	 * Adds a MoveableObject to this Inventory (provided that the Inventory has available space)
	 * @param item the MoveableObject to add
	 * @return true if added
	 */
	public boolean add(MoveableObject item){
		if(inventory.size() == maxSize) return false;
		else return inventory.add(item);
	}
	
	/**
	 * Adds a Key to this Inventory. A Player can only carry one Key at a time. 
	 * If the Inventory already contains a Key it will be removed from the Inventory and returned.
	 * @param key the Key to add
	 * @return the Key that has been removed, returns null if no Key was already held
	 */
	public Key addKey(Key key){
		Key removeKey = null;
		for(int i = 0; i < inventory.size(); i++){
			if(inventory.get(i) instanceof Key){
				removeKey = (Key) inventory.remove(i);
			}
		}
		inventory.add(key);
		return removeKey;
	}
	
	/**
	 * Adds a Torch to this Inventory.  A Player can only carry one Torch at a time.
	 * If the Inventory already contains a Torch the Inventory is not updated.
	 * @param torch the Torch to add
	 * @return true if successfully added, false if the Inventory already contains a Torch
	 */
	public boolean addTorch(Torch torch){
		for(int i = 0; i < inventory.size(); i++){
			if(inventory.get(i) instanceof Torch){
				return false;
			}
		}
		inventory.add(torch);
		return true;
	}
	
	/**
	 * Removes a MoveableObject from this Inventory
	 * @param item the MoveableObject to remove
	 * @return true if removed
	 */
	public boolean remove(MoveableObject item){
		return inventory.remove(item);
	}
	
	/**
	 * Returns the MoveableObject at a given index
	 * @param index the index to retrieve the MoveableObject from
	 * @return the MoveableObject at index
	 */
	public MoveableObject get(int index){
		return inventory.get(index);
	}
	
	/**
	 * Returns the size of this Inventory
	 * @return the size of this Inventory
	 */
	public int size(){
		return inventory.size();
	}
	
	/**
	 * Checks if this Inventory contains a given MoveableObject
	 * @param object the MoveableObject to check for
	 * @return true if contained
	 */
	public boolean contains(MoveableObject object){
		return inventory.contains(object);
	}
	
	public String toString(){
		String contents = "";
		for(MoveableObject m: inventory){
			contents += m.toString() + " ";
		}
		return contents;
	}
}
