package src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public final class BigBlast extends Blast {

    private int blastCount;
    private int speed = 10;
    private int count = 1;
    private int x = 0;
    private int y = 0;
    private static BufferedImage img;

    static {
        img = Sprite.loadSprite("Explosion_large");
    }//end static

    BigBlast() {
    }//end default constructor

    @Override
    public void paint(Graphics g) {
        if (!blastOver()) {
            g.drawImage(img, x, y, null);
            ++blastCount;
        }
    }//end paint

    @Override
    protected boolean blastOver() {
        return !(blastCount / speed < count);
    }//end blastOver

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }//end setLocation
}//end BigBlast

