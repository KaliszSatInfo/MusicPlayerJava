import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.spec.RSAOtherPrimeInfo;

public class MusicPlayer {
    private static File location;
    private static FileInputStream fileInputStream;

    static {
        try {
            fileInputStream = new FileInputStream(location);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static AdvancedPlayer player;

    static {
        try {
            player = new AdvancedPlayer(fileInputStream);
        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        BasicGUI gui = new BasicGUI();
        gui.setVisible(true);

    }

    public static void PlayMusic(File file) {
        try {
            location = file;
            player.play();
        } catch (JavaLayerException e) {
            System.out.println("Error playing MP3: " + e.getMessage());
        }
    }
    public static void shutUp(){
        player.close();
    }
}
