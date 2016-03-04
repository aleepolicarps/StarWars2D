package upm.cmsc.starwars.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import upm.cmsc.starwars.CustomFileUtil;

public class Laser {
	
	public static final int DAMAGE = 25;
	
	private final float VELOCITY = 0.3f;

	
	 private float x;
	 private float y;
	 private Image image;
	 
	 public Laser() throws SlickException{
		 loadImage();
	 }
	 
	 public Laser(float x, float y) throws SlickException{
		 this.x = x;
		 this.y = y;
		 loadImage();
	 }
	 
	 public float getX(){
		 return x;
	 }
	 public void setX(float x){
		 this.x = x;
	 }
	 public float getY(){
		 return y;
	 }
	 public void setY(float y){
		 this.y = y;
	 }
	 public Image getImage(){
		 return image;
	 }
	 public void updateXPosition(int delta){
		 x -= delta * VELOCITY;
	 }
	 
	 private void loadImage() throws SlickException{
		 image = new Image(CustomFileUtil.getFilePath("/elements/laser.png"));
	 }
 
}
