package upm.cmsc.starwars.states;

import static upm.cmsc.starwars.entities.Action.ATTACK;
import static upm.cmsc.starwars.entities.Action.JUMP;
import static upm.cmsc.starwars.entities.Action.STILL;
import static upm.cmsc.starwars.entities.Action.TOATTACK;
import static upm.cmsc.starwars.entities.Action.WALK;
import static upm.cmsc.starwars.entities.Action.TOSHIELD;
import static upm.cmsc.starwars.entities.Action.ASSEMBLE;
import static upm.cmsc.starwars.entities.LukeSkywalker.ATTACK_DURATION;
import static upm.cmsc.starwars.entities.LukeSkywalker.H_DISPLACEMENT_BACKWARD;
import static upm.cmsc.starwars.entities.LukeSkywalker.H_DISPLACEMENT_FORWARD;
import static upm.cmsc.starwars.entities.LukeSkywalker.INIT_H_VELOCITY;
import static upm.cmsc.starwars.entities.LukeSkywalker.JUMP_DURATION;
import static upm.cmsc.starwars.entities.LukeSkywalker.MIN_DIST_FROM_DISTANCE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import upm.cmsc.starwars.entities.Droideka;
import upm.cmsc.starwars.entities.GeneralGrievous;
import upm.cmsc.starwars.entities.Laser;
import upm.cmsc.starwars.entities.LukeSkywalker;
import upm.cmsc.starwars.entities.StormTrooper;

public class SecondLevelState extends BasicGameState{
	
	private final float LUKE_MIN_Y = 340;
	private final float TROOPER_Y = 350;
	private final float LASER_Y = 350;
	private final float MAX_X = 400;
	private final float MIN_X = 0;
	private final int TROOPER_COUNT = 1;
	private final long LASER_INTERVAL = 3000;
	
	private Image background,avatar_grievous,avatar_luke;
	private boolean attacking,jumping,paused,preboss=false,inplace=false,postboss=false,assemble=false;
	
	//trial gun
	private boolean pickgun, alreadyPickedGun;
	private float lastTrooperX = 0;
	private int pickCounter = 0;
	
	private float hVelocity;
	private long timeStarted;
	private long timeLastBulletFired;
	private float bgX = 0;
	private int talkCounter = 0;
	private int randChoice = 0;
	private boolean randSet = false;
	
	private LukeSkywalker luke;
	private Droideka droideka;
	private List<StormTrooper> troopers = new ArrayList<StormTrooper>();
	private List<Laser> lasers = new ArrayList<Laser>();
	private List<Laser> laserLuke = new ArrayList<Laser>();
	
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
		x = bgX;
		while(x<background.getWidth()){
			background.draw(x, 0);
			x+=800;
		}
		
		if(!preboss){
			g.setColor(Color.black);
			g.drawString("Luke Skywalker", 30, 10);
			g.setColor(Color.black);
			g.fillRect(40, 30, LukeSkywalker.MAX_HEALTH, 20);
			g.setColor(luke.getCurrentHealthColor());
			g.fillRect(40, 30, luke.getCurrHealth(), 20);
		}
		
		for(StormTrooper trooper:troopers){
			trooper.getAnimation().draw(trooper.getX(), trooper.getY());
		}
		
		luke.getAnimation().draw(luke.getX(),luke.getY());
		
		for(Laser laser:lasers){
			laser.getImage().draw(laser.getX(), laser.getY());
		}
		for(Laser laser:laserLuke){
			laser.getImage().draw(laser.getX(), laser.getY());
		}
		
		if(pickgun){
			g.setColor(Color.white);
			g.fillRect(40, 30, 700, 120);
			g.setColor(Color.black);
			g.fillRect(50, 40, 680, 100);
			switch(pickCounter){
			case 0: g.setColor(Color.white);
					g.drawString("You picked up a laser rifle!", 55, 45);
					g.drawString("Press S to continue.", 55, 100);
					luke.setRifleLuke(true);
					break;
			case 1: g.drawImage(avatar_luke, 60, 50);
					g.setColor(Color.blue);
					g.drawString("Luke Skywalker", 150, 50);
					g.setColor(Color.white);
					g.drawString("This might be useful.", 150, 80);
					break;
			case 2: pickgun = false;
					alreadyPickedGun = true;
					break;
			}
		}
		
