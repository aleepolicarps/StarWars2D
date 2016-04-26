package upm.cmsc.starwars.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class TransitionState extends BasicGameState {
	
	private int counter;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		counter = 0;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		switch(counter){
		case 0:	g.setColor(Color.white);
				g.drawString("Proceeding to next level. Click S to continue.", 60, 80);
				break;
		case 1: if(MenuState.getCurrentGameLevel()==2){
					game.enterState(State.SECOND_LEVEL);
					counter = 0;
				}else if(MenuState.getCurrentGameLevel()==3){
					game.enterState(State.THIRD_LEVEL);
					counter = 0;
				}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(container.getInput().isKeyPressed(Input.KEY_S)){
			counter++;
		}
	}

	@Override
	public int getID() {
		return State.TRANS_STATE;
	}

}
