package upm.cmsc.starwars.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Background {
	
	private Image image;
	private float x;
	
	
	public Background(String filename) throws SlickException{
		image = new Image(this.getClass().getResource("/background/"+filename).getPath());
		x = -1 * image.getWidth();
	}

	public void addToX(float number){
		x+=number;
	}
	public void incrementX(){
		x++;
	}
	public float getX(){
		return x;
	}
	public void draw(float x){
		image.draw(x,0);
	}
	public int getWidth(){
		return image.getWidth();
	}
	
}