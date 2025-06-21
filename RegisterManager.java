import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class RegisterManager extends JFrame {

    private JTextField usernameField, emailField, phoneField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;
    private JButton registerButton, cancelButton;

    public RegisterManager() {
        initComponents();
    }

    private void initComponents() {
        // Set up the window
        setTitle("Register Manager");
        setSize(400, 400);
        setLocationRelativeTo(null);  // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Using a GridBagLayout for better control over components
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Padding between components

        // Add Username Label and Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username: "), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        add(usernameField, gbc);

        // Add Password Label and Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password: "), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        // Show Password Checkbox below the password field
        gbc.gridx = 1;
        gbc.gridy = 2;
        showPassword = new JCheckBox("Show Password");
        showPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0);  // Show password
                } else {
                    passwordField.setEchoChar('*');  // Hide password
                }
            }
        });
        add(showPassword, gbc);

        // Add Email Label and Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Email: "), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        add(emailField, gbc);

        // Add Phone Label and Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Phone: "), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        add(phoneField, gbc);

        // Buttons for Register and Cancel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerManager();
            }
        });
        buttonPanel.add(registerButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to ManagerLogin.java when Cancel is pressed
                new Role();  // Open the Manager Login page
                dispose();  // Close the registration form
            }
        });
        buttonPanel.add(cancelButton);

        gbc.gridx = 1;
        gbc.gridy = 5;
        add(buttonPanel, gbc);

        // Make window visible
        setVisible(true);
    }

    private void registerManager() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate username (only alphabets allowed)
        if (!username.matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "Username must only contain alphabets.");
            return;
        }

        // Validate password (check length, characters, numbers, special characters)
        if (password.length() < 8 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*") || !password.matches(".*[@$%&].*")) {
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long, contain numbers, alphabets, and special characters (@ $ % & ).");
            return;
        }

        // Validate email (contains '@')
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(null, "Email must contain '@' symbol.");
            return;
        }

        // Validate phone (only numbers)
        if (!phone.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(null, "Phone number must only contain numbers.");
            return;
        }       

        // Add the new manager record to the manager.txt file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("manager.txt", true))) {
            // Format: pending||uniqueId||username||password||email||phone
            String uniqueId = generateUniqueId();
            writer.write("pending||" + uniqueId + "||" + username + "||" + password + "||" + email + "||" + phone);
            writer.newLine();  // Add a new line after each entry
            JOptionPane.showMessageDialog(this, "Manager registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // After successful registration, redirect to ManagerLogin.java
            new Role();  // Open the Manager Login page
            dispose();  // Close the registration form

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateUniqueId() {
        // Generate a unique ID for the manager (this could be customized based on your needs)
        return "MGR-" + System.currentTimeMillis(); // Example unique ID based on the current timestamp
    }

//    public static void main(String[] args) {
//        new RegisterManager();  // Open the Register Manager window
//    }
}
