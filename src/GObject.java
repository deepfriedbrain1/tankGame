package src;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GObject {
    protected int x, y, width, height, speed, diagSpeed; 
    protected PowerUp powerup;
    protected BufferedImage img;
    
    public GObject(){
        this(0, 0, 0);
    }//end default constructor
    
    public GObject(int x, int y, int speed){
        this(null, x, y, speed);
    }//end constructor
    
    public GObject(BufferedImage img, int x, int y, int speed){
        this.img = img;
        this.x = x;
        this.y = y;
        this.diagSpeed=(int)(speed / Math.sqrt(2));
        this.speed = speed;
        if(img != null){
            this.width = img.getWidth();
            this.height = img.getHeight();
        }
         
    }//end constructor
    
    public BufferedImage getImage(){
        return img;
    }//end getImage
    
    public int getX(){
        return this.x;
    }//end getX
    
    public int getY(){
        return this.y;
    }//end getY
    
    public int getWidth(){
        return this.width;
    }//end getWidth
    
    public int getDiagSp(){
        return this.diagSpeed;
    }//end getDiagSp
    
    public int getHeight(){
        return this.height;
    }//end getHeight
    
    public void setX(int a){
        this.x = a;
    }//end setX
    
    public void setY(int b){
        this.y = b;
    }//end setY
     
    public Rectangle getRectangle() {
	return new Rectangle(x, y, width, height);
    }//end getRectangle
}//end GObject
