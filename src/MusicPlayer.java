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
    private boolean stopRequested;
    private int originalindex;
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
        if (!BasicGUI.isPlaying()) {
            playShit(getTruePath(true));
        }
        else playShit(getTruePath(false));

    }
    public File getTruePath(boolean play){
        if (play){
            BasicGUI.setPlaying(false);
            return BasicGUI.getPlayListIndex();}

        else {

            BasicGUI.setPlaying(true);

        return new File("doom.mp3");
    }}

    public void playShit(File path){
        System.out.println("running");
        while (!isStopRequested()){
            try {
                FileInputStream fileInputStream = new FileInputStream(path);
                AdvancedPlayer player = new AdvancedPlayer(fileInputStream);
                player.play();

            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
            } catch (JavaLayerException e) {
                System.out.println("Error playing MP3: " + e.getMessage());
            }
        }
    }



}
