import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class MusicPlaylist extends JDialog {
    private MusicPlayerGUI musicPlayerGUI;
    private ArrayList<String> songPaths;


    public MusicPlaylist(MusicPlayerGUI musicPlayerGUI){
        this.musicPlayerGUI = musicPlayerGUI;
        songPaths = new ArrayList<>();

        setTitle("Crear Lista de Reproduccion");
        setSize(400,400);
        setResizable(false);
        getContentPane().setBackground(MusicPlayerGUI.FRAME_COLOR);
        setLayout(null);
        setModal(true);
        setLocationRelativeTo(musicPlayerGUI);

        addDialogComponents();
    }

    private void addDialogComponents(){
        JPanel songContainer = new JPanel();
        songContainer.setLayout(new BoxLayout(songContainer, BoxLayout.Y_AXIS));
        songContainer.setBounds((int)(getWidth() * 0.025), 10, (int)(getWidth() * 0.90), (int) (getHeight() * 0.75));
        add(songContainer);

        JButton addSongButton = new JButton("Add");
        addSongButton.setBounds(60, (int) (getHeight() * 0.80), 100, 25);
        addSongButton.setFont(new Font("Dialog", Font.BOLD, 14));
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // open file explorer
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
                jFileChooser.setCurrentDirectory(new File("src/assets"));
                int result = jFileChooser.showOpenDialog(MusicPlaylist.this);

                File selectedFile = jFileChooser.getSelectedFile();
                if(result == JFileChooser.APPROVE_OPTION && selectedFile != null){
                    JLabel filePathLabel = new JLabel(selectedFile.getPath());
                    filePathLabel.setFont(new Font("Dialog", Font.BOLD, 12));
                    filePathLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    songPaths.add(filePathLabel.getText());

                    songContainer.add(filePathLabel);

                    songContainer.revalidate();
                }
            }
        });
        add(addSongButton);


        JButton savePlaylistButton = new JButton("Save");
        savePlaylistButton.setBounds(215, (int) (getHeight() * 0.80), 100, 25);
        savePlaylistButton.setFont(new Font("Dialog", Font.BOLD, 14));
        savePlaylistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setCurrentDirectory(new File("src/assets"));
                    int result = jFileChooser.showSaveDialog(MusicPlaylist.this);

                    if(result == JFileChooser.APPROVE_OPTION){

                        File selectedFile = jFileChooser.getSelectedFile();

                        if(!selectedFile.getName().substring(selectedFile.getName().length() - 4).equalsIgnoreCase(".txt")){
                            selectedFile = new File(selectedFile.getAbsoluteFile() + ".txt");
                        }

                        selectedFile.createNewFile();

                        FileWriter fileWriter = new FileWriter(selectedFile);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        for(String songPath : songPaths){
                            bufferedWriter.write(songPath + "\n");
                        }
                        bufferedWriter.close();

                        JOptionPane.showMessageDialog(MusicPlaylist.this, "Lista creada con Exito");

                        MusicPlaylist.this.dispose();
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        });
        add(savePlaylistButton);
    }
}
