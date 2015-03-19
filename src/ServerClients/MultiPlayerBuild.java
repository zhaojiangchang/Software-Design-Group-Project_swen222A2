package ServerClients;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import ui.components.GameView;
import window.components.GUI;
import world.game.GameState;
import world.game.Player;
import controllers.NetworkController;
import controllers.RendererController;
import controllers.UIController;

public class MultiPlayerBuild {
	
	NetworkController netCon;
	
	public MultiPlayerBuild(GameState state, GUI gui, String name){
		
		GLProfile.initSingleton();
		GLProfile glprofile = GLProfile.getDefault();
		GLCapabilities glcapabilities = new GLCapabilities( glprofile );
		System.out.println("state.getPlayers().size()>= "+ state.getPlayers().size());
		
		GameView gameView = new GameView( glcapabilities, gui.getFrame(), state, state.getPlayer(name) );
		gameView.setEnabled( true );
		gameView.setVisible( true );
		gameView.setFocusable( true );
		
		Player player = state.getPlayer(name);
		
		UIController uiCon = new UIController(state, gui);
		
		RendererController renCon = new RendererController(false);
		
		netCon = new NetworkController(uiCon, renCon);
		
		renCon.setState(state);
		renCon.setNetCon(netCon);
		renCon.setUICon(uiCon);
		
		netCon.setGameView(gameView);
		
		gui.startClientWindows(player, gameView);
	}
	
	public NetworkController getNetworkController(){
		return netCon;
	}

}
