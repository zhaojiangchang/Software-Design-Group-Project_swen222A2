package ui.components;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;

import javax.media.opengl.GL2;

import world.components.CellType;

/**
 * @author Vivian Stewart
 * Any graphical object that exists persistently and never changes is rendered
 * through this class.
 */
public class StaticRender implements GraphicalObject
{

	public final CellType	type;
	private Point2D.Float	position;
	private float[]			meshColor, surfaceColor;
	private boolean	north, east, south, west;

	/**
	 * @param n, @param e, @param s, @param w indicate what this static mesh is
	 * surrounded by and hence what shape of it.
	 * @param position
	 * @param GameView.cellsize
	 */
	public StaticRender( CellType type, CellType[] nesw
			, Point position, Color meshColour )
	{	// walls connect with other walls or doors
		north = nesw[0] == CellType.WALL || nesw[0] == CellType.DOOR || nesw[0] == CellType.KEYDOOR;
		east = nesw[1] == CellType.WALL || nesw[1] == CellType.DOOR || nesw[1] == CellType.KEYDOOR;
		west = nesw[3] == CellType.WALL || nesw[3] == CellType.DOOR || nesw[3] == CellType.KEYDOOR;
		south = nesw[2] == CellType.WALL || nesw[2] == CellType.DOOR || nesw[2] == CellType.KEYDOOR;
		this.type = type;
		this.position = new Point2D.Float( position.x * GameView.cellsize
				, position.y * GameView.cellsize );
		this.meshColor = meshColour.getRGBColorComponents( null );
		this.surfaceColor = Color.BLACK.getRGBColorComponents( null );
	}

	/* (non-Javadoc)
	 * @see ui.components.GraphicalObject#draw(javax.media.opengl.GL2)
	 */
	@Override
	public boolean draw( GL2 gl )
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see ui.components.GraphicalObject#initialise(javax.media.opengl.GL2)
	 */
	@Override
	public boolean initialise( GL2 gl )
	{
		gl.glPushMatrix();
		gl.glTranslatef( position.x, position.y, 0 );
		switch ( type )
		{
		case WALL : drawWall( gl ); break;
		case DOOR : drawDoorArch( gl ); break;
		case TELEPORT : drawTeleport( gl ); break;
		case KEYDOOR : drawDoorArch( gl ); break;
		case EMPTY : drawFloorCeiling( gl ); break;
		default:
			break;
		}
		gl.glPopMatrix();
		return true;
	}

