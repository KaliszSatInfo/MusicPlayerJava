import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BasicGUI extends JFrame{
    private JButton stopButton;
    private JButton startButton;
    public JPanel panel;
    private JProgressBar progressBar;
    private JTextField songNameField;
    private JMenuBar menu =new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem loadItem = new JMenuItem("Load");
    private JFileChooser fc = new JFileChooser(".");
    private List<String> fullPlaylist = new ArrayList<>();

    public BasicGUI(){
        Loader();
        initWindow();
        initMenu();

    }

    public void initWindow(){
        setTitle("Music Player");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

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
            fullPlaylist.add(String.valueOf(fc.getSelectedFile()));

        }
        Saver();
    }
    public void Saver(){
        try(PrintWriter wr = new PrintWriter(new BufferedWriter(new FileWriter("Deskovky")))) {
            for (String path : fullPlaylist){
                wr.print(String.valueOf(path));
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
                fullPlaylist.add(file);
            }
            songNameField.setText(String.valueOf(fullPlaylist.get(0)));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Could not find save file" + e.getLocalizedMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
            System.err.println("nebyl nalezen soubor " + e.getLocalizedMessage());

        }


}}
