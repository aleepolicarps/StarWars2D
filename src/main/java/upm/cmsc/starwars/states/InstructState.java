package upm.cmsc.starwars.states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class InstructState extends BasicGameState{
	Image bg;
	Image instruct_title;
	Image back;
	String text = "The gameplay is just simple.\n" + 
					"\nUse the left and right arrow keys to move your player backward and forward.\n" +
					"Use the up arrow key to jump. Press 'A' to attack.\n" +
					"Press 'S' as indicated during conversations.\n" +
					"\nKill all enemies per level to reach the boss.\n" +
					"If the player dies, just use the mouse pointer to choose the desired option.\n";
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		bg = new Image("menu/bg.png");
		instruct_title = new Image("menu/instruct_title.png");
		back = new Image("menu/back.png");
	
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		
		g.drawImage(bg, 0, 0);
		instruct_title.draw(150,50);
		g.drawString(text, 80, 150);
		back.draw(320,500);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		
		int posX= Mouse.getX();
		int posY= Mouse.getY();
		
		if((posX>320 && posX<450)&& (posY>70 && posY<95)){
			if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)){
				s.enterState(State.MENU);
			}
		}

	}

	@Override
	public int getID() {
		return State.INSTRUCT;
	}

}