	/**
	 * Initialise the displaylist for this wall, to be called later.
	 * @param gl
	 * @param wallwidth
	 */
	private void drawWall( GL2 gl )
	{
		float wallwidth = GameView.cellsize/3.0f; // wall spacing
		gl.glBegin( GL2.GL_QUAD_STRIP ); // filled polygons of wall
		gl.glColor3fv( surfaceColor, 0 );
		gl.glVertex3f( wallwidth, wallwidth, 0 );
		gl.glVertex3f( wallwidth, wallwidth, 2 * GameView.cellsize );
		if ( west )
		{
			gl.glVertex3f( 0, wallwidth, 0 );
			gl.glVertex3f( 0, wallwidth, 2 * GameView.cellsize );
			gl.glVertex3f( 0, 2 * wallwidth, 0 );
			gl.glVertex3f( 0, 2 * wallwidth, 2 * GameView.cellsize );
		}
		gl.glVertex3f( wallwidth, 2 * wallwidth, 0 );
		gl.glVertex3f( wallwidth, 2 * wallwidth, 2 * GameView.cellsize );
		if ( south )
		{
			gl.glVertex3f( wallwidth, GameView.cellsize, 0 );
			gl.glVertex3f( wallwidth, GameView.cellsize, 2 * GameView.cellsize );
			gl.glVertex3f( 2 * wallwidth, GameView.cellsize, 0 );
			gl.glVertex3f( 2 * wallwidth, GameView.cellsize, 2 * GameView.cellsize );
		}
		gl.glVertex3f( 2 * wallwidth, 2 * wallwidth, 0 );
		gl.glVertex3f( 2 * wallwidth, 2 * wallwidth, 2 * GameView.cellsize );
		if ( east )
		{
			gl.glVertex3f( GameView.cellsize, 2 * wallwidth, 0 );
			gl.glVertex3f( GameView.cellsize, 2 * wallwidth, 2 * GameView.cellsize );
			gl.glVertex3f( GameView.cellsize, wallwidth, 0 );
			gl.glVertex3f( GameView.cellsize, wallwidth, 2 * GameView.cellsize );
		}
		gl.glVertex3f( 2 * wallwidth, wallwidth, 0 );
		gl.glVertex3f( 2 * wallwidth, wallwidth, 2 * GameView.cellsize );
		if ( north )
		{
			gl.glVertex3f( 2 * wallwidth, 0, 0 );
			gl.glVertex3f( 2 * wallwidth, 0, 2 * GameView.cellsize );
			gl.glVertex3f( wallwidth, 0, 0 );
			gl.glVertex3f( wallwidth, 0, 2 * GameView.cellsize );
		}
		gl.glVertex3f( wallwidth, wallwidth, 0 );
		gl.glVertex3f( wallwidth, wallwidth, 2 * GameView.cellsize );
		gl.glEnd();
		gl.glBegin( GL2.GL_LINE_LOOP ); // lines around the bottom edges
		gl.glColor3fv( meshColor, 0 );
		gl.glVertex3f( wallwidth, wallwidth, 0 );
		if ( west )
		{
			gl.glVertex3f( 0, wallwidth, 0 );
			gl.glVertex3f( 0, 2 * wallwidth, 0 );
		}
		gl.glVertex3f( wallwidth, 2 * wallwidth, 0 );
		if ( south )
		{
			gl.glVertex3f( wallwidth, GameView.cellsize, 0 );
			gl.glVertex3f( 2 * wallwidth, GameView.cellsize, 0 );
		}
		gl.glVertex3f( 2 * wallwidth, 2 * wallwidth, 0 );
		if ( east )
		{
			gl.glVertex3f( GameView.cellsize, 2 * wallwidth, 0 );
			gl.glVertex3f( GameView.cellsize, wallwidth, 0 );
		}
		gl.glVertex3f( 2 * wallwidth, wallwidth, 0 );
		if ( north )
		{
			gl.glVertex3f( 2 * wallwidth, 0, 0 );
			gl.glVertex3f( wallwidth, 0, 0 );
		}
		gl.glEnd();
		gl.glBegin( GL2.GL_LINE_LOOP ); // lines around the top edges
		gl.glVertex3f( wallwidth, wallwidth, 2 * GameView.cellsize );
		if ( west )
		{
			gl.glVertex3f( 0, wallwidth, 2 * GameView.cellsize );
			gl.glVertex3f( 0, 2 * wallwidth, 2 * GameView.cellsize );
		}
		gl.glVertex3f( wallwidth, 2 * wallwidth, 2 * GameView.cellsize );
		if ( south )
		{
			gl.glVertex3f( wallwidth, GameView.cellsize, 2 * GameView.cellsize );
			gl.glVertex3f( 2 * wallwidth, GameView.cellsize, 2 * GameView.cellsize );
		}
		gl.glVertex3f( 2 * wallwidth, 2 * wallwidth, 2 * GameView.cellsize );
		if ( east )
		{
			gl.glVertex3f( GameView.cellsize, 2 * wallwidth, 2 * GameView.cellsize );
			gl.glVertex3f( GameView.cellsize, wallwidth, 2 * GameView.cellsize );
		}
		gl.glVertex3f( 2 * wallwidth, wallwidth, 2 * GameView.cellsize );
		if ( north )
		{
			gl.glVertex3f( 2 * wallwidth, 0, 2 * GameView.cellsize );
			gl.glVertex3f( wallwidth, 0, 2 * GameView.cellsize );
		}
		gl.glEnd();
		// vertical lines along the z-axis in the corners of the room
		gl.glBegin( GL2.GL_LINES );
		if ( west && north )
		{
			gl.glVertex3f( wallwidth - 0.1f, wallwidth - 0.1f, 2 * GameView.cellsize );// 1, 1
			gl.glVertex3f( wallwidth - 0.1f, wallwidth - 0.1f, 0 );
		} if ( west && south )
		{
			gl.glVertex3f( wallwidth - 0.1f, 2 * wallwidth + 0.1f, 2 * GameView.cellsize );// 1, 2
			gl.glVertex3f( wallwidth - 0.1f, 2 * wallwidth + 0.1f, 0 );
		} if ( north && east )
		{
			gl.glVertex3f( 2 * wallwidth + 0.1f, wallwidth - 0.1f, 2 * GameView.cellsize );// 2, 1
			gl.glVertex3f( 2 * wallwidth + 0.1f, wallwidth - 0.1f, 0 );
		} if ( east && south )
		{
			gl.glVertex3f( 2 * wallwidth + 0.1f, 2 * wallwidth + 0.1f, 2 * GameView.cellsize );// 2, 2
			gl.glVertex3f( 2 * wallwidth + 0.1f, 2 * wallwidth + 0.1f, 0 );
		}
		gl.glEnd();
	}

