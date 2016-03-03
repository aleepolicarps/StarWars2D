package upm.cmsc.starwars.entities;

public class Contstants {
	public static final long ATTACK_DURATION = 170;
	public static final long JUMP_DURATION = 300;
	public static final float H_DISPLACEMENT_FORWARD = 0.2f;
	public static final float H_DISPLACEMENT_BACKWARD = 0.1f;
	public static final float INITIAL_VELOCITY = 5;
	public static final float INIT_H_VELOCITY = 50f;
	
	public static boolean isWindows(){
		String operatingSystem = System.getProperty("os.name").toLowerCase();
		return operatingSystem.equals("windows");
	}
	
}
