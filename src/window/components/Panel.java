package window.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The Panel class represents JPanels on the GUI at the game entry stage.
 * Panels has the GUI that it is on.
 * 
 * @author Zhiheng Sun,  ID: 300256273
 * 
 */
public abstract class Panel extends JPanel{

	protected GUI gui;	// the GUI that panel is on

	public Panel (GUI gui){
		this.gui = gui;
		// set the panel to transparent and call methods to set up buttons and listener
		setOpaque(false);
		setUpComponents();
		addListenner();
	}

	/**
	 * The following method initializes the components on it and calls method to set the 
	 * style of the buttons
	 */
	protected abstract void setUpComponents();

	/**
	 * The following method adds action listeners onto buttons of the panel
	 */
	protected abstract void addListenner();

	/**
	 * The following method sets the button style by the given 
	 * characteristics and adds the button onto the panel
	 * @param button	the given button to set style on
	 * @param buttonWidth	the given width of the button
	 * @param defaultColor	the default color of the given button
	 */
	protected void setButtonStyle (final JButton button, int buttonWidth, final Color defaultColor){
		// set the button size and font
		button.setPreferredSize(new Dimension(buttonWidth, 60));
		button.setFont(new Font("Arial", Font.PLAIN, 30));
		button.setForeground(defaultColor);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setBorder(null);

		// set the button to transparent
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusPainted(false);

		// add mouseListener onto the button 
		button.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {
				button.setForeground(defaultColor);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setForeground(new Color(100, 200, 100).brighter());
			}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});

		// add the button to the panel
		add(button);
	}
}
