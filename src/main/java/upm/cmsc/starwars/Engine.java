package upm.cmsc.starwars;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import upm.cmsc.starwars.states.*;

public class Engine extends StateBasedGame {

	public Engine(String title) {
		super(title);
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		
		gc.setMaximumLogicUpdateInterval(120);		
		gc.setTargetFrameRate(120);
		gc.setAlwaysRender(true);
		gc.setShowFPS(false);
		gc.setVSync(true);
		
		this.addState(new MenuState());
		this.addState(new FirstLevelState());
		this.addState(new SecondLevelState());
		this.addState(new GameOverState());
	}

	public static void main(String[] args) {
		try {
			AppGameContainer game = new AppGameContainer(new Engine(Window.GAME_TITLE));
			game.setDisplayMode(Window.WIDTH, Window.HEIGHT, Window.isFullScreen());
			game.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
	
}
