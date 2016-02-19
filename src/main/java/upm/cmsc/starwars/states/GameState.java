package upm.cmsc.starwars.states;

import java.net.URISyntaxException;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import upm.cmsc.starwars.characters.LukeSkywalker;


public class GameState extends BasicGameState{
	
	private final long ATTACK_DURATION = 170;
	private final long JUMP_DURATION = 150;
	private final float H_DISPLACEMENT = 0.2f;
	private final float MIN_Y = 395;
	private final float MAX_X = 200;
	private final float GRAVITY = 9.8f;
	private final float INITIAL_VELOCITY = 5;
	
	
	private Animation sprite, rightMove, noMove,attackMove,jumpMove;
	private boolean attacking,jumping;
	private float bgX,hVelocity;
	private long timeStarted;
	
	private LukeSkywalker luke;
	private Image background;
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		luke = new LukeSkywalker();
		
		try {
			background = new Image(this.getClass().getResource("/background/dessert.jpg").toURI().getPath().substring(1));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bgX = background.getWidth() * -1;
		
		rightMove = luke.getRightAnimation();
		noMove = luke.getNoAnimation();
		attackMove = luke.getAttackAnimation();
		jumpMove = luke.getJumpAnimation();
		sprite = noMove;
		
		
		luke.setY(MIN_Y);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		float x = bgX;
		while(x<0){
			background.draw(x+background.getWidth(),0);
			x+=background.getWidth();
		}
		sprite.draw(luke.getX(),luke.getY());
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
			s.enterState(State.MENU);
		}
		if(gc.getInput().isKeyDown(Input.KEY_RIGHT) && !jumping){
			if(luke.getX()<MAX_X){
				luke.addToX(delta*H_DISPLACEMENT);
			}
			else{
				bgX-=delta*H_DISPLACEMENT;
			}
			if(luke.getX()>MAX_X){
				luke.setX(MAX_X);
			}
			sprite = rightMove;	
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_A) && !attacking){
				sprite = attackMove;
				attacking = true;
				timeStarted = System.currentTimeMillis();
		}
		else if(attacking){
			long timeCurr = System.currentTimeMillis();
			if(timeCurr-timeStarted>ATTACK_DURATION){
				attacking = false;
			}
		}
		// TODO Polish jumping / Simulate Gravity
		else if(gc.getInput().isKeyPressed(Input.KEY_UP) && !jumping){
			sprite = jumpMove;
			jumping = true;
			timeStarted = System.currentTimeMillis();
			hVelocity = 100f;
		}
		else if(jumping){
			long timeCurr = System.currentTimeMillis();
			if((timeCurr-timeStarted<JUMP_DURATION)&&hVelocity>0){
				luke.addToY(hVelocity*-1);
				hVelocity = (float) Math.sqrt(INITIAL_VELOCITY+2*(GRAVITY*(luke.getY()-MIN_Y)));
			}
			else if(timeCurr-timeStarted>JUMP_DURATION){
				jumping=false;
				luke.setY(MIN_Y);
			}
		}
		else{
			sprite = noMove;
		}
		sprite.update(delta);
	}
	@Override
	public int getID() {
		return State.GAME;
	}

}
