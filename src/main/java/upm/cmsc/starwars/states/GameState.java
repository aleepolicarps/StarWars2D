package upm.cmsc.starwars.states;

import java.net.URISyntaxException;

import org.lwjgl.opengl.Drawable;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import upm.cmsc.starwars.entities.LukeSkywalker;
import static upm.cmsc.starwars.entities.Contstants.*;


public class GameState extends BasicGameState{
	
	private final float MIN_Y = 510;
	private final float MAX_X = 300;
	private final float MIN_X = 0;
	
	
	private Animation sprite, right, still,attack,jump;
	private Image tree,tumbleweed,background,path;
	private boolean attacking,jumping;
	private float hVelocity;
	private long timeStarted;
	private float treeX = 10;
	private float tumbleweedX = -10;
	
	private LukeSkywalker luke;
	
	private void loadAnimations(){
		right = luke.getRightAnimation();
		still = luke.getNoAnimation();
		attack = luke.getAttackAnimation();
		jump = luke.getJumpAnimation();
		sprite = still;	
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
		
		g.setColor(Color.black);
		g.drawString("Luke Skywalker", 20, 0);
		g.setColor(Color.black);
		g.fillRect(20, 20, LukeSkywalker.MAX_HEALTH, 30);
		g.setColor(Color.green);
		g.fillRect(20, 20, luke.getCurrHealth(), 30);

		
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
			sprite = right;	
			if(luke.getX()<MAX_X){
				luke.addToX(delta*H_DISPLACEMENT_FORWARD);
				if(luke.getX()>MAX_X){
					luke.setX(MAX_X);
				}
			}
			else{
				treeX -= delta * 0.05f;
				tumbleweedX -= delta*H_DISPLACEMENT_FORWARD;
			}
		}
		else if(gc.getInput().isKeyDown(Input.KEY_LEFT) && !jumping){
			sprite = right;	
			if(luke.getX()<=MAX_X){
				luke.addToX(-1*delta*H_DISPLACEMENT_BACKWARD);
				if(luke.getX()<MIN_X){
					luke.setX(MIN_X);
					sprite = still;
				}
			}
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_A) && !attacking){
				sprite = attack;
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
			sprite = jump;
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
		else if(gc.getInput().isKeyPressed(Input.KEY_SPACE)){
			luke.decreaseHealth(10);
		}
		else{
			sprite = still;
		}
		sprite.update(delta);
	}
	@Override
	public int getID() {
		return State.GAME;
	}

}