package sample;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public enum SoundEffects {
    START ("\\sample\\Sounds\\start1.wav"),
    COLLIDE ("\\sample\\Sounds\\collision.wav"),
    TURNCHANGE ("\\sample\\Sounds\\turnchange.wav");

    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;
    private Clip clip;

    SoundEffects(String soundFileName) {
        try {
            URL url = this.getClass ().getClassLoader ().getResource (soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream (url);
            clip = AudioSystem.getClip ();
            clip.open (audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        } catch (LineUnavailableException e) {
            e.printStackTrace ();
        }
    }

    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning ())
                clip.stop ();
            clip.setFramePosition (0);
            clip.start ();
        }
    }

    static void init() {
        values ();
    }
}

