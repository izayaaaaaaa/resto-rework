package restaurant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        createUI();
    }

    private void createUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });
        add(loginButton);

        setSize(300, 150);
        setLocationRelativeTo(null);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}
