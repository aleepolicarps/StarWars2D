package upm.cmsc.starwars.states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


// TODO TEST STATE (needs refactoring)
public class GameOverState extends BasicGameState{
	
	Image bg;
	Image gameover;
	Image restart;
	Image main_menu;
	Image exit;
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		bg = new Image("menu/bg.png");
		gameover = new Image("menu/gameover.png");
		restart = new Image("menu/restart.png");
		main_menu = new Image("menu/main_menu.png");
		exit = new Image("menu/exit.png");
	
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		
		g.drawImage(bg, 0, 0);
		gameover.draw(250,100);
		restart.draw(310,300);
		main_menu.draw(290,350);
		exit.draw(340,400);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		
		int posX = Mouse.getX();
		int posY = Mouse.getY();
		
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
			s.enterState(6);
		}
		
		//TO DO: initialize all values from the start
		//restart
		if((posX>310 && posX<470)&& (posY>275 && posY<300)){
			if(Mouse.isButtonDown(0)){
				//s.enterState(6);
			}
		}
		
		//main menu
		if((posX>290 && posX<470)&& (posY>225 && posY<250)){
			if(Mouse.isButtonDown(0)){
				s.enterState(1);
			}
		}
		
		//exit
		if((posX>340 && posX<435)&& (posY>175 && posY<200)){
			if(Mouse.isButtonDown(0)){
				System.exit(0);
			}
		}
	}

	@Override
	public int getID() {
		return State.GAMEOVER;
	}

}
