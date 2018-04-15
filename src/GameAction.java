package src;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

public final class GameAction extends GObject{
    private Direction direction; 
    private boolean live = true;
    private int numberOfLives = 3;
    private boolean hit = false;
    private int health = 100;
    private int direc;
    private TankGame field;
    private int w, h;
    private boolean moving;
    private static BufferedImage img;
    private PowerUp powerup;

    static {
        img = Sprite.loadSprite("Tank2"); 
    }//end static
  
    public GameAction(TankGame field, int x, int  y, int speed, Direction direction){
        super(img,x,y, speed);
        this.field = field;
        this.direction = direction;
        this.direc = 0;
        powerup = new PowerUp();
    }//end constructor

    public void getW(int w, int h){
        this.w = w;
        this.h = h;
    }//end getW
    
    public boolean hasLives(){
        return (numberOfLives > 0);
    }//end hasLives
    
    public void reducePowerUp(){
        powerup.reducePowerUp();
    }//end reducePowerUp
    
    public void setHealth(){
        health = 100;
    }//end setHealth
    
    public void resetPowerUps(){
        powerup.resetPowerUps();
    }//end resetPowerUps
    
    public void setEnableMultiFire(boolean b){
        powerup.setMultiFire(b);
    }//end setEnableMultiFire
    
    public boolean drawMissile(){
        return powerup.drawMissile();
    }//end drawMissile
    
    public boolean isMultiFireActivated(){
        return powerup.isMultiFireActivated();
    }//end isMultiFireActivated
 
    //missile getting shot from the center of the tank
    public Missile fireMissile(Direction direction) {
	Missile missile = new Missile(field, this, direction, x + this.getWidth()/4 + w/4 , y + this.getHeight()/4 + h/4, 6);
	return missile;
    }//end fireMissile
	
    public Missile fireMissile() {
	return fireMissile(direction);       
    }//end fireMissile
	
    public LinkedList<Missile> fireMissiles() {
	LinkedList<Missile> missiles = new LinkedList<Missile>();
	Direction[] directions = Direction.values();
        for(int i = 0; i < directions.length-1; ++i) {
            missiles.add(fireMissile(directions[i]));
        }
        return missiles;
    }//end fireMissiles
  
    public static boolean testIntersection(Shape shapeA, Shape shapeB) {
        Area areaA = new Area(shapeA);
        areaA.intersect(new Area(shapeB));
        
        if(!areaA.isEmpty()){
            return true; 
        }else{
            return false;
        }
    }//end testIntersection   

    public void Collision(int oldX, int oldY) {
	Rectangle mine = getRectangle();
    
	// Detect collision from walls.
	LinkedList<Wall> walls = field.getWalls();
            if (walls != null) {
		Iterator<Wall> itrWall = walls.iterator();
		while (itrWall.hasNext()) {
                    Wall wall = itrWall.next();
                    if(testIntersection(mine, wall.getRectangle())==true){     
                        setLocation(oldX,oldY);
                        return;				
                    }//end if
                }//end while
            }//end if
            
        // Detect collision from breakable walls.
	LinkedList<BreakableWall> wall = field.getBWalls();
            if (wall != null) {
		Iterator<BreakableWall> itrWalls = wall.iterator();
		while (itrWalls.hasNext()) {
                   BreakableWall bwall = itrWalls.next();
                    if(testIntersection(mine, bwall.getRectangle())==true){     
                        setLocation(oldX,oldY);
                        return;				
                    }//end if
                }//end while
            }//end if
		
        // Detect collision from tanks.
        GameAction player1 = field.getPlayerTank();
        GameAction player2 = field.getPlayerTank2();
        if(player1.Live() && player2.Live() && player1.getRectangle().intersects(player2.getRectangle())){
            setLocation(oldX, oldY); 
        }//end if   
        
        LinkedList<PowerUp> powerup = field.getPowerUps();
        if(powerup != null){
            Iterator<PowerUp> itrPowerUps = powerup.iterator();
            while(itrPowerUps.hasNext()){
                PowerUp powerUp = itrPowerUps.next();
                if(player1.getRectangle().intersects(powerUp.getRectangle())){
                    player1.getPowerUp(); 
                    itrPowerUps.remove();
                    player1.setPlayer1score(20);//add 20 to score PowerUp pickup
                }
                if(player2.getRectangle().intersects(powerUp.getRectangle())){  
                    player2.getPowerUp(); 
                    itrPowerUps.remove();
                    player2.setPlayer2score(20);//ad 20 to score PowerUp pickup    
                }
            }//end while
        }//end if
    }//end Collision
    
