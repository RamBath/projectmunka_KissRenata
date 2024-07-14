package ui;

import logics.ActionType;
import logics.MapLogic;
import logics.TileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Map {
    private static final int TILE_SIZE = 70;
    private static final int MAP_SIZE = 5;
    private static final int MAP_SIZE_FULL = 100;
    private JPanel mapContainer;
    private JTextArea infoPanel;
    private JButton homeButton;
    private Tile[][] tiles;
    private String[][] mapDataFull;
    private String[][] mapData;
    private int playerX, playerY;
    

    public Map(JPanel parentPanel, JTextArea infoPanel, JButton homeButton) {
        this.mapContainer = new JPanel(new GridLayout(MAP_SIZE, MAP_SIZE,0, 0));
        this.infoPanel = infoPanel;
        this.homeButton=homeButton;
        this.tiles = new Tile[MAP_SIZE][MAP_SIZE];       
        this.playerX = 50;
        this.playerY = 50;
        this.mapDataFull = MapLogic.generateInitialMap();
        fullMapToSubMap();

        //parentPanel.add(infoPanel, BorderLayout.SOUTH);

        parentPanel.add(mapContainer, BorderLayout.CENTER);
        parentPanel.add(infoPanel, BorderLayout.NORTH);
        parentPanel.add(homeButton, BorderLayout.SOUTH);
        
        initializeMapPanel();
        updateMapDisplay(mapData, playerX, playerY);
    }

    private void initializeMapPanel() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                tiles[i][j] = new Tile();
                tiles[i][j].setBorder(BorderFactory.createEmptyBorder()); 
                tiles[i][j].setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                tiles[i][j].setOpaque(true);
                //tiles[i][j].setHorizontalAlignment(SwingConstants.CENTER);
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
        boolean isValid=false;
        String tileCode = mapDataFull[playerX][playerY].substring(6, 8);
        TileType tile = TileType.getByCode(tileCode);
        boolean[] directions = tile.getDirections(); // L, D, R, U
        int newX = playerX + dx;
        int newY = playerY + dy;
        
        if (dx==-1){//up step
            if (directions[3]==true){isValid=true;}
        }
        else if (dx==1){//down step
            if (directions[1]==true){isValid=true;}
        }
        else if (dy==-1){//left step
            if (directions[0]==true){isValid=true;}
        }
        else if (dy==1){//right step
            if (directions[2]==true){isValid=true;}
        }
        if (isValid){
            if (newX >= 0 && newX < MAP_SIZE_FULL && newY >= 0 && newY < MAP_SIZE_FULL) {
                playerX = newX;
                playerY = newY;
                generateSurroundingTiles(playerX, playerY);
                fullMapToSubMap();
                updateMapDisplay(mapData, playerX, playerY);
            } else  {infoPanel.setText("Out of boundaries!");}
        } 
        else{
            infoPanel.setText("You can not step there!");
        }
       
    }

    private void fullMapToSubMap(){
        this.mapData = new String[5][5];
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) 
            {
                if (mapDataFull[playerX+i][playerY+j]!=null){
                    this.mapData[i+2][j+2]=mapDataFull[playerX+i][playerY+j];
                }
                else {this.mapData[i+2][j+2]=null;}
            }         
        }

    }

    private void generateSurroundingTiles(int x, int y) {
        if (x > 0 && mapDataFull[x - 1][y] == null) {
            mapDataFull[x - 1][y] = MapLogic.generateTileData(mapDataFull, x - 1, y);
        }
        if (x < MAP_SIZE_FULL - 1 && mapDataFull[x + 1][y] == null) {
            mapDataFull[x + 1][y] = MapLogic.generateTileData(mapDataFull, x + 1, y);
        }
        if (y > 0 && mapDataFull[x][y - 1] == null) {
            mapDataFull[x][y - 1] = MapLogic.generateTileData(mapDataFull, x, y - 1);
        }
        if (y < MAP_SIZE_FULL - 1 && mapDataFull[x][y + 1] == null) {
            mapDataFull[x][y + 1] = MapLogic.generateTileData(mapDataFull, x, y + 1);
        }
    }

    public void updateMapDisplay(String[][] mapData, int playerX, int playerY) {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                tiles[i][j].clearOverlayImage();
                tiles[i][j].clearBaseImage();
                if (mapData[i][j] != null) {
                    String tileCode = mapData[i][j].substring(6, 8);
                    String actionCode = mapData[i][j].substring(8, 10);
                    TileType tileType = TileType.getByCode(tileCode);
                    ActionType actionType = ActionType.getByCode(actionCode);
                    if (tileType != null) {
                        tiles[i][j].setBaseImage("/images/" + tileType.getImage());

                        if (!actionCode.equals("00")) {
                            tiles[i][j].setOverlayImage("/images/" +actionType.getImage()); //put player
                        }
                        if (i==2 &&j==2){
                            tiles[i][j].setOverlayImage("/images/player.png" ); //put player
                        }

                    }
                } else {
                    tiles[i][j].setBaseImage("/images/unknown.png");
                }
            }
        }
        infoPanel.setText("Player position: (" + playerX + ", " + playerY + ")");
    }

    public void show() {
        mapContainer.setVisible(true);
        mapContainer.requestFocusInWindow();
    }
    public String[][] getMap() {
        return mapDataFull;
    }
    public void loadMap() {
        // this.mapDataFull;
    }

}
