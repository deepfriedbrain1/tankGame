package src;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public final class PowerUp extends GObject{
    
    private int numberOfMultiDirectionFire = 0;
    private boolean enableMultiFire = false;
    private boolean enableShield = false;
    private int shieldHitPoints = 300;
    private boolean useShield = true;
    private boolean drawMissile = false;
    private boolean multiFireActivated = false;
    private static BufferedImage shield, damagedShield, img, poweimg;
    private Random r;
   
    static{
        poweimg=Sprite.loadSprite("powerup");
        img =  Sprite.loadSprite("Pickup");
        shield = Sprite.loadSprite("Shield1");
        damagedShield = Sprite.loadSprite("Shield2");
    }//end static
    
    public PowerUp(){
    }//end default constructor
    
    public PowerUp(int x, int y, int width, int height, int speed){
        super(x, y, speed);
        this.width = poweimg.getWidth(null);
        this.height = poweimg.getHeight(null); 
    }//end constructor
    
    public void damageShield(int damage){
        switch(damage){
            case 1: shieldHitPoints -= 10;
            case 2: shieldHitPoints -= 20;
            case 3: shieldHitPoints -= 30;
        }//end switch
        
        if(shieldHitPoints <= 0)
            enableShield = false; //disable shield
    }//end damageShield
    
    public void resetPowerUps(){
        numberOfMultiDirectionFire = 0;
        enableMultiFire = false;
        enableShield = false;
        shieldHitPoints = 300;
        useShield = true; 
        drawMissile = false;
    }//end resetPowerUps
    
    public boolean drawMissile(){
        return drawMissile;
    }//end drawMissile
    
    public boolean isMultiFireActivated(){
        return multiFireActivated;
    }//end isMultiFireActivated
   
    public void enableShield(){
        enableShield = true;
    }//end enableShield
    
    public void useShield(){
        useShield = false;
    }//end useShield
    
    public boolean wasShieldUsed(){
        return useShield;          
    }//end wasShieldUsed
    
    public boolean hasShield(){
        return enableShield;
    }//end hasShield
    
    public int shieldHealth(){
        return shieldHitPoints;
    }//end shieldHealth
    
    public BufferedImage getShieldImage(){
        return shield;
    }//end getShieldImage
    
    public BufferedImage getDamagedShieldImage(){
        return damagedShield;
    }//end getDamagedShieldImage
    
    // One shield powerUp, multiMissiles allowed
    public void getPowerUp(){
        r = new Random();
        int value = r.nextInt(3);// 0-2 values
        
        if(value == 0 || value == 2){ // 0 or 2 for multi missles : probability 66.66%
            this.numberOfMultiDirectionFire = 5;
            setMultiFire(true);
            drawMissile = true;
            multiFireActivated = true;
        }
        if(value == 1){ // 1 for shield : probability 33.33%
            numberOfMultiDirectionFire = 0;
            multiFireActivated = false;
            setMultiFire(false);
            enableShield();
        }   
    }//end getPowerUp
    
    public void setMultiFire(boolean b){
        enableMultiFire = b;
    }//end setMultiFire
    
    public boolean isMultiFireOn(){
        return enableMultiFire;
    }//end isMultiFireOn
    
    public void disableMultiFireActivation(){
        multiFireActivated = false;
    }//end disableMultiFireActivation
    
    public BufferedImage getBufferedImage(BufferedImage bi){
        return bi = img;
    }//end getBufferedImage
   
    public void reducePowerUp(){
        if(numberOfMultiDirectionFire > 0)
            numberOfMultiDirectionFire -= 1;
        if(numberOfMultiDirectionFire <= 0){
            setMultiFire(false);  
        }
    }//end reducePowerUp    
    
    @Override
    public int getX(){
        return this.x;
    }//end getX
    
    @Override
    public int getY(){
        return this.y;
    }//end getY
    
    @Override
    public int getWidth(){
        return this.width;
    }//end getWidth
    
    @Override
    public int getHeight(){
        return this.height;
    }//end getHeight
    
    @Override
    public void setX(int x){
        this.x = x;
    }//end setX    
    
    @Override
    public void setY(int y){
        this.y = y;
    }//end setY
    
    public void setLocation(int x, int y){
        this.x = super.getX();
        this.y = super.getY();
    }//end setLocation 
    
    public boolean hitObjects(){
        return false;
    }//end hitObjects
    
    public void draw(Graphics g){
        g.drawImage(poweimg, x, y, width, height, null);
    }//end draw
    
    @Override
    public Rectangle getRectangle(){
        return new Rectangle(x, y, width, height);
    }//end getRectangle  
}//end PowerUp