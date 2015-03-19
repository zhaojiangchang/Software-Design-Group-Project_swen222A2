/**
 *
 */
package ServerClients.UDPpackets;

import java.awt.Point;

import world.game.GameState;
import ServerClients.Client;
import ServerClients.Server;

/**
 * A Packet04Teleport to handle teleport packets 
 * @author zhaojiang chang - 300282984
 *
 */
public class Packet04Teleport extends UDPPacket {

	private String username;
	private int floorNumber;
	private byte[] data;

	/**Constructor - creates a teleport package
	 * @param username
	 * @param floorNumber
	 * 
	 * */
	public Packet04Teleport(String username, int floorNumber) {
		super(04);
		this.username = username;
		this.floorNumber = floorNumber;
	}
	
	/**Constructor - creates a teleport package
	 * @param  data - byte array - name and floornumber
	 *  
	 * */
	public Packet04Teleport(byte[] data) {
		super(04);
		this.data = data;
		String[]dataArray = readData(data).split(",");
		this.username = dataArray[1];
		this.floorNumber = Integer.parseInt(dataArray[2]);
	}


	/**
	 * writeData(client) - this method is going to send data from client to server
	 * @param client - once package created will call this method to send data to client
	 */
	@Override
	public void writeData(Client client) {
		client.sendData(getData());

	}

	/**
	 * writeData(server) - this method is going to send data from server to all client (except current player)
	 * @param server - once package received from client and broadcast to all client
	 */
	@Override
	public void writeData(Server server) {
		server.sendActionDataToAllClients(getData());
	}
	/**
	 * getRealData - this method is going to return a bytes array with message with type
	 * @return byte array
	 */
	
	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return ("04"+ ","+this.username +","+this.floorNumber).getBytes();
	}
	/**
	 * getRealData - this method is going to return a bytes array with message without type
	 * @return byte array
	 */
	public byte[] getRealData(){
		return(this.username +","+this.floorNumber).getBytes();
	}
	public String getUsername() {
		return username;
	}

	public int getFloorNumber(){
		return floorNumber;
	}


}
