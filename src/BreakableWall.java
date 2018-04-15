package src;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class BreakableWall extends GObject {
    private int x;
    private int y;
    private int width;
    private int height;
    private int hit = 100;
    private static BufferedImage wall_image;
	
    static {
        wall_image = Sprite.loadSprite("Wall2");
    }//end static
  
    public BreakableWall(int x, int y, int width, int height, int speed) throws IOException {
        super(wall_image, x, y, speed);
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
    }//end constructor

    public void draw(Graphics g) {
        g.drawImage(img, x, y, width, height, null);  
    }//end draw 
  
    public void setHit(){
        hit = 100;
    }//end setHealth
  
    public int damage(int type){
        if(type == 0){
            hit -= 10;
        }
        if(type == 2){
            hit -= 20;
        }
        if(type == 3){
            hit -= 30;  
        }
        return hit;
    }//end damage
    
   
    public int getHit(){
        return hit;
    }//end getHealth
}//end BreakableWall    
	
