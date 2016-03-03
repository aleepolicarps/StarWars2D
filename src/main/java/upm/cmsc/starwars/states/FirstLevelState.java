package upm.cmsc.starwars.states;

import static upm.cmsc.starwars.entities.Contstants.ATTACK_DURATION;
import static upm.cmsc.starwars.entities.Contstants.H_DISPLACEMENT_BACKWARD;
import static upm.cmsc.starwars.entities.Contstants.H_DISPLACEMENT_FORWARD;
import static upm.cmsc.starwars.entities.Contstants.INIT_H_VELOCITY;
import static upm.cmsc.starwars.entities.Contstants.JUMP_DURATION;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.jws.soap.SOAPBinding.Style;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import upm.cmsc.starwars.CustomFileUtil;
import upm.cmsc.starwars.Window;
import upm.cmsc.starwars.entities.Laser;
import upm.cmsc.starwars.entities.LukeSkywalker;
import upm.cmsc.starwars.entities.LukeSkywalker.LukeAction;
import upm.cmsc.starwars.entities.StormTrooper;


public class FirstLevelState extends BasicGameState{
	
	private final float LUKE_MIN_Y = 510;
	private final float TROOPER_Y = 525;
	private final float LASER_Y = 540;
	private final float MAX_X = 300;
	private final float MIN_X = 0;
	private final int TROOPER_COUNT = 5;
	private final int LUKE_TROOPER_DISTANCE= 30;
	private final long LASER_INTERVAL = 3000;
	private final int LASER_DAMAGE = 25;
	
	
	private Image tree,tumbleweed,background,path;
	private boolean attacking,jumping,paused;
	private float hVelocity;
	private long timeStarted;
	private long timeLastBulletFired;
	private float treeX = 10;
	private float tumbleweedX = -10;
	
	private LukeSkywalker luke;
	private List<StormTrooper> troopers = new ArrayList<StormTrooper>();
	private List<Laser> lasers = new ArrayList<Laser>();

	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		loadLukeAnimations();
		loadImages();
		loadTroopers();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		
		
		removeDeadTroopers();
		if(troopers.isEmpty()){
			// TODO mega boss? new state?
		}
		
		float x;
		
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
		
		for(StormTrooper trooper:troopers){
			trooper.getAnimation().draw(trooper.getX(), trooper.getY());
			long currTime = System.currentTimeMillis();
			if(currTime - timeLastBulletFired > LASER_INTERVAL){
				if(trooper.getX()<=Window.WIDTH && trooper.getX()>= 0 && !trooper.isDead()){
					lasers.add(new Laser(trooper.getX()-10, TROOPER_Y));
					timeLastBulletFired = System.currentTimeMillis();
				}
			}
		}
		
		luke.getAnimation().draw(luke.getX(),luke.getY());
		
		for(Laser laser:lasers){
			laser.getImage().draw(laser.getX(), laser.getY());
		}
		
		
		if(paused){
			g.setColor(Color.black);
			g.drawString("PAUSED", 370, 200);
		}
		
	}

	
	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		
		
		if(gc.getInput().isKeyPressed(Input.KEY_SPACE)){
			paused = !paused;
			gc.setPaused(paused);
		}
		
		if(paused){
			return;
		}
		
		if(gc.getInput().isKeyDown(Input.KEY_RIGHT) && !jumping && !isLukeCollidingWithTrooper()){
			luke.setAnimation(LukeAction.WALK);
			if(luke.getX()<MAX_X){
				luke.addToX(delta*H_DISPLACEMENT_FORWARD);
				if(luke.getX()>MAX_X){
					luke.setX(MAX_X);
				}
			}
			else{
				treeX -= delta * 0.05f;
				tumbleweedX -= delta*H_DISPLACEMENT_FORWARD;
				for(StormTrooper trooper: troopers){
					trooper.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				}
			}
			
			
		}
		else if(gc.getInput().isKeyDown(Input.KEY_LEFT) && !jumping){
			luke.setAnimation(LukeAction.WALK);
			if(luke.getX()<=MAX_X){
				luke.addToX(-1*delta*H_DISPLACEMENT_BACKWARD);
				if(luke.getX()<MIN_X){
					luke.setX(MIN_X);
					luke.setAnimation(LukeAction.STILL);
				}
			}
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_A) && !attacking){
				luke.setAnimation(LukeAction.ATTACK);
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
			luke.setAnimation(LukeAction.JUMP);
			jumping = true;
			timeStarted = System.currentTimeMillis();
			hVelocity = INIT_H_VELOCITY;
		}
		else if(jumping){
			long timeCurr = System.currentTimeMillis();
			if((timeCurr-timeStarted<JUMP_DURATION)&&hVelocity>0){
				luke.addToY(hVelocity*-1);
				hVelocity -= delta;
			}
			else if(timeCurr-timeStarted>JUMP_DURATION){
				jumping=false;
				luke.setY(LUKE_MIN_Y);
			}
		}
		else{
			luke.setAnimation(LukeAction.STILL);
		}
		
		
		for(StormTrooper trooper:troopers){
			if(!trooper.isDead()){
				trooper.getAnimation().update(delta);
			}
		}
		
		for(Laser laser: lasers){
			laser.updateXPosition(delta);
		}
		checkIfAttacked();
		luke.getAnimation().update(delta);
		if(luke.isDead()){
			s.enterState(State.GAMEOVER);
		}
	}
	@Override
	public int getID() {
		return State.FIRST_LEVEL;
	}
	
	
	
	private void loadTroopers() throws SlickException{
		float x = 0;
		for(int i=0;i<TROOPER_COUNT;i++){
			x+=500+Math.random()*1000;
			troopers.add(new StormTrooper(x,TROOPER_Y));
		} 

	}
	
	private void loadLukeAnimations() throws SlickException{
		luke = new LukeSkywalker();
		luke.setY(LUKE_MIN_Y);
		
	}
	
	private void loadImages() throws SlickException{
		tree = new Image(CustomFileUtil.getFilePath("/elements/tree.png"));
		tumbleweed = new Image(CustomFileUtil.getFilePath("/elements/tumbleweed.png"));
		background = new Image(CustomFileUtil.getFilePath("/background/desert.jpg"));
		path = new Image(CustomFileUtil.getFilePath("/elements/desert_path.png"));
	}
	
	private void removeDeadTroopers(){
		for(int i=0;i<troopers.size();i++){
			StormTrooper trooper = troopers.get(i);
			float trooperRight = trooper.getX() + trooper.getAnimation().getWidth();
			if(trooper.isDead() && trooperRight<= 0 ){
				troopers.remove(i);
				i--;
			}
		}
	}
	
	private boolean isLukeCollidingWithTrooper(){
		for(StormTrooper st:troopers){
			if(!st.isDead() && st.getX()-luke.getX()<=LUKE_TROOPER_DISTANCE){
				return true;
			}
		}
		return false;
	}
	
	private void checkIfAttacked(){
		for(Laser laser:lasers){
			float lukeRight = ((float)luke.getAnimation().getWidth()/2) + luke.getX();
			if(laser.getX() <= lukeRight && laser.getX()>=luke.getX() 
					&& !jumping){
				luke.decreaseHealth(LASER_DAMAGE);
				lasers.remove(laser);
				return;
			}
		}
	}
	
	private void attack(){
		for(StormTrooper trooper: troopers){
			if(!trooper.isDead() && trooper.getX() - luke.getX() <= LUKE_TROOPER_DISTANCE + 20
					&& !trooper.isDying()){
				trooper.setDead(true);
				return;
			}
		}
	}
}