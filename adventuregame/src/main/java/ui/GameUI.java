package ui;

import javax.swing.*;
import logics.*;
import repository.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GameUI {
    private JFrame frame;
    private GameLogic gameLogic;
    private MapLogic mapLogic;
    private JTextArea itemDescription;
    private JList<Item> inventoryList;
    private JPanel entryPanel;
    private JPanel mainPanel;
    private JPanel shopPanel;
    private JPanel trainingPanel;
    private JPanel mapPanel;
    private CardLayout cardLayout;
    private int inventoryIndex = 0;
    private Map mapScreen;

    public GameUI() {
        gameLogic = new GameLogic();
        mapLogic =new  MapLogic();
        initialize();
    }
    

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 350, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        frame.getContentPane().setLayout(cardLayout);

        // Entry panel setup
        entryPanel = new JPanel();
        frame.getContentPane().add(entryPanel, "EntryPanel");
        entryPanel.setLayout(new GridLayout(2, 1, 0, 0));

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainScreen();
            }
        });
        entryPanel.add(newGameButton);

        JButton loadButton = new JButton("Load Game");
        // Define load game action later
        entryPanel.add(loadButton);

        // Main panel setup
        mainPanel = new JPanel();
        frame.getContentPane().add(mainPanel, "MainPanel");
        mainPanel.setLayout(new GridLayout(5, 1, 0, 0));

        JButton shopButton = new JButton("Shop");
        shopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showShopScreen();
            }
        });
        mainPanel.add(shopButton);

        JButton trainingButton = new JButton("Training");
        trainingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showTrainingScreen();
            }
        });
        mainPanel.add(trainingButton);

        JButton mapButton = new JButton("Map");
        mapButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMapScreen();
            }
        });
        mainPanel.add(mapButton);

        JButton infoButton = new JButton("Info");
        infoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showInfoScreen();
            }
        });
        mainPanel.add(infoButton);

        // Shop panel setup
        shopPanel = new JPanel();
        frame.getContentPane().add(shopPanel, "ShopPanel");
        shopPanel.setLayout(new GridLayout(2, 1, 0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        shopPanel.add(tabbedPane);

        JPanel buyPanel = new JPanel();
        tabbedPane.addTab("Buy", null, buyPanel, null);
        buyPanel.setLayout(new BorderLayout(0, 0));

        JPanel buyButtonPanel = new JPanel();
        buyPanel.add(buyButtonPanel, BorderLayout.SOUTH);

        JButton buyButton = new JButton("Buy");
        buyButtonPanel.add(buyButton);

        JButton nextButton = new JButton("Next Item");
        buyButtonPanel.add(nextButton);

        JButton previousButton = new JButton("Prev. Item");
        buyButtonPanel.add(previousButton);

        JButton backToMainButton = new JButton("Main");
        backToMainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainScreen();
            }
        });
        buyButtonPanel.add(backToMainButton);

        itemDescription = new JTextArea();
        buyPanel.add(itemDescription, BorderLayout.CENTER);

        JPanel sellPanel = new JPanel();
        tabbedPane.addTab("Sell", null, sellPanel, null);
        sellPanel.setLayout(new BorderLayout(0, 0));

        inventoryList = new JList<>();
        sellPanel.add(new JScrollPane(inventoryList), BorderLayout.CENTER);

        JPanel sellButtonPanel = new JPanel();
        sellPanel.add(sellButtonPanel, BorderLayout.SOUTH);

        JButton sellButton = new JButton("Sell");
        sellButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Item selectedItem = inventoryList.getSelectedValue();
                if (selectedItem != null) {
                    gameLogic.sellItem(selectedItem);
                    inventoryList.setListData(gameLogic.getCharacter().getInventory().toArray(new Item[0]));
                    JOptionPane.showMessageDialog(frame, "Item sold!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Select an item to sell!");
                }
            }
        });
        sellButtonPanel.add(sellButton);

        JButton nextSellItemButton = new JButton("Next Item");
        nextSellItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameLogic.getCharacter().getInventory().size() > 0) {
                    inventoryIndex = (inventoryIndex + 1) % gameLogic.getCharacter().getInventory().size();
                    inventoryList.setSelectedIndex(inventoryIndex);
                }
            }
        });
        sellButtonPanel.add(nextSellItemButton);

        JButton previousSellItemButton = new JButton("Prev. Item");
        previousSellItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameLogic.getCharacter().getInventory().size() > 0) {
                    inventoryIndex = (inventoryIndex - 1 + gameLogic.getCharacter().getInventory().size()) % gameLogic.getCharacter().getInventory().size();
                    inventoryList.setSelectedIndex(inventoryIndex);
                }
            }
        });
        sellButtonPanel.add(previousSellItemButton);

        JButton backToMainFromSellButton = new JButton("Main");
        backToMainFromSellButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainScreen();
            }
        });
        sellButtonPanel.add(backToMainFromSellButton);

        // Training panel setup
        trainingPanel = new JPanel();
        frame.getContentPane().add(trainingPanel, "TrainingPanel");
        trainingPanel.setLayout(new GridLayout(5, 1, 0, 0));

        JButton healthButton = new JButton("Train Health");
        healthButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.trainAttribute("health");
                JOptionPane.showMessageDialog(frame, "+1 added to health. Now it is " + gameLogic.getCharacter().getHealth());
            }
        });
        trainingPanel.add(healthButton);

        JButton attackButton = new JButton("Train Attack");
        attackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.trainAttribute("attack");
                JOptionPane.showMessageDialog(frame, "+1 added to attack. Now it is " + gameLogic.getCharacter().getAttack());
            }
        });
        trainingPanel.add(attackButton);

        JButton defenseButton = new JButton("Train Defense");
        defenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.trainAttribute("defense");
                JOptionPane.showMessageDialog(frame, "+1 added to defense. Now it is " + gameLogic.getCharacter().getDefense());
            }
        });
        trainingPanel.add(defenseButton);

        JButton agilityButton = new JButton("Train Agility");
        agilityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.trainAttribute("agility");
                JOptionPane.showMessageDialog(frame, "+1 added to agility. Now it is " + gameLogic.getCharacter().getAgility());
            }
        });
        trainingPanel.add(agilityButton);

        JButton levelButton = new JButton("Train Level");
        levelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameLogic.trainAttribute("level");
                JOptionPane.showMessageDialog(frame, "+1 added to level. Now it is " + gameLogic.getCharacter().getLevel());
            }
        });
        trainingPanel.add(levelButton);
        
        JButton backToMainButton4 = new JButton("Main");
        backToMainButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainScreen();
            }
        });
        trainingPanel.add(backToMainButton4);

        // Map panel setup
        mapPanel = new JPanel(new BorderLayout());
        frame.getContentPane().add(mapPanel, "MapPanel");

        JPanel mapControlPanel = new JPanel(new BorderLayout());
        JTextArea infoPanel = new JTextArea();
        infoPanel.setEditable(false);
        JButton homeButton = new JButton("Back");
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainScreen();
            }
            
        });

        mapControlPanel.add(infoPanel, BorderLayout.NORTH);
        mapControlPanel.add(homeButton, BorderLayout.SOUTH);

        mapPanel.add(mapControlPanel, BorderLayout.SOUTH);

        mapScreen = new Map(mapPanel, infoPanel, homeButton, gameLogic, this);

        frame.setVisible(true);


        
    }

    void showMainScreen() {
        cardLayout.show(frame.getContentPane(), "MainPanel");
    }

    private void showShopScreen() {
        cardLayout.show(frame.getContentPane(), "ShopPanel");
    }

    private void showTrainingScreen() {
        cardLayout.show(frame.getContentPane(), "TrainingPanel");
    }

    private void showMapScreen() {
        cardLayout.show(frame.getContentPane(), "MapPanel");
        mapScreen.show(); // Show the map
        frame.revalidate();
        frame.repaint();
    }
    private void showInfoScreen() {
        final JDialog infoDialog = new JDialog(frame, "Character Information", true);
        infoDialog.setSize(300, 400);
        infoDialog.setLocationRelativeTo(frame);
        
        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        
        // Collect character information and stats
        StringBuilder info = new StringBuilder();
        info.append("Character Attributes:\n");
        info.append("Health: ").append(gameLogic.getCharacter().getHealth()).append("\n");
        info.append("Attack: ").append(gameLogic.getCharacter().getAttack()).append("\n");
        info.append("Defense: ").append(gameLogic.getCharacter().getDefense()).append("\n");
        info.append("Agility: ").append(gameLogic.getCharacter().getAgility()).append("\n");
        info.append("Level: ").append(gameLogic.getCharacter().getLevel()).append("\n");
        info.append("Gold: ").append(gameLogic.getCharacter().getGold()).append("\n\n");
        
        info.append("Stats:\n");
        //info.append("Monsters Killed: ").append(gameLogic.getMonstersKilled()).append("\n");
        //info.append("Steps Taken: ").append(gameLogic.getStepsTaken()).append("\n");
        //info.append("Chests Opened: ").append(gameLogic.getChestsOpened()).append("\n\n");
        
        info.append("Inventory:\n");
        for (Item item : gameLogic.getCharacter().getInventory()) {
            info.append(item.getName()).append("\n");
        }
        
        infoTextArea.setText(info.toString());
    
        // Create a panel to hold the text area and the back button
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(new JScrollPane(infoTextArea), BorderLayout.CENTER);
    
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                infoDialog.dispose();  // Close the dialog
            }
        });
        infoPanel.add(backButton, BorderLayout.SOUTH);
    
        infoDialog.add(infoPanel);
        infoDialog.setVisible(true);
    }
    
    

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GameUI window = new GameUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveMapToFile(String[][] largeMap, String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                writer.write(largeMap[i][j] == null ? "00000000" : largeMap[i][j]);
                if (j < 99) {
                    writer.write(" ");
                }
            }
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public String[][] loadMapFromFile(String filePath) {
    String[][] largeMap = new String[100][100];
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        int row = 0;
        while ((line = reader.readLine()) != null && row < 100) {
            String[] tokens = line.split(" ");
            for (int col = 0; col < 100; col++) {
                largeMap[row][col] = tokens[col].equals("00000000") ? null : tokens[col];
            }
            row++;
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return largeMap;}
    
    

    public void gameOver() {
        // Show Game Over message
        JOptionPane.showMessageDialog(null, "Game Over!");
    
        // Reset game logic and data
        mapLogic.clearMonsters();
        mapLogic.clearChests();
        gameLogic = new GameLogic();
    
        // Reset the map
        mapScreen.resetMap();
    
        // Switch to entry screen
        showEntryScreen();
    }
    
    private void showEntryScreen() {
        cardLayout.show(frame.getContentPane(), "EntryPanel");
    }

    
    

}



