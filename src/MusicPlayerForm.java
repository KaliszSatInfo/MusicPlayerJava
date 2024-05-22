import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MusicPlayerForm extends JFrame {
    private JButton playStopButton;
    private JSlider volumeSlider;
    private JPanel panel;
    private JTable songTable;
    private JSlider timeSlider;
    private JLabel currTime;
    private JLabel songLength;
    private JButton rightButton;
    private JButton leftButton;
    private JLabel songTitle;
    private JButton loopButton;
    private boolean isLive = false;
    private JMenuBar mBar = new JMenuBar();
    private JMenuItem menu0 = new JMenuItem("Add folder");
    private DefaultTableModel tableModel;
    private MusicPlayer player = new MusicPlayer();
    private final List<MySong> availableSongs = new ArrayList<>();

    public MusicPlayerForm() {
        setContentPane(panel);
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Music Player");
        setJMenuBar(mBar);
        mBar.add(menu0);
        JMenuItem menu1 = new JMenuItem("Delete all");
        mBar.add(menu1);
        startProgressUpdate();
        songTable.getTableHeader().setReorderingAllowed(false);
        menu0.addActionListener(e -> filesChooser());
        menu1.addActionListener(e -> clearTable());
        loopButton.setForeground(Color.LIGHT_GRAY);
        loopButton.addActionListener(e -> {
            if (!player.isToLoop()) {
                player.setToLoop(true);
                loopButton.setForeground(Color.BLACK);
            } else {
                player.setToLoop(false);
                loopButton.setForeground(Color.LIGHT_GRAY);
            }
        });
        rightButton.addActionListener(e -> moveForward(true));
        leftButton.addActionListener(e -> moveForward(false));
        playStopButton.addActionListener(e -> updateIcons());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                writeToMemory();
            }
        });
        volumeSlider.addChangeListener(e -> {
            int volume = volumeSlider.getValue();
            if (MusicPlayer.isPlaying()) player.setVolume(volume);
        });
        timeSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                player.setProgress(timeSlider.getValue() * 1000000L);
            }
        });
        songTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = songTable.getSelectedRow();
            if (selectedRow != -1) {
                stopCurrentSong();
                playSongAt(selectedRow);
            }
        });
        tableModel = new DefaultTableModel(new String[]{"File Name"}, 0);
        songTable.setModel(tableModel);
        readMemory();
        updateTable(availableSongs);
    }

    public void updateIcons() {
        if (isLive){


        if (MusicPlayer.isPlaying()) {
            player.stop();
            playStopButton.setText("►");
        } else {
            player.play();
            playStopButton.setText("❚❚");
        }}
        else JOptionPane.showMessageDialog(this, "Please select a song to play first");
    }

    public String getFilePathFromSong(String name, List<MySong> list) {
        for (MySong song : list) {
            if (name.equals(song.getName())) {
                return song.getPath();
            }
        }
        return "";
    }

    public void writeToMemory() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("Song-info")))) {
            writer.print("");
            for (MySong songs : availableSongs) {
                writer.println(songs.getPath() + ";" + songs.getName() + ";" + songs.getPlaylistID());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readMemory() {
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader("Song-info")))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] blocks = line.split(";");
                String path = blocks[0];
                String name = blocks[1];
                int playlistID = Integer.parseInt(blocks[2]);
                availableSongs.add(new MySong(path, name, playlistID));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void processFolder(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (isPlayable(file)) {
                    availableSongs.add(new MySong(String.valueOf(file), stripPrefix(String.valueOf(file)), 1));
                }
            }
            SwingUtilities.invokeLater(() -> updateTable(availableSongs));
        }
    }

    public void filesChooser() {
        JFileChooser fc = new JFileChooser(".");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            processFolder(fc.getSelectedFile().getAbsolutePath());
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid directory");
        }
    }

    private void startProgressUpdate() {
        Thread progressUpdater = new Thread(() -> {
            while (true) {
                try {
                    Thread.onSpinWait();
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

    public String stripPrefix(String path) {
        File name = new File(path);
        return name.getName();
    }

    private boolean isPlayable(File file) {
        String fileName = file.getName();
        String[] supportedExtensions = {"wav", "ogg"};

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
    public String removeExtension(String file){
        int lastDotIndex = file.lastIndexOf(".");
        return file.substring(0, lastDotIndex);
    }

    private void playSongAt(int index) {
        if (index >= 0 && index < availableSongs.size()) {
            isLive = true;
            String filename = getFilePathFromSong((String) tableModel.getValueAt(index, 0), availableSongs);
            File selectedFile = new File(filename);
            try {
                int volume = volumeSlider.getValue();
                player.load(selectedFile, volume);
                songLength.setText(secToMin(MusicPlayer.getClipSize()));
                timeSlider.setMaximum(MusicPlayer.getClipSize());
                timeSlider.setPaintTicks(false);
                timeSlider.setPaintLabels(false);
                timeSlider.setMajorTickSpacing(0);
                timeSlider.setMinorTickSpacing(0);
                timeSlider.setSnapToTicks(false);
                songTable.setRowSelectionInterval(index, index);
                songTitle.setText(removeExtension((String) tableModel.getValueAt(index, 0)));
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                ex.getLocalizedMessage();
                return;
            }
            playStopButton.setText("❚❚");
            player.play();
        }
    }

    public void moveForward(boolean right) {
        int selectedRow = songTable.getSelectedRow();
        int rowCount = songTable.getRowCount();
        if (selectedRow != -1) {

        if (right) {
            stopCurrentSong();
            if (selectedRow < rowCount - 1) {
                playSongAt(selectedRow + 1);
            } else {
                playSongAt(0);
            }
        } else {
            if (selectedRow > 0) {
                stopCurrentSong();
                playSongAt(selectedRow-1);
            } else if(selectedRow==0) {
                stopCurrentSong();
                playSongAt(rowCount);
            }
        }}
    }
    private void stopCurrentSong() {
        if (MusicPlayer.isPlaying()) {
            player.stop();
            playStopButton.setText("►");
        }
    }

    private void clearTable() {
        availableSongs.clear();
        updateTable(availableSongs);
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

    public boolean isToLoop() {
        return toLoop;
    }

    public void setToLoop(boolean toLoop) {
        this.toLoop = toLoop;
    }

    public void load(File audioFile, int volume) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.addLineListener(event -> {
            if (!clip.isRunning()) {
                if (event.getType() == LineEvent.Type.STOP) {
                    if (toLoop) {
                        System.out.println("End of song triggered");
                        clip.setMicrosecondPosition(0);
                        clip.start();
                    } else {
                        playing = false;
                        clip.setMicrosecondPosition(0);
                    }
                }
            }
        });
        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        setVolume(volume);
        clipSize = (long) (audioStream.getFrameLength() / audioStream.getFormat().getFrameRate());
        pausedTime = 0;
    }

    public void play() {
        if (clip != null) {
            clip.setMicrosecondPosition(pausedTime);
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
            if (!playing){
                pausedTime = targetTime;
            }
        }
    }
}

class MySong {
    private String path;
    private String name;
    private int playlistID;

    public MySong(String path, String name, int playlistID) {
        this.path = path;
        this.name = name;
        this.playlistID = playlistID;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public int getPlaylistID() {
        return playlistID;
    }
}

