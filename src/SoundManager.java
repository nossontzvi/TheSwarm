import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class SoundManager {
    public Clip clip;

    public void setFile(String soundFileName) {
        try {
            URL url = getClass().getResource(soundFileName);
            if (url != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(ais);
            }
        } catch (Exception e) {
            System.out.println("Audio file not found yet.");
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}