package ui.components;
import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.Map.Entry;

import controllers.RendererController;
import world.ColourPalette;
import world.components.CellType;
import world.components.Container;
import world.components.Direction;
import world.components.Furniture;
import world.components.GameObject;
import world.components.GameToken;
import world.components.Key;
import world.components.Map;
import world.components.MoveableObject;
import world.components.TokenType;
import world.components.Torch;
import world.game.GameState;
import world.game.Player;

/**
 * @author Vivian Stewart
 * - Setup of the visual world.
 *		* Populate the two rendering lists
 * - Setup collision detection between the world and the player.
 *		* Populate collision/selection map
 * - Define the boundaries of the world
 * - Execute collision detection between the world and the player and
 * 	 trigger behaviour of graphical objects
 */
public class GameScene
{
	private GameState		game;
	private Player			player;
	private boolean			teleport = false;
	private GameViewData	graphicData = GameViewData.instance();
	private CellType[][]	staticMap;
	public final int		xlimit, ylimit;

	public GameScene( GameState state, Player player )
	{
		//Kalo added
		game = state;
		this.player = player;
		// size of map is in the header
		xlimit = player.getFloor().getXLimit();
		ylimit = player.getFloor().getYLimit();
		// read in the map
		staticMap = player.getFloor().getCellTypeMap();
	}


	/**
	 * Convert continuous coordinates to discrete square map coordinates and
	 * possibly trigger behaviour of the object in that square if applicable.
	 * @param x - continuous x position value
	 * @param y - continuous y position value
	 * @return if the position is inside a collidable square
	 */
	public boolean isCollidable( float x, float y, boolean trigger )
	{
		if ( trigger ) return isCollidable( (int)(x/GameView.cellsize), (int)(y/GameView.cellsize) );
		return simpleCollidable( (int)(x/GameView.cellsize), (int)(y/GameView.cellsize) );
	}

	/**
	 * @param newXCoordinate - row select
	 * @param newYCoordinate - column select
	 * @return if this square has a collidable object in it
	 */
	private boolean simpleCollidable( int newXCoordinate, int newYCoordinate )
	{
		if ( newXCoordinate >= xlimit || newYCoordinate >= ylimit )
		{
			return false;
		}
		Point p = new Point( newXCoordinate, newYCoordinate );
		if ( staticMap[newXCoordinate][newYCoordinate].ordinal()
				> CellType.WALL.ordinal()
			|| graphicData.getGameElements().get( p ) != null )
		{
			CellType ct =  graphicData.getGameElements().get( p ).getType();
			// tokens, keys, rings and torch
			if ( ( 
					ct.ordinal() > CellType.CHEST.ordinal()
					&& ct.ordinal() < CellType.OUTOFBOUNDS.ordinal() 
				 )
				 || ct == CellType.RINGS )
				return false;
			return true;
		}
		return staticMap[newXCoordinate][newYCoordinate] == CellType.WALL;
	}

