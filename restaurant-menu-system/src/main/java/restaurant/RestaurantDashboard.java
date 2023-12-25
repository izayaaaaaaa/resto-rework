package restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RestaurantDashboard extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    public RestaurantDashboard() {
        createUI();
    }

    private void createUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        String[] categories = { "Pasta", "Burgers", "Pizzas", "Desserts", "Drinks", "Checkout" }; // Added Checkout
        for (String category : categories) {
            JButton button = new JButton(category);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, category);
                }
            });
            buttonPanel.add(button);
        }
        add(buttonPanel, BorderLayout.WEST);

        // Welcome card
        cardPanel.add(createWelcomePanel(), "Welcome");

        // Category cards
        cardPanel.add(
                createItemsPanel(
                        new String[] { "Spaghetti Bolognese", "Cheesy Lasagna", "Carbonara", "Vegetarian Pasta" }),
                "Pasta");
        cardPanel.add(
                createItemsPanel(
                        new String[] { "Chicken Burger", "Cheese Burger", "Double Patty Burger", "Veggie Burger" }),
                "Burgers");
        cardPanel.add(
                createItemsPanel(new String[] { "Pepperoni Pizza", "Cheese Pizza", "Mushroom Pizza", "Pesto Pizza" }),
                "Pizzas");
        cardPanel.add(
                createItemsPanel(
                        new String[] { "Chocolate Cake", "Berry Cheesecake", "Cookies & Cream", "Creme Brulee" }),
                "Desserts");
        cardPanel.add(
                createItemsPanel(
                        new String[] { "Chocolate Milkshake", "Caramel Frappe", "Iced Tea", "Cafe Americano" }),
                "Drinks");

        // Checkout card
        cardPanel.add(createCheckoutPanel(), "Checkout");

        // Set the welcome card as the initial view
        cardLayout.show(cardPanel, "Welcome");
        add(cardPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createItemsPanel(String[] items) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        for (String item : items) {
            panel.add(new JLabel(item));
        }
        return panel;
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Our Restaurant!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);
        return welcomePanel;
    }

    private JPanel createCheckoutPanel() {
        JPanel checkoutPanel = new JPanel(new GridBagLayout());
        JLabel checkoutLabel = new JLabel("Checkout");
        checkoutLabel.setFont(new Font("Arial", Font.BOLD, 24));
        checkoutPanel.add(checkoutLabel);
        // You can add more components to this panel as needed
        return checkoutPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RestaurantDashboard();
            }
        });
    }
}
