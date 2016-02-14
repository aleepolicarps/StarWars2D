package upm.cmsc.starwars;

import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Engine extends BasicGame {
	
	public static boolean _APPLET;

	public Engine(String title) {
		super(title);
	}


	public void render(GameContainer gc, Graphics g) throws SlickException {

		
	}


	@Override
	public void init(GameContainer gc) throws SlickException {
		gc.setMaximumLogicUpdateInterval(60);		
		gc.setTargetFrameRate(60);
		gc.setAlwaysRender(true);
		gc.setShowFPS(false);
		gc.setVSync(true);

	}


	@Override
	public void update(GameContainer gc, int delta) throws SlickException {

	}



	public static void main(String[] args) {
		_APPLET = false;
//		File f = new File("target/natives");
//		if(f.exists()){
//			System.setProperty("org.lwjgl.librarypath",f.getAbsolutePath());
//		}
		try {
			AppGameContainer game = new AppGameContainer(new Engine(Window.GAME_TITLE));
			game.setDisplayMode(Window.WIDTH, Window.HEIGHT, Window.isFullScreen());
			game.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

}
