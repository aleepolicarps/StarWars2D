package upm.cmsc.starwars.states;

import static upm.cmsc.starwars.entities.Action.ATTACK;
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
import upm.cmsc.starwars.entities.GeneralGrievous;
import upm.cmsc.starwars.entities.ImperialTIEFighter;
import upm.cmsc.starwars.entities.Laser;
import upm.cmsc.starwars.entities.XWingStarfighter;

public class ThirdLevelState extends BasicGameState{
	
	private final float LUKE_MIN_Y = 335;
	private final float MAX_X = 250;
	private final float MAX_Y = 550;
	private final float MIN_Y = 100;
	private final int TROOPER_COUNT = 5;
	private final long LASER_INTERVAL = 500;
	
	private Image background;
	private boolean attacking,jumping,paused;
	private long timeStarted;
	private long timeLastBulletFired;
	private float bgX = 0;
	
	private XWingStarfighter xwing;
	private GeneralGrievous general;
	private List<ImperialTIEFighter> fighters = new ArrayList<ImperialTIEFighter>();
	private List<Laser> lasers = new ArrayList<Laser>();
	private List<Laser> laserXwing= new ArrayList<Laser>();
	
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
		g.setColor(Color.white);
		g.drawString("Luke Skywalker", 30, 10);
		g.setColor(Color.white);
		g.fillRect(40, 30, XWingStarfighter.MAX_HEALTH, 20);
		g.setColor(xwing.getCurrentHealthColor());
		g.fillRect(40, 30, xwing.getCurrHealth(), 20);
		
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
		
		if(fighters.isEmpty()){
			general.getAnimation().draw(general.getX(),general.getY());
			g.setColor(Color.black);
			g.drawString("Death Star", 620, 10);
			g.setColor(Color.black);
			g.fillRect(560, 30, GeneralGrievous.MAX_HEALTH, 20);
			g.setColor(general.getCurrentHealthColor());
			g.fillRect(560, 30, general.getCurrHealth(), 20);
		}
		
