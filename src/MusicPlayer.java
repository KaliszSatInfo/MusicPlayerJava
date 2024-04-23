import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.spec.RSAOtherPrimeInfo;

public class MusicPlayer implements Runnable {
    private boolean stopRequsted = false;
    public synchronized  void  requestStop(){
        this.stopRequsted = true;
    }
    private synchronized  boolean  isStopRequested(){
       return this.stopRequsted;
    }
    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /*public static void main(String[] args) {
        BasicGUI gui = new BasicGUI();
        gui.setVisible(true);

    }*/
    @Override
    public void run() {
        System.out.println("running");
        while (!isStopRequested()){
        try {
            FileInputStream fileInputStream = new FileInputStream(BasicGUI.getPlayListIndex());
            AdvancedPlayer player = new AdvancedPlayer(fileInputStream);

           player.play();
           //Doesn't work, need to figure out what the hell a java Thread is and how to run stuff in parallel
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (JavaLayerException e) {
            System.out.println("Error playing MP3: " + e.getMessage());
        }
        }
    }



}
