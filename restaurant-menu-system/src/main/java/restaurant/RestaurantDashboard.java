package restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class RestaurantDashboard extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private Color orangeColor = new Color(255, 102, 0);
    private Color whiteColor = Color.WHITE;
    private Color checkoutColor = new Color(239, 23, 23);
    private Color paymentColor = new Color(204, 204, 204);
    private List<MenuItem> selectedItems = new ArrayList<>();
    private JTextPane orderBreakdown = new JTextPane();
    private JTextField vatField;
    private JTextField subtotalField;
    private JTextField totalPaymentField;
    private List<JSpinner> quantitySpinners = new ArrayList<>();
    private JLabel jLabelDate;
    private JLabel jLabelTime;
    JButton activeCategoryButton = null;

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
        styleMenuButton(menuButton, whiteColor, orangeColor, 48, 150, false);
        menuButton.addActionListener(e -> cardLayout.show(cardPanel, "Welcome"));
        buttonPanel.add(menuButton);
        buttonPanel.add(createSpacer());

        // Category buttons
        String[] categories = { "Pasta", "Burgers", "Pizzas", "Desserts", "Drinks" };
        for (String category : categories) {
            JButton button = new JButton(category);
            styleMenuButton(button, orangeColor, whiteColor, 36, 80, false);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Check if there's an active button and reset its color
                    if (activeCategoryButton != null) {
                        activeCategoryButton.setBackground(orangeColor); // Reset the color
                    }

                    cardLayout.show(cardPanel, category);
                    activeCategoryButton = button; // Set the current button as active
                    button.setBackground(new Color(255, 153, 51)); // Change the color to indicate activeness
                }
            });
            buttonPanel.add(createSpacer());
            buttonPanel.add(button);
            buttonPanel.add(createSpacer());
        }

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Checkout button
        JButton checkoutButton = new JButton("Check Order");
        styleMenuButton(checkoutButton, checkoutColor, whiteColor, 24, 70, true);
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Order currentOrder = new Order("456489"); // Replace with actual logic for generating unique IDs

                // Add selected items to the order
                for (MenuItem selectedItem : selectedItems) {
                    currentOrder.addItem(selectedItem);
                }

                // Create a receipt for the current order
                Receipt receipt = new Receipt(currentOrder);
                // Set the StyledDocument to orderBreakdown JTextArea to display centered text
                StyledDocument receiptDoc = receipt.printReceipt();
                orderBreakdown.setStyledDocument(receiptDoc);

                // Calculate subtotal, VAT, and total
                double subtotal = currentOrder.calculateTotal();
                double VAT = receipt.getTax(subtotal);
                double total = subtotal + VAT;

                // Update the text fields
                subtotalField.setText(String.format("%.2f", subtotal));
                vatField.setText(String.format("%.2f", VAT));
                totalPaymentField.setText(String.format("%.2f", total));

                cardLayout.show(cardPanel, "Checkout");
            }
        });
        buttonPanel.add(createSpacer());
        buttonPanel.add(checkoutButton);
        buttonPanel.add(createSpacer());

        // Welcome card
        cardPanel.add(createWelcomePanel(), "Welcome");
        cardPanel.setBackground(whiteColor);

        // Checkout card
        cardPanel.add(createCheckoutPanel(), "Checkout");

        add(buttonPanel, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);

        cardPanel.setBackground(orangeColor);

        setSize(new Dimension(1350, 791));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createItemsPanel(List<MenuItem> items) {
        // Set preferred size for itemPanels
        Dimension itemPanelSize = new Dimension(285, 295);

        // GridLayout with 2 rows and 2 columns for a total of 4 components
        JPanel itemsGridPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        itemsGridPanel.setPreferredSize(new Dimension(2 * (285 + 10), 2 * (295 + 10)));
        itemsGridPanel.setBackground(orangeColor);

        for (MenuItem item : items) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBackground(orangeColor);
            itemPanel.setPreferredSize(itemPanelSize);
            itemPanel.setMaximumSize(itemPanelSize);
            itemPanel.setMinimumSize(itemPanelSize);

            // Custom titled border with white title
            TitledBorder titledBorder = BorderFactory.createTitledBorder(item.getName());
            titledBorder.setTitleJustification(TitledBorder.CENTER);
            titledBorder.setTitlePosition(TitledBorder.TOP);
            titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 24));
            titledBorder.setTitleColor(Color.WHITE); // Set the title color to white
            itemPanel.setBorder(titledBorder);

            // Load, resize, and add the image
            try {
                Image img = ImageIO.read(new File(item.getPhotoPath()));
                ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(250, 150, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(imageIcon);
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemPanel.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Create a new JPanel for details (price, quantity, purchase)
            JPanel detailsPanel = new JPanel(new GridLayout(3, 2)); // 3 rows, 2 columns
            detailsPanel.setBackground(orangeColor);
            detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 75, 0, 10));

            // Create and add the "Price:" label
            JLabel priceTextLabel = new JLabel("Price: ");
            priceTextLabel.setFont(new Font("Times New Roman", Font.BOLD, 13));
            priceTextLabel.setForeground(whiteColor);
            detailsPanel.add(priceTextLabel);

            // Create and add the actual price label
            JLabel actualPriceLabel = new JLabel(String.format("%.2f", item.getPrice()));
            actualPriceLabel.setFont(new Font("Times New Roman", Font.BOLD, 13));
            actualPriceLabel.setForeground(whiteColor);
            detailsPanel.add(actualPriceLabel);

            // Quantity panel
            JLabel quantityLabel = new JLabel("Quantity: ");
            quantityLabel.setFont(new Font("Times New Roman", Font.BOLD, 13));
            quantityLabel.setForeground(whiteColor);

            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            quantitySpinner.setMaximumSize(new Dimension(50, 20));
            quantitySpinners.add(quantitySpinner);
            quantitySpinner.addChangeListener(e -> {
                item.setQuantity((Integer) quantitySpinner.getValue());
            });

            // Wrapper panel for quantitySpinner
            JPanel quantitySpinnerWrapper = new JPanel();
            quantitySpinnerWrapper.setLayout(new FlowLayout(FlowLayout.LEFT)); // Or use BoxLayout
            quantitySpinnerWrapper.setBackground(orangeColor);
            quantitySpinnerWrapper.add(quantitySpinner);

            detailsPanel.add(quantityLabel);
            detailsPanel.add(quantitySpinnerWrapper);

            // Purchase checkbox
            JLabel purchaseLabel = new JLabel("Purchase: ");
            purchaseLabel.setForeground(whiteColor);
            purchaseLabel.setFont(new Font("Times New Roman", Font.BOLD, 13));
            JCheckBox purchaseCheckbox = new JCheckBox();
            purchaseCheckbox.setBackground(orangeColor);
            purchaseCheckbox.addActionListener(e -> {
                if (purchaseCheckbox.isSelected() && item.getQuantity() > 0) {
                    selectedItems.add(item);
                } else {
                    selectedItems.remove(item);
                }
            });
            detailsPanel.add(purchaseLabel);
            detailsPanel.add(purchaseCheckbox);

            itemPanel.add(detailsPanel);

            itemsGridPanel.add(itemPanel);
        }

        // Intermediate panel with GridBagLayout for centering
        JPanel intermediatePanel = new JPanel(new GridBagLayout());
        intermediatePanel.setBackground(orangeColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        intermediatePanel.add(itemsGridPanel, gbc);

        return intermediatePanel;
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(orangeColor);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Invisible filler at the top
        constraints.weighty = 1.0;
        welcomePanel.add(Box.createVerticalGlue(), constraints);

        // "Welcome to" label setup
        JLabel welcomeLabel = new JLabel("Welcome to", SwingConstants.CENTER);
        welcomeLabel.setForeground(whiteColor);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 85));
        constraints.gridy = 1;
        constraints.weighty = 0;
        welcomePanel.add(welcomeLabel, constraints);

        // "Our Restaurant" label setup
        JLabel restaurantLabel = new JLabel("Our Restaurant!", SwingConstants.CENTER);
        restaurantLabel.setForeground(whiteColor);
        restaurantLabel.setFont(new Font("Arial", Font.BOLD, 85));
        constraints.gridy = 2;
        constraints.insets = new Insets(20, 0, 20, 0);
        welcomePanel.add(restaurantLabel, constraints);

        // Reset insets to default
        constraints.insets = new Insets(0, 0, 0, 0);

        // Date label setup
        jLabelDate = new JLabel("", SwingConstants.CENTER);
        jLabelDate.setFont(new Font("Arial", Font.BOLD, 36));
        jLabelDate.setForeground(whiteColor);
        constraints.gridy = 3;
        constraints.insets = new Insets(5, 0, 5, 0);
        welcomePanel.add(jLabelDate, constraints);

        // Time label setup
        jLabelTime = new JLabel("", SwingConstants.CENTER);
        jLabelTime.setFont(new Font("Arial", Font.BOLD, 36));
        jLabelTime.setForeground(whiteColor);
        constraints.gridy = 4;
        welcomePanel.add(jLabelTime, constraints);

        // Invisible filler at the bottom
        constraints.gridy = 5;
        constraints.weighty = 1.0;
        welcomePanel.add(Box.createVerticalGlue(), constraints);

        // Display the date and time
        showDate();
        showTime();

        return welcomePanel;
    }

    public void showTime() {
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("hh:mm a");
                jLabelTime.setText(s.format(d));
            }
        }).start();
    }

    public void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("EEEE, MM/dd/yyyy");
        jLabelDate.setText(s.format(d));
    }

    private JPanel createCheckoutPanel() {
        JPanel checkoutPanel = new JPanel(new GridBagLayout());
        checkoutPanel.setBackground(whiteColor);
        checkoutPanel.setBorder(BorderFactory.createLineBorder(orangeColor, 5));

        GridBagConstraints gbc = new GridBagConstraints();

        // Create a filler panel for the left column
        JPanel fillerPanel = new JPanel();
        fillerPanel.setBackground(whiteColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        checkoutPanel.add(fillerPanel, gbc);

        // Panel to hold the order breakdown and paymentPanel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(whiteColor);

        // ScrollPane for order breakdown
        // orderBreakdown.setLineWrap(true);
        // orderBreakdown.setWrapStyleWord(true);

        // ScrollPane for orderBreakdown
        orderBreakdown.setEditable(false);
        orderBreakdown.setPreferredSize(new Dimension(395, 695));
        orderBreakdown.setBorder(BorderFactory.createLineBorder(paymentColor, 15));
        JScrollPane scrollPane = new JScrollPane(orderBreakdown);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(395, 695));
        scrollPane.setMinimumSize(new Dimension(395, 695));
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Set an empty border

        // Adding scrollPane to contentPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 2; // Spanning two rows
        gbc.weightx = 1.0;
        gbc.weighty = 2.0; // Assign more weight to the scrollPane
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 0, 10); // Padding (Top, Left, Bottom, Right)
        contentPanel.add(scrollPane, gbc);

        // Panel for VAT, Subtotal, and Total Payment
        JPanel paymentPanel = new JPanel(new GridLayout(3, 2, 20, 5));
        paymentPanel.setBackground(paymentColor);
        paymentPanel.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 30));

        JLabel vatLabel = new JLabel("VAT (₱):");
        vatField = new JTextField("0", 10);
        vatField.setEditable(false);

        JLabel subtotalLabel = new JLabel("Subtotal (₱):");
        subtotalField = new JTextField("0", 10);
        subtotalField.setEditable(false);

        JLabel totalPaymentLabel = new JLabel("Total Payment (₱):");
        totalPaymentField = new JTextField("0", 10);
        totalPaymentField.setEditable(false);

        paymentPanel.add(vatLabel);
        paymentPanel.add(vatField);
        paymentPanel.add(subtotalLabel);
        paymentPanel.add(subtotalField);
        paymentPanel.add(totalPaymentLabel);
        paymentPanel.add(totalPaymentField);

        // Adding paymentPanel to contentPanel
        gbc.gridx = 0;
        gbc.gridy = 2; // Start at row 2, after the scrollPane
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1; // Span only one row
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Assign less weight compared to the scrollPane
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(0, 10, 10, 10); // Padding (Top, Left, Bottom, Right)
        contentPanel.add(paymentPanel, gbc);

        // Adding contentPanel to checkoutPanel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 10, 0); // Padding for the entire contentPanel
        checkoutPanel.add(contentPanel, gbc);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(whiteColor);

        // Custom font for buttons
        Font buttonPanelBtnsFont = new Font("Arial", Font.BOLD, 24);

        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.setBackground(new Color(51, 153, 0));
        placeOrderButton.setForeground(whiteColor);
        placeOrderButton.setPreferredSize(new Dimension(185, 50));
        placeOrderButton.setFont(buttonPanelBtnsFont);
        placeOrderButton.setBorderPainted(false);
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedItems.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select an item first before placing order.");
                } else {
                    // Create a new order and add selected items
                    Order currentOrder = new Order("456489"); // Replace with actual logic for generating unique IDs
                    for (MenuItem selectedItem : selectedItems) {
                        currentOrder.addItem(selectedItem);
                    }
                    Receipt receipt = new Receipt(currentOrder);

                    double subtotal = currentOrder.calculateTotal();
                    double tax = receipt.getTax(subtotal);
                    double total = subtotal + tax;

                    // Append tax, subtotal, and total to the JTextArea
                    try {
                        StyledDocument doc = orderBreakdown.getStyledDocument();

                        // Define the center alignment attribute set
                        SimpleAttributeSet center = new SimpleAttributeSet();
                        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                        StyleConstants.setFontFamily(center, "Arial");
                        StyleConstants.setFontSize(center, 12);

                        // Append tax, subtotal, and total to the JTextArea
                        String footer = "\n \t\t==============================\n"
                                + "\t\t Tax: \t" + String.format("%.2f", tax) + "\n"
                                + "\t\t Subtotal: \t" + String.format("%.2f", subtotal) + "\n"
                                + "\t\t Total: \t" + String.format("%.2f", total) + "\n"
                                + " ===================================================\n\n";
                        doc.insertString(doc.getLength(), footer, null);

                        // Append and center the 'Official Receipt' and 'Thank You' text
                        String centeredText = "\tOFFICIAL RECEIPT\n"
                                + "\tTHANK YOU AND COME AGAIN";
                        doc.insertString(doc.getLength(), centeredText, null);
                        int start = doc.getLength() - centeredText.length();
                        int end = doc.getLength();
                        doc.setParagraphAttributes(start, end - start, center, false);
                    } catch (BadLocationException error) {
                        error.printStackTrace();
                    }

                    // Update the text fields
                    vatField.setText(String.format("%.2f", tax));
                    subtotalField.setText(String.format("%.2f", subtotal));
                    totalPaymentField.setText(String.format("%.2f", total));

                    // Disable the 'Place Order' button to prevent duplicate orders
                    placeOrderButton.setEnabled(false);
                }
            }
        });

        JButton printReceiptButton = new JButton("Print Receipt");
        printReceiptButton.setBackground(orangeColor);
        printReceiptButton.setForeground(whiteColor);
        printReceiptButton.setPreferredSize(new Dimension(185, 50));
        printReceiptButton.setFont(buttonPanelBtnsFont);
        printReceiptButton.setBorderPainted(false);
        printReceiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean complete = orderBreakdown.print();
                    if (complete) {
                        // Inform the user that the print was successful
                        JOptionPane.showMessageDialog(null, "Receipt Printed");
                    } else {
                        // Inform the user that the print was cancelled
                        JOptionPane.showMessageDialog(null, "Printing Cancelled");
                    }
                } catch (PrinterException pe) {
                    // Handle the printer exception
                    JOptionPane.showMessageDialog(null, "Printing Failed: " + pe.getMessage());
                }
            }
        });

        JButton cancelOrderButton = new JButton("Cancel Order");
        cancelOrderButton.setBackground(checkoutColor);
        cancelOrderButton.setForeground(whiteColor);
        cancelOrderButton.setPreferredSize(new Dimension(185, 50));
        cancelOrderButton.setFont(buttonPanelBtnsFont);
        cancelOrderButton.setBorderPainted(false);
        cancelOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset all spinners
                for (JSpinner spinner : quantitySpinners) {
                    spinner.setValue(0);
                }

                // Clear the selected items list
                selectedItems.clear();

                // Reset the JTextArea and text fields
                orderBreakdown.setText("");
                vatField.setText("0");
                subtotalField.setText("0");
                totalPaymentField.setText("0");

                // Re-enable the 'Place Order' button if needed
                placeOrderButton.setEnabled(true);

                // Refresh the UI if necessary
                cardPanel.revalidate();
                cardPanel.repaint();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(255, 204, 0));
        exitButton.setForeground(whiteColor);
        exitButton.setPreferredSize(new Dimension(185, 50));
        exitButton.setFont(buttonPanelBtnsFont);
        exitButton.setBorderPainted(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add buttons to the panel
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; // Align to the left
        g.gridy = GridBagConstraints.RELATIVE; // Place components relative to each other
        g.anchor = GridBagConstraints.SOUTHWEST; // Anchor buttons to the lower left
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(placeOrderButton, g);
        buttonPanel.add(printReceiptButton, g);
        buttonPanel.add(cancelOrderButton, g);
        buttonPanel.add(exitButton, g);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        checkoutPanel.add(buttonPanel, gbc);

        return checkoutPanel;
    }

    // Helper method to create spacers between buttons in main menu
    private Component createSpacer() {
        return Box.createRigidArea(new Dimension(0, 5));
    }

    // Helper method to style main menu buttons
    private void styleMenuButton(JButton button, Color bgColor, Color fgColor, int fontSize, int height,
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