		if(troopers.isEmpty()){			
			if(preboss){
				g.setColor(Color.white);
				g.fillRect(40, 30, 700, 120);
				g.setColor(Color.black);
				g.fillRect(50, 40, 680, 100);
				if(!inplace){
					droideka.setAnimation(WALK);
				}else if(!assemble){
					droideka.setAnimation(ASSEMBLE);
				}
				else{
					droideka.setAnimation(STILL);
					switch(talkCounter){
						case 0: g.setColor(Color.white);
								g.drawString("Press S to continue.", 55, 45);
								break;
						case 1: g.drawImage(avatar_grievous, 640, 50);
								g.setColor(Color.red);
								g.drawString("Droideka", 60, 50);
								g.setColor(Color.white);
								g.drawString("BOOP BEEP BOOP.", 60, 80);
								break;
						case 2: g.drawImage(avatar_luke, 60, 50);
								g.setColor(Color.blue);
								g.drawString("Luke Skywalker", 150, 50);
								g.setColor(Color.white);
								g.drawString("A Droideka.", 150, 80);
								//g.drawString("", 150, 100);
								break;
						case 3: preboss = false;
								postboss = true;
								break;
					}
				}
			}else{
				g.setColor(Color.black);
				g.drawString("Droideka", 620, 10);
				g.setColor(Color.black);
				g.fillRect(560, 30, Droideka.MAX_HEALTH, 20);
				g.setColor(droideka.getCurrentHealthColor());
				g.fillRect(560, 30, droideka.getCurrHealth(), 20);
			}
			droideka.getAnimation().draw(droideka.getX(),droideka.getY());
		}
		
