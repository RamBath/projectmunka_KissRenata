package logics;

public enum TileType {
    TYPE_01("01", new boolean[]{true, true, true, true}, "tile00.png"),   // All directions
    TYPE_02("02", new boolean[]{true, true, true, false}, "tile04.png"),  // L, D, R
    TYPE_03("03", new boolean[]{false, true, true, true}, "tile01.png"),  // D, R, U
    TYPE_04("04", new boolean[]{true, false, true, true}, "tile02.png"),  // L, R, U
    TYPE_05("05", new boolean[]{true, true, false, true}, "tile03.png"),  // L, D, U
    TYPE_06("06", new boolean[]{false, true, false, true}, "tile09.png"), // D, U
    TYPE_07("07", new boolean[]{true, false, true, false}, "tile10.png"), // L, R
    TYPE_08("08", new boolean[]{true, true, false, false}, "tile08.png"), // L, D
    TYPE_09("09", new boolean[]{false, true, true, false}, "tile05.png"), // D, R
    TYPE_10("10", new boolean[]{false, false, true, true}, "tile06.png"), // R, U
    TYPE_11("11", new boolean[]{true, false, false, true}, "tile07.png"), // L, U
    TYPE_12("12", new boolean[]{true, false, false, false}, "tile11.png"),// L
    TYPE_13("13", new boolean[]{false, true, false, false}, "tile12.png"),// D
    TYPE_14("14", new boolean[]{false, false, true, false}, "tile13.png"),// R
    TYPE_15("15", new boolean[]{false, false, false, true}, "tile14.png");// U

    

    private String code;
    private boolean[] directions; // L, D, R, U
    private String image;

    TileType(String code, boolean[] directions, String image) {
        this.code = code;
        this.directions = directions;
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public boolean[] getDirections() {
        return directions;
    }

    public String getImage() {
        return image;
    }

    public static TileType getByCode(String code) {
        for (TileType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}