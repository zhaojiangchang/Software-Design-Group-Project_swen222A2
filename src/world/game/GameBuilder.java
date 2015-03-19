package world.game;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import world.ColourPalette;
import world.components.CellType;
import world.components.Container;
import world.components.Direction;
import world.components.Door;
import world.components.Furniture;
import world.components.GameToken;
import world.components.Key;
import world.components.Map;
import world.components.TokenType;
import world.components.Torch;


public class GameBuilder {

	private List<Player> players;
	private Map[] floors;
	private GameState state;
	private ArrayList<Key> keys = new ArrayList<Key>();
	
	/**
	 * Constructor - creates a new game with a given list of Players.  Currently builds the same number of floors as there are Players
	 * @param players the list of Players in this game.
	 */
	public GameBuilder(List<String> playerNames){
		if(playerNames.size() > 4) System.out.println("This game supports a maximum of 4 players only");
		else{
			players = new ArrayList<Player>();
			for(int i = 0; i < playerNames.size(); i++){
				players.add(new Player(playerNames.get(i), TokenType.values()[i]));
			}
			
			// Build and populate the game world - the order here matters
			buildFloors(players.size());
			placePlayers();
			placeFurniture();
			placePlayerTokens();
			placeKeys();
			placeTorches();
			state = new GameState(players, floors);
		}
	}
	
	/**
	 * Single Player Constructor - creates a new game with a given player name. 
	 * Currently builds a world with 3 "dumb" players to populate the world with extra items and floors
	 * @param players the list of Player names for this game.
	 */
	public GameBuilder(String playerName){
		players = new ArrayList<Player>();
		new ColourPalette();
		players.add(new Player(playerName, TokenType.values()[0]));
		
		// Add additional Players to the game to expand the world and show Player avatars
		players.add(new Player("aaa", TokenType.values()[1]));
		players.add(new Player("bbb", TokenType.values()[2]));
		players.add(new Player("ccc", TokenType.values()[3]));
		
		// Build and populate the game world - the order here matters
		buildFloors(players.size());
		placePlayers();
		placeFurniture();
		placePlayerTokens();
		placeKeys();
		placeTorches();
		state = new GameState(players, floors);
	}
	
	/**
	 * Returns the newly constructed GameState
	 * @return the newly constructed GameState
	 */
	public GameState getGameState(){
		return state;
	}
	