	public void drawTeleport( GL2 gl )
	{
		MeshStore m = MeshStore.instance();
		Mesh mesh = m.getMesh( CellType.TELEPORT );
		List<float[]>	vertices = mesh.getVertices();
		List<int[]>		indices = mesh.getIndices();
		gl.glScalef( GameView.cellsize, GameView.cellsize, GameView.cellsize );
		renderMesh( gl, vertices, indices );
	}

	private void renderMesh( GL2 gl, List<float[]> vertices, List<int[]> indices )
	{
		for ( int[] i: indices )
		{
			switch ( i.length )
			{
			case 4:
				gl.glBegin( GL2.GL_QUADS );
				gl.glColor3fv( surfaceColor, 0 );
				gl.glVertex3fv( vertices.get( i[0] ), 0 );
				gl.glVertex3fv( vertices.get( i[1] ), 0 );
				gl.glVertex3fv( vertices.get( i[2] ), 0 );
				gl.glVertex3fv( vertices.get( i[3] ), 0 );
				gl.glEnd();
				gl.glBegin( GL2.GL_LINE_LOOP );
				gl.glColor3fv( meshColor ,0 );
				gl.glVertex3fv( vertices.get( i[0] ), 0 );
				gl.glVertex3fv( vertices.get( i[1] ), 0 );
				gl.glVertex3fv( vertices.get( i[2] ), 0 );
				gl.glVertex3fv( vertices.get( i[3] ), 0 );
				gl.glEnd();
				break;
			case 3:
				gl.glBegin( GL2.GL_TRIANGLES );
				gl.glColor3fv( surfaceColor, 0 );
				gl.glVertex3fv( vertices.get( i[0] ), 0 );
				gl.glVertex3fv( vertices.get( i[1] ), 0 );
				gl.glVertex3fv( vertices.get( i[2] ), 0 );
				gl.glEnd();
				gl.glBegin( GL2.GL_LINE_LOOP );
				gl.glColor3fv( meshColor ,0 );
				gl.glVertex3fv( vertices.get( i[0] ), 0 );
				gl.glVertex3fv( vertices.get( i[1] ), 0 );
				gl.glVertex3fv( vertices.get( i[2] ), 0 );
				gl.glEnd();
				break;
			case 2:
				gl.glBegin( GL2.GL_LINES );
				gl.glColor3fv( meshColor ,0 );
				gl.glVertex3fv( vertices.get( i[0] ), 0 );
				gl.glVertex3fv( vertices.get( i[1] ), 0 );
				gl.glEnd();
				break;
			case 1:
				gl.glBegin( GL2.GL_POINTS );
				gl.glColor3fv( meshColor ,0 );
				gl.glVertex3fv( vertices.get( i[0] ), 0 );
				gl.glEnd();
				break;
			default: // n-gons
				gl.glBegin( GL2.GL_TRIANGLES );
				gl.glColor3fv( surfaceColor, 0 );
				// make triangle fan out of polygon
				int end = i.length - 1;
				for ( int j = 0; j < end - 1; ++j )
				{
					gl.glVertex3fv( vertices.get( end ), 0 );
					gl.glVertex3fv( vertices.get( i[j] ), 0 );
					gl.glVertex3fv( vertices.get( i[j + 1] ), 0 );
				}
				gl.glEnd();
				gl.glBegin( GL2.GL_LINE_LOOP );
				gl.glColor3fv( meshColor ,0 );
				for ( int j: i )
					gl.glVertex3fv( vertices.get( j ), 0 );
				gl.glEnd();
				break;
			}
		}
	}

