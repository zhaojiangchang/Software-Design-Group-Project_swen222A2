package ui.components;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import world.game.GameState;
import world.game.Player;

import com.jogamp.opengl.util.FPSAnimator;

import controllers.NetworkController;
import controllers.RendererController;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * A simple line drawn 3D adventure board game program that draws with JOGL in
 * a Swing JFrame using the AWT GLJPanel. This is the root class for the
 * renderer which interacts with the scene and the input listener while housing
 * an OpenGL rendering context (GLAutoDrawable)
 * @author Vivian Stewart
 */
@SuppressWarnings( "serial" )
public class GameView extends GLJPanel
{
	// OpenGl size units
	public static final int		cellsize = 10;
	// radians
	public static final double
					PI2 = Math.PI * 2;
	// conversion to degrees
	public static final float
					DEG = 180.0f / (float) Math.PI;
	// actual direction before update
	private float 				direction = 0.0f;
	// actual position before update
	private Point2D.Float 		position;
	// collections of drawable graphical objects
	private GameViewData		data	= GameViewData.instance();
	private GameScene			scene;
	// keyInput (keyboard) is also responsible for position and direction changes
	private GameListener		keyInput;
	public static Player		player; // TODO: remove 'public static' once tested
	private GameState			state;  // TODO: remove once tested

