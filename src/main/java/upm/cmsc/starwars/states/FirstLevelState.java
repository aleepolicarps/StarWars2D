package upm.cmsc.starwars.states;

import static upm.cmsc.starwars.entities.Contstants.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import upm.cmsc.starwars.entities.LukeSkywalker;
import upm.cmsc.starwars.entities.StormTrooper;


public class FirstLevelState extends BasicGameState{
	
	private final float LUKE_MIN_Y = 510;
	private final float TROOPER_MIN_Y = 525;
	private final float MAX_X = 300;
	private final float MIN_X = 0;
	private final int TROOPER_COUNT = 10;
	private final int LUKE_TROOPER_DISTANCE= 30;
	
	
	private Animation sprite, right, still,attack,jump;
	private Image tree,tumbleweed,background,path;
	private boolean attacking,jumping;
	private float hVelocity;
	private long timeStarted;
	private float treeX = 10;
	private float tumbleweedX = -10;
	
	private LukeSkywalker luke;
	private List<StormTrooper> troopers;
	private List<Animation> trooperAnimation;

	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		loadLukeAnimations();
		loadImages();
		loadTroopers();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		
		float x,y;
		
		background.draw();
		
		g.setColor(Color.black);
		g.drawString("Luke Skywalker", 30, 10);
		g.setColor(Color.black);
		g.fillRect(40, 30, LukeSkywalker.MAX_HEALTH, 20);
		g.setColor(luke.getCurrentHealthColor());
		g.fillRect(40, 30, luke.getCurrHealth(), 20);
		
		x = treeX;
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
		
		for(int i = 0;i<TROOPER_COUNT;i++){
			x = troopers.get(i).getX();
			y = troopers.get(i).getY();
			trooperAnimation.get(i).draw(x, y);
		}

		
	}

	
	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {	
		if(gc.getInput().isKeyDown(Input.KEY_RIGHT) && !jumping && !isLukeColliding()){
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
				for(StormTrooper st: troopers){
					st.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				}
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
				attack();	
				
		}
		else if(attacking){
			long timeCurr = System.currentTimeMillis();
			if(timeCurr-timeStarted>ATTACK_DURATION){
				attacking = false;
			}
		}
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
				hVelocity = (float) Math.sqrt(INITIAL_VELOCITY+2*(GRAVITY*(luke.getY()-LUKE_MIN_Y)));
			}
			else if(timeCurr-timeStarted>JUMP_DURATION){
				jumping=false;
				luke.setY(LUKE_MIN_Y);
			}
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_SPACE)){
			luke.decreaseHealth(10);
			if(luke.isDead()){
				s.enterState(State.GAMEOVER);
			}
		}
		else{
			sprite = still;
		}
		for(int i = 0; i< TROOPER_COUNT; i++){
			if(!troopers.get(i).isDead()){
				trooperAnimation.get(i).update(delta);
			}
		}
		sprite.update(delta);
	}
	@Override
	public int getID() {
		return State.FIRST_LEVEL;
	}
	
	
	
	private void loadTroopers() throws SlickException{
		troopers = new ArrayList<StormTrooper>(TROOPER_COUNT);
		float x = 0;
		for(int i=0;i<TROOPER_COUNT;i++){
			x+=500+Math.random()*1000;
			troopers.add(new StormTrooper(x,TROOPER_MIN_Y));
		} 
		trooperAnimation = new ArrayList<Animation>(TROOPER_COUNT);
		for(int i=0;i<TROOPER_COUNT;i++){
			trooperAnimation.add(StormTrooper.getAttackAnimation());
		}
	}
	
	private void loadLukeAnimations() throws SlickException{
		luke = new LukeSkywalker();
		luke.setY(LUKE_MIN_Y);
		right = LukeSkywalker.getRightAnimation();
		still = LukeSkywalker.getNoAnimation();
		attack = LukeSkywalker.getAttackAnimation();
		jump = LukeSkywalker.getJumpAnimation();
		sprite = still;	
		
	}
	
	private void loadImages() throws SlickException{
		try {
			String treeFilePath = this.getClass().getResource("/elements/tree.png").toURI().getPath();
			String tumbleWeedFilePath = this.getClass().getResource("/elements/tumbleweed.png").toURI().getPath();
			String desertFilePath = this.getClass().getResource("/background/desert.jpg").toURI().getPath();
			String desertPathFilePath = this.getClass().getResource("/elements/desert_path.png").toURI().getPath();
			if(isWindows()){
				treeFilePath = treeFilePath.substring(1);
				tumbleWeedFilePath = tumbleWeedFilePath.substring(1);
				desertFilePath = desertFilePath.substring(1);
				desertPathFilePath = desertPathFilePath.substring(1);
			}
			tree = new Image(treeFilePath);
			tumbleweed = new Image(tumbleWeedFilePath);
			background = new Image(desertFilePath);
			path = new Image(desertPathFilePath);
		} catch (URISyntaxException e) {
			// TODO do something here
		}
	}
	
	private boolean isLukeColliding(){
		for(StormTrooper st:troopers){
			if(!st.isDead() && st.getX()-luke.getX()<=LUKE_TROOPER_DISTANCE){
				return true;
			}
		}
		return false;
	}
	
	private void attack(){
		for(int i = 0;i<TROOPER_COUNT;i++){
			StormTrooper st = troopers.get(i);
			if(!st.isDead() && st.getX()-luke.getX()<=LUKE_TROOPER_DISTANCE){
				st.setDead(true);
				trooperAnimation.set(i, StormTrooper.getDeadAnimation());
			}
		}
	}
}