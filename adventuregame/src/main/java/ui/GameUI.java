package ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import logics.*;
import repository.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class GameUI {
    private JFrame frame;
    private GameLogic gameLogic;
    private MapLogic mapLogic;
    private JTextArea itemDescription;
    private JList<Item> itemList;
    private JList<Item> inventoryList;
    private JPanel entryPanel;
    private JPanel mainPanel;
    private JPanel trainingPanel;
    private JPanel mapPanel;
    private CardLayout cardLayout;
    private Map mapScreen;
    private List<Item> shopItems;
    private int currentItemIndex = 0;
    private JLabel itemImageLabel;
    private JButton buyButton;
    private JButton nextButton;
    private JButton previousButton;
    private GameRepository gameRepository;

    public GameUI() {
        gameLogic = new GameLogic();
        mapLogic = new MapLogic();
        initialize();
        gameRepository = new GameRepository();

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
        entryPanel.setLayout(new GridLayout(3, 1, 0, 0));

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainScreen();
            }
        });
        entryPanel.add(newGameButton);

        JButton loadButton = new JButton("Load Game");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Text files", "txt");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("You chose to open this file: " +
                            chooser.getSelectedFile().getName());
                    mapScreen.loadMapDataFull(loadMapFromFile("saves/" + chooser.getSelectedFile().getName()));
                    showMainScreen();

                }

            }
        });
        entryPanel.add(loadButton);

        JButton loadFromDBButton = new JButton("Load from DataBase");
        loadFromDBButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadfromDTbase();
                showMainScreen();
            }
        });
        entryPanel.add(loadFromDBButton);

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

        JButton saveToDatabase = new JButton("Backup into the SQL");
        saveToDatabase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveToDTBase();
            }
        });
        mainPanel.add(saveToDatabase);

        // Shop panel setup
        JPanel shopPanel = new JPanel();
        frame.getContentPane().add(shopPanel, "ShopPanel");
        shopPanel.setLayout(new BorderLayout());

        // Display area for item image and description
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());

        itemImageLabel = new JLabel();
        displayPanel.add(itemImageLabel, BorderLayout.WEST);

        itemDescription = new JTextArea();
        itemDescription.setEditable(false);
        displayPanel.add(new JScrollPane(itemDescription), BorderLayout.EAST);

        shopPanel.add(displayPanel, BorderLayout.CENTER);

        // Button panel for navigation and buying
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        previousButton = new JButton("Prev. Item");
        previousButton.addActionListener(e -> showPreviousItem());
        buttonPanel.add(previousButton);

        nextButton = new JButton("Next Item");
        nextButton.addActionListener(e -> showNextItem());
        buttonPanel.add(nextButton);

        buyButton = new JButton("Buy");
        buyButton.addActionListener(e -> buyCurrentItem());
        buttonPanel.add(buyButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainScreen());
        buttonPanel.add(backButton);

        shopPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Training panel setup
        trainingPanel = new JPanel();
        frame.getContentPane().add(trainingPanel, "TrainingPanel");
        trainingPanel.setLayout(new GridLayout(5, 1, 0, 0));

        JButton healthButton = new JButton("Train Health - 1g");
        healthButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameLogic.trainAttribute("health")) {
                    JOptionPane.showMessageDialog(frame,
                            "+1 added to health. Now it is " + gameLogic.getCharacter().getHealth());
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Not enough gold!");
                }
            }
        });
        trainingPanel.add(healthButton);

        JButton attackButton = new JButton("Train Attack - 1g");
        attackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameLogic.trainAttribute("attack")) {
                    JOptionPane.showMessageDialog(frame,
                            "+1 added to attack. Now it is " + gameLogic.getCharacter().getAttack());
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Not enough gold!");
                }
            }
        });
        trainingPanel.add(attackButton);

        JButton defenseButton = new JButton("Train Defense - 1g");
        defenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameLogic.trainAttribute("defense")) {
                    JOptionPane.showMessageDialog(frame,
                            "+1 added to defense. Now it is " + gameLogic.getCharacter().getDefense());
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Not enough gold!");
                }
            }
        });
        trainingPanel.add(defenseButton);

        JButton agilityButton = new JButton("Train Agility - 1g");
        agilityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameLogic.trainAttribute("agility")){
                JOptionPane.showMessageDialog(frame,
                        "+1 added to agility. Now it is " + gameLogic.getCharacter().getAgility());
                    }else{
                        JOptionPane.showMessageDialog(frame,
                                "Not enough gold!");
                    }
            }
        });
        trainingPanel.add(agilityButton);

        JButton levelButton = new JButton("Train Level - 10g");
        levelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameLogic.trainAttribute("level")){
                JOptionPane.showMessageDialog(frame,
                        "+1 added to level. Now it is " + gameLogic.getCharacter().getLevel());
                    }else{
                        JOptionPane.showMessageDialog(frame,
                                "Not enough gold!");
                    }
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
                if (mapScreen.getMapDataFull() != null) {
                    String date = "saves/backup" + java.time.LocalDateTime.now();
                    date = date.replace("-", "");
                    date = date.replace(":", "");
                    date = date.replace(".", "");
                    saveMapToFile(mapScreen.getMapDataFull(), date + ".txt");
                }
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
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "ShopPanel");
        loadShopItems();
        displayItem(currentItemIndex);
    }

    private void loadShopItems() {
        shopItems = gameLogic.generateShopItems();
        if (shopItems.isEmpty()) {
            itemImageLabel.setIcon(null);
            itemDescription.setText("No items available.");
        }
    }

    private void displayItem(int index) {
        if (shopItems == null || shopItems.isEmpty()) {
            itemImageLabel.setIcon(null);
            itemDescription.setText("No items available.");
            return;
        }

        if (index < 0 || index >= shopItems.size()) {
            return; // Invalid index
        }

        Item item = shopItems.get(index);
        itemDescription.setText(
                "Name: " + item.getName() + "\n" +
                        "Description: " + item.getDescription() + "\n" +
                        "Price: " + item.getPrice() + " gold");

        String imagePath = "/images/" + item.getImagePath(); // Adjust path as needed
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            itemImageLabel.setIcon(new ImageIcon(imageUrl));
        } else {
            itemImageLabel.setIcon(null);
            itemDescription.append("\nImage not found.");
        }
    }

    private void showNextItem() {
        if (shopItems != null && !shopItems.isEmpty()) {
            currentItemIndex = (currentItemIndex + 1) % shopItems.size();
            displayItem(currentItemIndex);
        }
    }

    private void showPreviousItem() {
        if (shopItems != null && !shopItems.isEmpty()) {
            currentItemIndex = (currentItemIndex - 1 + shopItems.size()) % shopItems.size();
            displayItem(currentItemIndex);
        }
    }

    private void buyCurrentItem() {
        if (shopItems == null || shopItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No items available to buy.");
            return;
        }

        Item item = shopItems.get(currentItemIndex);
        if (gameLogic.getCharacter().getGold() >= item.getPrice()) {
            gameLogic.buyItem(item);
            JOptionPane.showMessageDialog(frame, "Item purchased successfully!");
            loadShopItems(); // Refresh item list
            displayItem(currentItemIndex); // Refresh the currently displayed item
        } else {
            JOptionPane.showMessageDialog(frame, "Not enough gold to buy this item.");
        }
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
        // info.append("Monsters Killed:
        // ").append(gameLogic.getMonstersKilled()).append("\n");
        // info.append("Steps Taken: ").append(gameLogic.getStepsTaken()).append("\n");
        // info.append("Chests Opened:
        // ").append(gameLogic.getChestsOpened()).append("\n\n");

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
                infoDialog.dispose(); // Close the dialog
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
            writer.write(mapLogic.getAllMonsters());
            writer.newLine();
            writer.write(mapLogic.getAllChests());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[][] loadMapFromFile(String filePath) {
        String[][] largeMap = new String[100][100];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                if (row < 100) {
                    String[] tokens = line.split(" ");
                    for (int col = 0; col < 100; col++) {
                        largeMap[row][col] = tokens[col].equals("00000000") ? null : tokens[col];
                    }
                } else if (line.contains("m ")) {
                    String[] tokens = line.split(" ");
                    mapLogic.setMonsterList(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7],
                            tokens[8]);
                } else if (line.contains("c ")) {
                    String[] tokens = line.split(" ");
                    mapLogic.setChestList(tokens[1], tokens[2], tokens[3], tokens[4]);
                }

                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return largeMap;
    }

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

    private void loadfromDTbase() {
        mapLogic.loadFromDBMonstersAndChests();

        String[][] largeMap = new String[100][100];
        for (int row = 0; row < 100; row++) {
            String line = gameRepository.getMap(row);
            String[] tokens = line.split(",");
            for (int colm = 0; colm < 100; colm++) {
                largeMap[row][colm] = tokens[colm].equals("null") ? null : tokens[colm];
            }
        }
        mapScreen.loadMapDataFull(largeMap);
    }

    private void saveToDTBase() {
        gameRepository.createTables();
        String[][] largeMap = mapScreen.getMapDataFull();

        gameRepository.overwriteMapTable(largeMap);
        gameRepository.populateMonsterTable(mapLogic.getMonsters());
        gameRepository.populateChest(mapLogic.getChests());

    }

}