    public GameView( GLCapabilities gc, JFrame frame, GameState state, Player player )
    {
    	super( gc );
    	// load game elements into the scene for rendering
    	this.state = state;
    	this.player = player;
    	scene = new GameScene( state, player );
    	// initial point of extraction (where the player starts)
    	Point p = state.getPlayer().getPosition();
        position = new Point2D.Float(
				p.x * cellsize, p.y * cellsize );
        // start receiving input
        keyInput = new GameListener( position, direction, scene );
        addGLEventListener( new GLEventListener() {
            /* (non-Javadoc)
             * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLAutoDrawable, int, int, int, int)
             * when the window size changes...
             */
        	@Override
            public void reshape( GLAutoDrawable glautodrawable
            		, int x, int y, int width, int height ) {
            	GL2 gl2 = glautodrawable.getGL().getGL2();
            	GLU glu = GLU.createGLU( gl2 );
            	gl2.glViewport( 0, 0, width, height );
            	gl2.glClearColor( 0.0f, 0.0f, 0.01f, 0.0f );
            	gl2.glMatrixMode( GL2.GL_PROJECTION );
            	gl2.glLoadIdentity();
            	glu.gluPerspective( 65.0f, (float)width/(float)height, 1.0f, 1000.0f );
            }
            /* (non-Javadoc)
             * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
             * first time setup of OpenGL
             */
            @Override
            public void init( GLAutoDrawable glautodrawable ) {
            	glautodrawable.getGL().setSwapInterval(1);
            	GL2 gl2 = glautodrawable.getGL().getGL2();
            	gl2.glPointParameterf( GL2.GL_POINT_SIZE_MIN, 1.0f );
            	gl2.glPointParameterf( GL2.GL_POINT_SIZE_MAX, 100.0f );
            	FloatBuffer fb = FloatBuffer.wrap( new float[]{0f,0f,0.01f} );
            	gl2.glPointParameterfv( GL2.GL_POINT_DISTANCE_ATTENUATION, fb );
            	gl2.glEnable( GL2.GL_POINT_SMOOTH );
            	gl2.glEnable( GL.GL_LINE_SMOOTH );
            	gl2.glEnable( GL2.GL_POLYGON_SMOOTH );
            	// gl2.glEnable( GL2.GL_POLYGON_OFFSET_LINE );
            	// gl2.glPolygonOffset( 0.1f, 100f );
            	gl2.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
            	gl2.glHint( GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST );
            	// Antialias lines
            	gl2.glEnable( GL.GL_BLEND );
            	gl2.glPointSize( 10.0f );
            	gl2.glBlendFunc( GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA );
            	gl2.glClearColor( 0.0f, 0.0f, 0.01f, 0.0f );
            	gl2.glEnable( GL.GL_DEPTH_TEST );
            	gl2.glDepthFunc(GL.GL_LEQUAL);
                gl2.glShadeModel(GL2.GL_SMOOTH);
            	scene.addSurrounds();
            	gl2.glLineWidth( 2f );
            	for( GraphicalObject go: data.getDynamicScene() )
            	{
            		go.initialise( gl2 );
            	}
            	StaticDisplayList staticDisplayList = StaticDisplayList.instance();
            	staticDisplayList.createDisplaylist( gl2 );
            }

            /* (non-Javadoc)
             * @see javax.media.opengl.GLEventListener#dispose(javax.media.opengl.GLAutoDrawable)
             * called upon shutdown
             */
            @Override
            public void dispose( GLAutoDrawable glautodrawable ) {
            	GL2 gl = glautodrawable.getGL().getGL2();
            	StaticDisplayList.instance().destroy( gl );
            }

            /* (non-Javadoc)
             * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
             * called every frame
             */
            @Override
            public void display( GLAutoDrawable glautodrawable ) {
            	GL2 gl2 = glautodrawable.getGL().getGL2();
            	update( gl2 );
            	GLU glu = GLU.createGLU( gl2 );
            	gl2.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
                // Draw some rotating stuff
            	gl2.glMatrixMode( GL2.GL_MODELVIEW );
            	gl2.glLoadIdentity();

            	glu.gluLookAt( 0.0f, 0.0f, 0.0f   // position
            				 , 0.0f,-1.0f, 0.0f   // direction
            				 , 0.0f, 0.0f, 1.0f );// up
            	gl2.glRotatef( direction, 0 ,0 ,1.0f );
            	gl2.glTranslatef( -position.x, -position.y, -10.0f );
                render( gl2 );
                mouseSelect( gl2 );
            }

        });
        addKeyListener( keyInput );
        addMouseListener( keyInput );

        frame.addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent windowevent )
            {
                dispose();
                System.exit( 0 );
            }
        });
        // steady 60 frame animation
        FPSAnimator animator = new FPSAnimator(60);
        animator.add( this );
        animator.start();

        setSize( 800, 600 );
        setVisible( true );
    }

    /**
     * Various updates that are to be done before the rendering of each frame.
     * - update from key and mouse input, the position and direction.
     * - teleport to new floor and reset all visual and state info.
     */
    private void update( GL2 gl )
	{
    	// keep moving and turning even if there are no key press or release events
    	keyInput.update();
    	direction = keyInput.getDirection() * DEG;
    	// new position
    	float newx = keyInput.getNewX(), newy = keyInput.getNewY();
    	// new cell to occupy?
    	int cellx = (int) ( newx / cellsize ), celly = (int) ( newy / cellsize );
    	if ( 	   (int) ( position.x / cellsize ) != cellx
    			|| (int) ( position.y / cellsize ) != celly )
    		// tell the game server
    		RendererController.movePlayer( player, new Point( cellx, celly ) ); // should move to movePos in GameListener
    	// do update
    	position.setLocation( newx, newy );
    	// update key input every frame unless input is received
    	keyInput.resetKeyUpdate( );
    	if ( scene.isTeleport() && RendererController.teleport( player ) ) {
    		scene.resetTeleport();
			scene = new GameScene( state, player );
			gl.glLoadIdentity();
			scene.addSurrounds();
        	gl.glLineWidth( 2f );
        	StaticDisplayList staticDisplayList = StaticDisplayList.instance();
        	staticDisplayList.createDisplaylist( gl );
        	keyInput.reset( scene );
    	}
    }

    /**
     * Uses glUnproject to find the object in 3D space that was clicked on and
     * change its' colour to selection colour. Also changing the colour of the
     * previously selected object back to the unselected colour. 
     * @param gl context
     */
    private void mouseSelect( GL2 gl )
    {
    	Point click = keyInput.getClick();
		if ( click != null )
		{
			this.grabFocus();
			IntBuffer viewport= IntBuffer.allocate(4);
			FloatBuffer modelview = FloatBuffer.allocate(16);
			FloatBuffer projection = FloatBuffer.allocate(16);
			FloatBuffer pos = FloatBuffer.allocate(4);// returned xyz coords

		    gl.glGetIntegerv( GL.GL_VIEWPORT, viewport );
	        gl.glGetFloatv( GL2.GL_MODELVIEW_MATRIX, modelview );
	        gl.glGetFloatv( GL2.GL_PROJECTION_MATRIX, projection );

	        int realy = viewport.get( 3 ) - click.y - 1;
	        FloatBuffer posZ = FloatBuffer.allocate(1);
	        GLU glu = GLU.createGLU( gl );
	        gl.glReadPixels( click.x, realy, 1, 1, GL2.GL_DEPTH_COMPONENT, GL.GL_FLOAT, posZ );
        	glu.gluUnProject((float) click.x, (float) realy, (float)posZ.get( 0 ),
                modelview,
                projection,
                viewport,
                pos );
        	Point p = new Point( (int) (pos.get( 0 )/cellsize)
        					   , (int) (pos.get( 1 )/cellsize) );
        	GraphicalObject go = GameViewData.instance().getGameElements().get( p );
        	if ( go != null && go instanceof DymanicRender )
        	{
        		DymanicRender dr = (DymanicRender)go;
        		dr.setSelectColor( Color.decode( "#110000" ) );
        		GameViewData.instance().replacePreviousSelection( dr );
        	}
		}
    }

	/**
	 * All GraphicalObjects to render themselves, both static and dynamic.
	 * @param gl2 - opengl rendering context
	 */
	private synchronized void render( GL2 gl2 )
	{
		StaticDisplayList.instance().drawDisplayList( gl2 );
		gl2.glLineWidth( 2f );
		for( int i = 0; i < data.getDynamicScene().size(); ++i )
		{
			data.getDynamicScene().get( i ).draw( gl2 );
		}

    }
}
