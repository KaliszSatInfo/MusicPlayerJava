import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.SocketOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class MusicPlayerForm extends JFrame {
    private  JButton playStopButton;
    private JSlider volumeSlider;
    private JPanel panel;
    private JTable songTable;
    private JSlider timeSlider;
    private JLabel currTime;
    private JLabel songLenght;
    private JButton optionBar;
    private JButton rightButton;
    private JButton leftButton;
    private JLabel songTitle;
    private JButton loopButton;
    private JMenuBar mBar = new JMenuBar();
    private JMenuItem menu0 = new JMenuItem("Add folder");
    //private JMenuItem mItem = new JMenuItem("");
    private DefaultTableModel tableModel;
    private MusicPlayer player = new MusicPlayer();
    private JCheckBox loop = new JCheckBox();
    private int index;



    private List<MySong> avalSongs = new ArrayList<>();

    public MusicPlayerForm() {
        setContentPane(panel);
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Music Player");
        setJMenuBar(mBar);
        mBar.add(menu0);
        startProgressUpdate();
        //MusicPlayer.getMyMusicPlayerForm(this);
        optionBar.addActionListener(e -> {
            Object[] fields = {
                    "⟳: ", loop,
            };

            int result = JOptionPane.showConfirmDialog(this, fields, "Options", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION ){
                player.setToLoop(loop.isSelected());
            }
        });

        menu0.addActionListener(e -> {
            //addFolder();
            fileschooser();
        });
        rightButton.addActionListener(e -> moveForward(true));
        leftButton.addActionListener(e -> moveForward(false));
        loopButton.setForeground(Color.LIGHT_GRAY);
        loopButton.addActionListener(e -> {
           if (!player.isToLoop()){
               player.setToLoop(true);
               loopButton.setForeground(Color.BLACK);
           } else {
               player.setToLoop(false);
               loopButton.setForeground(Color.LIGHT_GRAY);
           }
        });




        playStopButton.addActionListener(e -> {
            updateIcons();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("window closed");
                writeToMemory();
            }
        });



        volumeSlider.addChangeListener(e -> {
            int volume = volumeSlider.getValue();
            if (MusicPlayer.isPlaying()) player.setVolume(volume);
        });
        /*timeSlider.setValue(0);
        timeSlider.setMajorTickSpacing(MusicPlayer.getClipSize());
        timeSlider.setMinorTickSpacing(1);
        timeSlider.setPaintTicks(true);
        timeSlider.setPaintLabels(true);*/

        timeSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                player.setProgress(timeSlider.getValue()*1000000L);
            }
        });
        songTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = songTable.getSelectedRow();
            if (selectedRow != -1) {


                try {
                    player.stop();
                    int volume = volumeSlider.getValue();
                    player.load(new File(getFilePathFromSong((String) tableModel.getValueAt(selectedRow, 0), avalSongs)), volume);
                    songTitle.setText(removeExtension(String.valueOf(tableModel.getValueAt(selectedRow, 0))));
                    index = getSongIndex(String.valueOf(tableModel.getValueAt(selectedRow, 0)));
                    System.out.println(getFilePathFromSong((String) tableModel.getValueAt(selectedRow, 0), avalSongs));
                    player.play();
                    System.out.println("began song " + index );
                    songLenght.setText(secToMin(MusicPlayer.getClipSize()));
                    playStopButton.setText("❚❚");
                    timeSlider.setMaximum(MusicPlayer.getClipSize());
                    timeSlider.setPaintTicks(false);
                    timeSlider.setPaintLabels(false);
                    timeSlider.setMajorTickSpacing(0);
                    timeSlider.setMinorTickSpacing(0);
                    timeSlider.setSnapToTicks(false);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
        });

        tableModel = new DefaultTableModel(new String[]{"File Name"}, 0);
        songTable.setModel(tableModel);

        readMemory();

        updateTable(avalSongs);
    }
    public void fleshedOutPlay(String sCase){
        try {
            player.stop();
            switch(sCase) {
                case "right":
                    songTable.changeSelection(0, songTable.getSelectedRow()+1, false, false);

                case "left":
                    songTable.changeSelection(0, songTable.getSelectedRow()-1, false, false);
                case "max":
                    songTable.changeSelection(0, avalSongs.size(), false, false);
                case "min":
                    songTable.changeSelection(0, 0, false, false);
            }
            player.load(new File(getFilePathFromSong((String) tableModel.getValueAt(songTable.getSelectedRow(), 0), avalSongs)), volumeSlider.getValue());
            songTitle.setText(removeExtension(String.valueOf(tableModel.getValueAt(songTable.getSelectedRow(), 0))));
            player.play();
            System.out.println("began song " + index );
            playStopButton.setText("❚❚");

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            JOptionPane.showMessageDialog(this, "Indexing problem in list of songs");
        }
    }
    public void moveForward(boolean right) {
        int index = songTable.getSelectedRow();
        if (right){
            if (index < songTable.getColumnCount())
                System.out.println("smaller than max");
                fleshedOutPlay("right");
            } else if (index==songTable.getColumnCount()) {
                fleshedOutPlay("min");
            }

        else {
            if (index>0){
                System.out.println("bigger than min");
                fleshedOutPlay("left");
            } else if (index == 0) {
                fleshedOutPlay("max");
            }
            }
        }

    public void updateIcons(){
        if (MusicPlayer.isPlaying()) {
            player.stop();
            playStopButton.setText("►");
        } else {
            player.play();
            playStopButton.setText("❚❚");
        }
    }
    public String getFilePathFromSong(String name, List<MySong> list){
        String path="";
        for (MySong song: list){
            if (name.equals(song.getName())){
                path = song.getPath();
                break;
            }
        }

        return path;
    }


    public void writeToMemory(){
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("songinfo")));)
        {
            writer.print("");
            for (MySong songs: avalSongs){
                writer.println(songs.getPath() + ";" + songs.getName() + ";" + songs.getPlaylistID());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String removeExtension(String file){
        int lastDotIndex = file.lastIndexOf(".");
        return file.substring(0, lastDotIndex);
    }
    public void readMemory(){
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader("songinfo")));){
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] bloky = line.split(";");
                String path = bloky[0];
                String name = bloky[1];
                int playlistID = Integer.parseInt(bloky[2]);
                avalSongs.add(new MySong(path, name, playlistID));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void processFolder(String path){
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
            if (isPlayable(file)){
                avalSongs.add(new MySong(String.valueOf(file), stripPrefix(String.valueOf(file)), 1));
                //playableFiles.add(String.valueOf(file));

            }
            }
            updateTable(avalSongs);
        }
    }
    public void fileschooser(){
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            processFolder(fc.getSelectedFile().getAbsolutePath());
        }
        JOptionPane.showMessageDialog(this, "Please select a valid directory");
    }
    private void startProgressUpdate() {
        Thread progressUpdater = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (MusicPlayer.isPlaying()) {
                        long progress = player.getProgress();
                        currTime.setText(secToMin(player.getProgress()));
                        if (timeSlider.getValue() != progress) {
                            timeSlider.setValue((int) progress);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        progressUpdater.start();
    }

    public int getSongIndex(String songName){
        int index = 0;
        for(MySong song: avalSongs){
            if (Objects.equals(song.getName(), songName)){
                break;
            }
            else index++;
        }
        return index;
    }

    public String stripPrefix(String path){ //:o
        File name = new File(path);
        return name.getName();
    }

    /*private List<String> findPlayableFiles(File folder) {

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (isPlayable(file)) {
                    playableFiles.add(file.getName());
                }
            }
        }
        return playableFiles;
    }
    /*public static void setPlayStopButton(String bool) {
        playStopButton.setText(bool);
    }*/

    private boolean isPlayable(File file) {
        String fileName = file.getName();
        String[] supportedExtensions = { "wav", "ogg", };

        for (String extension : supportedExtensions) {
            if (fileName.endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }


    private void updateTable(List<MySong> data) {
        tableModel.setRowCount(0);
        for (MySong datum : data) {
            tableModel.addRow(new Object[]{datum.getName()});
        }
    }
    public static String secToMin(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public static void main(String[] args) {
        MusicPlayerForm m = new MusicPlayerForm();
        m.setVisible(true);
    }
}

class MusicPlayer {
    private Clip clip;
    private static boolean playing = false;
    private FloatControl volumeControl;
    private static long clipSize;
    private long pausedTime;
    private boolean toLoop;
   // private static MusicPlayerForm player;
    // !!!!!!!!!!!!!!!!!!!!!!   DANGER DANGER DANGER probably should delete DANGER DANGER DANGER !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public boolean isToLoop() {
        return toLoop;
    }

    public void setToLoop(boolean toLoop) {
        this.toLoop = toLoop;
    }

    public void load(File audioFile, int volume) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        final CountDownLatch playingFinished = new CountDownLatch(1);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.addLineListener(event -> {
            if (!clip.isRunning()){
            if (event.getType() == LineEvent.Type.STOP) {
                if (toLoop) {
                    clip.setMicrosecondPosition(0);
                    clip.start();
                }
                else {
                    playing = false;
                    clip.setMicrosecondPosition(0);
                   //player.updateIcons();
                }}


            }
        });
        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        setVolume(volume);
        clipSize = (long) (audioStream.getFrameLength() / audioStream.getFormat().getFrameRate());

    }

    public void play() {
        if (clip != null) {
            clip.start();
            playing = true;
        }
    }

    public void stop() {
        if (clip != null) {
            pausedTime = clip.getMicrosecondPosition();
            clip.stop();

            playing = false;
        }
    }


    public static boolean isPlaying() {
        return playing;
    }

    public void setVolume(int volume) {
        float gain = (float) (20 * Math.log10(volume / 100.0) - 20f);
        volumeControl.setValue(gain);
    }

    public long getProgress() {
        if (clip != null && clip.isRunning()) {
            return clip.getMicrosecondPosition() / 1_000_000;
        } else {
            return pausedTime / 1_000_000;
        }
    }

    public static int getClipSize() {
        return (int) clipSize;
    }

    public void setProgress(long targetTime) {
        if (clip != null && clip.isRunning()) {
            clip.setMicrosecondPosition(targetTime);
        }
    }
    /*public static void getMyMusicPlayerForm(MusicPlayerForm form){
        player = form;
    }*/
}