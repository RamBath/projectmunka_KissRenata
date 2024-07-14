package logics;

public enum ActionType {
    
    
    TYPE_01("01", "snake.png"),
    TYPE_02("02", "wolf.png"),   // All directions
    TYPE_03("03", "snake.png"),
    TYPE_04("04", "skeleton.png"),
    TYPE_05("05", "zombie.png"),
    TYPE_06("06", "thumbstone.png"),
    TYPE_07("07", "chest_unopened.png"),
    TYPE_08("08", "chest_opened.png")    ;
   
    private String code;
    private String image;
    
    ActionType(String code, String image) {
        this.code=code;
        this.image=image;
    }
    public String getCode() {
        return code;
    }
    public String getImage() {
        return image;
    }
    public static ActionType getByCode(String code) {
        for (ActionType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }



}
