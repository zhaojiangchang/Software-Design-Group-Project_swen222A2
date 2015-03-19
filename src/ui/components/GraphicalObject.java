package ui.components;
import java.awt.Point;

import javax.media.opengl.GL2;

import world.components.CellType;


/**
 * @author Vivian Stewart
 * A visible 3D object occupying one square (cube) in the 3D environment
 * and game map.
 */
public interface GraphicalObject
{
	/**
	 * Called every frame to draw this graphical object
	 * @param gl - opengl drawing context for openGL 2.0
	 * @return if successful
	 */
	public boolean draw( GL2 gl );
	/**
	 * initialise any resources need to draw the Graphical Object
	 * i.e displaylists and meshes.
	 * @param gl - opengl drawing context
	 * @return if successful
	 */
	public Point getLocation();
	/**
	 * Opportunity Load meshes and incorporate in setup 
	 * @param gl
	 * @return
	 */
	public boolean initialise( GL2 gl );
	public boolean isDynamic();
	public CellType getType();
}
