package world.game;

import java.net.InetAddress;

import world.components.TokenType;


public class MultyPlayer extends Player {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public InetAddress ipAddress;
	public int port;
	int x;
	int y;
	//private Map floor;

	public MultyPlayer( String username, TokenType type,InetAddress ipAddress, int port) {
		super(username, type);
		this.ipAddress = ipAddress;
		this.port = port;
	}

	

}