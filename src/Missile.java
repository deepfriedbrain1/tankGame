package src;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.*;


public final class Missile extends GObject {
    
    private Direction direction;
    private TankGame field;
    private GameAction launcher;
    private GSound explS;
    private GSound explB;
    private int direc;
    private static BufferedImage img;
    private int w;
    private int h;
    
    static {
        img =  Sprite.loadSprite("Rocket");
    }//end static
	
    public Missile(TankGame field, GameAction launcher, Direction direction, int x, int y, int speed) {
        super(img, x, y, 10);
	this.field = field;
	this.launcher = launcher;
	this.direction = direction;
        this.w = img.getWidth();
        this.h = img.getHeight();
        this.direc = 0;
        launcher.getW(w, h);
        explS = new GSound(2, "resources/Explosion_small.wav");
        explB = new GSound(2, "resources/Explosion_large.wav");
    }//end Missile

    public void setLocation(int x, int y) {
	this.x = super.getX();
	this.y = super.getY();
    }//end setLocation
    
    //set wall health
    public void hitted(){
        LinkedList<BreakableWall> wall = field.getBWalls();
            if(wall != null){
	  	Iterator<BreakableWall> itrWalls = wall.iterator();
	   	while (itrWalls.hasNext()){
                    BreakableWall bwall = itrWalls.next(); 
                    bwall.setHit();
                }//end while  
            }//end if
    }//end hitted
    
    public void setDirection(Direction direction){
        this.direction = direction;
    }//end setDirection
  
    public boolean hitObjects(){
        Rectangle mine = getRectangle();
        
        // If hit walls.
        LinkedList<Wall> walls = field.getWalls();
        if(walls != null){
            Iterator<Wall> itrWall = walls.iterator();
                while(itrWall.hasNext()){
                    Wall wall = itrWall.next();
                        if(mine.intersects(wall.getRectangle())){          
                            explS.play();
                            return true;
                        }//end if
                }//end while
        }//end if
        
         // Detect collision from breakable walls.
	LinkedList<BreakableWall> wall = field.getBWalls();
            if (wall != null) {
		Iterator<BreakableWall> itrWalls = wall.iterator();
		while (itrWalls.hasNext()) {
                   BreakableWall bwall = itrWalls.next();
                    if(mine.intersects(bwall.getRectangle())){
                        bwall.damage(2);
                        if(bwall.getHit()<=0){
                            itrWalls.remove();
                        }
                        explS.play();
                        return true;
                    }//end if
                }//end while
            }//end if
		
    // If hit tanks.
    GameAction player = field.getPlayerTank();
    GameAction player2 = field.getPlayerTank2();
    
    if(player == launcher) {
	  if(player2.Live() && mine.intersects(player2.getRectangle())) {
            explS.play();
            if(!player2.hasShield()){
                player2.damage(3);
                player2.setPlayer1score(30); //add 30 to score for direct damage
            }
            if(player2.hasShield()){
                player2.damageShield(3);
                player2.setPlayer1score(15); //add 15 to score for shield damage
            }
            
            if(!player2.Live() && mine.intersects(player2.getRectangle())){
                explB.play(); // large explosion sound when player 2 dies
                player2.setPlayer1score(100);
            }
            return true;
	}//end if
    }else{
   
	if(player.Live() && mine.intersects(player.getRectangle())) {   
            explS.play();
            if(!player.hasShield()){
                player.damage(3);
                player.setPlayer2score(30); //add 30 to score for direct damage
            }
            if(player.hasShield()){
                player.damageShield(3);
                player.setPlayer2score(15); //add 15 to score for shield damage
            }
            
            if(!player.Live() && mine.intersects(player.getRectangle())){
                explB.play();
                player.setPlayer2score(100);
            }
            return true;

	}//end if
    }//end if-else
		
    return false;
    }//end hitObjects
  
    //missile rotation
    public void rotation(int direc){
        this.direc = direc;  
    }//end rotation
  	
    public void paint(Graphics g) {
        int dir = 0;
        switch (direction) {
	case DOWN:
            y += speed;
            dir=90;
            rotation(dir);
            break;
	case LEFT:
            x -= speed;
            dir=180;
            rotation(dir);
            break;
	case LEFT_DOWN:
            x -= getDiagSp();
            y += getDiagSp();
            dir=135;
            rotation(dir);
            break;
	case LEFT_UP:
            x -= getDiagSp();
            y -= getDiagSp();
            dir=225;
            rotation(dir);
            break;
	case RIGHT:
            x += speed;
            dir=0;
            rotation(dir);
            break;
        case RIGHT_DOWN:
            x += getDiagSp();
            y += getDiagSp();
            dir=45;
            rotation(dir);
            break;
	case RIGHT_UP:
            x +=getDiagSp();
            y -= getDiagSp();
            dir=315;
            rotation(dir);
            break;
	case UP:
            y -= speed;
            dir=270;
            rotation(dir);
            break;
        default:
            break;
        }//end switch
		
    Graphics2D g2d =  (Graphics2D) g;
    double rotationRequired = Math.toRadians (direc);
    double locationX = img.getWidth() / 2;
    double locationY = img.getHeight() / 2;
    AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    g2d.drawImage(op.filter(img, null), x, y, this.getWidth(), this.getHeight(), null);
    
    }//end paint
	
    protected Blast produceBlast() {
	Blast blast = new Blast();
	blast.setLocation(x, y);
        return blast;
    }//end produceBlast
    
    protected BigBlast produceBigBlast(){
        BigBlast bigBlast = new BigBlast();
        bigBlast.setLocation(x, y);
        return bigBlast;
    }//end produceBigBlast
}//end Missle
