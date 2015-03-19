package window.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * The ChooseNamePanel class is a JPanel which is represented on 
 * the frame once the player choose to start a single-mode game. ChooseNamePanel
 * class is responsible for letting player enter a player name.  
 * 
 * @author Zhiheng Sun,  ID: 300256273
 * 
 */
public class ChooseNamePanel extends Panel{

	// buttons on the panel
	private JButton jbStart;
	private JTextField textFieldName;

	public ChooseNamePanel(GUI gui) {
		super(gui);
		setBounds(325, 200, 150, 600);
	}

	@Override
	protected void setUpComponents() {
		// label, textField and button used on chooseNamePanel
		JLabel chooseName = new JLabel("Name:");
		textFieldName = new JTextField(6);
		jbStart = new JButton("START");

		chooseName.setPreferredSize(new Dimension(100, 60));
		chooseName.setFont(new Font("Arial", Font.PLAIN, 30));
		chooseName.setForeground(new Color(0, 135, 200).brighter());

		textFieldName.setPreferredSize(new Dimension(130, 40));
		textFieldName.setFont(new Font("Arial", Font.PLAIN, 30));
		textFieldName.setForeground(new Color(30, 30, 30));

		JLabel space = new JLabel("");
		space.setPreferredSize(new Dimension(100, 20));

		add(chooseName);
		add(textFieldName);
		add(space);
		setButtonStyle(jbStart, 110, Color.MAGENTA);
	}

	@Override
	protected void addListenner() {
		jbStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JButton button = (JButton) ae.getSource();
				if(button == jbStart){	// if button Start is clicked, chooseNamePanel will be removed and single-player mode game will be started
					if(!textFieldName.getText().equals("")){
						gui.setName(textFieldName.getText());
						gui.removePanel(ChooseNamePanel.this);
						gui.removePanel(gui.getBackgroundPanel());
						gui.startGame();
					}}}});

		textFieldName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.setName(textFieldName.getText());	// get the name player entered from the textField
			}
		});
	}

}
