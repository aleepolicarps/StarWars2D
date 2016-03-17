package upm.cmsc.starwars.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import upm.cmsc.starwars.CustomFileUtil;

// TODO TEST STATE (needs refactoring)
public class GameOverState extends BasicGameState{
	Image bg;
	Image gameover;

	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		bg = new Image(CustomFileUtil.getFilePath("/menu/bg.png"));
		gameover = new Image(CustomFileUtil.getFilePath("/menu/gameover.png"));	
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		
		g.drawImage(bg, 0, 0);
		gameover.draw(240,200);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
			s.enterState(State.FIRST_LEVEL);
		}
	}

	@Override
	public int getID() {
		return State.GAMEOVER;
	}

}