	public void drawDoorArch( GL2 gl )
	{
		float doorwidth = GameView.cellsize/3.0f;
		if ( east && west )
		{
			gl.glBegin( GL2.GL_QUAD_STRIP ); // arch of doorway
			gl.glColor3fv( surfaceColor, 0 );
			gl.glVertex3f( 0,  doorwidth, 1.5f * GameView.cellsize );
			gl.glVertex3f( 0,  doorwidth, 2 * GameView.cellsize );
			gl.glVertex3f( 0,  2 * doorwidth, 1.5f * GameView.cellsize );
			gl.glVertex3f( 0,  2 * doorwidth, 2 * GameView.cellsize );
			gl.glVertex3f( GameView.cellsize,  2 * doorwidth, 1.5f * GameView.cellsize );
			gl.glVertex3f( GameView.cellsize,  2 * doorwidth, 2 * GameView.cellsize );
			gl.glVertex3f( GameView.cellsize,  doorwidth, 1.5f * GameView.cellsize );
			gl.glVertex3f( GameView.cellsize,  doorwidth, 2 * GameView.cellsize );
			gl.glVertex3f( 0,  doorwidth, 1.5f * GameView.cellsize );
			gl.glVertex3f( 0,  doorwidth, 2 * GameView.cellsize );
			gl.glEnd();
			gl.glBegin( GL2.GL_LINE_LOOP ); // side of doorway
			gl.glColor3fv( meshColor, 0 );
			gl.glVertex3f( 0,  doorwidth, 1.5f * GameView.cellsize );
			gl.glVertex3f( 0,  doorwidth, 0 );
			gl.glVertex3f( 0,  2 * doorwidth, 0 );
			gl.glVertex3f( 0,  2 * doorwidth, 1.5f * GameView.cellsize );
			gl.glEnd();
			gl.glBegin( GL2.GL_LINE_LOOP ); // other side of doorway
			gl.glVertex3f( GameView.cellsize,  doorwidth, 1.5f * GameView.cellsize );
			gl.glVertex3f( GameView.cellsize,  doorwidth, 0 );
			gl.glVertex3f( GameView.cellsize,  2 * doorwidth, 0 );
			gl.glVertex3f( GameView.cellsize,  2 * doorwidth, 1.5f * GameView.cellsize );
			gl.glEnd();
			gl.glBegin( GL2.GL_LINE_LOOP ); // line across the top of the arch
			gl.glVertex3f( 0,  doorwidth, 1.5f * GameView.cellsize );
			gl.glVertex3f( 0,  2 * doorwidth, 1.5f * GameView.cellsize );
			gl.glVertex3f(  GameView.cellsize,  2 * doorwidth, 1.5f * GameView.cellsize );
			gl.glVertex3f(  GameView.cellsize,  doorwidth, 1.5f * GameView.cellsize );
			gl.glEnd();
			gl.glBegin( GL2.GL_LINE_LOOP ); // lines underneath the arch
			gl.glVertex3f( 0,  doorwidth, 2 * GameView.cellsize );
			gl.glVertex3f( 0,  2 * doorwidth, 2 * GameView.cellsize );
			gl.glVertex3f( GameView.cellsize,  2 * doorwidth, 2 * GameView.cellsize );
			gl.glVertex3f( GameView.cellsize,  doorwidth, 2 * GameView.cellsize );
			gl.glEnd();
		}
		else
		{// same order as above but different orientation
			gl.glBegin( GL2.GL_QUAD_STRIP );
			gl.glColor3fv( surfaceColor, 0 );
			gl.glVertex3f(  doorwidth, 0, 1.5f * GameView.cellsize );
			gl.glVertex3f(  doorwidth, 0, 2 * GameView.cellsize );
			gl.glVertex3f(  2 * doorwidth, 0, 1.5f * GameView.cellsize );
			gl.glVertex3f(  2 * doorwidth, 0, 2 * GameView.cellsize );
			gl.glVertex3f(  2 * doorwidth,  GameView.cellsize, 1.5f * GameView.cellsize );
			gl.glVertex3f(  2 * doorwidth,  GameView.cellsize, 2 * GameView.cellsize );
			gl.glVertex3f(  doorwidth,  GameView.cellsize, 1.5f * GameView.cellsize );
			gl.glVertex3f(  doorwidth,  GameView.cellsize, 2 * GameView.cellsize );
			gl.glVertex3f(  doorwidth, 0, 1.5f * GameView.cellsize );
			gl.glVertex3f(  doorwidth, 0, 2 * GameView.cellsize );
			gl.glEnd();
			gl.glBegin( GL2.GL_LINE_LOOP );
			gl.glColor3fv( meshColor, 0 );
			gl.glVertex3f( doorwidth, 0, 1.5f * GameView.cellsize );
			gl.glVertex3f( doorwidth, 0, 0 );
			gl.glVertex3f( 2 * doorwidth, 0, 0 );
			gl.glVertex3f( 2 * doorwidth, 0, 1.5f * GameView.cellsize );
			gl.glEnd();
			gl.glBegin( GL2.GL_LINE_LOOP );
			gl.glVertex3f( doorwidth, GameView.cellsize, 1.5f * GameView.cellsize );
			gl.glVertex3f( doorwidth, GameView.cellsize, 0 );
			gl.glVertex3f( 2 * doorwidth, GameView.cellsize, 0 );
			gl.glVertex3f( 2 * doorwidth, GameView.cellsize, 1.5f * GameView.cellsize );
			gl.glEnd();
			gl.glBegin( GL2.GL_LINE_LOOP );
			gl.glVertex3f(  doorwidth, 0, 1.5f * GameView.cellsize );
			gl.glVertex3f(  2 * doorwidth, 0, 1.5f * GameView.cellsize );
			gl.glVertex3f(  2 * doorwidth,  GameView.cellsize, 1.5f * GameView.cellsize );
			gl.glVertex3f(  doorwidth,  GameView.cellsize, 1.5f * GameView.cellsize );
			gl.glEnd();
			gl.glBegin( GL2.GL_LINE_LOOP );
			gl.glVertex3f(  doorwidth, 0, 2 * GameView.cellsize );
			gl.glVertex3f(  2 * doorwidth, 0, 2 * GameView.cellsize );
			gl.glVertex3f(  2 * doorwidth,  GameView.cellsize, 2 * GameView.cellsize );
			gl.glVertex3f(  doorwidth,  GameView.cellsize, 2 * GameView.cellsize );
			gl.glEnd();
		}
	}

