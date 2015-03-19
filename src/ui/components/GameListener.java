package ui.components;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import controllers.NetworkController;

/**
 * @author Vivian Stewart
 * Mouse and Keyboard control of the player in this network conneected client 
 */
public class GameListener implements KeyListener, MouseListener
{
	private float			direction = 0f; // buffered direction
	private boolean			keyUpdate = false; // toggle actual and sustained updates 
	private boolean			wKey = false, aKey = false, sKey = false
						  , dKey = false, ctrlKey = false;
	private final float		speed = 0.5f; // forward/reverse constant
	private final float		turnSpeed = 0.05f;
	private Point2D.Float	position; // buffered position
	private Point			click = null; // buffered mouse selection
	private GameScene		scene; 

/**
 * 
 * @param position - start position of the player
 * @param direction - start direction of the player
 * @param map - map of surrounding objects
 */
	public GameListener( Point2D.Float position, float direction, GameScene map )
	{
		this.position = new Point2D.Float( position.x, position.y );
		this.direction = direction;
		this.scene = map;
	}

/**
 * Buffered continuous y value.
 * @return new player position y
 */
	public float getNewX()
	{
		return position.x;
	}
/**
 * Buffered continuous x value
 * @return new player position x
 */
	public float getNewY()
	{
		return position.y;
	}
/**
 * Update happened so reset keyUpdate until it is needed again.
 */
	public void resetKeyUpdate()
	{
		this.keyUpdate = false;
	}

/**
 * Has a keyboard event updated the position/direction of the player.
 * @return if keys were pressed and changed the state.
 */
	public boolean isKeyUpdate()
	{
		return keyUpdate;
	}

	public float getDirection()
	{
		return direction;
	}

	@Override
	public void keyTyped( KeyEvent arg0 ){}

	@Override
	public void keyReleased( KeyEvent key )
	{ // turn off persistent key presses
		if ( key.getKeyCode() == KeyEvent.VK_W )
		{
			wKey = false;
		}
		else if ( key.getKeyCode() == KeyEvent.VK_S )
		{
			sKey = false;
		}
		else if ( key.getKeyCode() == KeyEvent.VK_A )
		{
			aKey = false;
		}
		else if ( key.getKeyCode() == KeyEvent.VK_D )
		{
			dKey = false;
		}
		if ( !key.isControlDown() ) ctrlKey = false;
	}

	@Override
	public void keyPressed( KeyEvent key )
	{ // Switch on persistant key presses. Key is either pressed or sustained
		if ( key.getKeyCode() == KeyEvent.VK_W || (!keyUpdate && wKey) )
		{
			movePos( direction - (float)Math.PI );
			wKey = true;
		}
		if ( key.getKeyCode() == KeyEvent.VK_S || (!keyUpdate && sKey) )
		{
			movePos( direction );
			sKey = true;
		}
		if ( key.getKeyCode() == KeyEvent.VK_A || (!keyUpdate && aKey) )
		{
			if ( key.isControlDown() ) // strafing
			{
				movePos( direction + 0.5f * (float)Math.PI );
				ctrlKey = true;
			}
			else
				addToDirection( -turnSpeed );
			aKey = true;
		}
		if ( key.getKeyCode() == KeyEvent.VK_D || (!keyUpdate && dKey) )
		{
			if ( key.isControlDown() ) // strafing
			{
				movePos( direction - 0.5f * (float)Math.PI );
				ctrlKey = true;
			}
			else
				addToDirection( turnSpeed );
			dKey = true;
		}
		keyUpdate = true;
	}

/**
 * keep moving and turning even if there are no key press or release events,
 * but only if keys have not been released and there has not been a recent
 * update.
 */
	public void update()
	{
		if ( !keyUpdate )
    	{	// forward/backward
	    	if ( wKey )
			{
	    		movePos( direction - (float)Math.PI );
			}
			if ( sKey )
			{
				movePos( direction );
			}
			// turn left/right or strafe left/right
			if ( aKey )
			{
				if ( ctrlKey ) movePos( direction + 0.5f * (float)Math.PI );
				else addToDirection( -turnSpeed );
			}
			if ( dKey )
			{
				if ( ctrlKey ) movePos( direction - 0.5f * (float)Math.PI );
				else addToDirection( turnSpeed );
			}
		}
	}

/**
 * Used when loading a new level through a portal collision.
 * @param map
 */
	public void reset( GameScene map )
	{
		this.scene = map;
		keyUpdate = false;
		wKey = false;
		aKey = false;
		sKey = false;
		dKey = false;
		ctrlKey = false;
	}

/**
 * Buffer a new player position to be updated when display() is called.
 * @param backward
 */
	private void movePos( float dir )
	{
		float newx = position.x + (float)Math.sin( dir ) * speed;
		float newy = position.y	+ (float)Math.cos( dir ) * speed;
		// movement crosses square boundaries
		boolean xcross = (int)(newx / GameView.cellsize)
				!= (int)(position.x / GameView.cellsize);
		boolean ycross = (int)(newy / GameView.cellsize)
				!= (int)(position.y / GameView.cellsize);
		// collision detection
		if ( ( xcross || ycross )
				&& scene.isCollidable( newx, newy, true ) )
		{
			// TODO: NetworkController.movePlayer( NetworkController.?, new Point( cellx, celly ) );
			if ( xcross && !scene.isCollidable( position.x, newy, false ) )
				position.setLocation( position.x, newy );
			else if ( ycross && !scene.isCollidable( newx, position.y, false ) )
				position.setLocation( newx, position.y );
		}
		else position.setLocation( newx, newy );
	}

/**
 * Adjust direction and constrained within 0 - 2 * PI (radians)
 * @param increment/decrement direction by f
 */
	private void addToDirection( float f )
	{
		direction += f;
		direction %= GameView.PI2;
	}

/**
 * Gets the point clicked to be unprojected by OpenGL to find selected
 * Graphical object. Resets the click to null to avoid retriggering.
 * @return current clicked point
 */
	public Point getClick()
	{
		Point p = click;
		click = null;
		return p;
	}

	@Override
	public void mouseClicked( MouseEvent e )
	{
		// System.out.println("Mouse Clicked");
		click = e.getPoint();
	}

	@Override
	public void mousePressed( MouseEvent e ){}

	@Override
	public void mouseReleased( MouseEvent e ){}

	@Override
	public void mouseEntered( MouseEvent e ){}

	@Override
	public void mouseExited( MouseEvent e ){}
}