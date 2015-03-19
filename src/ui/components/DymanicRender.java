package ui.components;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL2;

import world.ColourPalette;
import world.components.CellType;
import world.components.Direction;

public class DymanicRender implements GraphicalObject
{
	private Point2D.Float	position;
	private float[]			meshColor, selectColor;
	private CellType		type;
	private List<Behaviour>		anim;
	private List<float[]>	vertices;
	private List<int[]>		indices;
	private boolean			xaligned = false;

	/**
	 * Static constructors for each type of GraphicalObject
	 * @return new instance of renderable object.
	 */
	public static DymanicRender instanceDoor( Point position, Direction dir )
	{
		return new DymanicRender( CellType.DOOR, Behave.OPEN_CLOSE, position
				, dir, ColourPalette.GREYPURPLE );
	}
	
	public static DymanicRender instanceKeyDoor( Point position, Direction dir, Color col )
	{
		return new DymanicRender( CellType.KEYDOOR, Behave.OPEN_CLOSE, position
				, dir, col );
	}
	
	public static DymanicRender instanceTelePort( Point position )
	{
		return new DymanicRender( CellType.RINGS, Behave.RINGS, position
				, Direction.NORTH, Color.ORANGE );
	}
	
	public static DymanicRender instanceCone( Point position, Color col )
	{
		return new DymanicRender( CellType.CONE, Behave.ROTATE, position
			, Direction.NORTH, col );
	}
	
	public static DymanicRender instanceBall( Point position, Color col )
	{
		return new DymanicRender( CellType.BALL, Behave.ROTATE, position
			, Direction.NORTH, col );
	}
	
	public static DymanicRender instanceDiamond( Point position, Color col )
	{
		return new DymanicRender( CellType.DIAMOND, Behave.ROTATE, position
			, Direction.NORTH, col );
	}
	
	public static DymanicRender instanceCube( Point position, Color col )
	{
		return new DymanicRender( CellType.CUBE, Behave.ROTATE, position
			, Direction.NORTH, col );
	}
	
	public static DymanicRender instanceKey( Point position, Color col )
	{
		return new DymanicRender( CellType.KEY, Behave.ROTATE, position
			, Direction.NORTH, col );
	}
	
	public static DymanicRender instanceTorch( Point position, Color col )
	{
		return new DymanicRender( CellType.TORCH, Behave.ROTATE, position
			, Direction.NORTH, col );
	}
	
	public static DymanicRender instanceFurniture( CellType type, Behave act, Point position
			, Direction dir, Color meshColor )
	{
		return new DymanicRender( type, act, position, dir, meshColor );
	}
	
	public static DymanicRender instanceContainer( CellType type,
			Behave orientation, Point p, Direction facing, Color maroon )
	{
		return new DymanicRender( type, orientation, p, facing, maroon );
	}

	public static DymanicRender instancePlayer( Behave act, Point position
			, Direction dir, Color meshColor )
	{
		return new DymanicRender( CellType.PLAYER, act, position, dir, meshColor );
	}
	
	/**
	 * @param type - classification ID indicating the mesh to use
	 * @param act - what behaviour this object should have
	 * @param position - location in the map of squares
	 * @param dir - direction that this object should be facing
	 * @param meshColor - colour of the lines rendered around polygons and
	 * along lines
	 */
	private DymanicRender( CellType type, Behave act, Point position
			, Direction dir, Color meshColor )
	{
		// initial colour of outline an fill
		this.meshColor = meshColor.getRGBColorComponents( null );
		this.selectColor = Color.BLACK.getRGBColorComponents( null );
		this.type = type;
		// tokens, key and torch should be centered in the middle of the square
		if ( (type.ordinal() > CellType.OUTOFBOUNDS.ordinal()
				&& type.ordinal() < CellType.CHEST.ordinal())
					|| type == CellType.PLAYER )
			this.position = new Point2D.Float(
					  (position.x * GameView.cellsize) + (GameView.cellsize/2f)
					, (position.y * GameView.cellsize) + (GameView.cellsize/2f) );
		else
			this.position = new Point2D.Float(
				  position.x * GameView.cellsize
				, position.y * GameView.cellsize );
		this.xaligned = dir == Direction.NORTH || dir == Direction.SOUTH;
		// assign behaviours
		anim = new LinkedList<Behaviour>();
		switch ( act )
		{
		case ROTATE: anim.add( new Rotate( 0 ) ); break;
		case OPEN_CLOSE: anim.add( new OpenClose() ); break;
		case RINGS: 
			anim.add( new Rings( false ) );
			anim.add( new Rings( true ) );
			break;
		case ORIENTATION:
			if ( type == CellType.BED )
				anim.add( new Orientation( 3, 2, dir ) );
			else if ( type == CellType.COUCH || type == CellType.TABLE )
				anim.add( new Orientation( 2, 1, dir ) );
			else anim.add( new Orientation( 1, 1, dir ) );
			break;
		case CONTROLLED:
			anim.add( new Controlled() );
			break;
		default: break;
		}
		MeshStore m = MeshStore.instance();
		Mesh mesh = m.getMesh( type );
		vertices = mesh.getVertices();
		indices = mesh.getIndices();
	}

