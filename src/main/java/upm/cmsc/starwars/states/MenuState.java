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

public class MenuState extends BasicGameState{
	
	Image bg,title,saber,start,storyboard,instructions,
		credits,exit;
	
	private static int currentGameLevel = 0;
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		bg = new Image(CustomFileUtil.getFilePath("/menu/bg.png"));
		title = new Image(CustomFileUtil.getFilePath("/menu/title.png"));
		saber = new Image(CustomFileUtil.getFilePath("/menu/saber.png"));
		start = new Image(CustomFileUtil.getFilePath("/menu/start.png"));
		storyboard = new Image(CustomFileUtil.getFilePath("/menu/storyboard.png"));
		instructions = new Image(CustomFileUtil.getFilePath("/menu/instructions.png"));
		credits = new Image(CustomFileUtil.getFilePath("/menu/credits.png"));
		exit = new Image(CustomFileUtil.getFilePath("/menu/exit.png"));

	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		
		g.drawImage(bg, 0, 0);
		title.draw(240,20);
		saber.draw(250,170);
		start.draw(320,250);
		storyboard.draw(240,310);
		instructions.draw(240,370);
		credits.draw(300,420);
		exit.draw(330,480);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		
		int posX = Mouse.getX();
		int posY = Mouse.getY();
		
		//start
		if((posX>310 && posX<460)&& (posY>320 && posY<350)){
			if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)){
				MenuState.setCurrentGameLevel(1);
				s.enterState(State.FIRST_LEVEL, new FadeOutTransition(), new FadeInTransition());
			}
		}
				
		//storyboard
		if((posX>235 && posX<540)&& (posY>260 && posY<290)){
			if(Mouse.isButtonDown(0)){
				s.enterState(State.STORYBOARD);
			}
		}
		
		//instructions
		if((posX>240 && posX<550)&& (posY>200 && posY<230)){
			if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)){
				s.enterState(State.INSTRUCT);
			}
		}
		
		//credits
		if((posX>300 && posX<485)&& (posY>150 && posY<180)){
			if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)){
				s.enterState(State.CREDITS);
			}
		}
		
		//exit
		if((posX>330 && posX<430)&& (posY>90 && posY<120)){
			if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)){
				System.exit(0);
			}
		}
				
	}

	@Override
	public int getID() {
		return State.MENU;
	}

	public static int getCurrentGameLevel() {
		return currentGameLevel;
	}

	public static void setCurrentGameLevel(int currentGameLevel) {
		MenuState.currentGameLevel = currentGameLevel;
	}

}