    public boolean isMultiFireOn(){
        return powerup.isMultiFireOn();
    }//end hasPowerUp
    
    public boolean hasShield(){
        return powerup.hasShield();
    }//end hasShield
    
    public int shieldHealth(){
        return powerup.shieldHealth();
    }//end shieldHealth
    
    public void useShield(){
        powerup.useShield();
    }//end useShield
    
    public boolean wasShieldUsed(){
        return powerup.wasShieldUsed();
    }//end wasShieldUsed
    
    public void damageShield(int damage){
        powerup.damageShield(damage);
    }
    
    public void disableMultiFireActivation(){
        powerup.disableMultiFireActivation();
    }
    
    public void getPowerUp(){
        powerup.getPowerUp();
    }//end getPowerUp
    
    public boolean hit(boolean hit){
        return this.hit = hit;
    }//end hit
    
    public void setPlayer1score(int score){
        field.setPlayer1score(score);
    }//end setPlayer1score
    
    public void setPlayer2score(int score){
        field.setPlayer2score(score);
    }//end setPlayer2score
 
    public int damage(int type){
        if(type == 0){
            health -= 10;        
        }
        if(type == 2){
            health -= 20;
        }
        if(type == 3){
            health -= 30;  
        }
        if(health <= 0){
            setLive(false);
            if(numberOfLives > 0){
                numberOfLives--;
                
            }
        }
        return health;
    }//end damage
    
    public int getNumberOfLives(){
        return numberOfLives;
    }//end getNumberOfLives
    
    public void setNumberOfLives(int numOfLives){
        this.numberOfLives = numOfLives;
    }//end setNumberOfLives
   
    public int getHealth(){
        return health;
    }//end getHealth
  
    public boolean Live() {
	return live;
    }//end Live
    
    public void setLive(boolean live) {
	this.live = live;
        if(live == false)
            powerup.resetPowerUps();
    }//end setLive
	
    public void setMoving(boolean moving) {
	this.moving = moving;
    }//end setMoving
	
    public void setDirection(Direction direction) {
	this.direction = direction;
    }//end setDirection
  
    public void rotation(int direc){
        this.direc = direc;
    }//end rotation
 
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }//end setLocation
	
	
    public void move(Graphics g){
     
	if (moving) {
            
        int dir=0;
	int oldX = x;
	int oldY =y;
        
	switch (direction) {
            case DOWN:
		y += speed;
                dir=90;
                break;
            case LEFT:
		x -= speed;
                dir=180;
                break;
            case LEFT_DOWN:
		x -= getDiagSp();
		y += getDiagSp();
                dir=135;
                break;
            case LEFT_UP:
		x -= getDiagSp();
		y -= getDiagSp();
                dir=225;
                break;
            case RIGHT:
                x += speed;
                dir=0;
                break;
            case RIGHT_DOWN:
		x += getDiagSp();
                y += getDiagSp();
                dir=45;
                break;
            case RIGHT_UP:
		x +=getDiagSp();
		y -= getDiagSp();
                dir=315;
                break;
            case UP:
		y -= speed;
                dir=270;
                break;
	}//end switch
        
        rotation(dir);
        Collision(oldX, oldY);
	}//end if
        
     
    //tank rotation
    Graphics2D g2d =  (Graphics2D) g;
    double rotationRequired = Math.toRadians (direc);
    double locatX = img.getWidth() / 2;
    double locatY = img.getHeight() / 2;
    AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locatX, locatY);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    g2d.drawImage(op.filter(img, null), x , y, this.getWidth(), this.getHeight(), null);
    
    }//end move
}//end GameAction