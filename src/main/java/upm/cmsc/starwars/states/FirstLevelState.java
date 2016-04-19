package upm.cmsc.starwars.states;

import static upm.cmsc.starwars.entities.Action.ATTACK;
import static upm.cmsc.starwars.entities.Action.JUMP;
import static upm.cmsc.starwars.entities.Action.STILL;
import static upm.cmsc.starwars.entities.Action.WALK;
import static upm.cmsc.starwars.entities.Action.TOATTACK;
import static upm.cmsc.starwars.entities.LukeSkywalker.ATTACK_DURATION;
import static upm.cmsc.starwars.entities.LukeSkywalker.H_DISPLACEMENT_BACKWARD;
import static upm.cmsc.starwars.entities.LukeSkywalker.H_DISPLACEMENT_FORWARD;
import static upm.cmsc.starwars.entities.LukeSkywalker.INIT_H_VELOCITY;
import static upm.cmsc.starwars.entities.LukeSkywalker.JUMP_DURATION;
import static upm.cmsc.starwars.entities.LukeSkywalker.MIN_DIST_FROM_DISTANCE;

import java.util.ArrayList;
import java.util.List;

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
import upm.cmsc.starwars.entities.Action;
import upm.cmsc.starwars.entities.BattleDroid;
import upm.cmsc.starwars.entities.GeneralGrievous;
import upm.cmsc.starwars.entities.Laser;
import upm.cmsc.starwars.entities.LukeSkywalker;

public class FirstLevelState extends BasicGameState{
	
	private final float LUKE_MIN_Y = 510;
	private final float DROID_Y = 520;
	private final float LASER_Y = 525;
	private final float MAX_X = 400;
	private final float MIN_X = 0;
	private final int DROID_COUNT = 1;
	private final long LASER_INTERVAL = 3000;
	
	private Image tree,tumbleweed,background,path,avatar_grievous,avatar_luke;
	private boolean attacking,jumping,paused,preboss=false,inplace=false,postboss=false;
	private float hVelocity;
	private long timeStarted;
	private long timeLastBulletFired;
	private float treeX = 10;
	private float tumbleweedX = -10;
	private int talkCounter = 0;
	
	private LukeSkywalker luke;
	private GeneralGrievous general;
	private List<BattleDroid> droids = new ArrayList<BattleDroid>();
	private List<Laser> lasers = new ArrayList<Laser>();
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		loadImages();
		loadEnemyUnits();
		initializeCharacters();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		
		removeDeadEnemyUnits();
		
		float x;
		
		background.draw();
		
		if(!preboss){
			g.setColor(Color.black);
			g.drawString("Luke Skywalker", 30, 10);
			g.setColor(Color.black);
			g.fillRect(40, 30, LukeSkywalker.MAX_HEALTH, 20);
			g.setColor(luke.getCurrentHealthColor());
			g.fillRect(40, 30, luke.getCurrHealth(), 20);
		}
		
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
		
		for(BattleDroid droid:droids){
			droid.getAnimation().draw(droid.getX(), droid.getY());
		}
		
		luke.getAnimation().draw(luke.getX(),luke.getY());
		
		for(Laser laser:lasers){
			laser.getImage().draw(laser.getX(), laser.getY());
		}
		
		if(droids.isEmpty()){
			if(preboss){
				g.setColor(Color.white);
				g.fillRect(40, 30, 700, 120);
				g.setColor(Color.black);
				g.fillRect(50, 40, 680, 100);
				if(!inplace){
					general.setAnimation(WALK);
				}
				else{
					general.setAnimation(STILL);
					switch(talkCounter){
						case 0: g.setColor(Color.white);
								g.drawString("Press S to continue.", 55, 45);
								break;
						case 1: g.drawImage(avatar_grievous, 640, 50);
								g.setColor(Color.red);
								g.drawString("General Grievous", 60, 50);
								g.setColor(Color.white);
								g.drawString("Luke Skywalker. Hello, it's me.", 60, 80);
								break;
						case 2: g.drawImage(avatar_luke, 60, 50);
								g.setColor(Color.blue);
								g.drawString("Luke Skywalker", 150, 50);
								g.setColor(Color.white);
								g.drawString("General Grievous. I was wondering if after all these years", 150, 80);
								g.drawString("you'd like to meet.", 150, 100);
								break;
						case 3: preboss = false;
								break;
					}
				}
			}else{
				g.setColor(Color.black);
				g.drawString("General Grievous", 620, 10);
				g.setColor(Color.black);
				g.fillRect(560, 30, GeneralGrievous.MAX_HEALTH, 20);
				g.setColor(general.getCurrentHealthColor());
				g.fillRect(560, 30, general.getCurrHealth(), 20);
			}
			general.getAnimation().draw(general.getX(),general.getY());
		}
		
