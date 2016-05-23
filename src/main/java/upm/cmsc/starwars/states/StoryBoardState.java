package upm.cmsc.starwars.states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class StoryBoardState extends BasicGameState{
	Image bg;
	Image storyboard;
	Image back;
	String text = "Princess Leia was captured by Darth Vader!\n\n" +
					"Our hero, Luke Skywalker, and his friends must now come and rescue her.\n" +
					"Unfortunately, Darth Vader's whereabouts are unknown. Luke must now defeat\n" +
					"different kinds of enemies to gain information about Darth Vader and rescue.\n" +
					"Princess Leia.\n" +
					"\nWill he succeed? Or will it be too late?";

	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		bg = new Image("menu/bg.png");
		storyboard = new Image("menu/storyboard.png");
		back = new Image("menu/back.png");
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		
		g.drawImage(bg, 0, 0);
		storyboard.draw(240,50);
		g.drawString(text, 80, 150);
		back.draw(320,500);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		
		int posX = Mouse.getX();
		int posY = Mouse.getY();
		
		if((posX>320 && posX<450)&& (posY>70 && posY<95)){
			if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)){
				s.enterState(State.MENU);
			}
		}
				
	}

	@Override
	public int getID() {
		return State.STORYBOARD;
	}

}
