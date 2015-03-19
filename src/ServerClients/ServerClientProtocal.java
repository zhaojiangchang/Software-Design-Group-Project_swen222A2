/**
 * 
 */
package ServerClients;

/**
 * @author  Zhaojiang Chang
 *
 */
public interface ServerClientProtocal {
	int PROTOCOL_LEN = 2;
	String playerNameRep = "-1";
	String loginSuccess = "1";//login success
	String chatSent = "@@";//for player sent message to other players
	String playerListSent = "**";//for server send playerList to all clients
	String playerName = "!!";//for check player name
	String KeyListeners = "^^";
}
