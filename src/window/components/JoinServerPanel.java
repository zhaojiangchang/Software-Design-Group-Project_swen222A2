package window.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * The JoinServerPanel class is a JPanel which is represented on 
 * the frame once the player choose to start a game in a server. 
 * JoinServerPanel class is responsible for letting player enter 
 * the server information and then start the game.
 * 
 * @author Zhiheng Sun,  ID: 300256273
 * 
 */
public class JoinServerPanel extends Panel{

	// buttons on the panel
	private JButton jbClientStart;

	public JoinServerPanel(GUI gui) {
		super(gui);
		setBounds(200, 200, 500, 500);	
	}

	@Override
	protected void setUpComponents() {
		// labels, textFields and button used on joinServerPanel
		JLabel nameP = new JLabel("Player Name : ");

		nameP.setPreferredSize(new Dimension(150, 60));
		nameP.setFont(new Font("Arial", Font.PLAIN, 20));
		nameP.setForeground(new Color(0, 135, 200).brighter());

		gui.getTextFieldNameC().setPreferredSize(new Dimension(250, 40));
		gui.getTextFieldNameC().setFont(new Font("Arial", Font.PLAIN, 20));
		gui.getTextFieldNameC().setForeground(new Color(30, 30, 30));

		JLabel name = new JLabel("Server IP: ");
		String serverIP = null;
		try {
			serverIP = gui.getSeverName().getHostAddress();
			int countDot = 0;
			for(int i = 0; i<serverIP.length(); i++){
				if(serverIP.substring(i, i+1).equals(".")){
					countDot++;
					if(countDot==3){
						serverIP = serverIP.substring(0, i+1);
						break;
					}
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		gui.getServerNameC().setText(serverIP);

		name.setPreferredSize(new Dimension(150, 60));
		name.setFont(new Font("Arial", Font.PLAIN, 20));
		name.setForeground(new Color(0, 135, 200).brighter());

		gui.getServerNameC().setPreferredSize(new Dimension(250, 40));
		gui.getServerNameC().setFont(new Font("Arial", Font.PLAIN, 20));
		gui.getServerNameC().setForeground(new Color(30, 30, 30));

		JLabel port = new JLabel("Port Number : ");

		port.setPreferredSize(new Dimension(150, 60));
		port.setFont(new Font("Arial", Font.PLAIN, 20));
		port.setForeground(new Color(0, 135, 200).brighter());

		gui.getPortNumC().setPreferredSize(new Dimension(130, 40));
		gui.getPortNumC().setFont(new Font("Arial", Font.PLAIN, 20));
		gui.getPortNumC().setForeground(new Color(30, 30, 30));

		jbClientStart = new JButton("START");

		add(nameP);
		add(gui.getTextFieldNameC());
		add(name);
		add(gui.getServerNameC());
		add(port);
		add(gui.getPortNumC());
		setButtonStyle(jbClientStart, 110, Color.MAGENTA);
	}

	@Override
	protected void addListenner() {
		jbClientStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JButton button = (JButton) ae.getSource();
				if(button == jbClientStart){	// if button Start is clicked, joinServerPanel will be removed and multiple-player mode game will be started
					if(!gui.getTextFieldNameC().getText().equals("") && !gui.getServerNameC().getText().equals("") && !gui.getPortNumC().getText().equals("")){
						gui.setNameC(gui.getTextFieldNameC().getText());
						gui.setStrPortNumC(gui.getPortNumC().getText());
						gui.setStrServerNameC(gui.getServerNameC().getText());
						if(isIPAdd(gui.getStrServerNameC())){
							gui.removePanel(JoinServerPanel.this);
							gui.removePanel(gui.getBackgroundPanel());					
							gui.startGame2();
						}
					}}}
		});
		gui.getTextFieldNameC().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.setNameC(gui.getTextFieldNameC().getText());	// get the player name player entered from the textField
			}
		});
		gui.getServerNameC().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.setStrServerNameC(gui.getServerNameC().getText());	// get the server name player entered from the textField
			}
		});
		gui.getPortNumC().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.setStrPortNumC(gui.getPortNumC().getText());	// get the port number player entered from the textField
			}
		});
	}

	/**
	 * The following method is check player input server Ip address
	 * if input is not ip address will return false
	 * */
	public static final boolean isIPAdd(final String ip) {
		boolean isIPv4;
		try {
			final InetAddress inet = InetAddress.getByName(ip);
			isIPv4 = inet.getHostAddress().equals(ip)
					&& inet instanceof Inet4Address;
		} catch (final UnknownHostException e) {
			isIPv4 = false;
		}
		return isIPv4;
	}
}
