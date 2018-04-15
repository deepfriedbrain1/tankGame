package src;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Sprite {

    private static BufferedImage sprite;
    
    public static BufferedImage loadSprite(String name){
        sprite = null;
        try {
            sprite = ImageIO.read(Sprite.class.getResource("resources/" + name + ".gif"));
        }catch(IOException ioe) {
            System.out.println("*****IOException : Sprite Class : loadSprite: " + ioe.getMessage());
        }//end try-catch
        
        return sprite;
        
    }//end loadSprite
}//end Sprite