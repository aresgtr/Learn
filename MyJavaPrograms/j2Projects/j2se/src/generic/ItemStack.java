package generic;

import java.util.LinkedList;

import property.Item;

public class ItemStack {

    LinkedList<Item> items = new LinkedList<>();

    public void push (Item h) {
        items.addLast(h);
    }

    public Item pull() {
        return items.removeLast();
    }

    public Item peek() {
        return items.getLast();
    }
}
