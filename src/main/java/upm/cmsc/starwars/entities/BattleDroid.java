package upm.cmsc.starwars.entities;

import static org.apache.commons.io.FilenameUtils.removeExtension;

import java.io.File;
import java.io.IOException;
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

import upm.cmsc.starwars.CustomFileUtil;
public class BattleDroid {
	private int ctr;
	private Map<String,Image> images;
	
	private int id = 0;
	private float x = 0;
	private float y = 0;
	private boolean dead;
	private long timeOfDeath;
	private Animation animation, attack, deadAnimation;
	
	public BattleDroid() throws SlickException{
		id = ctr;
		ctr++;
		loadSprites();
		loadAnimation();
		this.animation = attack;
	}
	
	public BattleDroid(float x, float y) throws SlickException{
		id = ctr;
		ctr++;
		this.x = x; 
		this.y = y;
		loadSprites();
		loadAnimation();
		this.animation = attack;
	}
	public Animation getAnimation(){
		return this.animation;
	}
	private void loadSprites() throws SlickException{
		try{
			String folderName = CustomFileUtil.getFilePath("/sprites/battledroid");
			List<File> rawFiles = Files.walk(Paths.get(folderName))
					.filter(Files::isRegularFile)
					.map(Path::toFile)
					.collect(Collectors.toList());
	
			Map<String, Image> images = new HashMap<String, Image>();
			for(File file:rawFiles){
				images.put(removeExtension(file.getName()), new Image(file.getAbsolutePath()));
			}
			this.images = images;
		}  catch (IOException e) {
			// TODO do something here
		}
	}
	
	private void loadAnimation(){
		Image[] imgSequence1 = {images.get("attack1"),images.get("attack2")};
		int[] duration1 = {2300,700};
		attack = new Animation(imgSequence1,duration1,false);
		
		Image[] imgSequence2 = {images.get("fall"),images.get("lose")};
		int[] duration2 = {300,1000};
		deadAnimation = new Animation(imgSequence2,duration2,false);
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
		return dead && (currTime-timeOfDeath>1000);
	}
	
	public boolean isDying(){
		long currTime = System.currentTimeMillis();
		return currTime-timeOfDeath<=1000;
	}

	public void setDead(boolean dead) {
		timeOfDeath = System.currentTimeMillis();
		this.dead = dead;
		this.animation = deadAnimation;
	}
	public void addToX(float num){
		this.x+=num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BattleDroid other = (BattleDroid) obj;
		if (id != other.id)
			return false;
		return true;
	}	
}
