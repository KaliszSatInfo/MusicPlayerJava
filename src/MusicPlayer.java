import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.spec.RSAOtherPrimeInfo;

public class MusicPlayer extends Thread {
    /*public static void main(String[] args) {
        BasicGUI gui = new BasicGUI();
        gui.setVisible(true);

    }*/

    public void run() {
        try {
            FileInputStream fileInputStream = new FileInputStream(BasicGUI.getPlayListIndex());
            AdvancedPlayer player = new AdvancedPlayer(fileInputStream);
            player.play();
            //else player.close(); //Doesn't work, need to figure out what the hell a java Thread is and how to run stuff in parallel
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (JavaLayerException e) {
            System.out.println("Error playing MP3: " + e.getMessage());
        }
    }

}
