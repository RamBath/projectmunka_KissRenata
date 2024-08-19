package logics;

public class Chests {
    private int goldDrop;
    private int x;
    private int y;
    private boolean isActive;

    public Chests(){}
    public Chests(int goldDrop, int x, int y, boolean isActive) {
        this.goldDrop = goldDrop;
        this.x = x;
        this.y = y;
        this.isActive = isActive;
    }
    public int getGoldDrop() {
        return goldDrop;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setGoldDrop(int goldDrop) {
        this.goldDrop = goldDrop;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    @Override
    public String toString() {

        return "c "+ goldDrop + " "+ x +" " + y +" " + isActive;
    }
    
    


}
