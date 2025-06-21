import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class RegisterResident extends JFrame {
    private JTextField usernameField, emailField, phoneField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;
    private JComboBox<String> roomTypeComboBox;
    private JButton registerButton, cancelButton;

    public RegisterResident() {
        initComponents();
    }

    private void initComponents() {
        // Set up the window
        setTitle("Register Resident");
        setSize(400, 400);  // Set window size
        setLocationRelativeTo(null);  // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create input fields
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        emailField = new JTextField();
        phoneField = new JTextField();

        // Create combo box for Room Type
        String[] roomTypes = {"Single", "Twin", "Triple"};
        roomTypeComboBox = new JComboBox<>(roomTypes);

        // Set fonts for the fields and button
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        emailField.setFont(new Font("Arial", Font.PLAIN, 20));
        phoneField.setFont(new Font("Arial", Font.PLAIN, 20));
        roomTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 20));

        // Set preferred size for input fields
        usernameField.setPreferredSize(new Dimension(250, 30));
        passwordField.setPreferredSize(new Dimension(250, 30));
        emailField.setPreferredSize(new Dimension(250, 30));
        phoneField.setPreferredSize(new Dimension(250, 30));
        roomTypeComboBox.setPreferredSize(new Dimension(250, 30));

        // Create Show Password checkbox
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

        // Create buttons for Register and Cancel
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerResident();
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 20));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Role();  // Open the Role page
                dispose();  // Close the Register Resident window
            }
        });

        // Set layout and add components
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Padding between components

        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Show Password checkbox
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(showPassword, gbc);

        // Email label and field
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        // Phone label and field
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        add(phoneField, gbc);

        // Room Type label and combo box
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 1;
        add(roomTypeComboBox, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);

        setVisible(true);
    }

    private void registerResident() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String roomType = (String) roomTypeComboBox.getSelectedItem();

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

        // Register resident in the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("resident.txt", true))) {
            String status = "pending"; // Resident status (can be customized)
            String uniqueId = generateUniqueId(); // Generate a unique ID
            bw.write(status + "||" + roomType + "||" + uniqueId + "||" + username + "||" + password + "||" + email + "||" + phone);
            bw.newLine();  // Add a new line after each entry
            JOptionPane.showMessageDialog(RegisterResident.this, "Resident Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new Role();
            dispose();  // Close the Register window after successful registration
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateUniqueId() {
        return "RES-" + System.currentTimeMillis(); // Example unique ID based on the current timestamp
    }

//    public static void main(String[] args) {
//        new RegisterResident();  // Open the Register Resident window
//    }
}