	private void drawFloorCeiling( GL2 gl )
	{
		int pad = 2, height = GameView.cellsize * 2;
		gl.glColor3fv( meshColor, 0 );
		gl.glBegin( GL2.GL_LINE_LOOP );
		gl.glVertex3f( pad, pad, 0 );
		gl.glVertex3f( GameView.cellsize - pad, pad, 0 );
		gl.glVertex3f( GameView.cellsize - pad, GameView.cellsize - pad, 0 );
		gl.glVertex3f( pad, GameView.cellsize - pad, 0 );
		gl.glEnd();
		gl.glBegin( GL2.GL_LINES );
		gl.glVertex3f( 0f, 0f, height );
		gl.glVertex3f( GameView.cellsize, GameView.cellsize, height );
		gl.glVertex3f( GameView.cellsize, 0f, height );
		gl.glVertex3f( 0f, GameView.cellsize, height );
		gl.glEnd();
		gl.glPointSize( 100f );
		gl.glBegin( GL2.GL_POINTS );
		gl.glVertex3f( GameView.cellsize/2f, GameView.cellsize/2f, height );
		gl.glEnd();
	}

	@Override
	public boolean isDynamic()
	{
		return false;
	}

	@Override
	public CellType getType()
	{
		return type;
	}

	@Override
	public Point getLocation()
	{
		return new Point( (int) (position.x / GameView.cellsize)
						, (int) (position.y / GameView.cellsize) );
	}

}
