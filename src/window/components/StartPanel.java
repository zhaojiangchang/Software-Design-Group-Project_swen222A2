package window.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * The StartPanel class is a JPanel which is represented on the frame 
 * once the game is built. StartPanel class is responsible for letting
 * player start a new game  
 * 
 * @author Zhiheng Sun,  ID: 300256273
 * 
 */
public class StartPanel extends Panel{

	// buttons on the panel
	private JButton jbNew;
	private JButton jbExit;

	public StartPanel(GUI gui) {
		super(gui);
		setBounds(350, 240, 90, 600);
	}

	@Override
	protected void setUpComponents() {
		// buttons used on startPanel
		jbNew = new JButton("New");
		jbExit = new JButton("Exit");
		
		setButtonStyle(jbNew, 75, new Color(0, 135, 200).brighter());
		setButtonStyle(jbExit, 65, new Color(0, 135, 200).brighter());
	}

	@Override
	protected void addListenner() {
		jbNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JButton button = (JButton) ae.getSource();
				if(button == jbNew){	// if button New is clicked, startPanel will be removed and choosePlayerPanel will appear
					gui.removePanel(StartPanel.this);
					gui.addPanel(new ChoosePlayerPanel(gui));
				}}});

		jbExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				System.exit(0);	// if button Exit is clicked, the frame will be closed
			}
		});
	}
}
