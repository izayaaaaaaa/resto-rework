package restaurant;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton viewPasswordButton;
    private ImageIcon eyeOpenIcon = new ImageIcon("src/main/java/photos/1eye.png");
    private ImageIcon eyeClosedIcon = new ImageIcon("src/main/java/photos/2eye.png");

    public LoginForm() {
        getContentPane().setBackground(Color.WHITE); // Set the background to white
        setUndecorated(true);
        createUI();
        Border orangeBorder = BorderFactory.createLineBorder(new Color(255, 102, 0)); // Create the border
        ((JComponent) getContentPane()).setBorder(orangeBorder); // Set the border to the content pane
    }

    private void createUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title
        JLabel titleLabel = new JLabel("Restaurant Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.BLACK);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 20, 10);
        add(titleLabel, gbc);

        // Username Label and Field
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                usernameField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Add padding
        add(usernameField, gbc);

        // Invisible label for alignment
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel(""), gbc);

        // Password Label and Field
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                passwordField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Add padding
        add(passwordField, gbc);

        // View Password Button
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        viewPasswordButton = new JButton(eyeClosedIcon);
        viewPasswordButton.setBorderPainted(false);
        viewPasswordButton.setBackground(Color.WHITE);
        viewPasswordButton.setContentAreaFilled(false);
        viewPasswordButton.setFocusPainted(false);
        viewPasswordButton.setOpaque(true);
        viewPasswordButton.addActionListener(e -> togglePasswordVisibility());
        add(viewPasswordButton, gbc);

        // Button Panel with FlowLayout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // 10-pixel gap between components
        buttonPanel.setOpaque(false); // Make the panel transparent

        JButton loginButton = new JButton("Login");
        styleButton(loginButton, new Color(255, 102, 0), Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton
                .addActionListener(e -> authenticate(usernameField.getText(), new String(passwordField.getPassword())));
        buttonPanel.add(loginButton);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 5, 10, 10);
        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(232, 58, 15), Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 18));
        cancelButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(cancelButton);

        // Add the button panel to the main layout
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(buttonPanel, gbc);

        setSize(720, 390);
        setLocationRelativeTo(null);
    }

    private void togglePasswordVisibility() {
        if (passwordField.getEchoChar() != '\0') {
            passwordField.setEchoChar('\0');
            viewPasswordButton.setIcon(eyeOpenIcon);
        } else {
            passwordField.setEchoChar('*');
            viewPasswordButton.setIcon(eyeClosedIcon);
        }
        // Refresh the password field and the button
        passwordField.repaint();
        passwordField.revalidate();
        viewPasswordButton.repaint();
        viewPasswordButton.revalidate();
    }

    private void authenticate(String username, String password) {
        // For demonstration, using hardcoded credentials
        if ("admin".equals(username) && "password".equals(password)) {
            // Successful login
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new RestaurantDashboard();
                }
            });
            this.dispose(); // Close the login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button, Color backgroundColor, Color textColor) {
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}
