package window.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import ServerClients.Server;

/**
 * The ChooseServerPanel class is a JPanel which is represented on the frame. 
 * ChooseServerPanel class is responsible for letting player choose to start 
 * a server or join a server  
 * 
 * @author Zhiheng Sun,  ID: 300256273
 * 
 */
public class ChooseServerPanel extends Panel{

	// buttons on the panel
	private JButton jbStartServer;
	private JButton jbJoinServer;

	public ChooseServerPanel(GUI gui) {
		super(gui);
		setBounds(300, 240, 200, 120);	
	}

	@Override
	protected void setUpComponents() {
		// buttons used on chooseServerPanel
		jbStartServer = new JButton("Start Server");
		jbJoinServer = new JButton("Join Server");

		setButtonStyle(jbStartServer, 170, new Color(0, 135, 200).brighter());
		setButtonStyle(jbJoinServer, 170, new Color(0, 135, 200).brighter());
	}

	@Override
	protected void addListenner() {
		jbStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JButton button = (JButton) ae.getSource();
				if(button == jbStartServer){	// if button Starts Server is clicked, chooseServerPanel will be removed and serverStartsPanel will appear
					String nP= JOptionPane.showInputDialog(
							gui.getFrame(), 
							"Enter the number of players of the game", 
							"Number of Players", 
							JOptionPane.INFORMATION_MESSAGE
							);
					if (nP == null){
						gui.removePanel(ChooseServerPanel.this);
					} else {
						boolean isStr = false;
						try {
							gui.setNumPlayer(Integer.parseInt(nP));
						} catch (NumberFormatException e){
							isStr = true;
						}
						if (isStr) {
						} else {
							gui.removePanel(ChooseServerPanel.this);
							try {
								gui.setStrServerName(gui.getSeverName().getHostAddress());
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							gui.setStrPortNum("4768");
							int port = Integer.parseInt(gui.getStrPortNum());
							while(!available(port)){
								port++;
							}
							gui.setStrPortNum(""+port);

							Server server = new Server(port, gui.getNumPlayer());
							server.start();

							gui.addPanel(new ServerStartsPanel(gui));
						}}}}});

		jbJoinServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JButton button = (JButton) ae.getSource();
				if(button == jbJoinServer){	// if button Starts Server is clicked, chooseServerPanel will be removed and joinServerPanel will appear
					gui.removePanel(ChooseServerPanel.this);
					gui.addPanel(new JoinServerPanel(gui));			
				}}});
	}

	/**
	 * The following method is to check the socket port 
	 * @param port the number is going to check
	 * \*/
	private static boolean available(int port) {
		DatagramSocket s = null;
		try {
			s = new DatagramSocket(port);

			// If the code makes it this far without an exception it means
			// something is using the port and has responded.
			s.close();
			return true;
		} catch (IOException e) {
			return false;
		} 
	}

}
