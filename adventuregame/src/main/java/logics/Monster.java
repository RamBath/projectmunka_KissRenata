package logics;

public class Monster {
    private int health;
    private int attack;
    private int agility;
    private int goldDrop;
    private int x;
    private int y;
    private String monsterTypeCode;
    private boolean isAlive;

    public Monster() {
    }

    public Monster(String monsterTypeCode, int health, int attack, int agility, int goldDrop, int x, int y,
            boolean isAlive) {
        this.monsterTypeCode = monsterTypeCode;
        this.health = health;
        this.attack = attack;
        this.agility = agility;
        this.goldDrop = goldDrop;
        this.x = x;
        this.y = y;
        this.isAlive = isAlive;
    }

    // Getters and setters for all fields
    public String getType() {
        return monsterTypeCode;
    }

    public void setType(String monsterTypeCode) {
        this.monsterTypeCode = monsterTypeCode;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getGoldDrop() {
        return goldDrop;
    }

    public void setGoldDrop(int goldDrop) {
        this.goldDrop = goldDrop;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    @Override
    public String toString() {
        return "m " + monsterTypeCode + " " + health + " " + attack + " " + agility + " " + goldDrop
                + " " + x + " " + y + " " + isAlive;
    }

    public void setMonsterTypeCode(String monsterTypeCode) {
        this.monsterTypeCode = monsterTypeCode;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public String getMonsterTypeCode() {
        return monsterTypeCode;
    }
    

    

}