	private void serialize(){
		try
	      {
	         FileOutputStream fileOut =
	         new FileOutputStream("../state.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(state);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in ../state.ser");
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	
	/**
	 * Builds the collection of "floors" for this game.  Each floor has an identical layout but contains different objects.
	 * Keys are also created for Lockable Doors in the floor and distributed throughout the game world.  There is a restriction here:
	 * the number of keys in any given world must be no bigger than the number of Colors in ColourPalette
	 * @param floorCount the number of floors to use in this game.
	 */
	public void buildFloors(int floorCount){
		floors = new Map[floorCount];
		for(int i = 0; i < floorCount; i++){
			floors[i] = new Map(new File("map1.txt"), i);
			
			// Check the doors in the new Map, if they're lockable create keys for each door
			for(Door door: floors[i].getDoors().values()){
				if(door.isLockable()){
					Color color = ColourPalette.get(keys.size());
					Key key = new Key(ColourPalette.getName(keys.size()) + " key", color);
					door.setKey(key);
					keys.add(key);
				}
			}
		}
	}
	
	/**
	 * Places each Player in the game world, setting their start position to a hard coded floor and x/y coordinate.
	 * Players are placed at the same Point on different floors.  
	 */
	private void placePlayers(){
		for(int i = 0; i < players.size(); i++){
			Player currentPlayer = players.get(i);
			Point startPoint = new Point(3, 20);
			currentPlayer.setPosition(startPoint);
			currentPlayer.setFloor(floors[i]);
			floors[i].placePlayer(startPoint, currentPlayer);
		}
	}
	
	/**
	 * Distributes each Player's Tokens throughout the game world, choosing floors and Points at random
	 */
	private void placePlayerTokens(){
		// Temporary collection of all GameTokens in this game
		ArrayList<GameToken> tokens = new ArrayList<GameToken>();
		for(int i = 0; i < players.size(); i++){
			Player currentPlayer = players.get(i);
			TokenList currentTokens = currentPlayer.getTokenList();
			// Add each Player's GameTokens to the tokens collection
			for(int t = 0; t < currentTokens.size(); t++){
				tokens.add(currentTokens.get(t));
			}
		}
		// Distribute tokens throughout the game world at random
		for(int i = 0; i < tokens.size(); i++){
			Random random = new Random();
			// Select a random floor in this world
			Map randomFloor = floors[random.nextInt(floors.length)];
			// Place the Token in a random cell on the floor
			randomFloor.addGameToken(randomFloor.randomEmptyCell(), tokens.get(i));
		}
	}
	
	/**x
	 * Distributes a torches (one per player) throughout the game world, choosing floors and Points at random
	 */
	private void placeTorches(){
		for(int i = 0; i < players.size(); i ++){
			Random random = new Random();
			// Select a random floor
			Map randomFloor = floors[random.nextInt(floors.length)];
			// Place the Torch in a random cell
			randomFloor.addMoveable(randomFloor.randomEmptyCell(), new Torch());
		}
	}
	
	/**
	 * Distributes keys throughout the game world, choosing floors and Points at random
	 */
	private void placeKeys(){
		for(Key key: keys){
			Random random = new Random();
			// Select a random floor
			Map randomFloor = floors[random.nextInt(floors.length)];
			
			// Select a random point to add the key at - must ensure it's not added in a locked room
			Point point = randomFloor.randomEmptyCell();
			while(point.x > 5 && point.x < 14 && point.y > 5 && point.y < 14){
				randomFloor.setEmpty(point);
				point = randomFloor.randomEmptyCell();
			}
			randomFloor.addMoveable(point, key);
		}
	}
	
	/**
	 * Places Furniture in the game world by reading furniture placement files for each floor
	 */
	private void placeFurniture(){
		
		for(int i = 0; i < floors.length; i++){
			try{
				
				Scanner scan = new Scanner(new File("furniture" + i + ".txt"));
	
				while(scan.hasNextLine()){
					String[] tokens = scan.nextLine().split(" ");
					
					// Process first token (should be furniture type)
					CellType furnitureType = furnitureType(tokens[0]);
					if(furnitureType == null) throw new Exception("Invalid Furniture type in furniture file: " + tokens[0]);
					
					// Process second token (should be direction)
					Direction furnitureDir = furnitureDirection(tokens[1]);
					if(furnitureDir == null) throw new Exception("Invalid Furniture direction in furniture file: " + tokens[1]);
					
					// Process third and fourth tokens (should be x/y coordinates)
					Point position = new Point(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
					
					// Check whether the item to add is furniture or a container and add it to the world
					if(furnitureType == CellType.BRIEFCASE || furnitureType == CellType.CHEST || furnitureType == CellType.DRAWERS){
						floors[i].addContainer(position, new Container(furnitureType, furnitureDir));
					}
					else{
						floors[i].addFurniture(position, new Furniture(furnitureType, position, furnitureDir));
					}
					
				}
				scan.close();
			} catch ( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns a Furniture CellType given a String name - uses a range of values from the CellType enum; not a good solution but it works
	 * @param furniture the name of the Furniture type
	 * @return the CellType of the furniture, returns null if not found
	 */
	private CellType furnitureType(String furniture){
		for(int i = 13; i < 20; i++){
			if(furniture.equals(CellType.values()[i].toString())) return CellType.values()[i];
		}
		return null;
	}
	
	/**
	 * Returns a Direction given a String direction
	 * @param direction the String describing the direction
	 * @return the Direction matching the String
	 */
	private Direction furnitureDirection(String direction){
		for(int i = 0; i < 4; i++){
			if(direction.equals(Direction.values()[i].toString())) return Direction.values()[i];
		}
		return null;
	}
	
}
