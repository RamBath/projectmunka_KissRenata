package ui;

import logics.ActionType;
import logics.Chests;
import logics.GameLogic;
import logics.MapLogic;
import logics.Monster;
import logics.TileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

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
    private GameLogic gameLogic;
    private GameUI gameUI;

    public Map(JPanel parentPanel, JTextArea infoPanel, JButton homeButton, GameLogic gameLogic, GameUI gameUI) {
        this.mapContainer = new JPanel(new GridLayout(MAP_SIZE, MAP_SIZE,0, 0));
        this.infoPanel = infoPanel;
        this.homeButton=homeButton;
        this.tiles = new Tile[MAP_SIZE][MAP_SIZE];       
        this.playerX = 50;
        this.playerY = 50;
        this.mapDataFull = MapLogic.generateInitialMap();
        this.gameLogic=gameLogic;
        this.gameUI=gameUI;
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
                checkForEncounters(mapDataFull,playerX, playerY);
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
                            tiles[i][j].setOverlayImage("/images/" +actionType.getImage()); 
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
    private void checkForEncounters(String[][] mapDataFull, int playerX,int playerY) {
        Monster monster = MapLogic.getMonsterAt(playerX, playerY);
        Chests chest = MapLogic.getChestAt(playerX, playerY);
        if (monster != null) {
            handleMonsterEncounter(monster);
            if (!monster.getIsAlive()){
                System.out.println(mapDataFull[playerX][playerY]);
                String newString= mapDataFull[playerX][playerY].substring(0, 8) +"06";
                mapDataFull[playerX][playerY]=newString;
                System.out.println(mapDataFull[playerX][playerY]);
            }
        } else if (chest != null){
            handleChestEncounter(chest);
            System.out.println(mapDataFull[playerX][playerY]);
            String newString= mapDataFull[playerX][playerY].substring(0, 8) +"08";
            mapDataFull[playerX][playerY]=newString;
            System.out.println(mapDataFull[playerX][playerY]);
        }
    }
    private void handleMonsterEncounter(Monster monster) {
        // Display initial encounter message
        if (monster.getIsAlive()){
        JOptionPane.showMessageDialog(null, "Encountered a monster with " + monster.getHealth() + " health!");
    
        // Combat loop
        while (true) {
            // Show options: Fight or Flee
            String[] options = {"Fight", "Flee"};
            int choice = JOptionPane.showOptionDialog(null, "What do you want to do?", "Monster Encounter",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    
            if (choice == 0) { // Player chooses to fight
                // Player attacks
                if (getEvadedAttack(monster.getAgility())){
                    JOptionPane.showMessageDialog(null,"The monster evaded the attack!");
                } else{
                int playerDamage = gameLogic.getCharacter().getAttack();
                monster.setHealth(monster.getHealth() - playerDamage);
                JOptionPane.showMessageDialog(null, "You attacked the monster and dealt " + playerDamage + " damage. Monster's health: " + monster.getHealth());
                }
                // Check if the monster is defeated
                if (monster.getHealth() <= 0) {
                    JOptionPane.showMessageDialog(null, "You have defeated the monster! You got " + monster.getGoldDrop() + " gold!");
                    monster.setIsAlive(false); 
                    gameLogic.addMonsterKill(); // Update stats
                    monster.setType("06"); //thumbstone
                    gameLogic.getCharacter().setGold(gameLogic.getCharacter().getGold()+monster.getGoldDrop());
                    return;
                }
    
                // Monster attacks
                int monsterDamage = monster.getAttack();
                if (getEvadedAttack(gameLogic.getCharacter().getAgility())){
                    JOptionPane.showMessageDialog(null,"You evaded the attack!");
                } else
                {
                    gameLogic.getCharacter().setHealth(gameLogic.getCharacter().getHealth() - (monsterDamage-gameLogic.getCharacter().getDefense()));
                    JOptionPane.showMessageDialog(null, "The monster attacked you and dealt " +(monsterDamage-gameLogic.getCharacter().getDefense()) + " damage. Your health: " + gameLogic.getCharacter().getHealth());
                }

                // Check if the player is defeated
                if (gameLogic.getCharacter().getHealth() <= 0) {
                    JOptionPane.showMessageDialog(null, "You have been defeated by the monster...");
                    itsGameOver();
                    return;
                }
            } else { // Player chooses to flee
                JOptionPane.showMessageDialog(null, "You fled from the monster!");
                return;
            }
        }
    }

    
    }
    private Boolean getEvadedAttack(int agility){
        Boolean Evaded=false;
        Random rand = new Random();
        int evadeInt=rand.nextInt(100) + 1;
        if (evadeInt<=agility) {Evaded=true;}
        return Evaded;
    }
    
    public void handleChestEncounter(Chests chest) {
        // Implement the logic for handling a monster encounter
        // e.g., combat mechanics, updating player and monster stats, etc.
        if (chest.isActive()){
            JOptionPane.showMessageDialog(null, "Found a chest with " + chest.getGoldDrop() + " gold!");
            gameLogic.findChest(chest.getGoldDrop());
            chest.setActive(false);
        }

    } 


    public void setGameUI(GameUI gameUI) {
        this.gameUI = gameUI;
    }

    public void itsGameOver() {
        if (gameUI != null) {
            gameUI.gameOver();
        }
    }

    public void resetMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                this.mapData[i][j] = null;                
            }
        }
        for (int i = 0; i < MAP_SIZE_FULL; i++) {
            for (int j = 0; j < MAP_SIZE_FULL; j++) {
                this.mapDataFull[i][j] = null;                
            }
        }
        this.playerX = 50;
        this.playerY = 50;
        this.mapDataFull = MapLogic.generateInitialMap();
        fullMapToSubMap();
        updateMapDisplay(mapData, playerX, playerY);
    }

    
    
    
    

}