		if(paused){
			g.setColor(Color.black);
			g.drawString("PAUSED", 370, 200);
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
				
		if(preboss&&general.getX()>=Window.WIDTH-200){
			general.addToX(-H_DISPLACEMENT_FORWARD*5);
			general.getAnimation().update(delta);
		}else if(preboss&&general.getX()<Window.WIDTH){
			inplace = true;
		}
		
		if(gc.getInput().isKeyPressed(Input.KEY_SPACE)){
			paused = !paused;
			gc.setPaused(paused);
		}
		
		if(gc.getInput().isKeyPressed(Input.KEY_S)&&preboss){
			talkCounter++;
		}
		
		if(paused){
			return;
		}
		
		if(gc.getInput().isKeyDown(Input.KEY_RIGHT) && !jumping && !isLukeColliding() && !preboss){
			luke.setAnimation(WALK);
			if(luke.getX()<MAX_X){
				luke.addToX(delta*H_DISPLACEMENT_FORWARD);
				if(luke.getX()>MAX_X){
					luke.setX(MAX_X);
				}
			}
			else{
				treeX -= delta * 0.05f;
				general.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				tumbleweedX -= delta*H_DISPLACEMENT_FORWARD;
				for(BattleDroid droid: droids){
					droid.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				}
			}
			
			
		}
		else if(gc.getInput().isKeyDown(Input.KEY_LEFT) && !jumping && !preboss){
			luke.setAnimation(WALK);
			if(luke.getX()<=MAX_X){
				luke.addToX(-1*delta*H_DISPLACEMENT_BACKWARD);
				if(luke.getX()<MIN_X){
					luke.setX(MIN_X);
					luke.setAnimation(STILL);
				}
			}
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_A) && !attacking && !jumping && !preboss){
				luke.setAnimation(ATTACK);
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
		else if(gc.getInput().isKeyPressed(Input.KEY_UP) && !jumping && !preboss){
			luke.setAnimation(JUMP);
			jumping = true;
			timeStarted = System.currentTimeMillis();
			hVelocity = INIT_H_VELOCITY;
		}
		else if(jumping){
			long timeCurr = System.currentTimeMillis();
			if((timeCurr-timeStarted<JUMP_DURATION)&&hVelocity>0){
				luke.addToY(hVelocity*-1);
				hVelocity -= 5;
			}
			else if(timeCurr-timeStarted>JUMP_DURATION){
				hVelocity += 5;
				luke.addToY(hVelocity);
				if(hVelocity >= INIT_H_VELOCITY){
					jumping=false;
					luke.setY(LUKE_MIN_Y);
				}
			}
		}
		else{
			luke.setAnimation(STILL);
		}
		
		for(BattleDroid droid:droids){
			if(!droid.isDead()){
				droid.getAnimation().update(delta);
			}
		}
		
		for(Laser laser: lasers){
			laser.updateXPosition(delta);
		}
		checkIfAttacked();
		
		general.getAnimation().update(delta);
		luke.getAnimation().update(delta);
		
		
		for(BattleDroid droid:droids){
			long currTime = System.currentTimeMillis();
			if(currTime - timeLastBulletFired > LASER_INTERVAL){
				if(droid.getX()<=Window.WIDTH && droid.getX()>= 0 && !droid.isDead() 
						&& !droid.isDying()){
					lasers.add(new Laser(droid.getX()-10, LASER_Y));
					timeLastBulletFired = System.currentTimeMillis();
				}
			}
		}
		if(general.isDead()){
			s.enterState(State.SECOND_LEVEL);
		}
		if(luke.isDead()){
			s.enterState(State.GAMEOVER);
		}
		if(droids.isEmpty()&&!postboss){
			preboss = true;
			postboss = true;
		}
	}

