package upm.cmsc.starwars;

public class Window {

	public static final String GAME_TITLE = "Star Wars";

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;	
	
	private static final boolean fullScreen = false;
//	private static final boolean fullScreen = true;
	
	public static boolean isFullScreen() {
		return fullScreen;
	}
	
}
