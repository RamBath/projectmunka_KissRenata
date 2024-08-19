package logics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import repository.GameRepository;
import repository.ItemRepository;

public class MapLogic {
    private static final int MAP_SIZE = 100;
    private static final Random RANDOM = new Random();
    private static List<Monster> monsters = new ArrayList<>();
    private static List<Chests> chests = new ArrayList<>();
    private static int monsterLevel = 1; 
    private GameRepository gameRepository;


    public MapLogic() {
        this.gameRepository = new GameRepository();
    }

    public void loadFromDBMonstersAndChests(){
        monsters=gameRepository.getMonsters();
        chests=gameRepository.getChests();
    }

    public static String[][] generateInitialMap() {
        String[][] mapData = new String[MAP_SIZE][MAP_SIZE];
        int centerX = MAP_SIZE / 2;
        int centerY = MAP_SIZE / 2;
        mapData[centerX][centerY] = "0500500100"; // Player starting tile

        for (int i = -1; i <= 1; i++) {
           
            if (i == 0) continue; // Skip the center tile
            mapData[centerX + i][centerY] = generateTileData(mapData, centerX + i, centerY);
        }
        for (int j = -1; j <= 1; j++) {
            if (j == 0) continue; // Skip the center tile
            mapData[centerX][centerY + j] = generateTileData(mapData, centerX, centerY + j);
        }
        return mapData;
    }


    public static String generateTileData(String[][] mapData, int x, int y) {
        String specialTile = generateSpecialTile(x, y, 2, 10); // 2% for chest, 10% for monster
        if (specialTile != null) {
            TileType newTile = getRandomMatchingTile(mapData, x, y);
            String coordX = String.format("%03d", x);
            String coordY = String.format("%03d", y);
            return coordX + coordY + newTile.getCode()+specialTile;
        }

        TileType newTile = getRandomMatchingTile(mapData, x, y);
        String coordX = String.format("%03d", x);
        String coordY = String.format("%03d", y);
        return coordX + coordY + newTile.getCode()+"00";
    }

