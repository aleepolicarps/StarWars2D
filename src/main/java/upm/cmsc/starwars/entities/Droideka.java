package upm.cmsc.starwars.entities;

import static org.apache.commons.io.FilenameUtils.removeExtension;
import static upm.cmsc.starwars.entities.LukeSkywalker.MIN_DIST_FROM_DISTANCE;

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

public class Droideka {
	
	public static final int MAX_HEALTH = 200;
	public static final int DAMAGE = 15;
	public static final int ATTACK_INTERVAL = 2000;
		
	private static Map<String,Image> images;
	private float x = 0;
	private float y = 0;
	private int currHealth = MAX_HEALTH;
	private Animation animation;
	private Animation still, roll, attack, dead, toattack, toshield, assemble, shield;
	private long timeLastAttack;
	private boolean attacking;
	private boolean shielding;
	private long toAssemble;
		
	public Droideka() throws SlickException{
		loadSprites();
		loadAnimations();
		animation = still;
	}

	private void loadSprites() throws SlickException{
		try{
			String folderName = CustomFileUtil.getFilePath("/sprites/droideka");
			List<File> rawFiles = Files.walk(Paths.get(folderName))
					.filter(Files::isRegularFile)
					.map(Path::toFile)
					.collect(Collectors.toList());
				Map<String, Image> images = new HashMap<String, Image>();
			for(File file:rawFiles){
				images.put(removeExtension(file.getName()), new Image(file.getAbsolutePath()));
			}
			Droideka.images = images;
		}  catch (IOException e) {
			// TODO do something here
		} 
	}
	
	private void loadAnimations(){
		Image[] imgSequence1 = {images.get("roll_1"),images.get("roll_2"),images.get("roll_3")};
		int[] duration1 = {100,100,100};
		roll = new Animation(imgSequence1,duration1,false);
		
		Image[] imgSequence2 = {images.get("assemble_3")};
		int[] duration2 = {200};
		still = new Animation(imgSequence2,duration2,false);
		
		Image[] imgSequence3 = {images.get("assemble_1"),images.get("assemble_2"),images.get("assemble_3")};
		int[] duration3 = {100,100,100};
		assemble = new Animation(imgSequence3,duration3,false);
		
		Image[] imgSequence4 = {images.get("fire_1"),images.get("fire_2"),images.get("fire_3")};
		int[] duration4 = {100,100,200};
		toattack = new Animation(imgSequence4,duration4,false);
		
		Image[] imgSequence5 = {images.get("shield_1"),images.get("shield_2"),images.get("shield_3")};
		int[] duration5 = {100,100,200};
		toshield = new Animation(imgSequence5,duration5,false);
		
		Image[] imgSequence6 = {images.get("destroyed_0"),images.get("destroyed_1"),images.get("destroyed_2")};
		int[] duration6 = {100,100,100};
		dead = new Animation(imgSequence6,duration6,false);
		
		Image[] imgSequence7 = {images.get("fire_3")};
		int[] duration7 = {200};
		attack = new Animation(imgSequence7,duration7,false);
		
		Image[] imgSequence8 = {images.get("shield_3")};
		int[] duration8 = {200};
		shield = new Animation(imgSequence8,duration8,false);
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
			animation = roll;
			break;
		case ATTACK:
			animation = attack;
			break;
		case DEAD:
			animation = dead;
			break;
		case TOSHIELD:
			animation = toshield;
			break;
		case TOATTACK:
			animation = toattack;
			break;
		case ASSEMBLE:
			animation = assemble;
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
		if(currHealth<Droideka.MAX_HEALTH * 0.70){
			return Color.orange;
		}
		else if(currHealth<Droideka.MAX_HEALTH * 0.25){
			return Color.red;
		}
		return Color.green;

	}

	public long getTimeLastAttack() {
		return timeLastAttack;
	}

	public void attack(){
		timeLastAttack = System.currentTimeMillis();
		animation = attack;
		shielding = false;
		attacking = true;
	}
	
	public void shield(){
		timeLastAttack = System.currentTimeMillis();
		animation = shield;
		attacking = false;
		shielding = true;
	}
	
	public boolean isAttacking(){
		return attacking;
	}
	
	public boolean isShielding(){
		return shielding;
	}
	
	public void endAttack(){
		attacking = false;
	}
	
	public void setToAssemble(Long time){
		toAssemble = time;
	}
	
	public Long getToAssemble(){
		return toAssemble;
	}
}

