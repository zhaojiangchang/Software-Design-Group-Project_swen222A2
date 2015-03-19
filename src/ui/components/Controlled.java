package ui.components;

import java.awt.Point;
import java.awt.geom.Point2D.Float;

import javax.media.opengl.GL2;

public class Controlled implements Behaviour
{

	public Controlled(){}

	/**
	 * Allow game logic control of this other players position
	 *  (non-Javadoc)
	 * @see ui.components.Behaviour#modify(javax.media.opengl.GL2, java.awt.geom.Point2D.Float)
	 */
	@Override
	public void modify( GL2 gl, Float pos )
	{

		GameViewData gdata = GameViewData.instance();
		Point oldLocation = new Point( (int) (pos.x/GameView.cellsize)
									 , (int) (pos.y/GameView.cellsize) );
		Point newLocation = gdata.getOtherPlayerMove().get( oldLocation );
		if ( newLocation == null ) return;
		gdata.movePlayer( oldLocation, newLocation );
		pos.setLocation( (newLocation.x * GameView.cellsize + (GameView.cellsize/2f))
				, (newLocation.y * GameView.cellsize + (GameView.cellsize/2f)) );
		System.out.println(
				"move player from:" + oldLocation + "->" + newLocation );
		gdata.removePlayerMove( oldLocation );
	}

	@Override
	public boolean activate()
	{
		return true;
	}

}