	/**
	 * @param newXCoordinate - row select
	 * @param newYCoordinate - column select
	 * @return if this square has a collidable object in it and trigger its' behaviour
	 */
	private boolean isCollidable( int newXCoordinate, int newYCoordinate )
	{
		if ( newXCoordinate >= xlimit || newYCoordinate >= ylimit )
		{
			return false;
		}
		Point p = new Point( newXCoordinate, newYCoordinate );
		GraphicalObject go = graphicData.getGameElements().get( p );
		Player po = graphicData.getPlayerElements().get( p );
		if ( staticMap[newXCoordinate][newYCoordinate].ordinal()
					> CellType.WALL.ordinal() || go != null || po != null )
		{
			if ( po != null )
				return true;
			CellType ct = null;
			if ( go != null ) ct = go.getType();
			if ( ct == CellType.RINGS )
			{
				teleport = true;
				System.out.println("Teleporting to Floor:" + teleport );
				return true;
			}
			if ( ct == CellType.KEYDOOR || ct == CellType.DOOR )
			{	if ( RendererController.canOpenDoor( player, p ) )
					return ((DymanicRender)go).collide();
				return true;
			} // tokens and torch
			else if ( ct.toString() == player.getType().toString()
					|| ct == CellType.TORCH )
			{ // collect torch and remove it from view if player does not have one
				if ( RendererController.pickupObjectAtPoint( player, p ) )
					graphicData.remove( p );
			}
			else if ( ct == CellType.KEY  )
			{ // collect key and discard already collected key if it exists in
			  // players inventory
				Key key = RendererController.pickupKey( player, p );
				graphicData.remove( p );
				if ( key != null )
				{
					DymanicRender dyn = DymanicRender.instanceKey(
							p, key.getColor() );
					graphicData.addGrapicalObject( dyn );
				}

			} else
				return ((DymanicRender)go).collide();
		}
		return staticMap[newXCoordinate][newYCoordinate] == CellType.WALL;
	}

