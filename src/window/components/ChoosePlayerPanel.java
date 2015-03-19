package window.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * The ChoosePlayerPanel class is a JPanel which is represented on 
 * the frame once the player choose to start a new game. ChoosePlayerPanel
 * class is responsible for letting player choose to start a single-player
 * mode game or a multiple-player mode game.  
 * 
 * @author Zhiheng Sun,  ID: 300256273
 * 
 */
public class ChoosePlayerPanel extends Panel{

	// buttons on the panel
	private JButton jbSingle;
	private JButton jbMultiple;

	public ChoosePlayerPanel(GUI gui) {
		super(gui);
		setBounds(325, 240, 150, 120);
	}

	@Override
	protected void setUpComponents() {
		// buttons used on choosePlayerPanel
		jbSingle = new JButton("Single");
		jbMultiple = new JButton("Multiple");

		setButtonStyle(jbSingle, 95, new Color(0, 135, 200).brighter());
		setButtonStyle(jbMultiple, 115, new Color(0, 135, 200).brighter());
	}

	@Override
	protected void addListenner() {
		jbSingle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JButton button = (JButton) ae.getSource();
				if(button == jbSingle){	// if button Single is clicked, choosePlayerPanel will be removed and chooseNamePanel will appear
					gui.removePanel(ChoosePlayerPanel.this);
					gui.addPanel(new ChooseNamePanel(gui));
				}}});

		jbMultiple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JButton button = (JButton) ae.getSource();
				if(button == jbMultiple){	// if button Multiple is clicked, choosePlayerPanel will be removed and chooseServerPanel will appear
					gui.removePanel(ChoosePlayerPanel.this);
					gui.addPanel(new ChooseServerPanel(gui));
				}}});
	}
}
