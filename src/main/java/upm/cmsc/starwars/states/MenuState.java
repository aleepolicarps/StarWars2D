package upm.cmsc.starwars.states;



import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;



public class MenuState extends BasicGameState{
	Image bg;
	Image title;
	Image saber;
	Image start;
	Image storyboard; 
	Image instructions;
	Image credits;
	Image exit;
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
		bg=new Image("menu/bg.png");
		title=new Image("menu/title.png");
		saber=new Image("menu/saber.png");
		start=new Image("menu/start.png");
		storyboard=new Image("menu/storyboard.png");
		instructions=new Image("menu/instructions.png");
		credits=new Image("menu/credits.png");
		exit=new Image("menu/exit.png");

	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		
		g.drawImage(bg, 0, 0);
		title.draw(230,20);
		saber.draw(240,170);
		start.draw(310,250);
		storyboard.draw(220,310);
		instructions.draw(220,370);
		credits.draw(290,420);
		exit.draw(320,480);
		
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		
		int posX= Mouse.getX();
		int posY= Mouse.getY();
	
		//To check coordinates
		//start
		if((posX>220 && posX<520)&& (posY>310 && posY<340)){
			if(Mouse.isButtonDown(0)){
				s.enterState(State.FIRST_LEVEL);
				}
		}
				
		//storyboard
		if((posX>310 && posX<453)&& (posY>250 && posY<280)){
			if(Mouse.isButtonDown(0)){
				s.enterState(State.GAMEOVER);
			}
		}
		
		
		//instructions
		if((posX>220 && posX<529)&& (posY>370 && posY<400)){
			if(Mouse.isButtonDown(0)){
				s.enterState(1);
			}
		}
		//credits
		if((posX>290 && posX<479)&& (posY>420 && posY<450)){
			if(Mouse.isButtonDown(0)){
				s.enterState(1);
			}
		}
		
		//exit
		if((posX>320 && posX<423)&& (posY>480 && posY<510)){
			if(Mouse.isButtonDown(0)){
				System.exit(0);
			}
		}
		
				
	}

	@Override
	public int getID() {
		return State.MENU;
	}

}
