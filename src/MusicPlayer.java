import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MusicPlayer {
    public static void main(String[] args) {
        String filePath = "bagpipe.mp3";
        PlayMusic(new File(filePath));
    }

    public static void PlayMusic(File location) {
        try {
            FileInputStream fileInputStream = new FileInputStream(location);
            AdvancedPlayer player = new AdvancedPlayer(fileInputStream);
            player.play();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (JavaLayerException e) {
            System.out.println("Error playing MP3: " + e.getMessage());
        }
    }
}
