package src;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Blast {

    private int blastCount;
    private int speed = 10;
    private int count = 1;
    private int x = 0;
    private int y = 0;
    private static BufferedImage img;

    static {
        img = Sprite.loadSprite("Explosion_small");
    }//end static

    public Blast() {
    }//end default constructor

    public void paint(Graphics g) {
        if (!blastOver()) {
            g.drawImage(img, x, y, null);
            ++blastCount;
        }
    }//end paint

    protected boolean blastOver() {
        return !(blastCount / speed < count);
    }//end blastOver

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }//end setLocation
}//end Blast
