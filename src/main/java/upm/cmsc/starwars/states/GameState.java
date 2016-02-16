package upm.cmsc.starwars.states;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import upm.cmsc.starwars.characters.LukeSkywalker;


public class GameState extends BasicGameState{
	
	private Animation sprite, moveRight, noMovement;
	private float lukeX = 0;
	private final float displacement = 0.05f;
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		moveRight = LukeSkywalker.getRightMovement();
		noMovement = LukeSkywalker.getNoAnimation();
		sprite = noMovement;
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		sprite.draw(lukeX,540);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
			s.enterState(State.MENU);
		}
		if(gc.getInput().isKeyDown(Input.KEY_RIGHT)){
			lukeX+=delta*displacement;
			sprite = moveRight;			
		}
		else{
			sprite = noMovement;
		}
		sprite.update(delta);

	}
	@Override
	public int getID() {
		return State.GAME;
	}

}
