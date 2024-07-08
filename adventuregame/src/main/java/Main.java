import ui.GameUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    GameUI window = new GameUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        });
    }
}


