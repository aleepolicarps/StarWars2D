package upm.cmsc.starwars.objects;

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

import static org.apache.commons.io.FilenameUtils.removeExtension;

public class LukeSkywalker {
	
	private Map<String,Image> images;
	private float x = 0;
	private float y = 0;
	private Animation currSprite,rightMove,noMove,attackMove,jumpMove;
	
	public LukeSkywalker() throws SlickException{
		loadSprites();
	}
	
	
	private void loadSprites() throws SlickException{
		try{
			String folderName = this.getClass().getResource("/sprites/luke").getPath();
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
	
//	private void loadAnimations(){
//		Image[] imgSequence = {images.get("walk1"),images.get("walk2")};
//		int[] duration = {100,100};
//		rightMove =  new Animation(imgSequence.clone(),duration.clone(),false);
//		
//		imgSequence = new Image[]{images.get("stand")};
//		duration = new int[]{200};
//		noMove =  new Animation(imgSequence.clone(),duration.clone(),false);
//	}
//	
	
	public Animation getRightAnimation(){
		Image[] imgSequence = {images.get("walk1"),images.get("walk2")};
		int[] duration = {100,100};
		return new Animation(imgSequence,duration,false);
	}
	public Animation getNoAnimation(){
		Image[] imgSequence = {images.get("stand")};
		int[] duration = {200};
		return new Animation(imgSequence,duration,false);
	}
	public Animation getAttackAnimation(){
		Image[] imgSequence = {images.get("attack1"),images.get("attack2"),images.get("attack3")};
		int[] duration = {50,50,70};
		return new Animation(imgSequence,duration,false);
	}
	public Animation getJumpAnimation(){
		Image[] imgSequence = {images.get("jump")};
		int[] duration = {200};
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
	
	public void addToX(float num){
		x+=num;
	}
	public void addToY(float num){
		y+=num;
	}


}
