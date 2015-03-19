package controllers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ui.components.GameView;
import ui.components.GameViewData;
import world.components.Key;
import world.components.MoveableObject;
import world.game.GameState;
import world.game.Player;

/**
 * Controller for the Renderer to handle communication between the NetworkController, GameState and Renderer
 * @author Kalo Pilato - ID: 300313803
 *
 */
public class RendererController {

	private static NetworkController netCon;
	private static UIController uiCon;
	private static GameState state;
	private static GameViewData view;
	private static boolean singlePlayer;

	/**
	 * Constructor - creates a RendererController with a Pointer to the Singleton GameViewData for updating Renderer database.
	 * @requires Constructor must be called after the GameView has been constructed.
	 */
	public RendererController(boolean single){
		view = GameViewData.instance();
		singlePlayer = single;
	}

	/**
	 * @param n the netCon to set
	 */
	public static void setNetCon(NetworkController n) {
		netCon = n;
	}

	/**
	 * @param s the state to set
	 */
	public static void setState(GameState s) {
		state = s;
	}

	/**
	 * @param u the UIController to set
	 */
	public static void setUICon(UIController u){
		uiCon = u;
	}

	/**
	 * @return true if this is a single player game
	 */
	public boolean isSinglePlayer() {
		return singlePlayer;
	}

	/**
	 * Moves another Player in the Game
	 * @param player the Player to move
	 * @param point the Point to move the Player to
	 */
	public static void moveOtherPlayer(Player player, Point point){
		Point oldPos = player.getPosition();
		state.movePlayer(state.getPlayer(player.getName()), point);
		if(player.getFloor() == GameView.player.getFloor()) view.addNewPlayerMove(oldPos, point, player);
	}

	/**
	 * Moves the current Player in the Game
	 * @param player the Player to move
	 * @param point the Point to move the Player to
	 */
	public static void movePlayer(Player player, Point point){
		if(!singlePlayer) netCon.movePlayer(player, point);
		state.movePlayer(player, point);
	}

	/**
	 * Picks up an item for the current Player in the Game
	 * @param player the Player to pick up an item
	 * @param p the Point to pick the item up from
	 * @return true if successfully picked up
	 */
	public static boolean pickupObjectAtPoint(Player player, Point p){
		boolean pickedUp = (state.pickupObjectAtPoint(player, p));
		System.out.println("object picked up -> calling networking");
		if(pickedUp){
			if(!singlePlayer) netCon.pickupObject(player, p);
			return true;
		}
		return false;
	}

	/**
	 * Picks up an item for another Player in the Game
	 * @param player the Player to pick up an item
	 * @param p the Point to pick the item up from
	 */
	public static void pickupObjectOtherPlayer(String playername, Point p){
		System.out.println("Player picking up object: " + playername);
		System.out.println("Point to pick up from : " + p.toString());
		Player player = state.getPlayer(playername);
		state.pickupObjectAtPoint(player, p);
		if(GameView.player.getFloor() == player.getFloor()) view.remove(p);
	}

	/**
	 * Picks up a Key for another Player, dropping their current Key (if they have one) and updating the Renderer (if on the same floor)
	 * @param playerName the name of the Player picking up the Key
	 * @param p the Point to pick up the Key from
	 */
	public static void pickupKeyOtherPlayer(String playerName, Point p){
		if(!playerName.equals(GameView.player.getName())){
			Player player = state.getPlayer(playerName);
			Key toDrop = state.pickupKey(player, p);
			if(player.getFloor() == GameView.player.getFloor()){
				if(toDrop == null) view.remove(p);
				else{
					System.out.println("Swapping Key");
					view.addKey(toDrop.getColor(), p);
				}
			}
		}
	}

	/**
	 * Picks up a Key for a Player, dropping their current Key (if they have one)
	 * @param player the Player picking up the Key
	 * @param p the Point to pick up the key from
	 * @return the Key to drop, returns null if no Key gets dropped
	 */
	public static Key pickupKey(Player player, Point p){
		Key toDrop = state.pickupKey(player, p);
		if(!singlePlayer) netCon.pickupKey(player.getName(), p);
		return toDrop;
	}


	/**
	 * Checks whether a Player can open a door
	 * @param player the Player opening the door
	 * @param point the Point of the door
	 * @return true if the Door can be opened
	 */
	public static boolean canOpenDoor(Player player, Point point){
		boolean canOpen = state.canOpenDoor(player, point);
		if(canOpen){
			if(!singlePlayer) netCon.openDoor(player.getName(), point);
			return true;
		}
		return false;
	}

	/**
	 * Triggers a Door to open in the Renderer from another Client
	 * @param name the name of the Player
	 * @param p the Point of the Door
	 */
	public static void triggerDoor(String name, Point p){
		if(state.getPlayer(name).getFloor() == GameView.player.getFloor()) view.triggerDoorOpen(p);
	}

	/**
	 * Returns a List of the current Players in this game
	 * @return a List of Players
	 */
	public List<Player> getPlayers(){
		return state.getPlayers();
	}

	/**
	 * Teleport the currentPlayer in the Game
	 * @param player the Player to teleport
	 * @return true if the Player was Teleported
	 */
	public static boolean teleport(Player player){
		if(uiCon.teleport(player)){
			if(!singlePlayer) netCon.teleport(player.getName(), player.getFloor().floorNumber());
			return true;
		}
		return false;
	}

	/**
	 * Teleport another Player in the Game
	 * @param playerName the name of the Player
	 * @param floorNumber the floor to teleport to
	 */
	public static void teleportOtherPlayer(String playerName, int floorNumber){
		Player player = state.getPlayer(playerName);
		// If the player starts on the same floor as the current player they need to be removed from the current view
		if(player.getFloor() == GameView.player.getFloor()){
			view.removePlayerAtPoint(player.getPosition());
		}
		// Teleport the player in the game state
		state.teleport(player, floorNumber);
		// If the player lands on the same floor as the current player they need to be added to the current view
		if(player.getFloor() == GameView.player.getFloor()){
			view.addNewPlayerMove(null, player.getPosition(), player);
		}
	}


	/**
	 * Notifies the Winner of this game
	 * @param p the Player that wins
	 */
	public static void setWinner(Player p){
		if(!singlePlayer) netCon.setWinner(p.getName());
		UIController.setWinner(p.getName());
	}

	/**
	 * Passes through the winner notification alert from the network
	 * @param p the Player that has won
	 */
	public static void setWinnerFromNetwork(String playerName){
		UIController.setWinner(playerName);
	}


}
