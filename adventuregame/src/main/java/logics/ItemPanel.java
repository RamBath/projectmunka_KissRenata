package logics;

import javax.swing.*;
import java.awt.*;
import java.net.URL;


public class ItemPanel extends JPanel {
    public ItemPanel(Item item) {
        setLayout(new BorderLayout());

        // Load image using the same method as in Tile
        String imagePath = "/images/"+item.getImagePath(); 
        URL imageUrl = getClass().getResource(imagePath);

        if (imageUrl != null) {
            JLabel imageLabel = new JLabel(new ImageIcon(imageUrl));
            add(imageLabel, BorderLayout.EAST);
        } else {
            JLabel imageLabel = new JLabel("Image not found");
            add(imageLabel, BorderLayout.EAST);
        }

       
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setText(item.getName() + "\n" + item.getDescription() + "\nPrice: " + item.getPrice() + " gold");
        add(descriptionArea, BorderLayout.CENTER);

        
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
    }
}
