package controllers;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.media.opengl.awt.GLJPanel;

import ServerClients.Client;
import ServerClients.UDPpackets.Packet03Move;
import ServerClients.UDPpackets.Packet04Teleport;
import ServerClients.UDPpackets.Packet05OpenDoor;
import ServerClients.UDPpackets.Packet06PickupObject;
import ServerClients.UDPpackets.Packet07DropObject;
import ServerClients.UDPpackets.Packet08PickupKey;
import ServerClients.UDPpackets.Packet09WinGame;
import world.components.Key;
import world.components.MoveableObject;
import world.game.Player;
/**
 * A NetworkController to handle interactions between the Server/Client and Controller(GameState and the GUI)
 * @author zhaojiang chang - 300282984
 *
 */
public class NetworkController  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Client client;
	static Player player;
	static MoveableObject object;
	private static UIController controller;
	private static RendererController renCon;
	public static Point point = null;

	/**
	 * Constructor - creates a Network Controller for GameState/GUI/Client interaction in this game
	 * @param state the GameState of this game
	 * @param gui the GUI for this game
	 * @param client the Client for the game
	 */
	public NetworkController(UIController controller, RendererController renCon){
		this.controller = controller;
		this.renCon = renCon;
	}

	/**
	 * 
	 * 
	 * */
	public static void teleportOtherPlayer(String name, int floorNumber){
		renCon.teleportOtherPlayer(name, floorNumber);
	}
	public static void teleport(String name, int floorNumber){

		Packet04Teleport teleport = new Packet04Teleport(name,floorNumber);
		teleport.writeData(client);

	}
	/**
	 * Receive action from gameView(user input) then create a new move package to send to server then broadcast to all other client
	 *  @param player - current player  
	 *  @param point -  the new position move to
	 */
	public static void movePlayer(Player player, Point point){
		Packet03Move move = new Packet03Move(player.getName(),point);
		move.writeData(client);

	}

	/**
	 * Receive action from server then move other player (current may able to see other player movement 
	 *  @param player - other player  
	 *  @param point -  the new position move to
	 */
	public static void moveOtherPlayer(Player player, Point point){
		System.out.println("NetworkController->moveOtherPlayer "+ point.x+" "+point.y);
		renCon.moveOtherPlayer(player, point);
	}
	/**
	 * pass gameView from GUI
	 *  @param set gameView 

	 */
	public void setGameView(GLJPanel gameView) {
	}
	/**
	 * set client from GUI
	 *  @param set client 

	 */
	public void setClient(Client client) {
		this.client = client;
	}


	/**
	 * get player
	 * @param get player
	 * */

	public Player getPlayer(String username) {
		return controller.getPlayer(username);
	}


	/**
	 * gameview will call this method through RendererController and 
	 * pass the action to client then send to server
	 * 
	 * */
	public void openDoor(String name, Point point){
		Packet05OpenDoor openDoor = new Packet05OpenDoor(name, point);
		openDoor.writeData(client);
	}

	
	/**
	 * this method will called from rendererController
	 * end packet to client, then broadcast to all clients
	 * @param player - current player
	 * @param object - 
	 * */
	public void pickupObject(Player player, Point point){
		this.player = player;
		this.point = point;
		Packet06PickupObject pickup = new Packet06PickupObject(player.getName(),point);
		pickup.writeData(client);

	}
	
	/**
	 * this method will called from rendererController
	 * end packet to client, then broadcast to all clients
	 * @param player - current player
	 * @param key - 
	 * */
	public void pickupKey(String name, Point point){
		Packet08PickupKey pickup = new Packet08PickupKey(name,point);
		pickup.writeData(client);

	}
	/**
	 * this method is going to create a dropObject package and send to server through the client
	 * 
	 * */

	public void dropObject(String name, Key key, Point point) {

		byte[]keyData = this.serialize(key);
		Packet07DropObject drop = new Packet07DropObject(name,keyData);
		drop.writeData(client);
	}

	/**
	 * this method will called after server send a drop message, 
	 * 
	 * */

	public void pickupObjectOtherPlayer(Packet06PickupObject packet) {

		
		renCon.pickupObjectOtherPlayer(packet.getUsername(),packet.getPoint());
	}
	
	/**
	 * this method will called after server send a drop message, 
	 * 
	 * */
	public void pickupKeyOtherPlayer(Packet08PickupKey packet) {

		renCon.pickupKeyOtherPlayer(packet.getUsername(),packet.getPoint());
	}

	/**
	 * trigger the door open  - send commond to rendererController to open the door
	 * */
	public void triggerDoor(String name, Point p) {
		renCon.triggerDoor(name, p);

	}


	public void setWinnerFromNetwork(String username) {
		renCon.setWinnerFromNetwork(username);
	}
	
	public void setWinner(String name){
		
		Packet09WinGame win = new Packet09WinGame(name);
		win.writeData(client);

		
	}

	/**
	 * serialize object
	 * 
	 * */
	public byte[] serialize(Object obj) {

		byte[] bytes = new byte[60000];
		try {
			//object to bytearray
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(obj);
			bytes = baos.toByteArray();
			out.flush();
			baos.close();
			out.close();
			return bytes;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * deserialize object 
	 * 
	 * */
	public Object deserialise(byte[]bytes) {

		ByteArrayInputStream bais = null;
		ObjectInputStream in = null;
		try{
			bais = new ByteArrayInputStream(bytes);
			in = new ObjectInputStream(bais);
			Object obj =  in.readObject();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				bais.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return null;
	}








}

