package ui.components;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL2;

/**
 * @author Vivian Stewart
 * The information loaded from file to draw a 3D object. Indices are the lists
 * of vertices in the polygon, line, or point (array can be variable length).  
 */
public class Mesh
{
	
	private List<Integer[]>	indices;
	private List<Float[]>	vertices;

	public Mesh( List<Integer[]> indices, List<Float[]> vertices )
	{
		this.indices = indices;
		this.vertices = vertices;
	}
	
	/**
	 * @return a defensive copy of the loaded indices
	 */
	public List<int[]> getIndices()
	{
		List<int[]> copy = new ArrayList<int[]>();
		int j;
		for ( Integer[] idxs: indices )
		{
			int[] copyi = new int[idxs.length];
			j = 0;
			for ( int i: idxs )
			{
				copyi[j] = i - 1; // convert from start at 1 to start at 0
				++j;
			}
			copy.add( copyi );
		}
		return copy;
	}
	/**
	 * @return a defensive copy of the loaded vertices
	 */
	public List<float[]> getVertices()
	{
		List<float[]> copy = new ArrayList<float[]>();
		int j;
		for ( Float[] flt: vertices )
		{
			float[] copyf = new float[flt.length];
			j = 0;
			for ( float f: flt )
			{
				copyf[j] = f;
				++j;
			}
			copy.add( copyf );
		}
		return copy;
	}
	
	/**
	 * Get indices of a particular array length as a Buffer for jogl compatibility
	 * @param glType
	 * @return
	 */
	public IntBuffer getIndBuffer( int glType )
	{
		List<Integer> prebuf = new LinkedList<Integer>();
		int n = glType == GL2.GL_QUADS ? 4 : 
				glType == GL2.GL_TRIANGLES ? 3 :
				glType == GL2.GL_LINES ? 2 :
				glType == GL2.GL_POINTS ? 1 : 0;
		if ( n == 0 ) throw new RuntimeException(
				"Error: GL_QUADS, TRIANGLES, LINES or POINTS are accepted" );
		// find the elements that are a certain size
		for ( Integer[] idxs: indices )
		{
			if ( idxs.length != n ) continue;
			for ( int i: idxs )
			{ // then pack each integer into a temporary list
				prebuf.add( i ); // this mite also be from 0 instead of 1
			}
		}
		// now we know how may elements
		Integer[] ind = new Integer[ prebuf.size() ]; // j is size
		return IntBuffer.wrap( unboxArrInt( prebuf.toArray( ind ) ) );
	}
	
	/**
	 * @return FloatBuffer version of the vertices
	 */
	public FloatBuffer getVertexBuffer()
	{
		LinkedList<Float> prebuf = new LinkedList<Float>();
		// pack all vertices into the buffer
		for ( Float[] flts: vertices )
		{
			for ( float f: flts )
			{
				prebuf.add( f );
			}
		}
		// now we know how may elements
		Float[] fs = new Float[ prebuf.size() ]; // j
		return FloatBuffer.wrap(
				unboxArrFloat( prebuf.toArray( fs ) ) );
	}
	
	/**
	 * Convert Integer array to int primitive array
	 * @param unbox - to be converted
	 * @return converted array
	 */
	private int[] unboxArrInt( Integer[] unbox )
	{
		
		    if (unbox == null) {
		        return null;
		    } else if (unbox.length == 0) {
		        return new int[0];
		    }
		    final int[] result = new int[unbox.length];
		    for (int i = 0; i < unbox.length; i++) {
		        result[i] = unbox[i].intValue();
		    }
		    return result;
	}
	/**
	 * Convert Float array to float primitive array
	 * @param unbox - to be converted
	 * @return converted array
	 */
	private float[] unboxArrFloat( Float[] unbox )
	{
		
		    if (unbox == null) {
		        return null;
		    } else if (unbox.length == 0) {
		        return new float[0];
		    }
		    final float[] result = new float[unbox.length];
		    for (int i = 0; i < unbox.length; i++) {
		        result[i] = unbox[i].floatValue();
		    }
		    return result;
	}
	/**
	 * test mesh loading
	 * @param args
	 */
	public static void main( String[] args )
	{
		ObjReader ob = new ObjReader( "mesh/door.obj" );
		Mesh mesh = new Mesh( ob.getIndices(), ob.getVertices() );
		for ( float[] fs: mesh.getVertices() )
		{	
			for ( float f: fs )
				System.out.print( f + " " );
			System.out.println();
		}
		for ( int[] is: mesh.getIndices() )
		{	
			for ( int i: is )
				System.out.print( i + " " );
			System.out.println();
		}
	}
}
