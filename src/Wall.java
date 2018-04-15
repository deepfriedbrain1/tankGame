package src;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class Wall extends GObject {
    private int x;
    private int y;
    private int width;
    private int height;
    private static BufferedImage wall_image;
  
    static {
        wall_image = Sprite.loadSprite("Wall1");	
    }//end static
  
    public Wall(int x, int y, int width, int height, int speed) throws IOException {
        super(wall_image, x, y, speed);
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
    }//end constructor

    public void draw(Graphics g) {
        g.drawImage(img, x, y, width, height, null); 
    }//end draw
}//end Wall
