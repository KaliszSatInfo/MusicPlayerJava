import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BasicGUI extends JFrame{
    private JPanel panel;
    private JProgressBar progressBar;
    private JTextField songNameField;
    private JButton playButton;
    private JButton nextButton;
    private JButton prevButton;
    private JButton stopButtton;
    //https://yaytext.com/emoji/last-track-button/
    private JMenuBar menu =new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem loadItem = new JMenuItem("Load");
    private JFileChooser fc = new JFileChooser(".");
    private static List<File> fullPlaylist = new ArrayList<>();
    private static int index = 0;
    private static boolean playing = false;
    private String benefactor = "https://github.com/sumeghana/Java-Audio-Player.git";

    public static void main(String[] args) {


    }
    public BasicGUI(){
        Loader();
        initWindow();
        initMenu();

    }
    public void initWindow(){
        setContentPane(panel);
        setTitle("Music Player");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        playButton.addActionListener(e ->{
            MusicPlayer mp = new MusicPlayer();
            Thread myThread = new Thread(mp);
            myThread.start();

        });
        //stopButtton.addActionListener(e ->plsStop());
        //playButton.setFont(new Font("Arial", Font.PLAIN, 21));    Button size

        prevButton.addActionListener(e -> move(false));
        nextButton.addActionListener(e -> move(true));
    }
    public void play(Thread myThread){

            myThread.start();;

    }
    public void plsStop(MusicPlayer mp){
        mp.requestStop();
        playing= false;

    }
    public void initMenu() {
        setJMenuBar(menu);
        menu.add(fileMenu);
        fileMenu.add(loadItem);

        loadItem.addActionListener( e -> ChooseFile());
    }
    public static boolean isPlaying(){
        return playing;
    }
    public static void setPlaying(boolean choice){
        playing = choice;
    }
    public void ChooseFile(){
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3", "mp3");
        fc.addChoosableFileFilter(filter);
        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            fullPlaylist.add((fc.getSelectedFile()));

        }
        Saver();
    }
    public void Saver(){
        try(PrintWriter wr = new PrintWriter(new BufferedWriter(new FileWriter("songPaths")))) {
            for (File path : fullPlaylist){
                wr.println(path);
            }
        }
        catch (FileNotFoundException e){
            System.err.println("Nebyl nalezen soubor " + e.getLocalizedMessage());
        }
        catch (IOException e){
            System.err.println("IOE problem" + e.getLocalizedMessage());
        }
    }

    public void Loader(){
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader("songPaths")))) {
            while (sc.hasNextLine()) {
                String file = sc.nextLine();
                fullPlaylist.add(new File(file));
            }
            updateSongName();
        } catch (FileNotFoundException e) {
            System.err.println("nebyl nalezen soubor " + e.getLocalizedMessage());
        }
    }

    public static File getPlayListIndex() {
        return fullPlaylist.get(index);
    }

    public static File getPath(int index){
        return fullPlaylist.get(index);
    }
    public void updateSongName(){
        String line = String.valueOf(fullPlaylist.get(index));
        String[] block = line.split("\\.");
        songNameField.setText(block[0].trim());
    }
    public void move(boolean right){
        if (!fullPlaylist.isEmpty()){
            if (right & index+1 < fullPlaylist.size()){
                silence();
                index++;
                updateSongName();
            } else if (!right & index >= 1) {
                silence();
                index--;
                updateSongName();
            }
        }
    }
    public void silence(){

    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        BasicGUI.index = index;
    }
}