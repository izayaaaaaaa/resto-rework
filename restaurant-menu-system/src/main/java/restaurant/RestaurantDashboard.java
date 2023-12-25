package restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RestaurantDashboard extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private Color orangeColor = new Color(255, 102, 0);
    private Color whiteColor = Color.WHITE;
    private Color checkoutColor = new Color(239, 23, 23);

    public RestaurantDashboard() {
        setUndecorated(true);
        createUI();
    }

    private void createUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(whiteColor);
        // Add an empty border to the buttonPanel to create the right side gap
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        // Set the preferred width of the buttonPanel
        buttonPanel.setPreferredSize(new Dimension(289, buttonPanel.getPreferredSize().height));

        // Menu button with inverted colors
        JButton menuButton = new JButton("Menu");
        styleButton(menuButton, whiteColor, orangeColor, 48, 150, false);
        menuButton.addActionListener(e -> cardLayout.show(cardPanel, "Welcome"));
        // buttonPanel.add(createSpacer());
        buttonPanel.add(menuButton);
        buttonPanel.add(createSpacer());

        // Category buttons
        String[] categories = { "Pasta", "Burgers", "Pizzas", "Desserts", "Drinks" };
        for (String category : categories) {
            JButton button = new JButton(category);
            styleButton(button, orangeColor, whiteColor, 36, 80, false);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, category);
                }
            });
            buttonPanel.add(createSpacer());
            buttonPanel.add(button);
            buttonPanel.add(createSpacer());
        }

        // Checkout button
        JButton checkoutButton = new JButton("Checkout");
        styleButton(checkoutButton, checkoutColor, whiteColor, 24, 70, true);
        checkoutButton.addActionListener(e -> cardLayout.show(cardPanel, "Checkout"));
        buttonPanel.add(createSpacer());
        buttonPanel.add(checkoutButton);
        buttonPanel.add(createSpacer());

        add(buttonPanel, BorderLayout.WEST);

        // Welcome card
        cardPanel.add(createWelcomePanel(), "Welcome");
        cardPanel.setBackground(whiteColor);

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

        // Set the background of cardPanel
        cardPanel.setBackground(orangeColor);

        // Set the initial size
        setSize(new Dimension(1350, 791));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createItemsPanel(String[] items) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(orangeColor);
        for (String item : items) {
            JLabel itemLabel = new JLabel(item, SwingConstants.CENTER);
            itemLabel.setOpaque(true);
            itemLabel.setBackground(whiteColor); // Set the background of the item label to white
            itemLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set the font
            panel.add(itemLabel);
        }
        return panel;
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(orangeColor);
        JLabel welcomeLabel = new JLabel("Welcome to Our Restaurant!");
        welcomeLabel.setForeground(whiteColor);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);
        return welcomePanel;
    }

    private JPanel createCheckoutPanel() {
        JPanel checkoutPanel = new JPanel(new GridBagLayout());
        checkoutPanel.setBackground(checkoutColor);
        JLabel checkoutLabel = new JLabel("Checkout");
        checkoutLabel.setForeground(whiteColor);
        checkoutLabel.setFont(new Font("Arial", Font.BOLD, 24));
        checkoutPanel.add(checkoutLabel);
        // You can add more components to this panel as needed
        return checkoutPanel;
    }

    // Helper method to create spacers between buttons
    private Component createSpacer() {
        return Box.createRigidArea(new Dimension(0, 5));
    }

    // Helper method to style buttons (buttonsPanel)
    private void styleButton(JButton button, Color bgColor, Color fgColor, int fontSize, int height,
            boolean isCheckout) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMargin(new Insets(5, 20, 5, 20));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setBorderPainted(false); // Remove the border from the button
        button.setFocusPainted(false); // Remove the focus border from the button
        button.setBorder(null); // Set the border to null

        if (isCheckout) {
            // Specific size for checkout button
            button.setPreferredSize(new Dimension(195, height));
            button.setMaximumSize(new Dimension(195, height));
        } else {
            // Default behavior for other buttons
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
            button.setPreferredSize(new Dimension(button.getPreferredSize().width, height));
        }
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
