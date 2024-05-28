package logics;
import repository.ItemRepository;

public class GameLogic {
    private Character character;
    private ItemRepository itemRepository;

    public GameLogic() {
        this.character = new Character();
        this.itemRepository = new ItemRepository();
    }

    public Character getCharacter() {
        return character;
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

    public void trainAttribute(String attribute) {
        if (attribute.equalsIgnoreCase("level")) {
            if (character.getGold() >= 10) {
                character.setGold(character.getGold() - 10);
                character.setLevel(character.getLevel() + 1);
                character.setHealth(character.getHealth() + 2);
                character.setAttack(character.getAttack() + 2);
                character.setDefense(character.getDefense() + 2);
                character.setAgility(character.getAgility() + 2);
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
            }
        }
    }
}
