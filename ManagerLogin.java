import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class ManagerLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;
    private JButton loginButton;
    private JButton registerButton; // Renamed Sign Up to Register
    private JLabel loginLabel;

    public ManagerLogin() {
        initComponents();
    }

    private void initComponents() {
        // Set up the window
        setTitle("Manager Login");
        setSize(1300, 750); // Set window size
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the title label and customize it
        loginLabel = new JLabel("Manager Login", JLabel.CENTER);
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
        registerButton = new JButton("Register"); // Renamed to Register

        // Set font size for input fields and buttons
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        loginButton.setFont(new Font("Arial", Font.BOLD, 28));
        registerButton.setFont(new Font("Arial", Font.BOLD, 28)); // Set font for Register button

        // Set preferred size for input fields to make them larger
        usernameField.setPreferredSize(new Dimension(250, 40));  // Increased width and height
        passwordField.setPreferredSize(new Dimension(250, 40));  // Increased width and height

        // Add action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (login(username, password)) {
                    System.out.println("Manager login successful. Welcome " + username + "!");
                    // Proceed to the Manager Menu
                    new ManagerMenu();  // Open Manager Menu window
                    dispose();  // Close the login page after successful login
                } else {
                    JOptionPane.showMessageDialog(ManagerLogin.this, "Login failed. Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add action listener for Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open RegisterManager page
                new RegisterManager(); // Open the registration page
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

        // Register and Login buttons
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(registerButton, gbc); // Place the Register button on the left

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(loginButton, gbc); // Place the Login button on the right

        // Center the buttons horizontally
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(new JLabel(" "), gbc);  // This will push the buttons to the center

        // Apply padding between all elements and ensure proper spacing
        gbc.insets = new Insets(10, 20, 10, 20);  // Adjust padding for labels and fields
        setVisible(true);
    }

    private boolean login(String username, String password) {
        // Load the manager's credentials from the manager.txt file
        try {
            // Open manager.txt file (make sure it's located in your project directory)
            File file = new File("manager.txt");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                // Assuming manager.txt has lines in format: "status||unique_id||username||password||email||phone"
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|\\|"); // Split into multiple parts using "||"

                // Check if the username matches
                if (parts.length >= 4 && parts[2].equals(username)) {
                    // Check if the status is pending
                    if (parts[0].equalsIgnoreCase("pending")) {
                        JOptionPane.showMessageDialog(this, "Login failed. Your account is still pending approval.", "Error", JOptionPane.ERROR_MESSAGE);
                        return false; // Return false if the status is "pending"
                    }
                    // Check if the status is approved and the password matches
                    if (parts[0].equalsIgnoreCase("approved") && parts[3].equals(password)) {
                        System.out.println("Login successful. Welcome, " + username + "!");
                        return true; // Login successful
                    } else {
                        // If password does not match or status is not approved
                        JOptionPane.showMessageDialog(this, "Login failed. Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                        return false; // Incorrect password
                    }
                }
            }

            fileScanner.close(); // Close the scanner after reading the file
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error: Manager data file not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // This will only be reached if the username does not exist in the file
        JOptionPane.showMessageDialog(this, "Login failed. Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        return false; // Return false if the username is not found or credentials are incorrect
    }

//    public static void main(String[] args) {
//        new ManagerLogin();  // Open the Manager login window
//    }
}
