package window.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * The ServerStartsPanel class is a JPanel which is represented on the frame 
 * once the server is started. ServerStartsPanel class is responsible for 
 * showing player server information
 * 
 * @author Zhiheng Sun,  ID: 300256273
 * 
 */
public class ServerStartsPanel extends Panel{

	public ServerStartsPanel(GUI gui) {
		super(gui);
		setBounds(150, 180, 500, 500);	
	}

	@Override
	protected void setUpComponents() {
		// labels, textFields and button used on serverStartsPanel
		JLabel serverStarts = new JLabel("SERVER STARTS!");
		serverStarts.setPreferredSize(new Dimension(450, 100));
		serverStarts.setFont(new Font("Arial", Font.BOLD, 50));
		serverStarts.setForeground(new Color(100, 200, 100).brighter());

		JLabel information = new JLabel("The following information is for client to join server");
		information.setPreferredSize(new Dimension(500, 40));
		information.setFont(new Font("Arial", Font.BOLD, 20));
		information.setForeground(new Color(0, 135, 200).brighter());

		JLabel name = new JLabel("Server IP : ");

		name.setPreferredSize(new Dimension(150, 60));
		name.setFont(new Font("Arial", Font.PLAIN, 20));
		name.setForeground(new Color(0, 135, 200).brighter());

		gui.getServerName().setPreferredSize(new Dimension(250, 40));
		gui.getServerName().setFont(new Font("Arial", Font.PLAIN, 20));
		gui.getServerName().setForeground(new Color(30, 30, 30));
		gui.getServerName().setText(gui.getStrServerName());
		gui.getServerName().setEditable(false);

		JLabel port = new JLabel("Port Number : ");

		port.setPreferredSize(new Dimension(150, 60));
		port.setFont(new Font("Arial", Font.PLAIN, 20));
		port.setForeground(new Color(0, 135, 200).brighter());

		gui.getPortNum().setPreferredSize(new Dimension(130, 40));
		gui.getPortNum().setFont(new Font("Arial", Font.PLAIN, 20));
		gui.getPortNum().setForeground(new Color(30, 30, 30));
		gui.getPortNum().setText(gui.getStrPortNum());
		gui.getPortNum().setEditable(false);

		add(serverStarts);
		add(information);
		add(name);
		add(gui.getServerName());
		add(port);
		add(gui.getPortNum());

	}

	@Override
	protected void addListenner() {}

}
