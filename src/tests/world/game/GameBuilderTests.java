package tests.world.game;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import world.game.GameBuilder;
import world.game.GameState;

//TODO: Needs more tests - works fine in practice
/**
 * Tests for the GameBuilder class
 * @author Kalo Pilato - ID:300313803
 *
 */
public class GameBuilderTests {

	/**
	 * A newly constructed single player game should have only one Player
	 */
	@Test public void validConstructor1(){
		GameState state = new GameBuilder("Test Player").getGameState();
		assertTrue(state.getPlayers().size() == 1);
	}
	
	/**
	 * A newly constructed single player game should have only one floor
	 */
	@Test public void validConstructor2(){
		GameState state = new GameBuilder("Test Player").getGameState();
		assertTrue(state.floorCount() == 1);
	}
	
	/**
	 * A newly constructed multiplayer game should have one Player per player name
	 */
	@Test public void validConstructor3(){
		ArrayList<String> pNames = new ArrayList<String>();
		pNames.add("Dave");
		pNames.add("Stephen");
		pNames.add("Marco");
		pNames.add("BrainStackOverflow");
		GameState state = new GameBuilder(pNames).getGameState();
		assertTrue(state.getPlayers().size() == pNames.size());
	}
	
	/**
	 * A newly constructed multiplayer game should have only one floor per player
	 */
	@Test public void validConstructor4(){
		ArrayList<String> pNames = new ArrayList<String>();
		pNames.add("Dave");
		pNames.add("Stephen");
		pNames.add("Marco");
		pNames.add("BrainStackOverflow");
		GameState state = new GameBuilder(pNames).getGameState();
		assertTrue(state.floorCount() == pNames.size());
	}
}
