package ServerClients;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import controllers.NetworkController;
import ServerClients.UDPpackets.Packet00Login;
import ServerClients.UDPpackets.Packet01Disconnect;
import ServerClients.UDPpackets.Packet02Data;
import ServerClients.UDPpackets.Packet03Move;
import ServerClients.UDPpackets.Packet04Teleport;
import ServerClients.UDPpackets.Packet05OpenDoor;
import ServerClients.UDPpackets.Packet06PickupObject;
import ServerClients.UDPpackets.Packet07DropObject;
import ServerClients.UDPpackets.Packet08PickupKey;
import ServerClients.UDPpackets.Packet09WinGame;
import ServerClients.UDPpackets.UDPPacket;
import ServerClients.UDPpackets.UDPPacket.PacketTypes;
import window.components.GUI;
import world.game.GameState;
import world.game.MultyPlayer;
import world.game.Player;
/**
 * A Client to handle connection between server and player
 * @author zhaojiang chang - ID:300282984
 *
 */
public class Client extends Thread {
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private  int port;
	private NetworkController networkController;
	public boolean connection;
	public String name;
	public GUI gui;


	/**
	 * Constructor - creates a Client
	 * @param name - current player name
	 * @param gui the GUI for this game
	 * @param ipAddress - server ip address
	 * @param port - server port
	 */
	public Client(GUI gui,String name,String ipAddress,int port){
		this.port = port;
		this.gui = gui;
		this.name = name;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	/**
	 * client check the receive packet
	 * if received packet is valid then pass to parsePacket method
	 *
	 */
	public void run(){

		while(true){

			byte[]data = new byte[60000];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try{
				socket.receive(packet);

			}catch(IOException e){
				e.printStackTrace();
			}
			System.out.println("receive data from Server >");
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}

	/**
	 * the parsePacket will check the first two byte
	 * byte will identify the type of data received
	 * @param data received from client
	 * @param address client ip address
	 * @param port client port
	 *
	 * */
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = UDPPacket.lookupPacket(message.substring(0, 2));
		System.out.println("client type: "+message.substring(0,2)+ "  package size: " + message.length());

		UDPPacket packet = null;
		switch (type) {
		default:
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			handleLogin((Packet00Login) packet, address, port);

			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] "
					+ ((Packet01Disconnect) packet).getUsername() + " has left the game...");
			break;
		case DATA:
			packet = new Packet02Data(data);
			handleData((Packet02Data) packet);
			break;
		case MOVE:
			packet = new Packet03Move(data);
			handleMove((Packet03Move) packet);
			break;
		case OPENDOOR:
			packet = new Packet05OpenDoor(data);
			handleOpenDoor((Packet05OpenDoor) packet);
			break;
		case PICKUP:
			packet = new Packet06PickupObject(data);
			handlePickupObject((Packet06PickupObject)packet);
			break;
		case TELEPORT:
			packet = new Packet04Teleport(data);
			handleTeleport((Packet04Teleport)packet);
			break;
		case DROP:
			packet = new Packet07DropObject(data);
			handlePickupObject((Packet07DropObject)packet);
			break;
		case PICKUPKEY:
			packet = new Packet08PickupKey(data);
			handlePickupKey((Packet08PickupKey)packet);
			break;

		case WIN:
			packet = new Packet09WinGame(data);
			handleWin((Packet09WinGame) packet);
			break;
		}
	}





	/**
	 * this method will send message from client to server
	 * @param data  - byte array data packet with PacketType
	 * */
	public void sendData(byte[]data){
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * this method is handle login package
	 * @param packet - received packet from client
	 * */
	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername()
				+ " joined the game...");
		new MultyPlayer( packet.getUsername(),null,address, port);

	}
	/**
	 * this method is handle the game state,
	 * received serialized data from server,
	 * then deserialize
	 * @param packet - bytes array
	 * */
	private void handleData(Packet02Data packet) {

		GameState st = deserialize(packet.getRealData());
		networkController = new MultiPlayerBuild(st, gui, name).getNetworkController();
		networkController.setClient(this);
		
	}

	private void handleWin(Packet09WinGame packet) {
		System.out.println(packet.getUsername()+ " win the game ");
		networkController.setWinnerFromNetwork(packet.getUsername());

	}
	private void handlePickupObject(Packet07DropObject packet) {

	}
	/**
	 * this method is handle the open door action,
	 * received  data from server,
	 * then send to netowrkController to send the action to logic
	 * @param packet - bytes array
	 * */

	private void handleOpenDoor(Packet05OpenDoor packet) {
		//Player player = (Player) this.deserialize(packet.getData());
		networkController.triggerDoor(packet.getName(), packet.getPoint());
	}
	/**
	 * this method is handle the pickup object action,
	 * received  data from server,
	 * then send to netowrkController to send the action to logic
	 * @param packet - bytes array
	 * */
	private void handlePickupObject(Packet06PickupObject packet) {
		networkController.pickupObjectOtherPlayer(packet);
	}


	private void handlePickupKey(Packet08PickupKey packet) {
		networkController.pickupKeyOtherPlayer(packet);
	}

	/**
	 * this method is handle the move action,
	 * received  data from server,
	 * then send to netowrkController to send the action to logic
	 * @param packet - bytes array
	 * */
	private void handleMove(Packet03Move packet) {

		Player p = networkController.getPlayer(packet.getUsername());
		if(p!=null){
			//if(!GUI.nameC.equalsIgnoreCase(p.getName())){
				networkController.moveOtherPlayer(p, packet.getPoint());
//			}else{
//				System.out.println("local player should not move by server");
//			}
		}

	}

	/**
	 * this method is handle the teleport action,
	 * received  data from server,
	 * then send to netowrkController to send the action to logic
	 * @param packet - bytes array
	 * */
	private void handleTeleport(Packet04Teleport packet) {
		Player p = networkController.getPlayer(packet.getUsername());
		if(p!=null){
			if(!GUI.nameC.equalsIgnoreCase(p.getName())){
				networkController.teleportOtherPlayer(packet.getUsername(), packet.getFloorNumber());
			}else{
				System.out.println("local player should not teleport by server");
			}
		}
	}

	/**
	 * this method is deserialize the object
	 * @param bytes array
	 *
	 * */
	public GameState deserialize(byte[]bytes) {

		ByteArrayInputStream bais = null;
		ObjectInputStream in = null;
		try{
			bais = new ByteArrayInputStream(bytes);
			in = new ObjectInputStream(bais);
			GameState s = (GameState) in.readObject();
			return s;
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

