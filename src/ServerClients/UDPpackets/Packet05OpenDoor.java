package ServerClients.UDPpackets;

import java.awt.Point;

import ServerClients.Client;
import ServerClients.Server;
/**
 * this class is extends from UDPPacket abstract class
 * main function for this class:
 * create a openDoor message and change to byteCode
 * or change from byteCode to string message
 *
 * @author zhaojiang chang - 300282984
 * */
public class Packet05OpenDoor extends UDPPacket {
	private String name;
	private Point point;
	private byte[] data;
	private int x;
	private int y;
	/**
	 * Constructor - creates a Packet05OpenDoor package
	 * @param name  - string open
	 * @param Point - door location
	 */
	public Packet05OpenDoor(String name, Point point) {
		super(05);
		this.point = point;
		this.name = name;

	}
	/**
	 * Constructor - creates a Packet05OpenDoor package
	 * @param data  - received from server and change byte array to string message
	 */
	public Packet05OpenDoor(byte[] data) {
		super(05);
		this.data = data;
		String[]dataArray = readData(data).split(",");
		for(int i = 0; i<dataArray.length; i++){
			System.out.println(dataArray[i]);
		}
		this.name = dataArray[1];
		this.x = Integer.parseInt(dataArray[2]);
		this.y = Integer.parseInt(dataArray[3]);
		this.point = new Point(x,y);
		System.out.println("Packet05OpenDoor con 2: ");
	}


	/**
	 * writeData(client) - this method is going to send data from client to server
	 * @param client - once openDoor package created will call this method to send data to client
	 */
	@Override
	public void writeData(Client client) {
		client.sendData(getData());
		System.out.println("Packet05OpenDoor con 3: ");

	}

	/**
	 * writeData(server) - this method is going to send data from server to all client (except current player)
	 * @param server - once package received from client and broadcast to all client
	 */
	@Override
	public void writeData(Server server) {
		System.out.println("Packet05OpenDoor con 4: ");
		server.sendActionDataToAllClients(getData());

		System.out.println("Packet05OpenDoor con 5: ");
	}

	/**
	 * getData - this method is going to return a bytes array with PacketTypes and door open message and location
	 * @return byte array
	 */
	@Override
	public byte[] getData() {
		return ("05"+ ","+this.name +","+this.point.x+","+this.point.y).getBytes();
	}
	/**
	 * getRealData - this method is going to return a bytes array with door open message and location
	 * @return byte array
	 */
	public byte[] getRealData(){
		return(this.name +","+this.point.x+","+this.point.y).getBytes();
	}
	/**
	 * this method is going to return a string open
	 * @return string open
	 */
	public String getName() {
		return name;
	}
	/**
	 * this method is going to return a Point door location
	 * @return Point
	 */
	public Point getPoint() {
		return point;
	}


}


