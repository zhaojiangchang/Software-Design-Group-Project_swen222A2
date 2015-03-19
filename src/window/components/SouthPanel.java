package window.components;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import world.game.Player;

/**
 * The SouthPanel is responsible for setting up a JPanel to hold CollectItemsCanvas
 * and UsefulItemsCanvas. This class is added to the bottom of the game window.
 * 
 * @author Zhiheng Sun,  ID: 300256273
 * 
 */
public class SouthPanel {

	/**
	 * The following stores the panel shown on the bottom of the frame which has player's 
	 * collected tokens images and player's inventory images
	 */
	private JPanel panel;

	private CollectItemsCanvas collectItemsCanvas;	// the canvas with player's collected tokens will be drawn on panel
	private UsefulItemsCanvas usefulItemsCanvas;	// the canvas with player's inventory will be drawn on panel

	// the position of the panel on the frame
	private static int left = 0;
	private static int top = 577;
	private static int width = 800;
	private static int height = 170;

	/**
	 * Sets up the panel for the given player
	 * @param player	the player whose tokens and inventories will be drawn on canvases
	 */
	public SouthPanel(Player player) {
		panel = new JPanel();
		panel.setBounds(left, top, width, height);
		panel.setBackground(Color.BLACK);

		// add a border to the panel
		panel.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(Color.WHITE, 5)));

		// add the two canvases onto the panel
		collectItemsCanvas = new CollectItemsCanvas(player);
		panel.add(collectItemsCanvas, BorderLayout.WEST);
		usefulItemsCanvas = new UsefulItemsCanvas(player);
		panel.add(usefulItemsCanvas, BorderLayout.WEST);
		panel.repaint();
	}

	/**
	 * The following method returns the current CollectItemsCanvas on the panel
	 * @return	the current CollectItemsCanvas
	 */
	public CollectItemsCanvas getCollectItemsCanvas() {
		return collectItemsCanvas;
	}

	/**
	 * The following method returns the current UsefulItemsCanvas on the panel
	 * @return	the current UsefulItemsCanvas
	 */
	public UsefulItemsCanvas getUsefulItemsCanvas() {
		return usefulItemsCanvas;
	}

	/**
	 * The following method returns the current panel 
	 * @return	the current panel
	 */
	public JPanel getPanel() {
		return panel;
	}
}
