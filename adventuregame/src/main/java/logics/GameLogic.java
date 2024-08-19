package logics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import repository.GameRepository;
import repository.ItemRepository;

public class GameLogic {
    private Character character;
    private ItemRepository itemRepository;
    private int monstersKilled;
    private GameRepository gameRepository;

    public GameLogic() {
        this.character = new Character();
        this.itemRepository = new ItemRepository();
        this.monstersKilled = 0;
        this.gameRepository= new GameRepository();
    }

    public Character getCharacter() {
        return character;
    }
    


    public List<Item> generateShopItems() {
        List<Item> items = new ArrayList<>();
        Random random = new Random();

        // Define some base items
        String[] itemNames = { "Apple", "Ham", "Arrow", "Sword", "Axe", "Shield", "Charms" };
        String[] itemDescriptions = {
                "\nA basic apple. \nHeals a small amount.",
                "\nA hearty ham. \nHeals more than an apple.",
                "\nA simple arrow. \nProvides a low attack boost.",
                "\nA sturdy sword. \nMedium attack, but reduces agility.",
                "\nA heavy axe. \nHigh attack, but \nseverely reduces agility.",
                "\nA solid shield. \nIncreases defense but \nreduces agility.",
                "\nA mystical charm. \nProvides a random stat boost."
        };

        int[] basePrices = { 5, 15, 10, 30, 50, 40, 25 }; // Base prices for items

        // Generate items based on character's level
        int level = character.getLevel();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(itemNames.length);

            String name = itemNames[index];
            String description = itemDescriptions[index];
            int price = basePrices[index] + random.nextInt(level * 5); // Scale price with level

            items.add(new Item(name, description, price));
        }

        return items;
    }

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public boolean buyItem(Item item) {
        if (character.getGold() >= item.getPrice()) {
            character.setGold(character.getGold() - item.getPrice());
            character.addItem(item);
            return true;
        } else {
            return false;
        }
    }

    public void sellItem(Item item) {
        character.setGold(character.getGold() + item.getPrice());
        character.removeItem(item);
    }

    public boolean trainAttribute(String attribute) {
        if (attribute.equalsIgnoreCase("level")) {
            if (character.getGold() >= 10) {
                character.setGold(character.getGold() - 10);
                character.setLevel(character.getLevel() + 1);
                character.setHealth(character.getHealth() + 2);
                character.setAttack(character.getAttack() + 2);
                character.setDefense(character.getDefense() + 2);
                character.setAgility(character.getAgility() + 2);
                return true;
            }
        } else {
            if (character.getGold() >= 1) {
                character.setGold(character.getGold() - 1);
                switch (attribute.toLowerCase()) {
                    case "health":
                        character.setHealth(character.getHealth() + 1);
                        break;
                    case "attack":
                        character.setAttack(character.getAttack() + 1);
                        break;
                    case "defense":
                        character.setDefense(character.getDefense() + 1);
                        break;
                    case "agility":
                        character.setAgility(character.getAgility() + 1);
                        break;
                }
                return true;
            }
        }
        return false;
    }

    public void findChest(int gold) {
        this.character.setGold(character.getGold() + gold);
    }

    public void addMonsterKill() {
        monstersKilled++;
    }

}
