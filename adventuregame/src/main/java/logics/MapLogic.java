package logics;

import java.util.Random;

public class MapLogic {
    private static final int MAP_SIZE = 5;
    private static final String[] TILE_TYPES = {
        "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"
    };
    private static final Random RANDOM = new Random();

    public static String[][] generateInitialMap() {
        String[][] mapData = new String[MAP_SIZE][MAP_SIZE];
        int centerX = MAP_SIZE / 2;
        int centerY = MAP_SIZE / 2;
        mapData[centerX][centerY] = "00000001"; // Player starting tile

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue; // Skip the center tile
                mapData[centerX + i][centerY + j] = generateTileData(centerX + i, centerY + j);
            }
        }
        return mapData;
    }

    public static String generateTileData(int x, int y) {
        String tileType = TILE_TYPES[RANDOM.nextInt(TILE_TYPES.length)];
        String coordX = String.format("%03d", x);
        String coordY = String.format("%03d", y);
        return coordX + coordY + tileType;
    }

    public static void updateMap(String[][] mapData, int playerX, int playerY) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newX = playerX + i;
                int newY = playerY + j;
                if (newX >= 0 && newX < MAP_SIZE && newY >= 0 && newY < MAP_SIZE && mapData[newX][newY] == null) {
                    mapData[newX][newY] = generateTileData(newX, newY);
                }
            }
        }
    }
}
