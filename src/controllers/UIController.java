package controllers;

import java.awt.Point;

import window.components.GUI;
import world.components.MoveableObject;
import world.game.GameState;
import world.game.Player;

/**
 * A Controller to handle interactions between the GameState and the GUI
 * @author Kalo Pilato - ID:300313803
 *
 */
public class UIController {

	private static GameState state;
	private static GUI gui;
	
	/**
	 * Constructor - creates a UIController for GameState/GUI interaction in this game
	 * @param state the GameState of this game
	 * @param gui the GUI for this game
	 */
	public UIController(GameState state, GUI gui){
		this.state = state;
		this.gui = gui;
	}
	
	/**
	 * Constructor - creates a UIController with a given GUI
	 * @param gui
	 */
	public UIController(GUI gui){
		this.gui = gui;
	}
	
	/**
	 * Redraws the GameToken panel in the GUI - use for updating the GUI when a Player collects a GameToken
	 */
	public static void refreshTokenPanel(){
		gui.redrawCollectItemCanvas();
	}
	
	/**
	 * Redraws the Inventory panel in the GUI - use for updating the GUI when a Player collects a MoveableObject
	 */
	public static void refreshInventoryPanel(){
		gui.redrawUsefulItemCanvas();
	}
	
	/**
	 * Teleports a Player to a user provided floor number
	 * @param p the Player to teleport
	 * @return true if successfully completed
	 */
	public boolean teleport(Player p){
		int floor = gui.getFloor(state.floorCount());
		return state.teleport(p, floor);
	}
	
	/**
	 * Alerts the Winner of this game
	 * @param p the Player that wins this game
	 */
	public static void setWinner(String playerName){
		gui.setWinner(playerName);
	}

	public Player getPlayer(String username) {
		return state.getPlayer(username);
	}



}
