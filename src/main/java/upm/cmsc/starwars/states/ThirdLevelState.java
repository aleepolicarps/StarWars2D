package upm.cmsc.starwars.states;

import static upm.cmsc.starwars.entities.Action.ATTACK;
import static upm.cmsc.starwars.entities.Action.DEAD;
import static upm.cmsc.starwars.entities.Action.TOATTACK;
import static upm.cmsc.starwars.entities.Action.TOSHIELD;
import static upm.cmsc.starwars.entities.Action.STILL;
import static upm.cmsc.starwars.entities.Action.WALK;
import static upm.cmsc.starwars.entities.XWingStarfighter.ATTACK_DURATION;
import static upm.cmsc.starwars.entities.XWingStarfighter.H_DISPLACEMENT_FORWARD;
import static upm.cmsc.starwars.entities.XWingStarfighter.H_DISPLACEMENT_UPWARD;
import static upm.cmsc.starwars.entities.XWingStarfighter.H_DISPLACEMENT_DOWNWARD;
import static upm.cmsc.starwars.entities.XWingStarfighter.MIN_DIST_FROM_DISTANCE_X;
import static upm.cmsc.starwars.entities.XWingStarfighter.MIN_DIST_FROM_DISTANCE_Y;

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
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import upm.cmsc.starwars.CustomFileUtil;
import upm.cmsc.starwars.Window;
import upm.cmsc.starwars.entities.DeathStar;
import upm.cmsc.starwars.entities.Droideka;
import upm.cmsc.starwars.entities.GeneralGrievous;
import upm.cmsc.starwars.entities.ImperialTIEFighter;
import upm.cmsc.starwars.entities.Laser;
import upm.cmsc.starwars.entities.XWingStarfighter;

public class ThirdLevelState extends BasicGameState{
	
	private final float LUKE_MIN_Y = 335;
	private final float MAX_X = 250;
	private final float MIN_X = 0;
	private final float MAX_Y = 550;
	private final float MIN_Y = 100;
	private final int FIGHTER_COUNT = 5;
	private final long LASER_INTERVAL = 500;
	
	private Image background, avatar_luke, avatar_r2d2, avatar_han, avatar_dstar;
	private boolean attacking,paused;
	private long timeStarted;
	private long timeLastBulletFired;
	private float bgX = 0;
	private float lastFighterX = 0;
	private XWingStarfighter xwing;
	private DeathStar dstar;
	private List<ImperialTIEFighter> fighters = new ArrayList<ImperialTIEFighter>();
	private List<Laser> lasers = new ArrayList<Laser>();
	private List<Laser> laserXwing= new ArrayList<Laser>();
	
