package upm.cmsc.starwars.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class GameState extends BasicGameState{

	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		g.drawString("You are at < GAME > state.", 50, 50);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
			s.enterState(State.MENU);
		}
	}

	@Override
	public int getID() {
		return State.GAME;
	}

}
