package restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.swing.border.EmptyBorder;

public class RestaurantDashboard extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private Color orangeColor = new Color(255, 102, 0);
    private Color whiteColor = Color.WHITE;
    private Color checkoutColor = new Color(239, 23, 23);
    private List<MenuItem> selectedItems = new ArrayList<>();

    public RestaurantDashboard() {
        setUndecorated(true);
        createUI();
        createMenuItems(); // Populate UI with menu items
    }

    private void createUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(whiteColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        buttonPanel.setPreferredSize(new Dimension(289, buttonPanel.getPreferredSize().height));

        // Menu button
        JButton menuButton = new JButton("Menu");
        styleButton(menuButton, whiteColor, orangeColor, 48, 150, false);
        menuButton.addActionListener(e -> cardLayout.show(cardPanel, "Welcome"));
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
        checkoutButton.addActionListener(e -> {
            Order currentOrder = new Order("OrderID"); // Replace "OrderID" with actual ID logic
            for (MenuItem selectedItem : selectedItems) {
                currentOrder.addItem(selectedItem);
            }

            // Now you can use currentOrder to generate a receipt and show it in the
            // checkout panel
            // ... rest of the checkout logic ...
        });
        buttonPanel.add(createSpacer());
        buttonPanel.add(checkoutButton);
        buttonPanel.add(createSpacer());

        add(buttonPanel, BorderLayout.WEST);

        // Welcome card
        cardPanel.add(createWelcomePanel(), "Welcome");
        cardPanel.setBackground(whiteColor);

        // Checkout card
        cardPanel.add(createCheckoutPanel(), "Checkout");

        // Set the welcome card as the initial view
        cardLayout.show(cardPanel, "Welcome");
        add(cardPanel, BorderLayout.CENTER);

        cardPanel.setBackground(orangeColor);

        setSize(new Dimension(1350, 791));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createItemsPanel(List<MenuItem> items) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10)); // Adjust grid layout rows and columns as needed
        panel.setBackground(orangeColor);

        for (MenuItem item : items) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBackground(whiteColor);
            itemPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            // Load, resize, and add the image
            try {
                Image img = ImageIO.read(new File(item.getPhotoPath()));
                ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(250, 150, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(imageIcon);
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemPanel.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception or display a placeholder image
            }

            // Add the name label
            JLabel nameLabel = new JLabel(item.getName() + " (" + item.getPrice() + ")", SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemPanel.add(nameLabel);

            // Quantity panel
            JPanel quantityPanel = new JPanel();
            quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));
            quantityPanel.setBackground(whiteColor);
            JLabel quantityLabel = new JLabel("Quantity: ");
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1)); // Spinner with a range from
                                                                                           // 0 to 100 and step 1
            quantitySpinner.addChangeListener(e -> {
                item.setQuantity((Integer) quantitySpinner.getValue());
            });
            quantityPanel.add(quantityLabel);
            quantityPanel.add(quantitySpinner);
            itemPanel.add(quantityPanel);

            // Purchase checkbox
            JCheckBox purchaseCheckbox = new JCheckBox("Purchase");
            purchaseCheckbox.setBackground(whiteColor);
            purchaseCheckbox.setAlignmentX(Component.CENTER_ALIGNMENT);
            purchaseCheckbox.addActionListener(e -> {
                if (purchaseCheckbox.isSelected() && item.getQuantity() > 0) {
                    selectedItems.add(item);
                } else {
                    selectedItems.remove(item);
                }
            });
            itemPanel.add(purchaseCheckbox);

            panel.add(itemPanel);
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
        JPanel checkoutPanel = new JPanel();
        checkoutPanel.setLayout(new BorderLayout());
        checkoutPanel.setBackground(checkoutColor);

        // ScrollPane for order breakdown
        JTextArea orderBreakdown = new JTextArea();
        orderBreakdown.setEditable(false); // The receipt should not be editable directly
        JScrollPane scrollPane = new JScrollPane(orderBreakdown);
        checkoutPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel for VAT, Subtotal, and Total Payment
        JPanel paymentPanel = new JPanel(new GridLayout(3, 2, 5, 5)); // Use GridLayout for labels and text fields
        paymentPanel.setBackground(checkoutColor);

        JLabel vatLabel = new JLabel("VAT (₱):");
        JTextField vatField = new JTextField("0", 10);
        vatField.setEditable(false);

        JLabel subtotalLabel = new JLabel("Subtotal (₱):");
        JTextField subtotalField = new JTextField("0", 10);
        subtotalField.setEditable(false);

        JLabel totalPaymentLabel = new JLabel("Total Payment (₱):");
        JTextField totalPaymentField = new JTextField("0", 10);
        totalPaymentField.setEditable(false);

        paymentPanel.add(vatLabel);
        paymentPanel.add(vatField);
        paymentPanel.add(subtotalLabel);
        paymentPanel.add(subtotalField);
        paymentPanel.add(totalPaymentLabel);
        paymentPanel.add(totalPaymentField);

        checkoutPanel.add(paymentPanel, BorderLayout.SOUTH);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 5, 5)); // Stack buttons vertically
        buttonPanel.setBackground(checkoutColor);

        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.setBackground(Color.GREEN);

        JButton printReceiptButton = new JButton("Print Receipt");
        printReceiptButton.setBackground(Color.RED);

        JButton cancelOrderButton = new JButton("Cancel Order");
        cancelOrderButton.setBackground(Color.ORANGE);

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(Color.YELLOW);

        // Add buttons to the panel
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(printReceiptButton);
        buttonPanel.add(cancelOrderButton);
        buttonPanel.add(exitButton);

        // Add the button panel to the right side of the checkout panel
        checkoutPanel.add(buttonPanel, BorderLayout.EAST);

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
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(null);

        if (isCheckout) {
            button.setPreferredSize(new Dimension(195, height));
            button.setMaximumSize(new Dimension(195, height));
        } else {
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
            button.setPreferredSize(new Dimension(button.getPreferredSize().width, height));
        }
    }

    // generate the menu items via the menu item factory
    private void createMenuItems() {
        MenuItemFactory factory = new MenuItemFactory();

        Category pasta = new Category("Pasta");
        pasta.addItem(
                factory.createMenuItem("Spaghetti Bolognese", 149.00, "src/main/java/photos/a_spag bolognese.jpg"));
        pasta.addItem(factory.createMenuItem("Cheesy Lasagna", 169.00, "src/main/java/photos/c_lasagna.jpg"));
        pasta.addItem(factory.createMenuItem("Carbonara", 159.00, "src/main/java/photos/c_carbonara.jpg"));
        pasta.addItem(factory.createMenuItem("Vegetarian Pasta", 149.00, "src/main/java/photos/b_vegetaian pasta.jpg"));
        cardPanel.add(createItemsPanel(pasta.getItems()), "Pasta");

        Category burgers = new Category("Burgers");
        burgers.addItem(factory.createMenuItem("Chicken Burger", 69.00, "src/main/java/photos/d_chicken burger.jpg"));
        burgers.addItem(factory.createMenuItem("Cheese Burger", 59.00, "src/main/java/photos/f_cheese burger.jpg"));
        burgers.addItem(factory.createMenuItem("Double Patty Burger", 89.00, "src/main/java/photos/f_doublepatty.jpg"));
        burgers.addItem(factory.createMenuItem("Veggie Burger", 59.00, "src/main/java/photos/e_veggie burger.jpg"));
        cardPanel.add(createItemsPanel(burgers.getItems()), "Burgers");

        Category pizzas = new Category("Pizzas");
        pizzas.addItem(factory.createMenuItem("Pepperoni Pizza", 169.00, "src/main/java/photos/g_pepperoni pizza.jpg"));
        pizzas.addItem(factory.createMenuItem("Cheese Pizza", 159.00, "src/main/java/photos/h_cheese pizza.jpg"));
        pizzas.addItem(factory.createMenuItem("Mushroom Pizza", 179.00, "src/main/java/photos/i_mushroom.jpg"));
        pizzas.addItem(factory.createMenuItem("Pesto Pizza", 149.00, "src/main/java/photos/i_pesto pizza.jpg"));
        cardPanel.add(createItemsPanel(pizzas.getItems()), "Pizzas");

        Category desserts = new Category("Desserts");
        desserts.addItem(factory.createMenuItem("Chocolate Cake", 150.00, "src/main/java/photos/j_choco cake.jpg"));
        desserts.addItem(factory.createMenuItem("Berry Cheesecake", 120.00, "src/main/java/photos/k_cheesecake.jpg"));
        desserts.addItem(factory.createMenuItem("Cookies & Cream", 70.00, "src/main/java/photos/l_ice cream.jpg"));
        desserts.addItem(factory.createMenuItem("Creme Brulee", 60.00, "src/main/java/photos/l_cremebrulee.jpg"));
        cardPanel.add(createItemsPanel(desserts.getItems()), "Desserts");

        Category drinks = new Category("Drinks");
        drinks.addItem(factory.createMenuItem("Chocolate Milkshake", 90.00, "src/main/java/photos/m_milkshake.jpg"));
        drinks.addItem(factory.createMenuItem("Caramel Frappe", 85.00, "src/main/java/photos/n_frappe.jpg"));
        drinks.addItem(factory.createMenuItem("Iced Tea", 30.00, "src/main/java/photos/o_iced tea.jpg"));
        drinks.addItem(factory.createMenuItem("Cafe Americano", 60.00, "src/main/java/photos/p_americano.jpg"));
        cardPanel.add(createItemsPanel(drinks.getItems()), "Drinks");
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
