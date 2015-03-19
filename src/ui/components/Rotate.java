package ui.components;

import java.awt.geom.Point2D;

import javax.media.opengl.GL2;

public class Rotate implements Behaviour
{
	private int rotate = 0;
	
	public Rotate( int rotation )
	{
		rotate = rotation;
	}

	@Override
	public void modify( GL2 gl, Point2D.Float pos )
	{
		gl.glRotatef( rotate, 0f, 0f, 1.f );
		rotate = (rotate + 1) % 360;
	}

	/**
	 * @param args
	 */
	public static void main( String[] args ){}

	@Override
	public boolean activate()
	{
		return false;
	}

}
