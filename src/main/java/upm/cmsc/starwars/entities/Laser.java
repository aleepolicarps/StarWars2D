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
	 private int charType;
	 
	 public Laser() throws SlickException{
		 loadImage(0);
	 }
	 
	 public Laser(float x, float y) throws SlickException{
		 this.x = x;
		 this.y = y;
		 loadImage(0);
	 }
	 
	 public Laser(float x, float y, int charType) throws SlickException{
		 this.x = x;
		 this.y = y;
		 this.charType = charType;
		 loadImage(charType);
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
		 if(charType==0){
			 x -= delta * VELOCITY;
		 }else if(charType==1){
			 x += delta * VELOCITY;
		 }
	 }
	 
	 private void loadImage(int charType) throws SlickException{
		 if(charType==0){
			 image = new Image(CustomFileUtil.getFilePath("/elements/laser2.png")); 
		 }else if(charType==1){
			 image = new Image(CustomFileUtil.getFilePath("/elements/laser.png"));
		 }
	 }
 
}
