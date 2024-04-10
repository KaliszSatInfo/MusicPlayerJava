import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
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
    //https://yaytext.com/emoji/last-track-button/
    private JMenuBar menu =new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem loadItem = new JMenuItem("Load");
    private JFileChooser fc = new JFileChooser(".");
    private static List<File> fullPlaylist = new ArrayList<>();
    private int index = 0;
    public static void main(String[] args) {
        BasicGUI gui = new BasicGUI();
        gui.setVisible(true);
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

        playButton.addActionListener(e -> MusicPlayer.PlayMusic(fullPlaylist.get(index)));
        //playButton.setFont(new Font("Arial", Font.PLAIN, 21));    Button size

        prevButton.addActionListener(e -> move(false));
        nextButton.addActionListener(e -> move(true));
    }
    public void initMenu() {
        setJMenuBar(menu);
        menu.add(fileMenu);
        fileMenu.add(loadItem);

        loadItem.addActionListener( e -> ChooseFile());
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
        try(PrintWriter wr = new PrintWriter(new BufferedWriter(new FileWriter("Deskovky")))) {
            for (File path : fullPlaylist){
                wr.print(path);
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
                MusicPlayer.shutUp();
                index++;
                updateSongName();
            } else if (!right & index >= 1) {
                MusicPlayer.shutUp();
                index--;
                updateSongName();
            }
        }
    }
}