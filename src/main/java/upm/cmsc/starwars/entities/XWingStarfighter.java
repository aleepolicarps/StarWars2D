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
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import upm.cmsc.starwars.CustomFileUtil;

public class XWingStarfighter {
	public static final long ATTACK_DURATION = 300;
	public static final long JUMP_DURATION = 250;
	public static final int MAX_HEALTH = 300;
	public static final float H_DISPLACEMENT_FORWARD = 0.1f;
	public static final float H_DISPLACEMENT_UPWARD = 0.3f;
	public static final float H_DISPLACEMENT_DOWNWARD = 0.3f;
	public static final float INITIAL_VELOCITY = 5;
	public static final float INIT_H_VELOCITY = 50f;
	public static final int MIN_DIST_FROM_DISTANCE_X= 5;
	public static final int MIN_DIST_FROM_DISTANCE_Y= 5;
	
	private int damage = 10;	
	private static Map<String,Image> images;
	private float x = 0;
	private float y = 0;
	private int currHealth = MAX_HEALTH;
	private Animation animation;
	private Animation walk, attack, dead;
	
	public XWingStarfighter() throws SlickException{
		loadSprites();
		loadAnimations();
		animation = walk;
	}

	private void loadSprites() throws SlickException{
		try{
			String folderName = CustomFileUtil.getFilePath("/sprites/xwingstarfighter");
			List<File> rawFiles = Files.walk(Paths.get(folderName))
					.filter(Files::isRegularFile)
					.map(Path::toFile)
					.collect(Collectors.toList());

			Map<String, Image> images = new HashMap<String, Image>();
			for(File file:rawFiles){
				images.put(removeExtension(file.getName()), new Image(file.getAbsolutePath()));
			}
			XWingStarfighter.images = images;
		}  catch (IOException e) {
			// TODO do something here
		} 
	}
	
	private void loadAnimations(){
		Image[] imgSequence1 = {images.get("boost1"),images.get("boost2")};
		int[] duration1 = {100,100};
		walk = new Animation(imgSequence1,duration1,false);
		
		Image[] imgSequence2 = {images.get("attack")};
		int[] duration2 = {200};
		attack = new Animation(imgSequence2,duration2,false);
		
		Image[] imgSequence3 = {images.get("dead")};
		int[] duration3 = {200};
		dead = new Animation(imgSequence3,duration3,false);
		
	}
	
	
	public Animation getAnimation(){
		return animation;
	}
	public void setAnimation(Action action){
		switch (action) {
		case WALK:
			animation = walk;
			break;
		case ATTACK:
			animation = attack;
			break;
		case DEAD:
			animation = dead;
			break;
		case TOATTACK:
			animation = walk;
			break;
		default:
			animation = walk;
			break;
		}
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
	
	public void addToX(float num){
		x+=num;
	}
	public void addToY(float num){
		y+=num;
	}

	public int getCurrHealth() {
		return currHealth;
	}

	public void decreaseHealth(int damage) {
		currHealth -= damage;
	}
	
	public boolean isDead(){
		return currHealth <= 0;
	}
	public Color getCurrentHealthColor(){
		if(currHealth<XWingStarfighter.MAX_HEALTH * 0.70){
			return Color.orange;
		}
		else if(currHealth<XWingStarfighter.MAX_HEALTH * 0.25){
			return Color.red;
		}
		return Color.green;
			
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}
