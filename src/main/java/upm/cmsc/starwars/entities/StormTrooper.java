package upm.cmsc.starwars.entities;

import static org.apache.commons.io.FilenameUtils.removeExtension;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class StormTrooper {
	
	private static Map<String,Image> images;
	private float x = 0;
	private float y = 0;
	private boolean dead;
	private long timeOfDeath;
	
	public StormTrooper() throws SlickException{
		loadSprites();
	}
	
	public StormTrooper(float x, float y) throws SlickException{
		this.x = x; 
		this.y = y;
		loadSprites();
	}

	private void loadSprites() throws SlickException{
		String folderName = "";
		try{
			try {
				folderName = this.getClass().getResource("sprites/trooper").toURI().getPath();
			} catch(NullPointerException e){
				folderName = this.getClass().getResource("/sprites/trooper").toURI().getPath();
			}
			List<File> rawFiles = Files.walk(Paths.get(folderName))
					.filter(Files::isRegularFile)
					.map(Path::toFile)
					.collect(Collectors.toList());

			Map<String, Image> images = new HashMap<String, Image>();
			for(File file:rawFiles){
				images.put(removeExtension(file.getName()), new Image(file.getAbsolutePath()));
			}
			StormTrooper.images = images;
		}  catch (IOException e) {
			// TODO do something here
		} catch (URISyntaxException e) {
			// TODO do something here
		}
	}
	
	public static Animation getAttackAnimation(){
		Image[] imgSequence = {images.get("shoot1"),images.get("shoot2")};
		int[] duration = {800,200};
		return new Animation(imgSequence,duration,false);
	}
	public static Animation getDeadAnimation(){
		Image[] imgSequence = {images.get("dead1"),images.get("dead2")};
		int[] duration = {300,1000};
		return new Animation(imgSequence,duration,false);
	}
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public boolean isDead() {
		long currTime = System.currentTimeMillis();
		return dead && (currTime-timeOfDeath>1200);
	}

	public void setDead(boolean dead) {
		timeOfDeath = System.currentTimeMillis();
		this.dead = dead;
	}
	public void addToX(float num){
		this.x+=num;
	}

	
}