		if(paused){
			g.setColor(Color.white);
			g.drawString("PAUSED", 370, 200);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		
		if(xwing.getX()<MAX_X){
			xwing.addToX(delta*H_DISPLACEMENT_FORWARD);
			if(xwing.getX()>MAX_X){
				xwing.setX(MAX_X);
			}
		}
		else{
			bgX -= delta * 0.2f;
			general.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
			for(ImperialTIEFighter fighter: fighters){
				fighter.addToX(-1*delta*H_DISPLACEMENT_FORWARD);
			}
		}
		
		if(gc.getInput().isKeyPressed(Input.KEY_SPACE)){
			paused = !paused;
			gc.setPaused(paused);
		}
		
		if(paused){
			return;
		}
		
		else if(gc.getInput().isKeyDown(Input.KEY_A) && !attacking){
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
		else if(gc.getInput().isKeyDown(Input.KEY_DOWN) && !isXWingColliding()){
			xwing.setAnimation(WALK);
			if(xwing.getY()<MAX_Y){
				xwing.addToY(delta*H_DISPLACEMENT_DOWNWARD);
				if(xwing.getY()>MAX_Y){
					xwing.setY(MAX_Y);
				}
			}
		}
		else if(gc.getInput().isKeyDown(Input.KEY_UP) && !isXWingColliding()){
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
		
		general.getAnimation().update(delta);
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
		if(general.isDead()){
			s.enterState(State.THIRD_LEVEL); // TODO change this according to which level is next
		}
		if(xwing.isDead()){
			s.enterState(State.GAMEOVER);
		}
	}
	
	private void initializeCharacters() throws SlickException{
		xwing = new XWingStarfighter();
		xwing.setY(LUKE_MIN_Y);
		
		general = new GeneralGrievous();
		general.setY(LUKE_MIN_Y - 8);
		ImperialTIEFighter laStormTrooper = fighters.get(fighters.size()-1);
		general.setX(laStormTrooper.getX() + Window.WIDTH);		
	}
	
	private void loadImages() throws SlickException{
		background = new Image(CustomFileUtil.getFilePath("/background/starfield.png"));
	}
	
	private void loadEnemyUnits() throws SlickException{
		float x = 0, y = 0; 
		for(int i=0;i<TROOPER_COUNT;i++){
			x+=500+Math.random()*1000;
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
			if(fighter.isDead() && fighterRight<= 0 ){
				fighters.remove(i);
				i--;
			}
		}
	}
	
	private boolean isXWingColliding(){
		/*float xwingRight = (float) xwing.getX() + (xwing.getAnimation().getWidth()/2);
		float xwingDown = (float) xwing.getY() + (xwing.getAnimation().getHeight()/2);
		float xwingUp = (float) xwing.getY() - (xwing.getAnimation().getHeight()/2);
	
		for(ImperialTIEFighter ft:fighters){
			float tieLeft = (float) ft.getX() - (ft.getAnimation().getWidth()/2);
			float tieDown = (float) ft.getY() + (ft.getAnimation().getHeight()/2);
			float tieUp = (float) ft.getY() - (ft.getAnimation().getHeight()/2);
			//System.out.println(tieLeft + " - " + xwingRight + " " + xwing.getX() + " " + (float)xwing.getAnimation().getWidth());
			//System.out.println(tieLeft + " - " + xwingRight + " " + xwing.getY() + " " + (float)xwing.getAnimation().getHeight()/2);
			if(!ft.isDead() && tieLeft - xwingRight  <= MIN_DIST_FROM_DISTANCE_X 
					&& ((tieDown - xwingUp <= MIN_DIST_FROM_DISTANCE_Y) ||
						(tieUp - xwingDown <= MIN_DIST_FROM_DISTANCE_Y))){
				System.out.println("Colliding");
				System.out.println(tieLeft + " - " + xwingRight);
				System.out.println(tieDown + " - " + xwingUp);
				System.out.println(tieUp + " - " + xwingDown);
				//ft.setDead(true);
				return true;
			}
			if(tieLeft - xwingRight <= MIN_DIST_FROM_DISTANCE_X){
				ft.setDead(true);
			a}
		}(/
		/*if(general.getX()-xwing.getX()<=MIN_DIST_FROM_DISTANCE){
			return true;
		}*/
		return false;
	}
	
	private void checkIfAttacked(){
		for(Laser laser:lasers){
			float xwingRight = ((float)xwing.getAnimation().getWidth()) + xwing.getX();
			float xwingDown = ((float)xwing.getAnimation().getHeight()/2) + xwing.getY();
			float xwingUp = ((float)xwing.getAnimation().getHeight()/2) - xwing.getY();
			if(laser.getX() <= xwingRight && laser.getX()>=xwing.getX() 
					&& (laser.getY() <= xwingUp || laser.getY() <= xwingDown) && laser.getY()>=xwing.getY()
					&& !jumping){
				xwing.decreaseHealth(Laser.DAMAGE);
				lasers.remove(laser);
				return;
			}
		}
		long timeDiff = System.currentTimeMillis() - general.getTimeLastAttack();
		if(general.getX()-xwing.getX()<=MIN_DIST_FROM_DISTANCE_X){
			if(timeDiff >= GeneralGrievous.ATTACK_INTERVAL){
				//general.attack(xwing);
			}
			else if(timeDiff >= 600){
				general.setAnimation(STILL);
			}
		}
		else if(timeDiff >= 600){
			general.setAnimation(Action.STILL);
		}
	}
	
	private void attack(){
		try {
			laserXwing.add(new Laser(xwing.getX()+30, xwing.getY(), 1));
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		if(general.getX()-xwing.getX()<=MIN_DIST_FROM_DISTANCE_X){
			general.decreaseHealth(xwing.getDamage());
		}
		
		for(Laser laser:laserXwing){
			/*float xwingRight = ((float)xwing.getAnimation().getWidth()) + xwing.getX();
			float xwingDown = ((float)xwing.getAnimation().getHeight()/2) + xwing.getY();
			float xwingUp = ((float)xwing.getAnimation().getHeight()/2) - xwing.getY();
			if(laser.getX() <= xwingRight && laser.getX()>=xwing.getX() 
					&& (laser.getY() <= xwingUp || laser.getY() <= xwingDown) && laser.getY()>=xwing.getY()
					&& !jumping){
				xwing.decreaseHealth(Laser.DAMAGE);
				lasers.remove(laser);
				return;
			}*/
			for(ImperialTIEFighter fighter: fighters){
				System.out.println(laser.getY() + " = "+ fighter.getY());
				if(!fighter.isDead() && (laser.getY() <= fighter.getY()+5 && laser.getY() >= fighter.getY()-5)){
					fighter.setDead(true);
					System.out.println("Dead");
				}
				return;
			}
		}
	}

	@Override
	public int getID() {
		return State.THIRD_LEVEL;
	}

}

