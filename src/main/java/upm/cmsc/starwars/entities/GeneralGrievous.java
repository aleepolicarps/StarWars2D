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

public class GeneralGrievous {
	
	public static final int MAX_HEALTH = 200;
	public static final int DAMAGE = 10;
	public static final int ATTACK_INTERVAL = 2000;
	
	private static Map<String,Image> images;
	private float x = 0;
	private float y = 0;
	private int currHealth = MAX_HEALTH;
	private Animation animation;
	private Animation still, walk, attack, dead;
	private long timeLastAttack;
	private boolean attacking;
	
	
	public GeneralGrievous() throws SlickException{
		loadSprites();
		loadAnimations();
		animation = still;
	}

	private void loadSprites() throws SlickException{
		try{
			String folderName = CustomFileUtil.getFilePath("/sprites/generalgrievous");
			List<File> rawFiles = Files.walk(Paths.get(folderName))
					.filter(Files::isRegularFile)
					.map(Path::toFile)
					.collect(Collectors.toList());

			Map<String, Image> images = new HashMap<String, Image>();
			for(File file:rawFiles){
				images.put(removeExtension(file.getName()), new Image(file.getAbsolutePath()));
			}
			GeneralGrievous.images = images;
		}  catch (IOException e) {
			// TODO do something here
		} 
	}
	
	private void loadAnimations(){
		Image[] imgSequence1 = {images.get("walk1"),images.get("walk2")};
		int[] duration1 = {100,100};
		walk = new Animation(imgSequence1,duration1,false);
		
		Image[] imgSequence2 = {images.get("stand")};
		int[] duration2 = {200};
		still = new Animation(imgSequence2,duration2,false);
		
		Image[] imgSequence3 = {images.get("attack1"),images.get("attack2"),images.get("attack3"),
				images.get("attack4"),images.get("attack5"),images.get("attack6")};
		int[] duration3 = {50,50,50,50,50,50};
		attack = new Animation(imgSequence3,duration3,false);
		
		Image[] imgSequence5 = {images.get("dead")};
		int[] duration5 = {200};
		dead = new Animation(imgSequence5,duration5,false);
		
	}
	
	
	public Animation getAnimation(){
		return animation;
	}
	public void setAnimation(Action action){
		switch (action) {
		case STILL:
			animation = still;
			break;
		case WALK:
			animation = walk;
			break;
		case ATTACK:
			animation = attack;
			break;
		case DEAD:
			animation = dead;
			break;
		default:
			animation = still;
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
		if(currHealth<GeneralGrievous.MAX_HEALTH * 0.70){
			return Color.orange;
		}
		else if(currHealth<GeneralGrievous.MAX_HEALTH * 0.25){
			return Color.red;
		}
		return Color.green;
			
	}

	public long getTimeLastAttack() {
		return timeLastAttack;
	}

	public void attack(LukeSkywalker luke){
		timeLastAttack = System.currentTimeMillis();
		animation = attack;
		luke.decreaseHealth(DAMAGE);
		attacking = true;
	}
	public boolean isAttacking(){
		return attacking;
	}
	public void endAttack(){
		attacking = false;
	}


}