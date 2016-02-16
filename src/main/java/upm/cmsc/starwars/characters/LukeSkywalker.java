package upm.cmsc.starwars.characters;

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

public class LukeSkywalker {
	
	private static final LukeSkywalker lukeSkywalker = new LukeSkywalker();
	private static Map<String,Image> images;
	
	private LukeSkywalker(){
		loadSprites();
	}
	
	
	private void loadSprites(){
		try {
			String folderName = LukeSkywalker.class.getResource("/sprites/luke").getPath();
			List<File> rawFiles = Files.walk(Paths.get(folderName))
					.filter(Files::isRegularFile)
					.map(Path::toFile)
					.collect(Collectors.toList());

			Map<String, Image> images = new HashMap<String, Image>();
			for(File file:rawFiles){
				images.put(file.getName(), new Image(file.getAbsolutePath()));
			}
			this.images = images;
		}  catch (IOException e) {
			// TODO do something here
		} catch (SlickException e) {
			// TODO do something here
		}
	}
	
	private static Image getImage(String key){
		return images.get(key);
	}
	
	public static Animation getRightMovement(){
		Image[] images = {getImage("9.png"),getImage("1.png")};
		int[] duration = {100,100};
		return new Animation(images,duration,false);
	}
	public static Animation getNoAnimation(){
		Image[] images = {getImage("9.png")};
		int[] duration = {200};
		return new Animation(images,duration,false);
	}

}
