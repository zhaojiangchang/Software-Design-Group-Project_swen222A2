package ServerClients.UDPpackets;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import world.components.MoveableObject;
import world.game.Player;
import ServerClients.Client;
import ServerClients.Server;

public class Packet08PickupKey extends UDPPacket {

	
	private String username;
	private Point point;
	private byte[] data;
	private int x;
	private int y;

	/**Constructor - creates a pickup key package
	 * @param username
	 * @param point - player location
	 * 
	 * */
	public Packet08PickupKey(String username, Point point) {
		super(-2);
		this.username = username;
		this.point = point;
	
	}
	
	/**Constructor - creates a pickup package
	 * @param data - byte array username and location
	 * 
	 * */
	public Packet08PickupKey(byte[] data) {
		super(-2);
		this.data = data;
		String[]dataArray = readData(data).split(",");
		this.username = dataArray[1];
		this.x = Integer.parseInt(dataArray[2]);
		this.y = Integer.parseInt(dataArray[3]);
		this.point = new Point(x,y);
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
		return ("-2"+ ","+this.username +","+this.point.x+","+this.point.y).getBytes();
	}
	/**
	 * getRealData - this method is going to return a bytes array with message without type
	 * @return byte array
	 */
	public byte[] getRealData(){
		return(this.username +","+this.point.x+","+this.point.y).getBytes();
	}
	public String getUsername() {
		return username;
	}
	public Point getPoint() {
		return point;
	}


}


