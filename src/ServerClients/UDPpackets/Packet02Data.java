package ServerClients.UDPpackets;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import world.components.Map;
import world.game.GameState;
import world.game.Player;
import ServerClients.Client;
import ServerClients.Server;

/**
 * A packet02Data to handle state data packets 
 * @author zhaojiang chang - 300282984
 *
 */
public class Packet02Data extends UDPPacket {

	private String username;
	private GameState state;

	private byte[] data;

	/**Constructor - creates a state data package
	 * @param username
	 * 
	 * */
	public Packet02Data( String username) {
		super(02);
		this.username = username;
	}
	/**Constructor - unpack a state data package
	 * @param data - byte array - serialize gameState
	 * 
	 * */

	public Packet02Data(byte[] data) {
		super(02);
		this.data = data;
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
		server.sendDataToAllClients(getData());
	}
	/**
	 * getRealData - this method is going to return a bytes array with message with type
	 * @return byte array
	 */

	public byte[] getRealData(){
		byte[]oldD = getData();
		byte[]newD =new byte[oldD.length-2];


		for(int i = 2; i<oldD.length;i++){
			newD[i-2] = oldD[i];
			if(newD[i-2] != oldD[i]){
			}

		}
		return newD;
	}
	
	/**
	 * getData - this method is going to return a bytes array with message without type
	 * @return byte array
	 */
	@Override
	public byte[] getData() {

		byte[]newData =new byte[data.length];
		byte[] a = "02".getBytes();

		newData[0] = a[0];
		newData[1] = a[1];
		for(int i = 0; i<data.length;i++){
			newData[i] = data[i];

		}
		return newData;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}




}
