import java.awt.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.*;
import javax.swing.*;

public class AnimacionVideo extends JPanel {

    private ImageIcon[] frames;
    private int indice = 0;

    public AnimacionVideo() {
        int totalFrames = 401; 
        frames = new ImageIcon[totalFrames];

        for (int i = 0; i < totalFrames; i++) {
            
            String ruta = String.format("/frames/frame_%03d.png", i + 1);
            java.net.URL imgURL = getClass().getResource(ruta);

            if (imgURL != null) {
                frames[i] = new ImageIcon(imgURL);
            } else {
                System.out.println("No se encontró: " + ruta);
            }
        }

        reproducirAudio();

        Timer timer = new Timer(33, e -> {
            indice = (indice + 1) % frames.length;
            repaint();
        });

        timer.start();
    }

    private void reproducirAudio() {
        new Thread(() -> {
            try {
                InputStream is = getClass().getResourceAsStream("/audio.wav");
                if (is != null) {
                    InputStream bufferedIn = new BufferedInputStream(is);
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                } else {
                    System.out.println("No se encontró el archivo /audio.wav en src.");
                }
            } catch (Exception e) {
                System.out.println("Error al reproducir audio: " + e.getMessage());
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (frames[indice] != null && frames[indice].getImage() != null) {
            g.drawImage(
                frames[indice].getImage(),
                0, 0, getWidth(), getHeight(), this
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame v = new JFrame("Animación 401 Frames");
            v.setSize(800, 600);
            v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            v.add(new AnimacionVideo());
            v.setLocationRelativeTo(null);
            v.setVisible(true);
        });
    }
}