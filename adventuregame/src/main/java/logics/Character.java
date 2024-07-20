package logics;

import java.util.ArrayList;
import java.util.List;

public class Character {
    private int health;
    private int attack;
    private int defense;
    private int agility;
    private int level;
    private int gold;
    private List<Item> inventory;

    public Character() {
        this.health = 10;
        this.attack = 1;
        this.defense = 1;
        this.agility = 1;
        this.level = 1;
        this.gold = 10;
        this.inventory = new ArrayList<>();
    }

    // Getters and Setters
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    public int getAgility() { return agility; }
    public void setAgility(int agility) { this.agility = agility; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    public List<Item> getInventory() { return inventory; }
    public void setInventory(List<Item> inventory) { this.inventory = inventory; }
    
    public void addItem(Item item) {
        this.inventory.add(item);
    }
    
    public void removeItem(Item item) {
        this.inventory.remove(item);
    }
    
}
