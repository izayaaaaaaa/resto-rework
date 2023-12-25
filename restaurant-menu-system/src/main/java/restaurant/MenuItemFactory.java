package restaurant;

public class MenuItemFactory {
    public MenuItem createMenuItem(String name, double price, String photoPath) {
        return new MenuItem(name, price, photoPath);
    }
}