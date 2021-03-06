package upm.cmsc.starwars;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import upm.cmsc.starwars.states.CreditsState;
import upm.cmsc.starwars.states.FirstLevelState;
import upm.cmsc.starwars.states.GameOverState;
import upm.cmsc.starwars.states.InstructState;
import upm.cmsc.starwars.states.MenuState;
import upm.cmsc.starwars.states.SecondLevelState;
import upm.cmsc.starwars.states.StoryBoardState;
import upm.cmsc.starwars.states.ThirdLevelState;
import upm.cmsc.starwars.states.TransitionState;

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
		this.addState(new InstructState());
		this.addState(new StoryBoardState());
		this.addState(new CreditsState());
		this.addState(new FirstLevelState());
		this.addState(new SecondLevelState());
		this.addState(new ThirdLevelState());
		this.addState(new GameOverState());
		this.addState(new TransitionState());
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
