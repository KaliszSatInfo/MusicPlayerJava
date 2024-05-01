import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//Alternate method im trying out, can's record where i stopped(time) with the other AdvancedPlayer

public class AltMusicPlayer {
    Long currentFrame;
    Clip clip;

    String status;

    AudioInputStream audioInputStream;
    static String filePath;
    public AltMusicPlayer(File filePath)
        throws UnsupportedAudioFileException,
                IOException, LineUnavailableException
        {
            audioInputStream = AudioSystem.getAudioInputStream(filePath);

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }


    public void play() {
        clip.start();

        status = "play";
    }

    public void pause() {
        if (status.equals("paused"))
        {
            System.out.println("audio is already paused");
            return;
        }
        this.currentFrame =
                this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }









}
