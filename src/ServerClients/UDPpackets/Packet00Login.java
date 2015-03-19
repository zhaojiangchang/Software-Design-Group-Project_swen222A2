package ServerClients.UDPpackets;

import java.awt.Point;
import ServerClients.Client;
import ServerClients.Server;

/**
 * A Packet00Login to handle player login 
 * @author zhaojiang chang - 300282984
 *
 */

public class Packet00Login extends UDPPacket {

	private String username;

	/**
	 * Constructor - unpack login package
	 * @param data - byte array  name of the player
	 */
	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		System.out.println("username = "+ username);
	}

	/**
	 * Constructor - create a login package with given name
	 * @param username - player name
	 */
	public Packet00Login(String username) {
		super(00);
		this.username = username;
	}

	/**
	 * writeData(client) - this method is going to send data from client to server
	 * @param client - once package created will call this method to send data to client
	 */
	@Override
	public void writeData(Client client) {
		if(client==null)System.out.println("null client");
		client.sendData(getData());

	}
	/**
	 * writeData(server) - this method is going to send data from server to all client (except current player)
	 * @param server - once package received from client and broadcast to all client
	 */
	@Override
	public void writeData(Server server) {
		server.sendDataToAllClients(getData());
	}
	
	/**
	 * getData - this method is going to return a bytes array with message without type
	 * @return byte array
	 */
	@Override
	public byte[] getData() {
		return ("00" + this.username).getBytes();
	}

	public String getUsername() {
		return username;
	}

	@Override
	public byte[] getRealData() {
		// TODO Auto-generated method stub
		return null;
	}




}
