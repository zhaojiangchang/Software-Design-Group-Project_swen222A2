package window.components;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import world.components.TokenType;
import world.game.Player;

/**
 * The CollectItemsCanvas is responsible for drawing tokens on canvas.
 * This class draws collected items in color, and draws uncollected items 
 * in grey.
 * 
 * @author Zhiheng Sun,  ID: 300256273
 * 
 */
public class CollectItemsCanvas extends Canvas {

	/**
	 * The following is a list of ImageIcons of the collected 
	 * items of the current player
	 */
	private ArrayList<ImageIcon> collectItems;

	/**
	 * The following is a list of booleans which indicate tokens 
	 * have been painted or not.
	 */
	private ArrayList<Boolean> isPaint;

	private Player player;	// the current player
	private TokenType type;	// the token type of the current player

	/**
	 * Sets up the CollectItemsCanvas for the given player
	 * @param player	the player whose tokens will be drawn on canvas
	 */
	public CollectItemsCanvas(Player player) {
		this.player = player;
		type = player.getType();
		this.setBackground(Color.BLACK);
		this.setBounds(0, 600, 750, 70);
		initialiseItems();
		getCollectItems();
	}

	/**
	 * The following method draws all the player's tokens in grey, 
	 * and sets all tokens unpainted.
	 */
	private void initialiseItems() {
		// go through the player's tokens, add grey images of player's tokens to collectItems
		collectItems = new ArrayList<ImageIcon>();
		for(int i = 0; i < player.getTokenList().size(); i++){
			String resource = "Resource/" + type.toString().toLowerCase() + "/" + type.toString().toLowerCase() + " grey" + ".png";
			collectItems.add(i, new ImageIcon(resource));			
		}

		// set all of the tokens unpainted
		isPaint = new ArrayList<Boolean>();
		for (int j = 0; j < player.getTokenList().size(); j++){
			isPaint.add(false);
		}
	}

	/**
	 * The following method gets the collected tokens of the player,
	 * and adds the corresponding images to the images list.
	 */
	private void getCollectItems() {		
		for (int i = 0; i < player.getTokenList().size(); i++){		// go through the player's tokens, add images of collected tokens to collectItems
			if (player.getTokenList().get(i).isFound() && !isPaint.get(i)){
				String color = identifyColor(player.getTokenList().get(i).getColor());	// get the color of the collected token
				String resource = "Resource/" + type.toString().toLowerCase() + "/" + type.toString().toLowerCase() + " " + color + ".png";
				collectItems.set(i, new ImageIcon(resource));	// replace the grey image with the color image of the token
				isPaint.set(i, true);
			}
		}
	}

	/**
	 * The following method returns the string of the given color, which is 
	 * useful to get image name.
	 * @param color	the given color
	 * @return	the string of the given color
	 */
	public String identifyColor(Color color) {
		if (color.equals(Color.BLUE)){
			return "blue";
		} else if (color.equals(Color.GREEN)){
			return "green";
		} else if (color.equals(Color.MAGENTA)){
			return "magenta";
		} else if (color.equals(Color.RED)){
			return "red";
		} else if (color.equals(Color.YELLOW)){
			return "yellow";
		}
		return null;
	}

	/**
	 * The following method draws all the images on the CollectItemsCanvas.
	 */
	public void paint(Graphics g) {
		getCollectItems();
		int gap = 65;
		for(int i = 0; i < collectItems.size(); i++){	// go through the player's tokens images, draw the images on canvas with given width and height
			g.drawImage(collectItems.get(i).getImage(), gap * i, 0, 65, 65, null);
		}

		// set the player name string style and draw it on canvas
		g.setFont(new Font("Arial", Font.BOLD, 31));
		g.setColor(Color.WHITE);
		String info = "Player : " + player.getName();
		g.drawString(info, 420, 45);
	}
}

