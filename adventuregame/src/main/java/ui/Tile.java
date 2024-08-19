package ui;
import javax.swing.*;
import java.awt.*;

public class Tile extends JPanel {
    private JLabel baseLabel;
    private JLabel overlayLabel;
    private JLayeredPane layeredPane;

    public Tile() {
        setLayout(new BorderLayout());
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(70, 70));

        baseLabel = new JLabel();
        baseLabel.setBounds(0, 0, 70, 70);
        layeredPane.add(baseLabel, JLayeredPane.DEFAULT_LAYER);

        overlayLabel = new JLabel();
        overlayLabel.setBounds(0, 0, 64, 64);
        layeredPane.add(overlayLabel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);
    }

    public void setBaseImage(String imagePath) {
        baseLabel.setIcon(new ImageIcon(getClass().getResource(imagePath)));
    }

    public void setOverlayImage(String imagePath) {
        overlayLabel.setIcon(new ImageIcon(getClass().getResource(imagePath)));
    }

    public void clearBaseImage() {
        baseLabel.setIcon(null);
    }
    public void clearOverlayImage() {
        overlayLabel.setIcon(null);
    }

}
