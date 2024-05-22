import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
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
    private final DefaultTableModel tableModel;
    private MusicPlayer player = new MusicPlayer();
    private final List<MySong> availableSongs = new ArrayList<>();

    public MusicPlayerForm() {
        setContentPane(panel);
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Music Player");

        JMenuBar JMenuBar = new JMenuBar();
        setJMenuBar(JMenuBar);

        JMenuItem AddMenu = new JMenuItem("Add folder");
        JMenuBar.add(AddMenu);

        JMenuItem DeleteMenu = new JMenuItem("Delete all");
        JMenuBar.add(DeleteMenu);

        startProgressUpdate();
        songTable.getTableHeader().setReorderingAllowed(false);
        AddMenu.addActionListener(e -> filesChooser());
        DeleteMenu.addActionListener(e -> clearTable());
        loopButton.setForeground(Color.LIGHT_GRAY);
        loopButton.addActionListener(e -> toggleLoop());
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
            player.setVolume(volume);
        });
        timeSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                player.setProgress(timeSlider.getValue() * 1000000L);
            }
        });
        songTable.getSelectionModel().addListSelectionListener(e -> playSelectedSong());
        tableModel = new DefaultTableModel(new String[]{"File Name"}, 0);
        songTable.setModel(tableModel);
        readMemory();
        updateTable(availableSongs);
    }

    private void toggleLoop() {
        if (!player.isToLoop()) {
            player.setToLoop(true);
            loopButton.setForeground(Color.BLACK);
        } else {
            player.setToLoop(false);
            loopButton.setForeground(Color.LIGHT_GRAY);
        }
    }

    public void updateIcons() {
        if (MusicPlayer.isPlaying()) {
            player.stop();
            playStopButton.setText("►");
        } else {
            player.play();
            playStopButton.setText("❚❚");
        }
    }

    public String getFilePathFromSong(String name, List<MySong> list) {
        for (MySong song : list) {
            if (name.equals(song.name())) {
                return song.path();
            }
        }
        return "";
    }

    public void writeToMemory() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("Song-info")))) {
            for (MySong song : availableSongs) {
                writer.println(song.path() + ";" + song.name() + ";" + song.playlistID());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to Song-info file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void readMemory() {
        availableSongs.clear();
        File file = new File("Song-info");
        if (!file.exists()) {
            return;
        }
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(file)))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] blocks = line.split(";");
                if (blocks.length == 3) {
                    String path = blocks[0];
                    String name = blocks[1];
                    int playlistID = Integer.parseInt(blocks[2]);
                    availableSongs.add(new MySong(path, name, playlistID));
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Song-info file not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error parsing Song-info file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void processFolder(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (isPlayable(file)) {
                    availableSongs.add(new MySong(file.getAbsolutePath(), file.getName(), 1));
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
                    Thread.sleep(1000);
                    if (MusicPlayer.isPlaying()) {
                        long progress = player.getProgress();
                        SwingUtilities.invokeLater(() -> {
                            currTime.setText(secToMin(progress));
                            timeSlider.setValue((int) progress);
                        });
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        progressUpdater.setDaemon(true);
        progressUpdater.start();
    }

    private boolean isPlayable(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".wav") || fileName.endsWith(".ogg");
    }

    private void updateTable(List<MySong> data) {
        tableModel.setRowCount(0);
        for (MySong datum : data) {
            tableModel.addRow(new Object[]{datum.name()});
        }
    }

    public void moveForward(boolean right) {
        int index = songTable.getSelectedRow();
        int rowCount = songTable.getRowCount();
        if (right) {
            if (index < rowCount - 1) {
                songTable.setRowSelectionInterval(index + 1, index + 1);
            } else {
                songTable.setRowSelectionInterval(0, 0);
            }
        } else {
            if (index > 0) {
                songTable.setRowSelectionInterval(index - 1, index - 1);
            } else {
                songTable.setRowSelectionInterval(rowCount - 1, rowCount - 1);
            }
        }
        playSelectedSong();
    }

    private void clearTable() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Are you sure?",
                "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // Process the user's response
        if (response == JOptionPane.YES_OPTION) {
            availableSongs.clear();
            updateTable(availableSongs);
        } else if (response == JOptionPane.NO_OPTION) {

        }

    }

    public static String secToMin(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public void playSelectedSong() {
        int selectedRow = songTable.getSelectedRow();
        if (selectedRow != -1) {
            String filename = getFilePathFromSong((String) tableModel.getValueAt(selectedRow, 0), availableSongs);
            File selectedFile = new File(filename);
            try {
                player.stop();
                int volume = volumeSlider.getValue();
                player.load(selectedFile, volume);
                songTitle.setText(removeExtension((String) tableModel.getValueAt(selectedRow, 0)));
                player.play();
                songLength.setText(secToMin(MusicPlayer.getClipSize()));
                playStopButton.setText("❚❚");
                timeSlider.setMaximum(MusicPlayer.getClipSize());
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                JOptionPane.showMessageDialog(this, "Problem with playing a selected song: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public String removeExtension(String file) {
        int lastDotIndex = file.lastIndexOf(".");
        return lastDotIndex == -1 ? file : file.substring(0, lastDotIndex);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MusicPlayerForm m = new MusicPlayerForm();
            m.setVisible(true);
        });
    }
}

class MusicPlayer {
    private Clip clip;
    private static boolean playing = false;
    private FloatControl volumeControl;
    private static long clipSize;
    private long pausedTime;
    private boolean toLoop;
    private boolean userStopped;

    public boolean isToLoop() {
        return toLoop;
    }

    public void setToLoop(boolean toLoop) {
        this.toLoop = toLoop;
    }

    public void load(File audioFile, int volume) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (clip != null && clip.isOpen()) {
            clip.close();
        }
        AudioInputStream audioStream = null;
        try {
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    if (!userStopped && toLoop) {
                        clip.setMicrosecondPosition(0);
                        clip.start();
                    } else {
                        playing = false;
                        clip.setMicrosecondPosition(0);
                    }
                }
            });
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume(volume);
            clipSize = (long) (audioStream.getFrameLength() / audioStream.getFormat().getFrameRate());
            pausedTime = 0;
            userStopped = false;
        } finally {
            if (audioStream != null) {
                audioStream.close();
            }
        }
    }

    public void play() {
        if (clip != null) {
            clip.setMicrosecondPosition(pausedTime);
            clip.start();
            playing = true;
            userStopped = false;
        }
    }

    public void stop() {
        if (clip != null) {
            pausedTime = clip.getMicrosecondPosition();
            clip.stop();
            playing = false;
            userStopped = true;
        }
    }

    public static boolean isPlaying() {
        return playing;
    }

    public void setVolume(int volume) {
        if (volumeControl != null) {
            float gain = (float) (20 * Math.log10(volume / 100.0) - 20f);
            volumeControl.setValue(gain);
        }
    }

    public long getProgress() {
        return clip != null ? clip.getMicrosecondPosition() / 1_000_000 : pausedTime / 1_000_000;
    }

    public static int getClipSize() {
        return (int) clipSize;
    }

    public void setProgress(long targetTime) {
        if (clip != null) {
            clip.setMicrosecondPosition(targetTime);
            if (!playing) {
                pausedTime = targetTime;
            }
        }
    }
}

record MySong(String path, String name, int playlistID) {
}
