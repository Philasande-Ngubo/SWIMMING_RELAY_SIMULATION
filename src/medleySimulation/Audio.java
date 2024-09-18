// Philasande Ngubo (NGBPHI016@myuct.ac.za)
// This class plays adio
package medleySimulation;
import java.io.File; 
import java.io.IOException;
  
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 
public class Audio{
    AudioInputStream audioInputStream;
	Long currentFrame; 
    Clip clip;
    boolean repeat;
    String song;
	public Audio(String song,boolean repeat) {
		this.repeat = repeat;
		this.song = song;
		

	}

	public void play(){
		
		try{
		audioInputStream = AudioSystem.getAudioInputStream(new File("sound/"+song).getAbsoluteFile());
	    clip = AudioSystem.getClip();
		clip.open(audioInputStream);
		if (repeat) {clip.loop(Clip.LOOP_CONTINUOUSLY);} 
        clip.start();
        }
        catch (Exception e){}

	}

	public void pause(){
		clip.stop();
	}


}