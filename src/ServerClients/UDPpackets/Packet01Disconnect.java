package ServerClients.UDPpackets;

import ServerClients.Client;
import ServerClients.Server;

/**
 * A packet01Disconnect to handle remove player from the game  
 * @author zhaojiang chang - 300282984
 */

public class Packet01Disconnect extends UDPPacket {

    private String username;
    /**Constructor - unpack a  package
	 * @param data - byte array
	 * 
	 * */
    public Packet01Disconnect(byte[] data) {
        super(01);
        this.username = readData(data);
    }
    /**Constructor - creates a disconnect package
	 * @param username - remove player from game 
	 * 
	 * */
    public Packet01Disconnect(String username) {
        super(01);
        this.username = username;
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

    @Override
    public byte[] getData() {
        return ("01" + this.username).getBytes();
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
