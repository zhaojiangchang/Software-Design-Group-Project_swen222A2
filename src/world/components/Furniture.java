package world.components;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Furniture implements StationaryObject, Serializable {

	private final Direction facing;
	private ArrayList<Point> points;
	private CellType type;

	/**
	 * Constructor - creates a Furniture object
	 * @param origin the "top right" square that this Furniture occupies
	 * @param facing the Direction that this Furniture faces
	 */
	public Furniture(CellType type, Point origin, Direction facing){
		points = new ArrayList<Point>();
		this.type = type;
		this.facing = facing;
		populatePoints(origin);
	}

	/**
	 * Returns the Direction that this Furniture is facing
	 * @return the Directin that this Furniture is facing
	 */
	public Direction getFacing() {
		return facing;
	}

	/**
	 * Returns the CellType of this Furniture
	 * @return the CellType of this Furniture
	 */
	public CellType getType(){
		return type;
	}
	
	/**
	 * Returns an ArrayList of the Points occupied by this piece of Furniture
	 * @return an ArrayList of the Points occupied by this piece of Furniture
	 */
	public ArrayList<Point> getPoints(){
		return points;
	}

	/**
	 * Generates and stores the points that this Furniture object occupies on the map
	 */
	private void populatePoints(Point origin){
		switch(type){
			case COUCH:{
				points.add(origin);
				switch(facing){
					case NORTH:	{
						points.add(new Point(origin.x + 1, origin.y));
						break;
					}
					case EAST: {
						points.add(new Point(origin.x, origin.y + 1));
						break;
					}
					case SOUTH: {
						points.add(new Point(origin.x + 1, origin.y));
						break;
					}
					case WEST: {
						points.add(new Point(origin.x, origin.y + 1));
						break;
					}
				}
				break;
			}
			case BED:{
				switch(facing){
					case NORTH:	
						for(int y = 0; y < 2; y++){
							for(int x = 0; x < 3; x++){
								points.add(new Point(origin.x + x, origin.y + y));
							}
						}
						break;
					case EAST: 
						for(int y = 0; y < 3; y++){
							for(int x = 0; x < 2; x++){
								points.add(new Point(origin.x + x, origin.y + y));
							}
						}
						break;
					case SOUTH: 
						for(int y = 0; y < 2; y++){
							for(int x = 0; x < 3; x++){
								points.add(new Point(origin.x + x, origin.y + y));
							}
						}
						break;
					case WEST: 
						for(int y = 0; y < 3; y++){
							for(int x = 0; x < 2; x++){
								points.add(new Point(origin.x + x, origin.y + y));
							}
						}
						break;
				}
				break;
			}
			case TABLE:{
				points.add(origin);
				switch(facing){
					case NORTH:	{
						points.add(new Point(origin.x + 1, origin.y));
						break;
					}
					case EAST: {
						points.add(new Point(origin.x, origin.y + 1));
						break;
					}
					case SOUTH: {
						points.add(new Point(origin.x + 1, origin.y));
						break;
					}
					case WEST: {
						points.add(new Point(origin.x, origin.y + 1));
						break;
					}
				}
				break;
			}
			case DRAWERS:{
				points.add(origin);
				break;
			}
		}
	}

}
