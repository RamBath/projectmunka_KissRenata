package logics;

import java.util.Random;

public class MapLogic {
    private static final int MAP_SIZE = 5;
    private static final Random RANDOM = new Random();

    public static String[][] generateInitialMap() {
        String[][] mapData = new String[MAP_SIZE][MAP_SIZE];
        int centerX = MAP_SIZE / 2;
        int centerY = MAP_SIZE / 2;
        mapData[centerX][centerY] = "00200201"; // Player starting tile

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
        TileType specialTile = generateSpecialTile(10, 5); // 10% for chest, 5% for monster
        if (specialTile != null) {
            String coordX = String.format("%03d", x);
            String coordY = String.format("%03d", y);
            return coordX + coordY + specialTile.getCode();
        }

        TileType newTile = getRandomMatchingTile(mapData, x, y);
        String coordX = String.format("%03d", x);
        String coordY = String.format("%03d", y);
        return coordX + coordY + newTile.getCode();
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
        boolean[] constraints = getConstraints(mapData, x, y);
        TileType selectedTile = null;
        boolean tileNotSelected= true;
        while (tileNotSelected){
            Random rand = new Random();
            int test=rand.nextInt(15) + 1;
            TileType tile = TileType.getByCode(String.format("%02d", rand.nextInt(15) + 1));

            boolean[] directions = tile.getDirections();
            if ((constraints[0] == directions[0] || !constraints[0]) &&
                (constraints[1] == directions[1] || !constraints[1]) &&
                (constraints[2] == directions[2] || !constraints[2]) &&
                (constraints[3] == directions[3] || !constraints[3])) {

                selectedTile = tile;
                tileNotSelected= false;
                break;
            }
        }

        return selectedTile;
    }

    private static boolean[] getConstraints(String[][] mapData, int x, int y) {
        boolean[] constraints = new boolean[]{false, false, false, false}; // Default: all directions allowed L, D, R, U

        if (x > 0 && mapData[x - 1][y] != null) { // Left neighbor
            constraints[2] = true ; // Must match right direction of left neighbor
        }
        if (y > 0 && mapData[x][y - 1] != null) { // Top neighbor
            constraints[3] = true; // Must match bottom direction of top neighbor
        }
        if (x < mapData.length - 1 && mapData[x + 1][y] != null) { // Right neighbor
            constraints[1] = true; // Must match left direction of right neighbor
        }
        if (y < mapData[0].length - 1 && mapData[x][y + 1] != null) { // Bottom neighbor
            constraints[1] = true; // Must match top direction of bottom neighbor
        }

        return constraints;
    }

    private static TileType generateSpecialTile(int chestProbability, int monsterProbability) {
        int chance = RANDOM.nextInt(100);
        if (chance < chestProbability) {
            return TileType.TYPE_17; //CHEST
        } else if (chance < chestProbability + monsterProbability) {
            return TileType.TYPE_16; //MONSTER
        }
        return null; // No special tile
    }
}