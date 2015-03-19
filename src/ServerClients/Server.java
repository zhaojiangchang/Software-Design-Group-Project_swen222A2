/**
 *
 */
package ServerClients;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

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
import world.game.GameBuilder;
import world.game.GameState;
import world.game.MultyPlayer;
import world.game.Player;

/**
 * A Server to handle connection between server and player
 * @author zhaojiang chang - ID:300282984
 *
 */
public class Server extends Thread {
	static DatagramSocket socket;
	public Client client;
	//private GameState state;
	private List<MultyPlayer> connectedPlayers;
	private GameState state;
	private boolean serverOpen = false;
	private String name;
	private int portNumber;
	private int numPlayers=0;
	public static int serverStart = 0;

	/**
	 * Constructor - creates a Server
	 * @param portNumber - server port number
	 * @param numPlayers - number of players
	 * */

	public Server(int portNumber, int numPlayers){
		this.numPlayers = numPlayers;
		if(numPlayers<1){
			numPlayers = 2;
		}
		this.portNumber = portNumber;
		connectedPlayers = new ArrayList<MultyPlayer>();

		//this.state = state;
		try {
			this.socket = new DatagramSocket(portNumber);
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("server socket"+ e);
		}


	}
	/**
	 * server check the receive packet
	 * if received packet is valid then pass to parsePacket method
	 *
	 */
	public void run(){

		while(true){

			this.serverStart = 99;
			System.out.println("Server Start>>>>>>>>>>"+ serverStart);

			byte[]data = new byte[60000];

			DatagramPacket packet = new DatagramPacket(data, data.length);
			try{


				socket.receive(packet);


			}catch(IOException e){

				e.printStackTrace();
			}

			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			System.out.println("server class connected players size:  "+connectedPlayers.size());
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
		//System.out.println("bbb9");

		//System.out.println("server>>parsePacket");
		String message = new String(data).trim();
		PacketTypes type = UDPPacket.lookupPacket(message.substring(0,2));
		System.out.println("server type: "+message.substring(0,2));
		UDPPacket packet = null;
		switch (type) {
		default:
		case INVALID:
			break;
		case LOGIN:
			//System.out.println("Server>parsePacket>LOGIN....");
			packet = new Packet00Login(data);
			name = ((Packet00Login) packet).getUsername();
			System.out.println("[" + address.getHostAddress() + ":" + port + "] "
					+ ((Packet00Login) packet).getUsername() + " has connected...");
			MultyPlayer player = new MultyPlayer( ((Packet00Login) packet).getUsername(), null, address, port);
			this.addConnection(player, (Packet00Login) packet);
			System.out.println("Server>parsePacket>LOGIN seccucssfully");
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] "
					+ ((Packet01Disconnect) packet).getUsername() + " has left...");
			this.removeConnection((Packet01Disconnect) packet);
			break;
		case DATA:
			packet = new Packet02Data(data);
			name = ((Packet02Data) packet).getUsername();//may through a exception
			this.handleData(((Packet02Data) packet));
			break;
		case MOVE:
			packet = new Packet03Move(data);
			name = ((Packet03Move) packet).getUsername();
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
			handleDropObject((Packet07DropObject)packet);
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
	 * this method is going to send init data to all cilents to
	 * start the game
	 *
	 * */

	private void sentStateToAllClients() {

		System.out.println("serverOpen = "+ serverOpen);
		ArrayList<String>names = new ArrayList<String>();
		for(MultyPlayer p:connectedPlayers ){
			names.add(p.getName());
		}

		GameBuilder builder =new GameBuilder(names);
		state = builder.getGameState();

		byte[] temp = serialize();

		byte[]newData =new byte[temp.length+2];
		byte[] b = "02".getBytes();
		newData[0] = b[0];
		newData[1] = b[1];

		for(int i = 0; i<temp.length;i++){
			newData[i+2] = temp[i];
		}
		Packet02Data p = new Packet02Data(newData);
		p.writeData(this);
		serverOpen  = true;


	}
	/**
	 * this method will send message from server to client
	 * @param data  - byte array data packet with PacketType
	 * */
	private void sendData(byte[]data, InetAddress ipAddress, int port){
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method is going to send the action data to all clients
	 * except from client
	 * @param data
	 * */
	public void sendActionDataToAllClients(byte[] data) {
		for (MultyPlayer p : connectedPlayers) {
			if(!name.equals(p.getName())){
				System.out.println("From "+name+"  send to: "+ p.getName());

				sendData(data, p.ipAddress, p.port);
				System.out.println("send data to "+p.ipAddress+ "  "+p.port);
			}
		}
	}
	/**
	 * this method is going to send the action data to all clients
	 * @param data
	 * */

	public void sendDataToAllClients(byte[] data) {
		for (MultyPlayer p : connectedPlayers) {
			//System.out.println(name+"  "+ p.getName());
			//if(!name.equals(p.getName())){
			sendData(data, p.ipAddress, p.port);
			//System.out.println(p.ipAddress+ "  "+p.port);
		}
	}

	/**
	 * get player by
	 * */
	public MultyPlayer getPlayer(String username) {
		for (MultyPlayer player : this.connectedPlayers) {
			if (player.getName().equals(username)) {
				//System.out.println(player.getPosition().x+"   "+player.getPosition().y);
				return player;
			}
		}
		return null;
	}
	public int getPlayerIndex(String username) {
		int index = 0;
		for (MultyPlayer player : this.connectedPlayers) {
			if (player.getName().equals(username)) {
				break;
			}
			index++;
		}
		return index;
	}
	private void handleData(Packet02Data packet) {

		byte[] temp = packet.getData();
		Packet02Data pk = new Packet02Data(temp);
		packet.writeData(this);

	}
	private void handleOpenDoor(Packet05OpenDoor packet) {
		byte[] temp = packet.getData();
		Packet05OpenDoor pk = new Packet05OpenDoor(temp);
		packet.writeData(this);
	}
	private void handlePickupObject(Packet06PickupObject packet) {
		byte[] temp = packet.getData();
		Packet06PickupObject pk = new Packet06PickupObject(temp);
		pk.writeData(this);

	}
	private void handlePickupKey(Packet08PickupKey packet) {
		byte[] temp = packet.getData();
		Packet08PickupKey pk = new Packet08PickupKey(temp);
		pk.writeData(this);

	}
	private void handleDropObject(Packet07DropObject packet) {

		byte[] temp = packet.getData();
		Packet07DropObject pk = new Packet07DropObject(temp);
		pk.writeData(this);

	}
	private void handleWin(Packet09WinGame packet) {
		byte[] temp = packet.getData();
		Packet09WinGame pk = new Packet09WinGame(temp);
		pk.writeData(this);
	}
	private void handleMove(Packet03Move packet) {
		if(getPlayer(packet.getUsername())!=null){
			int index = getPlayerIndex(packet.getUsername());
			Player player = state.getPlayer(packet.getUsername());
			state.movePlayer(player, packet.getPoint());
			packet.writeData(this);
		}

	}

	private void handleTeleport(Packet04Teleport packet) {

		packet.writeData(this);

	}

	public void addConnection(MultyPlayer player, Packet00Login packet) {
		boolean alreadyConnected = false;
		for (MultyPlayer p : this.connectedPlayers) {
			if (player.getName().equalsIgnoreCase(p.getName())) {
				if (p.ipAddress == null) {
					p.ipAddress = player.ipAddress;
				}
				if (p.port == -1) {
					p.port = player.port;
				}
				alreadyConnected = true;
				System.out.println("allrady connected");
			}
			else {
				//relay to the current connected player that there is a new player
				sendData(packet.getData(), p.ipAddress, p.port);

				// relay to the new player that the currently connect player exists
				packet = new Packet00Login(p.getName());
				sendData(packet.getData(), player.ipAddress, player.port);
			}
		}
		if (!alreadyConnected) {
			this.connectedPlayers.add(player);
			if(connectedPlayers.size()==numPlayers && serverOpen==false){
				sentStateToAllClients();
			}
		}
	}
	public ArrayList<String> getPlayerNames(){
		ArrayList<String>names = new ArrayList<String>();
		for(MultyPlayer p: connectedPlayers){
			names.add(p.getName());

		}
		return names;
	}

	public void removeConnection(Packet01Disconnect packet) {
		this.connectedPlayers.remove(getPlayerIndex(packet.getUsername()));
		packet.writeData(this);
	}

	public List<MultyPlayer> getConnectedPlayers() {
		return connectedPlayers;
	}
	public int getServerStart(){
		System.out.println("GetServerStart: "+ this.serverStart);
		return this.serverStart;
	}
	public void setServerStart(int num){
		this.serverStart = num;
	}

	private byte[] serialize() {

		byte[] bytes = new byte[60000];
		try {
			//object to bytearray
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(state);
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
}

