package upm.cmsc.starwars.states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class CreditsState extends BasicGameState{
	Image bg;
	Image credits_title;
	Image back;
	String text = "FONTS\n\n" +
					"* http://cooltext.com/Logo-Design-Skate?Font=129\n" +
					"* http://fontmeme.com/star-wars-font/";
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		bg = new Image("menu/bg.png");
		credits_title = new Image("menu/credits_title.png");
		back = new Image("menu/back.png");
	
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		
		g.drawImage(bg, 0, 0);
		credits_title.draw(240,50);
		g.drawString(text, 100, 150);
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
		return State.CREDITS;
	}

}
