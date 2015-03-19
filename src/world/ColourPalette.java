package world;

import java.awt.Color;
import java.util.ArrayList;

/**
 * The palette of Colors for this game.  Includes a mapping of the java.awt.Color object to a simple String name.
 * @author Kalo Pilato - ID: 300313803
 *
 */
public class ColourPalette {

	private static ArrayList<Color> colours;
	private static ArrayList<String> names;

	public static final Color LIGHTGREY1 = Color.decode("#f5f3ee");
	public static final Color LIGHTGREY2 = Color.decode("#dcd9d2");
	public static final Color LIGHTGREY3 = Color.decode("#edebe7");
	public static final Color LIGHTGREY4 = Color.decode("#ffffff");
	public static final Color LIGHTGREY5 = Color.decode("#f5f3ee");
	public static final Color PALEBLUE = Color.decode("#4a96ad");
	public static final Color MAROON = Color.decode("#7d1935");
	public static final Color LIGHTOCEANBLUE = Color.decode("#afeeee");
	public static final Color NEUTRALGREY = Color.decode("#808080");
	public static final Color PALEGREEN = Color.decode("#649987");
	public static final Color GREY1 = Color.decode("#b0acb4");
	public static final Color GREY2 = Color.decode("#848187");
	public static final Color GREY3 = Color.decode("#dcd7e1");
	public static final Color GREYPURPLE = Color.decode("#bfacc0");
	public static final Color PALEOCEANBLUE = Color.decode("#ccefec");
	public static final Color LIGHTOCEANGREEN = Color.decode("#c9ffd9");
	public static final Color LIGHTOCEANBLUE2 = Color.decode("#d3f9f1");
	public static final Color LIGHTOCEANGREEN2 = Color.decode("#daf4ea");
	public static final Color LIGHTOCEANBLUE3 = Color.decode("#c9fff4");
	public static final Color LIGHTOCEANGREEN4 = Color.decode("#d5ffe6");
	public static final Color LIGHTOCEANGREEN5 = Color.decode("#d0ffe3");
	public static final Color PALEOCEANGREEN = Color.decode("#d1f7e7");
	public static final Color PALEOCEANGREEN2 = Color.decode("#c7fceb");
	public static final Color BAIGE = Color.decode("#ffddc7");
	public static final Color LIGHTOCEANGREEN6 = Color.decode("#c7ffdd");
	public static final Color TAN = Color.decode("#ffc7a2");
	public static final Color DARKGREY = Color.decode("#383838");
	public static final Color MELLOWPINK = Color.decode("#ff7373");
	public static final Color LIGHTGREY6 = Color.decode("#dddddd");
	public static final Color GREY4 = Color.decode("#999999");
	public static final Color PALEOCEANBLUE2 = Color.decode("#c4f2e9");
	public static final Color PALEOCEANGREEN3 = Color.decode("#caf9eb");
	public static final Color GREYPURPLE2 = Color.decode("#b8a3b9");
	public static final Color DARKPURPLE = Color.decode("#603163");
	public static final Color NEUTRALGREY2 = Color.decode("#595959");
	public static final Color NEUTRALGREY3 = Color.decode("#474747");

	/**
	 * Constructor - builds the predefined collection of Colors for this ColourPalette
	 */
	public ColourPalette(){
		colours = new ArrayList<Color>();
		names = new ArrayList<String>();
		colours.add(Color.BLUE);
		names.add("Blue");
		colours.add(Color.GREEN);
		names.add("Green");
		colours.add(Color.YELLOW);
		names.add("Yellow");
		colours.add(Color.MAGENTA);
		names.add("Magenta");
		colours.add(Color.RED);
		names.add("Red");
	}

	/**
	 * Returns the Color at a given index in this ColourPalette
	 * @param index the index of the Color
	 * @return the Color at index
	 */
	public static Color get(int index){
		System.out.println(index);
		if(index >= colours.size()) return null;
		return colours.get(index);
	}
	
	/**
	 * Returns the Color corresponding to a given name
	 * @param name the name of the Color to return
	 * @return the Color corresponding to the given name
	 */
	public static Color get(String name){
		int index = names.indexOf(name);
		return get(index);
	}

	/**
	 * Returns the name of the Color at a given index in this ColourPalette
	 * @param index the index of the Color
	 * @return the name of the Color
	 */
	public static String getName(int index){
		if(index >= names.size()) return null;
		return names.get(index);
	}

	/**
	 * Returns the size of this ColourPalette
	 * @return the size of this ColourPalette
	 */
	public static int size(){
		return colours.size();
	}

}
