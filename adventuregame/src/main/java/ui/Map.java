package ui;

import logics.MapLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class Map {
    private JPanel mapPanel;
    private JTextArea infoPanel;
    private JLabel[][] tiles;
    private String[][] mapData;
    private final int MAP_SIZE = 5;
    private final int TILE_SIZE = 200;
    private final String[] tileImages = {
        "tile01.png", "tile02.png", "tile03.png", "tile04.png", "tile05.png",
        "tile06.png", "tile07.png", "tile08.png", "tile09.png", "tile10.png",
        "tile11.png", "tile12.png", "tile13.png", "tile14.png", "tile15.png",
        "tile16.png", "tile17.png"
    };
    private int playerX;
    private int playerY;

    public Map(JPanel parentPanel, JTextArea infoPanel) {
        this.infoPanel = infoPanel;
        initialize(parentPanel);
    }

    private void initialize(JPanel parentPanel) {
        mapPanel = new JPanel();
        mapPanel.setLayout(new GridLayout(MAP_SIZE, MAP_SIZE));
        parentPanel.add(mapPanel, BorderLayout.CENTER);

        tiles = new JLabel[MAP_SIZE][MAP_SIZE];
        mapData = MapLogic.generateInitialMap();
        playerX = MAP_SIZE / 2;
        playerY = MAP_SIZE / 2;

        // Initialize tiles and map data
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                tiles[i][j] = new JLabel();
                tiles[i][j].setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                tiles[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tiles[i][j].setVerticalAlignment(SwingConstants.CENTER);
                mapPanel.add(tiles[i][j]);
                updateTileImage(i, j);
            }
        }

        // Ensure the panel is focusable
        mapPanel.setFocusable(true);
        mapPanel.requestFocusInWindow();

        // Key listener for player movement
        mapPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        movePlayer(-1, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        movePlayer(1, 0);
                        break;
                    case KeyEvent.VK_UP:
                        movePlayer(0, -1);
                        break;
                    case KeyEvent.VK_DOWN:
                        movePlayer(0, 1);
                        break;
                }
            }
        });
    }

    private void movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;

        // Check map boundaries
        if (newX >= 0 && newX < MAP_SIZE && newY >= 0 && newY < MAP_SIZE) {
            playerX = newX;
            playerY = newY;
            MapLogic.updateMap(mapData, playerX, playerY);
            updateMapDisplay();
        }
    }

    private void updateTileImage(int x, int y) {
        String tileData = mapData[x][y];
        if (tileData != null) {
            String tileType = tileData.substring(6);
            int tileIndex = Integer.parseInt(tileType) - 1;
            String imagePath = "/images/" + tileImages[tileIndex];
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                tiles[x][y].setIcon(icon);
            } else {
                tiles[x][y].setIcon(null);
            }
            tiles[x][y].setText(tileData);
        } else {
            tiles[x][y].setIcon(null);
            tiles[x][y].setText("Unknown");
        }
    }

    private void updateMapDisplay() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                updateTileImage(i, j);
            }
        }
    }

    public void show() {
        mapPanel.setVisible(true);
        mapPanel.requestFocusInWindow();
    }
}
