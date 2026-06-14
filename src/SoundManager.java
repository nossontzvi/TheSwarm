import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SoundManager {
    private HashMap<String, ArrayList<Clip>> clipPools = new HashMap<>();

    public void loadSound(String name, String path, int poolSize) {
        ArrayList<Clip> pool = new ArrayList<>();
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                for (int i = 0; i < poolSize; i++) {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    pool.add(clip);
                }
                clipPools.put(name, pool);
            }
        } catch (Exception e) {
            System.out.println("Audio file not found: " + path);
        }
    }

    public void play(String name) {
        ArrayList<Clip> pool = clipPools.get(name);
        if (pool != null) {
            for (Clip clip : pool) {
                if (!clip.isRunning()) {
                    clip.setFramePosition(0);
                    clip.start();
                    return;
                }
            }

            Clip forceClip = pool.get(0);
            forceClip.stop();
            forceClip.flush();
            forceClip.setFramePosition(0);
            forceClip.start();
        }
    }

    public void loop(String name) {
        ArrayList<Clip> pool = clipPools.get(name);
        if (pool != null && !pool.isEmpty()) {
            pool.get(0).loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void stopLoop(String name) {
        // 1. Retrieve the specific ArrayList of Clips associated with the string 'name'
        ArrayList<Clip> pool = clipPools.get(name);

        // 2. Verify the pool exists and contains at least one element to prevent NullPointerExceptions
        if (pool != null && !pool.isEmpty()) {

            // 3. Target index 0, because loop() started the playback on index 0
            Clip loopingClip = pool.get(0);

            // 4. If the clip is currently outputting audio, halt it
            if (loopingClip.isRunning()) {
                loopingClip.stop(); // Immediately stops the audio thread
                loopingClip.setFramePosition(0); // Resets the playhead to 0 milliseconds
            }
        }
    }
}