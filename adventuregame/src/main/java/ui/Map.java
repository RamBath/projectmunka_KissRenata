package ui;

import logics.MapLogic;
import logics.TileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Map {
    private static final int TILE_SIZE = 70;
    private static final int MAP_SIZE = 5;
    private JPanel mapContainer;
    private JTextArea infoPanel;
    private JLabel[][] tiles;
    private String[][] mapData;
    private int playerX, playerY;

    public Map(JPanel parentPanel, JTextArea infoPanel) {
        this.mapContainer = new JPanel(new GridLayout(MAP_SIZE, MAP_SIZE));
        this.infoPanel = infoPanel;
        this.tiles = new JLabel[MAP_SIZE][MAP_SIZE];
        this.mapData = MapLogic.generateInitialMap();
        this.playerX = MAP_SIZE / 2;
        this.playerY = MAP_SIZE / 2;

        parentPanel.setLayout(new BorderLayout());
        parentPanel.add(mapContainer, BorderLayout.CENTER);

        initializeMapPanel();
        updateMapDisplay();
    }

    private void initializeMapPanel() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                tiles[i][j] = new JLabel();
                tiles[i][j].setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                tiles[i][j].setOpaque(true);
                tiles[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                mapContainer.add(tiles[i][j]);
            }
        }

        mapContainer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        movePlayer(0, -1);  // Should map to moving up
                        break;
                    case KeyEvent.VK_RIGHT:
                        movePlayer(0, 1);   // Should map to moving down
                        break;
                    case KeyEvent.VK_UP:
                        movePlayer(-1, 0);  // Should map to moving left
                        break;
                    case KeyEvent.VK_DOWN:
                        movePlayer(1, 0);   // Should map to moving right
                        break;
                }
            }
        });
        

        mapContainer.setFocusable(true);
        mapContainer.requestFocusInWindow();
    }

    private void movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;
        if (newX >= 0 && newX < MAP_SIZE && newY >= 0 && newY < MAP_SIZE) {
            playerX = newX;
            playerY = newY;
            generateSurroundingTiles(playerX, playerY);
            updateMapDisplay();
        }
    }

    private void generateSurroundingTiles(int x, int y) {
        if (x > 0 && mapData[x - 1][y] == null) {
            mapData[x - 1][y] = MapLogic.generateTileData(mapData, x - 1, y);
        }
        if (x < MAP_SIZE - 1 && mapData[x + 1][y] == null) {
            mapData[x + 1][y] = MapLogic.generateTileData(mapData, x + 1, y);
        }
        if (y > 0 && mapData[x][y - 1] == null) {
            mapData[x][y - 1] = MapLogic.generateTileData(mapData, x, y - 1);
        }
        if (y < MAP_SIZE - 1 && mapData[x][y + 1] == null) {
            mapData[x][y + 1] = MapLogic.generateTileData(mapData, x, y + 1);
        }
    }

    private void updateMapDisplay() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (mapData[i][j] != null) {
                    String tileCode = mapData[i][j].substring(6, 8);
                    TileType tileType = TileType.getByCode(tileCode);
                    if (tileType != null) {
                        tiles[i][j].setIcon(new ImageIcon(getClass().getResource("/images/" + tileType.getImage())));
                    }
                } else {
                    tiles[i][j].setIcon(new ImageIcon(getClass().getResource("/images/unknown.png")));
                }
            }
        }
        infoPanel.setText("Player position: (" + playerX + ", " + playerY + ")");
    }

    public void show() {
        mapContainer.setVisible(true);
        mapContainer.requestFocusInWindow();
    }
}
