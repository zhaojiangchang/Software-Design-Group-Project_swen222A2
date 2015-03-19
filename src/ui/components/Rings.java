package ui.components;

import java.awt.geom.Point2D;
import javax.media.opengl.GL2;

/**
 * @author Vivian Stewart
 * Animation that moves teleport rings continuously up and down
 */
public class Rings implements Behaviour
{

	private static final float ground = GameView.cellsize / 2f
			, ceiling = GameView.cellsize + GameView.cellsize - ground;
	private float	move = ground, moveSpeed = 0.1f;

	public Rings( boolean start )
	{
		move = start ? ground : ceiling + .1f;
	}

	@Override
	public void modify( GL2 gl, Point2D.Float pos )
	{
		if ( move > ceiling )
		{
			moveSpeed = -0.05f * ((float)Math.random() + .9f);
			move = ceiling;
		}
		else if ( move < ground )
		{
			moveSpeed = 0.05f * ((float)Math.random() + .9f);
			move = ground;
		}
		gl.glTranslated( 0f, 0f, move );
		move += moveSpeed;
	}

	@Override
	public boolean activate()
	{
		// TODO react to teleportation.
		return false;
	}

}
