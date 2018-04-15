package src;

import java.io.IOException;
import java.util.logging.*;
import javax.sound.sampled.*;

public final class GSound {
    
    private AudioInputStream audioStream;
    private String audioSource = "";
    private Clip clip;
    private FloatControl gainControl; // volume control
    private int type; // 1 for continuously audio; 2 for one time audio
    
    public GSound(int type, String audioSource){
        this.audioSource = audioSource;
        this.type = type;
       
        try{
            audioStream = AudioSystem.getAudioInputStream(TankGame.class.getResource(audioSource));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(5.0f);
        
        }catch(IOException | LineUnavailableException | UnsupportedAudioFileException e){
            System.out.println("***Audio Error: " + e.getMessage());
        }//end try-catch
        
        if(this.type == 1){
            Runnable myRun = () -> {
                while(true){
                    clip.start();
                    clip.loop(clip.LOOP_CONTINUOUSLY);            
                    
                    gainControl.setValue(-10.0f);
                    try{
                        Thread.sleep(10000);
                    }catch(InterruptedException ie){
                        Logger.getLogger(GSound.class.getName()).log(Level.SEVERE, null, ie);
                    }
                }
            };
            
            Thread thread = new Thread(myRun);
            thread.start();
        }
    }//end constructor

    public void play(){
        clip.start();
    }//end play
    
    public void stop(){
        clip.stop();
    }//end stop
}//end GSound