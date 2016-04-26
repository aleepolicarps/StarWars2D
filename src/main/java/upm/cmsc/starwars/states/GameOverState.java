package upm.cmsc.starwars.states;

import org.lwjgl.input.Mouse;
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


// TODO TEST STATE (needs refactoring)
public class GameOverState extends BasicGameState{
	
	Image bg,gameover,restart,main_menu,exit;
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		bg = new Image(CustomFileUtil.getFilePath("/menu/bg.png"));
		gameover = new Image(CustomFileUtil.getFilePath("/menu/gameover.png"));
		restart = new Image(CustomFileUtil.getFilePath("/menu/restart.png"));
		main_menu = new Image(CustomFileUtil.getFilePath("/menu/main_menu.png"));
		exit = new Image(CustomFileUtil.getFilePath("/menu/exit.png"));
	
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
			s.enterState(State.FIRST_LEVEL);
		}
		
		//TO DO: initialize all values from the start
		//restart
		if((posX>310 && posX<470)&& (posY>275 && posY<300)){
			if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)){
				switch (MenuState.getCurrentGameLevel()){
				case 1:	s.getState(State.FIRST_LEVEL).init(gc, s);
						s.enterState(State.FIRST_LEVEL, new FadeOutTransition(), new FadeInTransition());
						break;
				case 2:	s.getState(State.SECOND_LEVEL).init(gc, s);
						s.enterState(State.SECOND_LEVEL, new FadeOutTransition(), new FadeInTransition());
						break;
				case 3:	s.getState(State.THIRD_LEVEL).init(gc, s);
						s.enterState(State.THIRD_LEVEL, new FadeOutTransition(), new FadeInTransition());
						break;
				}
			}
		}
		
		//main menu
		if((posX>290 && posX<470)&& (posY>225 && posY<250)){
			if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)){
				s.enterState(State.MENU);
			}
		}
		
		//exit
		if((posX>340 && posX<435)&& (posY>175 && posY<200)){
			if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)){
				System.exit(0);
			}
		}
	}

	@Override
	public int getID() {
		return State.GAMEOVER;
	}

}