	/* (non-Javadoc)
	 * @see ui.components.GraphicalObject#draw(javax.media.opengl.GL2)
	 */
	@Override
	public boolean draw( GL2 gl )
	{
		if ( indices == null || vertices == null )
		{
			System.out.println( "Empty Vertices and Indices:" + type );
			System.exit( 1 );
		}
		if ( anim.isEmpty() ) // no modifications 
		{
			gl.glPushMatrix();
			gl.glTranslatef( position.x, position.y, 0 );
			gl.glScalef( GameView.cellsize, GameView.cellsize, GameView.cellsize );
			renderMesh( gl );
			gl.glPopMatrix();
			return true;
		}
		for ( Behaviour behave: anim ) // 1 or more modifications
		{
			gl.glPushMatrix();
			gl.glTranslatef( position.x, position.y, 0 );
			// doors need manual orientation as they have open/close behaviour
			if ( (type == CellType.DOOR || type == CellType.KEYDOOR) && !xaligned )
			{
				gl.glTranslatef( GameView.cellsize, 0f, 0f );
				gl.glRotatef( 90.0f, 0f, 0f, 1.f );
			}
			behave.modify( gl, position );
			gl.glScalef( GameView.cellsize, GameView.cellsize, GameView.cellsize );
			renderMesh( gl );
			gl.glPopMatrix();
		}
		return true;
	}
	/**
	 * behaviour defines collidability and can activate different behaviour.
	 * @return if player can walk through this GraphicalObject
	 */
	public boolean collide()
	{
		if ( anim.isEmpty() ) return true;
		boolean result = true;
		for ( Behaviour b: anim )
			result &= b.activate();
		return result;
	}

	/**
	 * All dynamic graphical objects are rendered from an imported from a .obj
	 * file mesh and drawn from the list of vertices and indices.
	 *  (non-Javadoc)
	 * @see ui.components.GraphicalObject#initialise(javax.media.opengl.GL2)
	 */
	@Override
	public boolean initialise( GL2 gl )
	{
		// initialisation as a bad idea for dynamic objects
		return true;
	}

	/**
	 * Renders the mesh of whatever type of GraphicalObject this happens to be.
	 * this method renders polygons (multi-sided and planar), quads, triangles,
	 * lines and points in the 'select' colour. Also draws the outline of each
	 * in the 'mesh' colour 
	 * @param gl
	 */
	private void renderMesh( GL2 gl )
	{
		for ( int[] i: indices )
		{
			switch ( i.length )
			{
			case 4:
				gl.glBegin( GL2.GL_QUADS );
				gl.glColor3fv( selectColor, 0 );
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
				gl.glColor3fv( selectColor, 0 );
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
				gl.glColor3fv( selectColor, 0 );
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

	/**
	 * @return the colour of the triangle faces that changes when selected
	 * and is by default, black.
	 */
	public Color getSelectColor()
	{
		return new Color( selectColor[0], selectColor[1], selectColor[2] );
	}

	/**
	 * @param selectColor - change the selection colour applied to all
	 * triangles of this GraphicalObject.
	 */
	public void setSelectColor( Color selectColor )
	{
		this.selectColor = selectColor.getColorComponents( null );
	}

	/* (non-Javadoc)
	 * @see ui.components.GraphicalObject#isDynamic()
	 */
	@Override
	public boolean isDynamic()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see ui.components.GraphicalObject#getType()
	 */
	@Override
	public CellType getType()
	{
		return type;
	}

	/* (non-Javadoc)
	 * @see ui.components.GraphicalObject#getLocation()
	 */
	@Override
	public Point getLocation()
	{
		return new Point( (int) (position.x / GameView.cellsize)
						, (int) (position.y / GameView.cellsize) );
	}

}