	/**
	 * Setup Scene with all the GraphicalObjects mapped for collision detection
	 * and Mouse Selection. And all the Static and Dynamic Objects in their
	 * rendering queues.
	 */
	public void addSurrounds()
	{
		graphicData.clear(); // need to destroy staticID too. for new floor
		Direction direction; // facing direction
		Point location; // map coordinates
		DymanicRender dynamicObject;
		GameObject graphicalObject;
		Furniture furniture;
		Container container; // object containing items to collect
		Map fmap = player.getFloor();
		for ( int j = 0; j < ylimit; ++j )
		{
			for ( int i = 0; i < xlimit; ++i )
			{
				CellType[] nesw = findNeighbours( i, j );
				location =  new Point( i, j );
				direction = nesw[0] == nesw[2] && nesw[0] == CellType.EMPTY
						? Direction.NORTH : Direction.EAST;
				switch( staticMap[i][j] )
				{ // Statically rendered objects
				case WALL :
					graphicData.addStaticOnly( new StaticRender(
							CellType.WALL, nesw, location, ColourPalette.TAN ) );
					break;
				case DOOR :
					dynamicObject = DymanicRender.instanceDoor( location, direction );
					StaticRender doorWay = new StaticRender(
							CellType.DOOR, nesw, location, ColourPalette.BAIGE );
					graphicData.addStaticOnly( doorWay );
					graphicData.addGrapicalObject( dynamicObject );
					break;
				case KEYDOOR :
					dynamicObject = DymanicRender.instanceKeyDoor( location, direction
							, fmap.getDoor( location ).getKey().getColor() );
					StaticRender keydoorWay = new StaticRender(
							CellType.KEYDOOR, nesw, location, ColourPalette.BAIGE );
					graphicData.addStaticOnly( keydoorWay );
					graphicData.addGrapicalObject( dynamicObject );
					break;
				case TELEPORT :
					dynamicObject = DymanicRender.instanceTelePort( location );
					graphicData.addStaticOnly( new StaticRender(
							CellType.TELEPORT, nesw, location, ColourPalette.GREYPURPLE ) );
					graphicData.addGrapicalObject( dynamicObject );
					break;
				default:
					graphicData.addStaticOnly( new StaticRender(
							CellType.EMPTY, nesw, location, ColourPalette.LIGHTOCEANBLUE ) );
					break;
				}
				// Dynamically rendered objects
				graphicalObject = fmap.objectAtPoint( location );
				furniture = fmap.furnitureAtPoint ( location );
				container = fmap.containerAtPoint( location );
				if ( graphicalObject == null
						&& container == null
						&& furniture == null ) continue;
				if ( graphicalObject instanceof GameToken )
				{ // Tokens to be collected
					if ( ((GameToken)graphicalObject).getType() == TokenType.CONE )
					{
						dynamicObject = DymanicRender.instanceCone(
								location, ((GameToken)graphicalObject).getColor() );
						graphicData.addGrapicalObject( dynamicObject );
					}
					else if ( ((GameToken)graphicalObject).getType() == TokenType.BALL )
					{
						dynamicObject = DymanicRender.instanceBall(
								location, ((GameToken)graphicalObject).getColor() );
						graphicData.addGrapicalObject( dynamicObject );
					}
					else if ( ((GameToken)graphicalObject).getType() == TokenType.DIAMOND )
					{
						dynamicObject = DymanicRender.instanceDiamond(
								location, ((GameToken)graphicalObject).getColor() );
						graphicData.addGrapicalObject( dynamicObject );
					}
					else if ( ((GameToken)graphicalObject).getType() == TokenType.CUBE )
					{
						dynamicObject = DymanicRender.instanceCube(
								location, ((GameToken)graphicalObject).getColor() );
						graphicData.addGrapicalObject( dynamicObject );
					}
				} // items that enable other items to be collected or triggered
				else if ( graphicalObject instanceof MoveableObject )
				{
					if ( graphicalObject instanceof Key )
					{
						dynamicObject = DymanicRender.instanceKey(
								location, ((Key)graphicalObject).getColor() );
						graphicData.addGrapicalObject( dynamicObject );
					}
					else if ( graphicalObject instanceof Torch )
					{
						dynamicObject = DymanicRender.instanceTorch(
								location, ColourPalette.GREYPURPLE2 );
						graphicData.addGrapicalObject( dynamicObject );
					}
				} else if ( furniture != null )
				{ // load furniture int the scene
					dynamicObject = DymanicRender.instanceFurniture(
							furniture.getType()
							, Behave.ORIENTATION
							, location
							, furniture.getFacing()
							, ColourPalette.PALEGREEN );
					graphicData.addDynamicOnly( dynamicObject );
					graphicData.addPointsToGameElement( furniture.getPoints(), dynamicObject );
				} else if ( container != null )
				{ // load containers (briefcase, chest etc.)
					dynamicObject = DymanicRender.instanceContainer(
							container.getType(), Behave.ORIENTATION, location
							, container.getFacing(), ColourPalette.MAROON );
					graphicData.addGrapicalObject( dynamicObject );
				}
			}
		}
		// load other players into the scene
		List<Player> players = game.getPlayers();
		for ( Player p: players )
		{
			if ( player != p && p.getFloor() == fmap )
			{
				graphicData.addNewPlayerMove( null, p.getPosition(), p );
			}
		}

		// testing
		for ( Entry<Point, GraphicalObject> entry
				: graphicData.getGameElements().entrySet() )
			System.out.println( "GameElement at:"
				+ entry.getKey().toString()
				+ " -> "
				+ entry.getValue().getType().toString() );
	}
	/**
	 * Return the surrounding square of the square(i,j) for static map
	 * evaluation and connecting of walls.
	 * @param i - row of square
	 * @param j - column of square
	 * @return the north, east, south and west facing adjacent squares
	 * of point (i,j)
	 */
	private CellType[] findNeighbours( int i, int j )
	{
		return new CellType[] {
			  j - 1 < 0		  ? CellType.OUTOFBOUNDS : staticMap[ i ]   [j - 1]
			, i + 1 >= xlimit ? CellType.OUTOFBOUNDS : staticMap[i + 1] [ j ]
			, j + 1 >= ylimit ? CellType.OUTOFBOUNDS : staticMap[ i ]   [j + 1]
			, i - 1 < 0       ? CellType.OUTOFBOUNDS : staticMap[i - 1] [ j ]
		};
	}


	/**
	 * @return true if teleporting is active.
	 */
	public boolean isTeleport()
	{
		return teleport;
	}

	/**
	 * Teleporting is done, return to normal
	 */
	public void resetTeleport()
	{
		teleport = false;
	}
}