    public static void updateMap(String[][] mapData, int playerX, int playerY) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newX = playerX + i;
                int newY = playerY + j;
                if (newX >= 0 && newX < MAP_SIZE && newY >= 0 && newY < MAP_SIZE && mapData[newX][newY] == null) {
                    mapData[newX][newY] = generateTileData(mapData, newX, newY);
                }
            }
        }
    }
   

    private static TileType getRandomMatchingTile(String[][] mapData, int x, int y) {
        boolean[][] constraints = getConstraints(mapData, x, y);
        TileType selectedTile = null;
        boolean tileNotSelected= true;
        while (tileNotSelected){
            Random rand = new Random();
            int test=rand.nextInt(15) + 1;
            TileType tile = TileType.getByCode(String.format("%02d", rand.nextInt(15) + 1));

            boolean[] directions = tile.getDirections();
            if (( //constrains
                (constraints[0][0] == directions[0] || !constraints[0][0]) &&
                (constraints[0][1] == directions[1] || !constraints[0][1]) &&
                (constraints[0][2] == directions[2] || !constraints[0][2]) &&
                (constraints[0][3] == directions[3] || !constraints[0][3]))  
                &&
                (//blocks
                (constraints[1][0] != directions[0] || !constraints[1][0]) &&
                (constraints[1][1] != directions[1] || !constraints[1][1]) &&
                (constraints[1][2] != directions[2] || !constraints[1][2]) &&
                (constraints[1][3] != directions[3] || !constraints[1][3]))){

                selectedTile = tile;
                tileNotSelected= false;
                break;
            }
        }

        return selectedTile;
    }

    private static boolean[][] getConstraints(String[][] mapData, int x, int y) {
        boolean [] [] pair= new boolean[2][4];
        boolean[] constraints = new boolean[]{false, false, false, false}; // Default: all directions allowed L, D, R, U
        boolean[] blocks = new boolean[]{false, false, false, false};
        if (x > 0 && mapData[x - 1][y] != null) { // top neighbor
            String tileCode = mapData[x - 1][y].substring(6, 8);
            TileType tile = TileType.getByCode(tileCode);
            boolean[] directions = tile.getDirections();
            if (directions[1]==true) {
                constraints[3] = true ;            
            } else blocks[3]=true;
                       
        }
        if (y > 0 && mapData[x][y - 1] != null) { // left neighbor
            String tileCode = mapData[x][y - 1] .substring(6, 8);
            TileType tile = TileType.getByCode(tileCode);
            boolean[] directions = tile.getDirections();
            if (directions[2]==true) {
                constraints[0] = true ;            
            } else blocks[0]=true;
        }
        if (x < mapData.length - 1 && mapData[x + 1][y] != null) { // bottom neighbor
            String tileCode = mapData[x + 1][y] .substring(6, 8);
            TileType tile = TileType.getByCode(tileCode);
            boolean[] directions = tile.getDirections();
            if (directions[3]==true) {
                constraints[1] = true ;            
            } else blocks[1]=true;
        }
        if (y < mapData[0].length - 1 && mapData[x][y + 1] != null) { // right neighbor
            String tileCode = mapData[x][y + 1] .substring(6, 8);
            TileType tile = TileType.getByCode(tileCode);
            boolean[] directions = tile.getDirections();
            if (directions[0]==true) {
                constraints[2] = true ;            
            } else blocks[2]=true;
        }
        for (int i = 0; i < 4; i++) {
            pair[0][i]=constraints[i];
            pair[1][i]=blocks[i];
        }

        return pair;

    }

    private static String generateSpecialTile(int x, int y, int chestProbability, int monsterProbability) {
        int chance = RANDOM.nextInt(100);
        
        if (chance < chestProbability) {       
            generateNewChest(x, y);
            return "07";
        } else if (chance < chestProbability + monsterProbability) {
            generateNewMonster(x, y);
            String monsterTypeCode=getMonsterAt(x, y).getType();
            return monsterTypeCode;
        }
        return null; // No special tile
    }
     private static void generateNewMonster(int x, int y) {
        int health = 10 + monsterLevel * 5;
        int attack = 5 + monsterLevel * 2;
        int agility = (int) Math.round(1 + monsterLevel * 0.02);
        int goldDrop = 10 + monsterLevel * 3;
        int randomNum = RANDOM.nextInt(5) + 1;
        String monsterType =String.format("%02d", randomNum);
       

        Monster newMonster = new Monster(monsterType,health,attack,agility,goldDrop,x,y,true);
        monsters.add(newMonster);

        monsterLevel++;
    }
    private static void generateNewChest(int x, int y) {
        int goldDrop = 10 + monsterLevel * 2;     
        Chests newChests = new Chests(goldDrop,x,y,true);
        chests.add(newChests);
    }

    public static List<Monster> getMonsters() {
        return monsters;
    }
    public static List<Chests> getChests() {
        return chests;
    }


    public static Monster getMonsterAt(int x, int y) {
        for (Monster monster : monsters) {
            if (monster.getX() == x && monster.getY() == y) {
                return monster;
            }
        }
        return null;
    }
    public static Chests getChestAt(int x, int y) {
        for (Chests chest : chests) {
            if (chest.getX() == x && chest.getY() == y) {
                return chest;
            }
        }
        return null;
    }

    public void setMonsterList(String monsterType, String health , String attack, String agility, String goldDrop, String x, String y, String isA){
        
        Monster newMonster = new Monster(monsterType,Integer.parseInt(health),Integer.parseInt(attack),Integer.parseInt(agility),Integer.parseInt(goldDrop),Integer.parseInt(x),Integer.parseInt(y),Boolean.parseBoolean(isA));
        monsters.add(newMonster);
        monsterLevel++;
    }

    public void setChestList(String g, String x, String y, String isA){
        Chests newChest = new Chests(Integer.parseInt(g),Integer.parseInt(x),Integer.parseInt(y), Boolean.parseBoolean(isA));
        chests.add(newChest);
    }

    public void clearMonsters() {
        monsters.clear();
        monsterLevel = 1;
    }

  
    public void clearChests() {
        chests.clear();
    }

    public String getAllMonsters()
    {    
        String monstersAll = new String(); 

        for ( Monster monster : monsters){
            monstersAll= monstersAll + monster.toString() + "\n";
        }
        return  monstersAll;     
    }
    public String getAllChests()
    {    
        String chestsAll =  new String(); 

        for ( Chests chest : chests){
            chestsAll= chestsAll + chest.toString() + "\n";
        }
        return  chestsAll;     
    }

    public void refreshMonsters(String[][] mapData){


    }

    


    

    

    

    

}