	private void initializeCharacters() throws SlickException{
		luke = new LukeSkywalker();
		luke.setY(LUKE_MIN_Y);
		
		general = new GeneralGrievous();
		general.setY(LUKE_MIN_Y - 35);
		System.out.println("Luke: " + luke.getY() + " - General: " + general.getY());
		BattleDroid laBattleDroid = droids.get(droids.size()-1);
		general.setX(laBattleDroid.getX() + Window.WIDTH);
		
	}
	
	private void loadImages() throws SlickException{
		tree = new Image(CustomFileUtil.getFilePath("/elements/tree.png"));
		tumbleweed = new Image(CustomFileUtil.getFilePath("/elements/tumbleweed.png"));
		background = new Image(CustomFileUtil.getFilePath("/background/desert.jpg"));
		path = new Image(CustomFileUtil.getFilePath("/elements/desert_path.png"));
		avatar_grievous = new Image(CustomFileUtil.getFilePath("/avatars/avatar_grievous.png"));
		avatar_luke = new Image(CustomFileUtil.getFilePath("/avatars/avatar_luke.png"));
	}
	
	private void loadEnemyUnits() throws SlickException{
		float x = 0;
		for(int i=0;i<DROID_COUNT;i++){
			x+=500+Math.random()*1000;
			droids.add(new BattleDroid(x,DROID_Y));
		} 
	}
	
	private void removeDeadEnemyUnits(){
		for(int i=0;i<droids.size();i++){
			BattleDroid droid = droids.get(i);
			float droidRight = droid.getX() + droid.getAnimation().getWidth();
			if(droid.isDead() && droidRight<= 0 ){
				droids.remove(i);
				i--;
			}
		}
	}
	
	private boolean isLukeColliding(){
		for(BattleDroid st:droids){
			if(!st.isDead() && st.getX()-luke.getX()<=MIN_DIST_FROM_DISTANCE){
				return true;
			}
		}
		if(general.getX()-luke.getX()<=MIN_DIST_FROM_DISTANCE){
			return true;
		}
		return false;
	}
	
	private void checkIfAttacked(){
		for(Laser laser:lasers){
			float lukeRight = ((float)luke.getAnimation().getWidth()/2) + luke.getX();
			if(laser.getX() <= lukeRight && laser.getX()>=luke.getX() 
					&& !jumping){
				luke.decreaseHealth(Laser.DAMAGE);
				lasers.remove(laser);
				return;
			}
		}
		long timeDiff = System.currentTimeMillis() - general.getTimeLastAttack();
		/*if(general.getX()-luke.getX()<=MIN_DIST_FROM_DISTANCE){
			if(timeDiff >= GeneralGrievous.ATTACK_INTERVAL){
				general.attack(luke);
			}
			else if(timeDiff >= 600){
				general.setAnimation(STILL);
			}
		}
		else if(timeDiff >= 600){
			general.setAnimation(Action.STILL);
		}*/
		if(timeDiff >= GeneralGrievous.ATTACK_INTERVAL){
			general.attack(luke);
		}
		else if(timeDiff >= 1000){
			general.setAnimation(TOATTACK);
		}else if(timeDiff >= 600){
			general.setAnimation(STILL);
		}
	}
	
	private void attack(){
		for(BattleDroid droid: droids){
			if(!droid.isDead() && droid.getX() - luke.getX() <= MIN_DIST_FROM_DISTANCE + 20
					&& !droid.isDying() && !jumping){
				droid.setDead(true);
				return;
			}
		}
		if(general.getX()-luke.getX()<=MIN_DIST_FROM_DISTANCE){
			general.decreaseHealth(luke.getDamage());
		}
	}
	
	@Override
	public int getID() {
		return State.FIRST_LEVEL;
	}
}