import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class ResidentLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel loginLabel;

    public ResidentLogin() {
        initComponents();
    }

    private void initComponents() {
        // Set up the window
        setTitle("Resident Login");
        setSize(1300, 750); // Set window size
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the title label and customize it
        loginLabel = new JLabel("Resident Login", JLabel.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 40)); // Set the title font

        // Create input fields
        usernameField = new JTextField();
        passwordField = new JPasswordField();
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

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // Set font size for input fields and buttons
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        loginButton.setFont(new Font("Arial", Font.BOLD, 28));
        registerButton.setFont(new Font("Arial", Font.BOLD, 28));

        // Set preferred size for input fields to make them larger
        usernameField.setPreferredSize(new Dimension(350, 40));  // Increased width and height
        passwordField.setPreferredSize(new Dimension(350, 40));  // Increased width and height

        // Add action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (login(username, password)) {
                    System.out.println("Resident login successful. Welcome " + username + "!");
                    // Proceed to the Resident's dashboard
                    new ResidentMenu(username);  // Open Resident Dashboard window
                    dispose();  // Close the login page after successful login
                } else {
                    JOptionPane.showMessageDialog(ResidentLogin.this, "Login failed. Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add action listener for Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open RegisterResident page
                new RegisterResident(); // Open the registration page
            }
        });

        // Set layout and add components
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);  // Padding for the top
        add(loginLabel, gbc);

        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Show Password checkbox below the password field
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(showPassword, gbc);

        // Register and Login buttons (move them to the right)
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 50, 10, 10); // Move Register button to the right
        add(registerButton, gbc);  // Place the Register button on the left

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 50, 10, 10); // Move Login button to the right
        add(loginButton, gbc); // Place the Login button on the right

        // Apply padding between all elements and ensure proper spacing
        gbc.insets = new Insets(10, 20, 10, 20);  // Adjust padding for labels and fields
        setVisible(true);
    }

    private boolean login(String username, String password) {
        // Load the resident's credentials from the resident.txt file
        try {
            File file = new File("resident.txt");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|\\|"); // Split into multiple parts using "||"

                // Check if the username matches
                if (parts.length >= 4 && parts[3].equals(username)) {
                    // Check if the status is approved
                    if (parts[0].equalsIgnoreCase("approved")) {
                        if (parts[4].equals(password)) {  // Check if password matches
                            return true;  // Login successful
                        } else {
                            JOptionPane.showMessageDialog(this, "Login failed. Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE);
                            return false;  // Incorrect password
                        }
                    } else {
                        // If the status is not approved, show an appropriate message and return false
                        JOptionPane.showMessageDialog(this, "Login failed. Your account is not approved.", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;  // Account not approved
                    }
                }
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error: Resident data file not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // If the username wasn't found, display the "Incorrect username or password" message
        JOptionPane.showMessageDialog(this, "Login failed. Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        return false; // Username not found or incorrect password
    }

//    public static void main(String[] args) {
//        new ResidentLogin();  // Open the Resident login window
//    }
}
