import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MusicPlayerForm extends JFrame {
    private /*static*/ JButton playStopButton;
    private JSlider volumeSlider;
    private JPanel panel;
    private JTable songTable;
    private JSlider timeSlider;
    private JMenuBar mBar = new JMenuBar();
    private JMenuItem menu0 = new JMenuItem("Add folder");
    //private JMenuItem mItem = new JMenuItem("");
    private DefaultTableModel tableModel;
    private final MusicPlayer player;
    private boolean isAdjustingSlider;
    private JFileChooser fc = new JFileChooser(".");
    private List<String> playableFiles = new ArrayList<>();

    public MusicPlayerForm() {
        setContentPane(panel);
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Music Player");
        setJMenuBar(mBar);
        mBar.add(menu0);
        startProgressUpdate();
        menu0.addActionListener(e -> {
            addFolder();
        });

        player = new MusicPlayer();


        playStopButton.addActionListener(e -> {
            if (MusicPlayer.isPlaying()) {
                player.stop();
                playStopButton.setText("Play");
            } else {
                player.play();
                playStopButton.setText("Stop");
            }
        });

        volumeSlider.addChangeListener(e -> {
            int volume = volumeSlider.getValue();
            player.setVolume(volume);
        });
        timeSlider.setValue(0);
        timeSlider.setMajorTickSpacing(MusicPlayer.getClipSize());
        timeSlider.setMinorTickSpacing(1);
        timeSlider.setPaintTicks(true);
        timeSlider.setPaintLabels(true);

        timeSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                player.setProgress(timeSlider.getValue()*1000000L);
            }
        });
        songTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = songTable.getSelectedRow();
            if (selectedRow != -1) {
                String filename = (String) tableModel.getValueAt(selectedRow, 0);
                File selectedFile = new File(".", filename);
                try {
                    player.stop();
                    int volume = volumeSlider.getValue();
                    player.load(selectedFile, volume);
                    player.play();
                    playStopButton.setText("Stop");
                    timeSlider.setMaximum(MusicPlayer.getClipSize());
                    timeSlider.setPaintTicks(false);
                    timeSlider.setPaintLabels(false);
                    timeSlider.setMajorTickSpacing(0);
                    timeSlider.setMinorTickSpacing(0);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
        });

        tableModel = new DefaultTableModel(new String[]{"File Name"}, 0);
        songTable.setModel(tableModel);

        List<String> playableFiles = findPlayableFiles(new File("."));
        updateTable(playableFiles);
    }
    public void addFolder(){
        String userInput = JOptionPane.showInputDialog(this, "Folder path:");
        if (userInput != null && !userInput.isEmpty()) {
            if (new File(userInput).isDirectory())
                processFolder(userInput);

        }
        else JOptionPane.showMessageDialog(this, "Not a valid directory");

    }
    public void processFolder(String path){
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
            if (isPlayable(file)){
                playableFiles.add(String.valueOf(file));

            }
            }
            updateTable(playableFiles);
        }
    }
    private void startProgressUpdate() {
        Thread progressUpdater = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (!isAdjustingSlider && MusicPlayer.isPlaying()) {
                        long progress = player.getProgress();
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
    public String StripPrefix(String path){
        File name = new File(path);
        return name.getName();
    }

    private List<String> findPlayableFiles(File folder) {

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

    private void updateTable(List<String> data) {
        tableModel.setRowCount(0);
        for (String datum : data) {
            tableModel.addRow(new Object[]{datum});
        }
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

    public void load(File audioFile, int volume) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        //final CountDownLatch playingFinished = new CountDownLatch(1);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                //MusicPlayerForm.setPlayStopButton("play");   when the song ends it doesn't register, doesn't change the text
                playing = false;
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
            clip.stop();
            pausedTime = clip.getMicrosecondPosition();
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
}