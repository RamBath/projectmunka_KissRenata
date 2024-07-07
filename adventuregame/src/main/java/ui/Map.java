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
    private JPanel parentPanel;
    private JTextArea infoPanel;
    private JLabel[][] tiles;
    private String[][] mapData;
    private int playerX, playerY;

    public Map(JPanel parentPanel, JTextArea infoPanel) {
        this.parentPanel = parentPanel;
        this.infoPanel = infoPanel;
        this.tiles = new JLabel[MAP_SIZE][MAP_SIZE];
        this.mapData = MapLogic.generateInitialMap();
        this.playerX = MAP_SIZE / 2;
        this.playerY = MAP_SIZE / 2;
        initializeMapPanel();
        updateMapDisplay();
    }

    private void initializeMapPanel() {
        parentPanel.setLayout(new GridLayout(MAP_SIZE, MAP_SIZE));
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                tiles[i][j] = new JLabel();
                tiles[i][j].setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                tiles[i][j].setOpaque(true);
                tiles[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                parentPanel.add(tiles[i][j]);
            }
        }

        parentPanel.addKeyListener(new KeyAdapter() {
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

        parentPanel.setFocusable(true);
        parentPanel.requestFocusInWindow();
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
                        tiles[i][j].setIcon(new ImageIcon(getClass().getResource("/resources/" + tileType.getImage())));
                    }
                } else {
                    tiles[i][j].setIcon(new ImageIcon(getClass().getResource("/resources/unknown.png")));
                }
            }
        }
        infoPanel.setText("Player position: (" + playerX + ", " + playerY + ")");
    }
    public void show() {
        updateMapDisplay();
    }
}