	private boolean preboss=false,postboss=false,endlevel=false,escape=true,nofire=false,predialog=true;
	private int pickCounter = 0;
	private ImperialTIEFighter leia;
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		loadImages();
		loadEnemyUnits();
		initializeCharacters();
		preboss=false;postboss=false;endlevel=false;escape=true;nofire=false;predialog=true;
		pickCounter = 0; bgX=0; lastFighterX=0;
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
			g.setColor(Color.white);
			g.drawString("Luke Skywalker", 30, 10);
			g.setColor(Color.white);
			g.fillRect(40, 30, XWingStarfighter.MAX_HEALTH, 20);
			g.setColor(xwing.getCurrentHealthColor());
			g.fillRect(40, 30, xwing.getCurrHealth(), 20);
		}
		
		for(ImperialTIEFighter fighter:fighters){
			fighter.getAnimation().draw(fighter.getX(), fighter.getY());
		}
		
		xwing.getAnimation().draw(xwing.getX(),xwing.getY());
		
		for(Laser laser:lasers){
			laser.getImage().draw(laser.getX(), laser.getY());
		}
		for(Laser laser:laserXwing){
			laser.getImage().draw(laser.getX(), laser.getY());
		}
		
		if(predialog){
			g.setColor(Color.white);
			g.fillRect(40, 30, 700, 120);
			g.setColor(Color.black);
			g.fillRect(50, 40, 680, 100);
			switch(pickCounter){
			case 0: g.drawImage(avatar_luke, 60, 50);
					g.setColor(Color.blue);
					g.drawString("Luke Skywalker", 150, 50);
					g.setColor(Color.white);
					g.drawString("Why is the Death Star here?!?", 150, 80);
					break;
			case 1: g.drawImage(avatar_r2d2, 640, 50);
					g.setColor(Color.blue);
					g.drawString("R2-D2", 60, 50);
					g.setColor(Color.white);
					g.drawString("Beep Bloop Bleep Blop Boop", 60, 80);
					break;
			case 2: g.drawImage(avatar_luke, 60, 50);
					g.setColor(Color.blue);
					g.drawString("Luke Skywalker", 150, 50);
					g.setColor(Color.white);
					g.drawString("WHAT?!? Don't tell me they are targeting the planet?", 150, 80);
					g.drawString("", 150, 100);
					break;
			case 3: g.drawImage(avatar_r2d2, 640, 50);
					g.setColor(Color.blue);
					g.drawString("R2-D2", 60, 50);
					g.setColor(Color.white);
					g.drawString("Beep Beep Boop Boop", 60, 80);
					break;
			case 4: g.drawImage(avatar_luke, 60, 50);
					g.setColor(Color.blue);
					g.drawString("Luke Skywalker", 150, 50);
					g.setColor(Color.white);
					g.drawString("We must stop them at once. It only has a few Imperial TIE ", 150, 80);
					g.drawString("fighters guarding it. ", 150, 100);
					break;
			case 5: predialog = false;
					pickCounter = 0;
					break;
			}
		}
		
		if(fighters.isEmpty()){
			leia.getAnimation().draw(leia.getX(),leia.getY());
			if(preboss){
				g.setColor(Color.white);
				g.fillRect(40, 30, 700, 120);
				g.setColor(Color.black);
				g.fillRect(50, 40, 680, 100);
				switch(pickCounter){
				case 0: g.drawImage(avatar_luke, 60, 50);
						g.setColor(Color.blue);
						g.drawString("Luke Skywalker", 150, 50);
						g.setColor(Color.white);
						g.drawString("There it is. The Death Star.", 150, 80);
						break;
				case 1: g.drawImage(avatar_r2d2, 640, 50);
						g.setColor(Color.blue);
						g.drawString("R2-D2", 60, 50);
						g.setColor(Color.white);
						g.drawString("BLEEP BLEEP-- INCOMING TRANSMISSION.", 60, 80);
						break;
				case 2: g.drawImage(avatar_dstar, 640, 50);
						g.setColor(Color.red);
						g.drawString("Death Star Transmission", 60, 50);
						g.setColor(Color.white);
						g.drawString("Luke. Why are you here?", 60, 80);
						break;
				case 3: g.drawImage(avatar_luke, 60, 50);
						g.setColor(Color.blue);
						g.drawString("Luke Skywalker", 150, 50);
						g.setColor(Color.white);
						g.drawString("I am here to stop you from destroying this planet!", 150, 80);
						break;
				case 4: g.drawImage(avatar_dstar, 640, 50);
						g.setColor(Color.red);
						g.drawString("Death Star Transmission", 60, 50);
						g.setColor(Color.white);
						g.drawString("Good luck.", 60, 80);
						break;
				case 5: preboss = false;
						nofire = false;
						postboss = true;
						pickCounter = 0;
						break;
				}
			}else{
				g.setColor(Color.white);
				g.drawString("Death Star", 620, 10);
				g.fillRect(560, 30, GeneralGrievous.MAX_HEALTH, 20);
				g.setColor(dstar.getCurrentHealthColor());
				g.fillRect(560, 30, dstar.getCurrHealth(), 20);
			}
			if(endlevel){
				g.setColor(Color.white);
				g.fillRect(40, 30, 700, 120);
				g.setColor(Color.black);
				g.fillRect(50, 40, 680, 100);
				switch(pickCounter){
				case 0: if(dstar.isAttacking()){
							dstar.setX(dstar.getX()+824);
							dstar.endAttack();
							xwing.setY(dstar.getY()+dstar.getAnimation().getHeight()/2);
						}
						g.drawImage(avatar_r2d2, 640, 50);
						g.setColor(Color.blue);
						g.drawString("R2-D2", 60, 50);
						g.setColor(Color.white);
						g.drawString("Boop Bleep Beep Blop", 60, 80);
						break;
				case 1: g.drawImage(avatar_luke, 60, 50);
						g.setColor(Color.blue);
						g.drawString("Luke Skywalker", 150, 50);
						g.setColor(Color.white);
						g.drawString("Yes, one more hit and we can destroy it!", 150, 80);
						break;
				case 2: g.drawImage(avatar_r2d2, 640, 50);
						g.setColor(Color.blue);
						g.drawString("R2-D2", 60, 50);
						g.setColor(Color.white);
						g.drawString("BLEEP BLEEP-- INCOMING TRANSMISSION.", 60, 80);
						break;
				case 3:	g.drawImage(avatar_han, 640, 50);
						g.setColor(Color.blue);
						g.drawString("Encrypted Transmission", 60, 50);
						g.setColor(Color.white);
						g.drawString("LUKE! This is Han! Leia is in the Death Star!!!", 60, 80);
						break;
				case 4:	g.drawImage(avatar_luke, 60, 50);
						g.setColor(Color.blue);
						g.drawString("Luke Skywalker", 150, 50);
						g.setColor(Color.white);
						g.drawString("WHAT!?! Okay, I'm going in to save her!", 150, 80);
						break;
				case 5: g.drawImage(avatar_r2d2, 640, 50);
						g.setColor(Color.blue);
						g.drawString("R2-D2", 60, 50);
						g.setColor(Color.white);
						g.drawString("BLEEP BLEEP-- INCOMING TRANSMISSION.", 60, 80);
						break;
				case 6:	g.drawImage(avatar_dstar, 640, 50);
						g.setColor(Color.blue);
						g.drawString("Death Star Emergency Transmission", 60, 50);
						g.setColor(Color.white);
						g.drawString("This is Princess Leia! I'm with several rebel forces and we ", 60, 80);
						g.drawString("acquired a TIE fighter. We are escaping from the western side! ", 60, 100);
						break;
				case 7:	g.drawImage(avatar_luke, 60, 50);
						g.setColor(Color.blue);
						g.drawString("Luke Skywalker", 150, 50);
						g.setColor(Color.white);
						g.drawString("Leia! This is Luke! Get out now!", 150, 80);
						break;
				case 8:	g.drawImage(avatar_dstar, 640, 50);
						g.setColor(Color.blue);
						g.drawString("Death Star Emergency Transmission", 60, 50);
						g.setColor(Color.white);
						g.drawString("Luke! We are heading out!", 60, 80);
						break;
				case 9:	escape=false;
						break;
				case 10:g.drawImage(avatar_luke, 60, 50);
						g.setColor(Color.blue);
						g.drawString("Luke Skywalker", 150, 50);
						g.setColor(Color.white);
						g.drawString("Time to destroy the Death Star!", 150, 80);
						break; 
				case 11:xwing.setAnimation(ATTACK);
						attacking = true;
						timeStarted = System.currentTimeMillis();
						attack();
						pickCounter++;
						break;
				case 12: g.drawImage(avatar_luke, 60, 50);
						g.setColor(Color.blue);
						g.drawString("Luke Skywalker", 150, 50);
						g.setColor(Color.white);
						g.drawString("Now it ends.", 150, 80);
						break;
				case 13: g.setColor(Color.white);
						 g.drawString("Congratulations! You have saved Princess Leia and destroyed the Death Star!", 55, 45);
  						 g.drawString("Press S to continue.", 55, 65);
  						break;
				case 14: s.enterState(State.MENU, new FadeOutTransition(), new FadeInTransition());
						 s.getState(State.FIRST_LEVEL).init(gc, s);
						 s.getState(State.SECOND_LEVEL).init(gc, s);
						 s.getState(State.THIRD_LEVEL).init(gc, s);
						 break;
				}
			}
			dstar.getAnimation().draw(dstar.getX(),dstar.getY());
		}	
		if(paused){
			g.setColor(Color.white);
			g.drawString("PAUSED", 370, 200);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		
		if(endlevel){
			if(pickCounter==9){
				if(leia.getX()>xwing.getX()-100){
					leia.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				}else{
					escape=true;
					pickCounter++;
				}
			}
		}
		if(!fighters.isEmpty()&& !predialog){
			if(xwing.getX()<MAX_X){
				xwing.addToX(delta*H_DISPLACEMENT_FORWARD);
				if(xwing.getX()>MAX_X){
					xwing.setX(MAX_X);
				}
			}
			else{
				bgX -= delta * 0.2f;
				dstar.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				leia.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				for(ImperialTIEFighter fighter: fighters){
					fighter.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				}
			}
		}
		if(gc.getInput().isKeyPressed(Input.KEY_S)&&(preboss||predialog||(endlevel&&escape))){
			pickCounter++;
		}
		
		if(gc.getInput().isKeyDown(Input.KEY_RIGHT)&& !predialog && !preboss && !endlevel){
			xwing.setAnimation(WALK);
			if(xwing.getX()<MAX_X){
				xwing.addToX(delta*H_DISPLACEMENT_FORWARD);
				if(xwing.getX()>MAX_X){
					xwing.setX(MAX_X);
				}
			}
			else{
				bgX -= delta * 0.2f;
				dstar.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				leia.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				for(ImperialTIEFighter fighter: fighters){
					fighter.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				}
			}
		}
		else if(gc.getInput().isKeyDown(Input.KEY_LEFT)&& !predialog && !preboss && !endlevel){
			xwing.setAnimation(WALK);
			if(xwing.getX()<=MAX_X){
				xwing.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
				if(xwing.getX()<MIN_X){
					xwing.setX(MIN_X);
					xwing.setAnimation(STILL);
				}
			}
		}
		if(gc.getInput().isKeyPressed(Input.KEY_SPACE)&& !predialog && !preboss && !endlevel){
			paused = !paused;
			gc.setPaused(paused);
		}
		if(paused){
			return;
		}
		else if(gc.getInput().isKeyDown(Input.KEY_A) && !attacking && !predialog && !preboss && !endlevel && !nofire){
				xwing.setAnimation(ATTACK);
				attacking = true;
				timeStarted = System.currentTimeMillis();
				attack();	
		}
		else if(attacking){
			long timeCurr = System.currentTimeMillis();
			if(timeCurr-timeStarted>ATTACK_DURATION){
				attacking = false;
				xwing.setAnimation(WALK);
			}
		}
		else if(gc.getInput().isKeyDown(Input.KEY_DOWN) && !isXWingColliding() && !predialog && !preboss && !endlevel){
			xwing.setAnimation(WALK);
			if(xwing.getY()<MAX_Y){
				xwing.addToY(delta*H_DISPLACEMENT_DOWNWARD);
				if(xwing.getY()>MAX_Y){
					xwing.setY(MAX_Y);
				}
			}
		}
		else if(gc.getInput().isKeyDown(Input.KEY_UP) && !isXWingColliding() && !predialog && !preboss && !endlevel){
			xwing.setAnimation(WALK);
			if(xwing.getY()<=MAX_Y){
				xwing.addToY(-1*delta*H_DISPLACEMENT_UPWARD);
				if(xwing.getY()<MIN_Y){
					xwing.setY(MIN_Y);
				}
			}
		}
		if(isXWingColliding()){
			xwing.decreaseHealth(xwing.getCurrHealth());
		}
		for(ImperialTIEFighter fighter:fighters){
			if(!fighter.isDead()){
				if(fighter.getY()<=MIN_Y){
					fighter.setGoingDown(true);
				}else if(fighter.getY()>=MAX_Y){
					fighter.setGoingDown(false);
				}
				if(fighter.isGoingDown()){
					fighter.addToY(delta*H_DISPLACEMENT_DOWNWARD);
				}
				else{
					fighter.addToY(-1*delta*H_DISPLACEMENT_UPWARD);
				}
				fighter.getAnimation().update(delta);				
			}
		}
	
		for(Laser laser: lasers){
			laser.updateXPosition(delta);
		}
		for(Laser laser: laserXwing){
			laser.updateXPosition(delta);
		}
		checkIfAttacked();
		
		dstar.getAnimation().update(delta);
		xwing.getAnimation().update(delta);
		
		for(ImperialTIEFighter fighter:fighters){
			long currTime = System.currentTimeMillis();
			if(currTime - timeLastBulletFired > LASER_INTERVAL){
				if(fighter.getX()<=Window.WIDTH && fighter.getX()>= 0 && !fighter.isDead() 
						&& !fighter.isDying()){
					lasers.add(new Laser(fighter.getX()+10, fighter.getY()+50));
					timeLastBulletFired = System.currentTimeMillis();
				}
			}
		}
		
		if(postboss&&!endlevel){
			long timeDiff = System.currentTimeMillis() - dstar.getTimeLastAttack();
			if(timeDiff >= DeathStar.ATTACK_INTERVAL){
				dstar.setX(dstar.getX()-824);
				dstar.attack(xwing);
			}
			else if(timeDiff >= 1600){
				dstar.setAnimation(TOATTACK);
			}else if(timeDiff >= 600){
				if(dstar.isAttacking()){
					dstar.setX(dstar.getX()+824);
					dstar.endAttack();
				}
				dstar.setAnimation(STILL);
			}
		}
		
		if(dstar.getCurrHealth()<dstar.MAX_HEALTH*0.1&&!endlevel){
			dstar.setAnimation(STILL);
			endlevel = true; 
		}
		if(xwing.isDead()){
			s.enterState(State.GAMEOVER);
		}
		if(fighters.isEmpty()&&!postboss){
			nofire = true;
		}
		if(fighters.isEmpty()&&!postboss&&(dstar.getX()+dstar.getAnimation().getWidth())<=Window.WIDTH){
			preboss = true;
		}
	}
	
	private void initializeCharacters() throws SlickException{
		xwing = new XWingStarfighter();
		xwing.setY(LUKE_MIN_Y);
			
		dstar = new DeathStar();
		dstar.setY(LUKE_MIN_Y - 8);
		ImperialTIEFighter laStormTrooper = fighters.get(fighters.size()-1);
		dstar.setX(laStormTrooper.getX() + Window.WIDTH);
		leia = new ImperialTIEFighter(dstar.getX()-25+dstar.getAnimation().getWidth()/2,dstar.getY()-25+dstar.getAnimation().getHeight()/2);
	}
	
	private void loadImages() throws SlickException{
		background = new Image(CustomFileUtil.getFilePath("/background/starfield.png"));
		avatar_luke = new Image(CustomFileUtil.getFilePath("/avatars/avatar_luke.png"));
		avatar_r2d2 = new Image(CustomFileUtil.getFilePath("/avatars/avatar_r2d2.png"));
		avatar_han = new Image(CustomFileUtil.getFilePath("/avatars/voice_only.png"));
		avatar_dstar = new Image(CustomFileUtil.getFilePath("/avatars/static.png"));
	}
	
	private void loadEnemyUnits() throws SlickException{
		float x = 0, y = 0; 
		for(int i=0;i<FIGHTER_COUNT;i++){
			x+=800+Math.random()*1000;
			y+=100+Math.random();
			if(y > MAX_Y || y < MIN_Y){
				y+=100+Math.random();
			}
			fighters.add(new ImperialTIEFighter(x,y));
		} 
	}
		
	private void removeDeadEnemyUnits(){
		for(int i=0;i<fighters.size();i++){
			ImperialTIEFighter fighter = fighters.get(i);
			float fighterRight = fighter.getX() + fighter.getAnimation().getWidth();
			if(fighter.isDead() || fighterRight<= 0 ){
				fighters.remove(i);
				i--;
			}
			if (fighters.size() == 1){
				lastFighterX = fighter.getX() - 20;
			}
		}
	}
	
	private boolean isXWingColliding(){		
		for(ImperialTIEFighter fighter:fighters){
			float xwingRight = ((float)xwing.getAnimation().getWidth()) + xwing.getX();
			float xwingDown = ((float)xwing.getAnimation().getHeight()/2) + xwing.getY();
			float xwingUp = ((float)xwing.getAnimation().getHeight()/2) - xwing.getY();
			
			float fighterLeft = ((float)fighter.getAnimation().getWidth()) + xwing.getX();
			float fighterDown = ((float)fighter.getAnimation().getHeight()/2) + xwing.getY();
			float fighterUp = ((float)fighter.getAnimation().getHeight()/2) - xwing.getY();
			
			if(!fighter.isDead() && (fighter.getX() <= xwingRight && fighter.getX() >= xwing.getX())
					&& (fighter.getY() <= xwingUp || fighter.getY() <= xwingDown) 
					&& fighter.getY() >= xwing.getY()){
				return true;
			}
		}
		return false;
	}
	
	private void checkIfAttacked(){
		for(Laser laser:lasers){
			float xwingRight = ((float)xwing.getAnimation().getWidth()) + xwing.getX();
			float xwingDown = ((float)xwing.getAnimation().getHeight()/2) + xwing.getY();
			float xwingUp = ((float)xwing.getAnimation().getHeight()/2) - xwing.getY();
			if(laser.getX() <= xwingRight && laser.getX() >= xwing.getX() 
					&& (laser.getY() <= xwingUp || laser.getY() <= xwingDown) 
					&& laser.getY() >= xwing.getY()){
				xwing.decreaseHealth(Laser.DAMAGE);
				lasers.remove(laser);
				return;
			}
		}
		
		int laserXwingCount = 0;
		int fighterCount = 0;
		
		while(laserXwing.size()>laserXwingCount){
			if(fighters.size()>fighterCount){
				while(fighters.size()>fighterCount){
					float fighterLeft = ((float)fighters.get(fighterCount).getX() );
					float fighterDown = ((float)fighters.get(fighterCount).getAnimation().getHeight()/2) + 
							fighters.get(fighterCount).getY();
					float fighterUp = ((float)fighters.get(fighterCount).getY());
					System.out.println(fighterCount + " " + laserXwing.get(laserXwingCount).getX() + " " + laserXwing.get(laserXwingCount).getY() + " " + fighterLeft + " " + fighterDown + " " + fighterUp);
					if(!fighters.get(fighterCount).isDead() 
							&& laserXwing.get(laserXwingCount).getX() >= fighterLeft 
							&& (laserXwing.get(laserXwingCount).getY() >= fighterUp 
							&& laserXwing.get(laserXwingCount).getY() <= fighterDown)
							){
						fighters.get(fighterCount).setDead(true);
						fighters.get(fighterCount).setGoingDown(true);
						laserXwing.remove(laserXwing.get(laserXwingCount));
						fighterCount++;
						break;
					}else if(laserXwing.get(laserXwingCount).getX()>=Window.WIDTH){
						laserXwing.remove(laserXwing.get(laserXwingCount));
					}
					laserXwingCount++;
					
					if(!fighters.get(fighterCount).isDead() 
							//&& xwing.getX() > fighters.get(fighterCount).getX()
							){
						fighterCount++;
					}
					return;
				}
			}else if(fighters.size()==fighterCount){
				float dstarLeft = ((float) dstar.getX() );
				float dstarUp = ((float) dstar.getY());
				float dstarDown = ((float) dstar.getY() + dstar.getAnimation().getHeight());
				if(dstarLeft-laserXwing.get(laserXwingCount).getX()<=MIN_DIST_FROM_DISTANCE_X && 
						(laserXwing.get(laserXwingCount).getY() >= dstarUp 
						&& laserXwing.get(laserXwingCount).getY() <= dstarDown)){
					if(endlevel){
						dstar.decreaseHealth(100);
						laserXwing.remove(laserXwing.get(laserXwingCount));
						dstar.setAnimation(DEAD);
					}else{
						dstar.decreaseHealth(xwing.getDamage());
						laserXwing.remove(laserXwing.get(laserXwingCount));
					}
				}else if(laserXwing.get(laserXwingCount).getX()>=Window.WIDTH){
					laserXwing.remove(laserXwing.get(laserXwingCount));
				}
			}
			laserXwingCount++;
		}
	}
	
	private void attack(){
		try {
			//laserXwing.add(new Laser(xwing.getX()+80, xwing.getY()+1, 1));
			laserXwing.add(new Laser(xwing.getX()+80, xwing.getY()-1, 1));
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Override
	public int getID() {
		return State.THIRD_LEVEL;
	}

}

