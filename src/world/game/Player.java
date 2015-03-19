package world.game;

import java.awt.Point;

import world.components.Direction;
import world.components.Map;
import world.components.TokenType;



/**
 * Represents a Player in the game world.
 * @author Kalo Pilato
 *
 */
public class Player implements java.io.Serializable{
	
	private final String name;
	private final TokenList toCollect;
	private final TokenType type;
	private final Inventory inventory;
	private Direction facing = Direction.NORTH;
	private Map floor;
	private Point position;
	
	/**
	 * Constructor - Creates a Player with a given name, an empty inventory, and a TokenList of items to collect
	 * @param name the name of this Player
	 */
	public Player(String name, TokenType type){
		this.name = name;
		this.type = type;
		toCollect = new TokenList(type);
		this.inventory = new Inventory();
	}
	
	/**
	 * Returns the name of this Player
	 * @return the name of this Player
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the position of this Player using x/y coordinates
	 * @param x the x coordinate of the Player
	 * @param y the y coordinate of the Player
	 */
	public void setPosition(int x, int y){
		position = new Point(x, y);
	}
	
	/**
	 * Sets the position of this Player using a Point
	 * @param p the Point to set as this Player's position
	 */
	public void setPosition(Point p){
		position = p;
	}
	
	/**
	 * Returns the current position of this Player
	 */
	public Point getPosition(){
		return position;
	}
	
	/**
	 * Returns the Direction that this Player is facing
	 * @return the Direction this Player is facing
	 */
	public Direction getFacing() {
		return facing;
	}

	/**
	 * Sets the Direction that this Player is facing
	 * @param facing the Direction to set this Player to facing
	 */
	public void setFacing(Direction facing) {
		this.facing = facing;
	}

	/**
	 * Sets the Map that this Player is currently on
	 * @param floor the Map to set for this Player
	 */
	public void setFloor(Map floor){
		this.floor = floor;
	}
	
	//TODO: return a clone of the TokenList to make it immutable
	/**
	 * Returns this Player's TokenList
	 * @return this Player's TokenList
	 */
	public TokenList getTokenList(){
		return toCollect;
	}
	
	/**
	 * Returns the Map representing the Player's current floor
	 * @return the Map representing the Player's current floor
	 */
	public Map getFloor(){
		return floor;
	}
	
	/**
	 * Moves this Player from one Point to another Point
	 * @param p the Point to move this Player to
	 * @return true if successfully moved
	 */
	public boolean move(Point p){
		if(floor.movePlayer(this, position, p)){
			position = p;
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the type of tokens this Player is collecting
	 * @return the TokenType for this Player
	 */
	public TokenType getType(){
		return type;
	}
	
	/**
	 * Returns this Player's Inventory
	 * @return this Player's Inventory
	 */
	public Inventory getInventory(){
		return inventory;
	}

}
