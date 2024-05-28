package ui;

import logics.*;
import repository.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameUI {
    private JFrame frame;
    private GameLogic gameLogic;
    private JTextArea itemDescription;
    private JList<Item> inventoryList;
    private JPanel entryPanel;
    private JPanel mainPanel;
    private JPanel shopPanel;
    private JPanel trainingPanel;
    private CardLayout cardLayout;
    private int inventoryIndex = 0;

    public GameUI() {
        gameLogic = new GameLogic();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
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
        mainPanel.setLayout(new GridLayout(3, 1, 0, 0));

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
        // Define map action later
        mainPanel.add(mapButton);

        // Shop panel setup
        shopPanel = new JPanel();
        frame.getContentPane().add(shopPanel, "ShopPanel");
        shopPanel.setLayout(new GridLayout(2, 1, 0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        shopPanel.add(tabbedPane);

        JPanel buyPanel = new JPanel();
        tabbedPane.addTab("Buy", null, buyPanel, null);
        buyPanel.setLayout(new BorderLayout(0, 0));

        itemDescription = new JTextArea();
        itemDescription.setEditable(false);
        buyPanel.add(itemDescription, BorderLayout.CENTER);

        JPanel buyButtonPanel = new JPanel();
        buyPanel.add(buyButtonPanel, BorderLayout.SOUTH);

        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Item currentItem = gameLogic.getItemRepository().getCurrentItem();
                if (gameLogic.buyItem(currentItem)) {
                    JOptionPane.showMessageDialog(frame, "Item bought!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Not enough gold!");
                }
            }
        });
        buyButtonPanel.add(buyButton);

        JButton nextButton = new JButton("Next Item");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Item nextItem = gameLogic.getItemRepository().getNextItem();
                itemDescription.setText(nextItem.getDescription());
            }
        });
        buyButtonPanel.add(nextButton);

        JButton previousButton = new JButton("Previous Item");
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Item previousItem = gameLogic.getItemRepository().getPreviousItem();
                itemDescription.setText(previousItem.getDescription());
            }
        });
        buyButtonPanel.add(previousButton);

        JButton backToMainButton = new JButton("Main");
        backToMainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainScreen();
            }
        });
        buyButtonPanel.add(backToMainButton);

        JPanel sellPanel = new JPanel();
        tabbedPane.addTab("Sell", null, sellPanel, null);
        sellPanel.setLayout(new BorderLayout(0, 0));

        inventoryList = new JList<>(gameLogic.getCharacter().getInventory().toArray(new Item[0]));
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

        JButton previousSellItemButton = new JButton("Previous Item");
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

        JButton backToMainFromTrainingButton = new JButton("Main");
        backToMainFromTrainingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainScreen();
            }
        });
        trainingPanel.add(backToMainFromTrainingButton);

        frame.setVisible(true);
    }

    private void showMainScreen() {
        cardLayout.show(frame.getContentPane(), "MainPanel");
    }

    private void showShopScreen() {
        cardLayout.show(frame.getContentPane(), "ShopPanel");
    }

    private void showTrainingScreen() {
        cardLayout.show(frame.getContentPane(), "TrainingPanel");
    }
}
