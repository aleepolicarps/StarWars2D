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

import upm.cmsc.starwars.objects.LukeSkywalker;
import static upm.cmsc.starwars.objects.Contstants.*;


public class GameState extends BasicGameState{
	
	private final float MIN_Y = 510;
	private final float MAX_X = 200;
	
	
	private Animation sprite, rightMove, noMove,attackMove,jumpMove;
	private Image tree,tumbleweed,background,path;
	private boolean attacking,jumping;
	private float hVelocity;
	private long timeStarted;
	private float treeX = 10;
	private float tumbleweedX = -10;
	
	private LukeSkywalker luke;
	
	private void loadAnimations(){
		rightMove = luke.getRightAnimation();
		noMove = luke.getNoAnimation();
		attackMove = luke.getAttackAnimation();
		jumpMove = luke.getJumpAnimation();
		sprite = noMove;	
	}
	
	private void loadImages() throws SlickException{
		try {
			try{
				tree = new Image(this.getClass().getResource("elements/tree.png").toURI().getPath());
				tumbleweed = new Image(this.getClass().getResource("elements/tumbleweed.png").toURI().getPath());
				background = new Image(this.getClass().getResource("background/desert.jpg").toURI().getPath());
				path = new Image(this.getClass().getResource("elements/desert_path.png").toURI().getPath());
			}catch(NullPointerException e){
				tree = new Image(this.getClass().getResource("/elements/tree.png").toURI().getPath());
				tumbleweed = new Image(this.getClass().getResource("/elements/tumbleweed.png").toURI().getPath());
				background = new Image(this.getClass().getResource("/background/desert.jpg").toURI().getPath());
				path = new Image(this.getClass().getResource("/elements/desert_path.png").toURI().getPath());
			}
		} catch (URISyntaxException e) {
			// TODO do something here
		}
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		luke = new LukeSkywalker();
		luke.setY(MIN_Y);
		
		loadAnimations();
		loadImages();
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		background.draw();
		
		float x = treeX;
		while(x<background.getWidth()){
			tree.draw(x, 300);
			x+=500;
		}
		
		x = tumbleweedX;
		while(x<background.getWidth()){
			tumbleweed.draw(x,420);
			x+=20;
		}
		
		x = tumbleweedX;
		while(x<background.getWidth()){
			path.draw(x,545);
			x+=background.getWidth();
		}
		
		sprite.draw(luke.getX(),luke.getY());
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {	
		if(gc.getInput().isKeyDown(Input.KEY_RIGHT) && !jumping){
			sprite = rightMove;	
			if(luke.getX()<MAX_X){
				luke.addToX(delta*H_DISPLACEMENT);
				if(luke.getX()>MAX_X){
					luke.setX(MAX_X);
				}
			}
			else{
				treeX -= delta * 0.05f;
				tumbleweedX -= delta*H_DISPLACEMENT;
//				background.addToX(-1*(delta*H_DISPLACEMENT));
			}
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