		if(paused){
			g.setColor(Color.black);
			g.drawString("PAUSED", 370, 200);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		
		if(luke.getX()>=lastTrooperX&&!alreadyPickedGun){
			pickgun = true;
		}
		
		if(preboss&&droideka.getX()>=Window.WIDTH-200){
			droideka.addToX(-H_DISPLACEMENT_FORWARD*5);
			droideka.getAnimation().update(delta);
		}else if(preboss&&droideka.getX()<Window.WIDTH&&!inplace){
			inplace = true;
			droideka.setToAssemble(System.currentTimeMillis());
		}else if(preboss&&inplace&&!assemble){
			long currTime = System.currentTimeMillis();
			if (currTime - droideka.getToAssemble() >= 300){
				assemble = true;
			}
		}
		
		if(gc.getInput().isKeyPressed(Input.KEY_SPACE)){
			paused = !paused;
			gc.setPaused(paused);
		}
		
		if(gc.getInput().isKeyPressed(Input.KEY_S)&&(preboss||pickgun)){
			if(pickgun){
				pickCounter++;
			}else if(preboss){	
				talkCounter++;
			}
		}
				
		if(paused){
			return;
		}
		
		if(gc.getInput().isKeyDown(Input.KEY_RIGHT) && !jumping && !isLukeColliding() && !preboss && !pickgun){
			luke.setAnimation(WALK);
			if(luke.getX()<MAX_X){
				luke.addToX(delta*H_DISPLACEMENT_FORWARD);
				if(luke.getX()>MAX_X){
					luke.setX(MAX_X);
				}
			}
			else{
				bgX -= delta * 0.2f;
				droideka.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				for(StormTrooper trooper: troopers){
					trooper.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				}
			}
			
			
		}
		else if(gc.getInput().isKeyDown(Input.KEY_LEFT) && !jumping && !preboss  && !pickgun){
			luke.setAnimation(WALK);
			if(luke.getX()<=MAX_X){
				luke.addToX(-1*delta*H_DISPLACEMENT_BACKWARD);
				if(luke.getX()<MIN_X){
					luke.setX(MIN_X);
					luke.setAnimation(STILL);
				}
			}
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_A) && !attacking && !jumping && !preboss  && !pickgun){
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
		else if(gc.getInput().isKeyPressed(Input.KEY_UP) && !jumping && !preboss  && !pickgun){
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
		
		for(StormTrooper trooper:troopers){
			if(!trooper.isDead()){
				trooper.getAnimation().update(delta);
			}
		}
		
		for(Laser laser: lasers){
			laser.updateXPosition(delta);
		}
		for(Laser laser: laserLuke){
			laser.updateXPosition(delta);
		}
		checkIfAttacked();
		
		droideka.getAnimation().update(delta);
		luke.getAnimation().update(delta);
		
		
		for(StormTrooper trooper:troopers){
			long currTime = System.currentTimeMillis();
			if(currTime - timeLastBulletFired > LASER_INTERVAL){
				if(trooper.getX()<=Window.WIDTH && trooper.getX()>= 0 && !trooper.isDead() 
						&& !trooper.isDying()){
					lasers.add(new Laser(trooper.getX()-10, LASER_Y));
					timeLastBulletFired = System.currentTimeMillis();
				}
			}
		}

		if(postboss){
			long timeDiff = System.currentTimeMillis() - droideka.getTimeLastAttack();
			Random rand = new Random();
			if(!randSet){
				randChoice = rand.nextInt(2);
				randSet = true;
			}
			if(timeDiff >= Droideka.ATTACK_INTERVAL){
				if(randChoice == 0){
					lasers.add(new Laser(droideka.getX()-10, LASER_Y));
					droideka.attack();
				}else{
					droideka.shield();
				}
				randSet = false;
			}
			else if(timeDiff >= 1600){
				if(randChoice == 0)
					droideka.setAnimation(TOATTACK);
				else
					droideka.setAnimation(TOSHIELD);
			}else if(timeDiff >= 600){
				droideka.setAnimation(STILL);
			}
		}
		
		if(droideka.isDead()){
			s.enterState(State.SECOND_LEVEL); // TODO change this according to which level is next
		}
		if(luke.isDead()){
			s.enterState(State.GAMEOVER);
		}
		
		if(troopers.isEmpty()&&!postboss){
			preboss = true;
			pickgun = true;
		}
	}
	
	private void initializeCharacters() throws SlickException{
		luke = new LukeSkywalker();
		luke.setY(LUKE_MIN_Y);
		
		droideka = new Droideka();
		droideka.setY(LUKE_MIN_Y - 20);
		StormTrooper laStormTrooper = troopers.get(troopers.size()-1);
		droideka.setX(laStormTrooper.getX() + Window.WIDTH);
		
	}
	
	private void loadImages() throws SlickException{
		background = new Image(CustomFileUtil.getFilePath("/background/spaceship_interior.png"));
		avatar_grievous = new Image(CustomFileUtil.getFilePath("/avatars/avatar_grievous.png"));
		avatar_luke = new Image(CustomFileUtil.getFilePath("/avatars/avatar_luke.png"));
	}
	
	private void loadEnemyUnits() throws SlickException{
		float x = 0;
		for(int i=0;i<TROOPER_COUNT;i++){
			x+=500+Math.random()*1000;
			troopers.add(new StormTrooper(x,TROOPER_Y));
		} 
	}
		
	private void removeDeadEnemyUnits(){
		for(int i=0;i<troopers.size();i++){
			StormTrooper trooper = troopers.get(i);
			float trooperRight = trooper.getX() + trooper.getAnimation().getWidth();
			if (troopers.size() == 1){
				lastTrooperX = trooper.getX() - 20;
			}
			if(trooper.isDead() && trooperRight<= 0 ){
				troopers.remove(i);
				i--;
			}
		}
	}
	
	private boolean isLukeColliding(){
		for(StormTrooper st:troopers){
			if(!st.isDead() && st.getX()-luke.getX()<=MIN_DIST_FROM_DISTANCE){
				return true;
			}
		}
		if(droideka.getX()-luke.getX()<=MIN_DIST_FROM_DISTANCE){
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
		int laserLukeCount = 0;
		while(laserLuke.size()>laserLukeCount){
			float droidekaLeft = ((float)droideka.getAnimation().getWidth()/2) + droideka.getX();
			System.out.println(droidekaLeft + " " + droideka.getX() + " " + laserLuke.get(laserLukeCount).getX());
			if(laserLuke.get(laserLukeCount).getX() <= droidekaLeft && laserLuke.get(laserLukeCount).getX()>=droideka.getX()){
				if(!droideka.isShielding()){
					droideka.decreaseHealth(luke.getDamage());
				}
				laserLuke.remove(laserLuke.get(laserLukeCount));
			}else if(laserLuke.get(laserLukeCount).getX()>=Window.WIDTH){
				laserLuke.remove(laserLuke.get(laserLukeCount));
			}
			laserLukeCount++;
		}
	}
	
	private void attack(){
		if(alreadyPickedGun){
			try {
				laserLuke.add(new Laser(luke.getX()+10, LASER_Y, 1));
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			for(StormTrooper trooper: troopers){
				if(!trooper.isDead() && trooper.getX() - luke.getX() <= MIN_DIST_FROM_DISTANCE + 20
						&& !trooper.isDying() && !jumping){
					trooper.setDead(true);
					return;
				}
			}
		}
		/*if(droideka.getX()-luke.getX()<=MIN_DIST_FROM_DISTANCE){
			if(!droideka.isShielding())
				droideka.decreaseHealth(luke.getDamage());
		}*/
	}

	@Override
	public int getID() {
		return State.SECOND_LEVEL;
	}
}