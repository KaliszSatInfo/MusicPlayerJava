import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.File;
import java.io.FileInputStream;

public class MusicPlayer {


    private static boolean isPlaying = false;
    private AdvancedPlayer player;



    public void play(File filePath) {

        try {
            FileInputStream fis = new FileInputStream(filePath);


            player = new AdvancedPlayer(fis);
            Thread playbackThread = new Thread(() -> {
                try {
                    isPlaying = true;
                    player.play();
                } catch (Exception e) {
                    System.out.println("Error playing file: " + e);
                }
            });
            playbackThread.start();
        } catch (Exception e) {
            System.out.println("Error playing file: " + e);
        }
    }

    public void pause() {
        if (player != null) {

                player.close();
                isPlaying = false;


        }
    }



    public static boolean isIsPlaying(){
        return isPlaying;
    }
    public static void setPaused(boolean truth){
        isPlaying = truth;
    }





}
