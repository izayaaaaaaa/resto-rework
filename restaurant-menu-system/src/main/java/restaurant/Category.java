package restaurant;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private List<MenuItem> menuItems;

    public Category(String name) {
        this.name = name;
        this.menuItems = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        menuItems.add(item);
    }

    public List<MenuItem> getItems() {
        return menuItems;
    }
}