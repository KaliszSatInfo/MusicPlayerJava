import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerForm extends JFrame {
    private JButton playStopButton;
    private JSlider volumeSlider;
    private JPanel panel;
    private JTable songTable;
    private DefaultTableModel tableModel;
    private final MusicPlayer player;

    public MusicPlayerForm() {
        setContentPane(panel);
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Music Player");

        player = new MusicPlayer();

        playStopButton.addActionListener(_ -> {
            if (player.isPlaying()) {
                player.stop();
                playStopButton.setText("Play");
            } else {
                player.play();
                playStopButton.setText("Stop");
            }
        });

        volumeSlider.addChangeListener(_ -> {
            int volume = volumeSlider.getValue();
            player.setVolume(volume);
        });

        songTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = songTable.getSelectedRow();
            if (selectedRow != -1) {
                String filename = (String) tableModel.getValueAt(selectedRow, 0);
                File selectedFile = new File(".", filename);
                try {
                    player.stop();
                    player.load(selectedFile);
                    player.play();
                    playStopButton.setText("Stop");
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    ex.getLocalizedMessage();
                }
            }
        });

        tableModel = new DefaultTableModel(new String[]{"File Name"}, 0);
        songTable.setModel(tableModel);

        List<String> playableFiles = findPlayableFiles(new File("."));
        updateTable(playableFiles);
    }

    private List<String> findPlayableFiles(File folder) {
        List<String> playableFiles = new ArrayList<>();
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

    private boolean isPlayable(File file) {
        String fileName = file.getName();
        String[] supportedExtensions = {"mp3", "wav", "ogg"};

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
    private boolean playing = false;
    private FloatControl volumeControl;

    public void load(File audioFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
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
            playing = false;
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setVolume(int volume) {
        float gain = (float) (Math.log10(volume / 100.0) * 20) - 20f;
        volumeControl.setValue(gain);
        //fix this so it doesn't tear of your ears when you start playing
    }
}
