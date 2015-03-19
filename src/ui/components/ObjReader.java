package ui.components;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @author vivian
 * Read in WaveFront .obj files
 */
public class ObjReader
{
	private int lineNumber = 0;
	private ArrayList<Float[]>	vertices = new ArrayList<Float[]>();
	private ArrayList<Integer[]>	indices = new ArrayList<Integer[]>();
	
	public ObjReader( String fileName )
	{
		try
		{
			Scanner sc = new Scanner( new BufferedInputStream( 
									  new FileInputStream( fileName ) ) );
			if ( !sc.hasNext() )
			{
				sc.close();
				fail( "No file content" );
			}
			parseLines( sc );
		} catch ( FileNotFoundException e )
		{
			e.printStackTrace();
		}
	}
	
	private void parseLines( Scanner sc )
	{
		while ( sc.hasNextLine() )
		{
			parseLine( sc.nextLine() );
		}
	}

	private void parseLine( String line )
	{
		++lineNumber;
		Scanner sc = new Scanner( line );
		sc.next();
		if ( line.startsWith( "v " ) ) parseVertex( sc );
		else if ( line.startsWith( "f " ) ) parseFace( sc );
		else if ( line.startsWith( "p " ) ) parsePoint( sc );
		else if ( line.startsWith( "l " ) ) parseEdge( sc );
		else if ( line.startsWith( "#" ) ) 
		{
			sc.close();
			return;
		}
		sc.close();
	}

	private void parseEdge( Scanner sc )
	{
		Integer[] idx = new Integer[2]; 
		if ( sc.hasNextFloat() )
		{
			idx[0] = sc.nextInt();
		} else fail( "first index missing at line " + lineNumber );
		if ( sc.hasNextFloat() )
		{
			idx[1] = sc.nextInt();
		} else fail( "second index missing at line " + lineNumber );
		indices.add( idx );
	}

	private void parsePoint( Scanner sc )
	{
		Integer[] idx = new Integer[1]; 
		if ( sc.hasNextInt() )
		{
			idx[0] = sc.nextInt();
		} else fail( "index missing at line " + lineNumber );
		indices.add( idx );
	}

	private void parseFace( Scanner sc )
	{
		LinkedList<Integer> list = new LinkedList<Integer>(); 
		while ( sc.hasNextInt() )
		{
			list.add( sc.nextInt() );
		}
		if ( list.size() > 4 ) System.out.println("N-Gon:" + list.size());
		Integer[] idx = new Integer[list.size()];
		indices.add( list.toArray( idx ) );
	}

	private void parseVertex( Scanner sc )
	{
		Float[] vert = new Float[3]; 
		if ( sc.hasNextFloat() )
		{
			vert[0] = sc.nextFloat();
		} else fail( "first vertex missing at line " + lineNumber );
		if ( sc.hasNextFloat() )
		{
			vert[1] = sc.nextFloat();
		} else fail( "second vertex missing at line " + lineNumber );
		if ( sc.hasNextFloat() )
		{
			vert[2] = sc.nextFloat();
		} else fail( "thrird vertex missing at line " + lineNumber );
		vertices.add( vert );
	}

	public List<Float[]> getVertices()
	{
		return vertices;
	}

	public List<Integer[]> getIndices()
	{
		return indices;
	}

	private void fail( String string )
	{
		throw new RuntimeException( string );
	}
	/**
	 * Test of .obj load file
	 * @param args
	 */
	public static void main( String[] args )
	{
		ObjReader ob = new ObjReader( "mesh/door.obj" );
		for ( Float[] fs: ob.vertices )
		{	
			for ( float f: fs )
				System.out.print( f + " " );
			System.out.println();
		}
		for ( Integer[] is: ob.indices )
		{	
			for ( int i: is )
				System.out.print( i + " " );
			System.out.println();
		}
	}
}
