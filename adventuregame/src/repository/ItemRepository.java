package repository;

import logics.Item;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    private List<Item> items;
    private int currentItemIndex;

    public ItemRepository() {
        items = new ArrayList<>();
        currentItemIndex = 0;
        loadItems();
    }

    private void loadItems() {
        items.add(new Item("Sword", "A sharp blade.", 5));
        items.add(new Item("Shield", "Protects against attacks.", 7));
        items.add(new Item("Potion", "Restores health.", 3));
        // Add more items as needed
    }

    public Item getCurrentItem() {
        return items.get(currentItemIndex);
    }

    public Item getNextItem() {
        currentItemIndex = (currentItemIndex + 1) % items.size();
        return getCurrentItem();
    }

    public Item getPreviousItem() {
        currentItemIndex = (currentItemIndex - 1 + items.size()) % items.size();
        return getCurrentItem();
    }
}
