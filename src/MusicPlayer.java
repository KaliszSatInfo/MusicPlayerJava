import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MusicPlayer extends Thread {
    private AdvancedPlayer player;
    private volatile boolean isPlaying;
    private File currentFile;

    public void run() {
        try {
            FileInputStream fileInputStream = new FileInputStream(currentFile);
            player = new AdvancedPlayer(fileInputStream);
            player.play();
            isPlaying = true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (JavaLayerException e) {
            System.out.println("Error playing MP3: " + e.getMessage());
        }
    }

    public void stopPlaying() {
        if (player != null) {
            player.close();
            isPlaying = false;
        }
    }

    public void setCurrentFile(File file) {
        currentFile = file;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void startPlaying() {
        if (player != null && !isPlaying) {
            try {
                player.play();
                isPlaying = true;
            } catch (JavaLayerException e) {
                System.out.println("Error playing MP3: " + e.getMessage());
            }
        }
    }
}