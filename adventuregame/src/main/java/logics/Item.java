package logics;

public class Item {
    private String name;
    private String description;
    private int price;
    private String effect1; // name of effect
    private String effect2;
    private int boost1;
    private int boost2;

    public Item() {
    }

    
    public Item(String name, String description, int price, String effect1, String effect2, int boost1, int boost2) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.effect1 = effect1;
        this.effect2 = effect2;
        this.boost1 = boost1;
        this.boost2 = boost2;
    }


    public Item(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.effect1 = null;
        this.effect2 = null;
        this.boost1 = 0;
        this.boost2 = 0;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String toString() {
        return name + " - " + price + " gold";
    }

    public String getImagePath() {
        return name.toLowerCase() + ".png";
    }

    public String getEffect1() {
        return effect1;
    }

    public String getEffect2() {
        return effect2;
    }

    public int getBoost1() {
        return boost1;
    }

    public int getBoost2() {
        return boost2;
    }

    public void setEffect1(String effect1) {
        this.effect1 = effect1;
    }

    public void setEffect2(String effect2) {
        this.effect2 = effect2;
    }

    public void setBoost1(int boost1) {
        this.boost1 = boost1;
    }

    public void setBoost2(int boost2) {
        this.boost2 = boost2;
    }

    
}
