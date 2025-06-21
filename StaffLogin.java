import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class StaffLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;
    private JButton loginButton;
    private JButton registerButton;

    public StaffLogin() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Staff Login");
        setSize(1300, 750);  // Adjust window size
        setLocationRelativeTo(null);  // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Staff Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));  
        usernameField = new JTextField(20);  

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));  
        passwordField = new JPasswordField(20);  
        
        // Create Show Password checkbox and add action listener
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
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));  
        loginButton.setBackground(new Color(103, 171, 240));  
        loginButton.setForeground(Color.WHITE);  
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));  
        registerButton.setBackground(new Color(103, 171, 240));  
        registerButton.setForeground(Color.WHITE);  
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the RegisterStaff page when "Register" is clicked
                new RegisterStaff();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

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
        gbc.insets = new Insets(10, 50, 10, 10); // Move Register button to the right
        add(registerButton, gbc);  // Place the Register button on the left

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 50, 10, 10); // Move Login button to the right
        add(loginButton, gbc); // Place the Login button on the right

        setVisible(true);
    }

    private void loginAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        boolean loginSuccessful = validateLogin(username, password);

        if (loginSuccessful) {
            // Pass the username to StaffMenu when login is successful
            StaffMenu staffMenu = new StaffMenu(username);
            staffMenu.setVisible(true);
            dispose();  
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
        }
    }

    private boolean validateLogin(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("staff.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split("\\|\\|");

                if (details.length >= 6 && details[2].equals(username) && details[3].equals(password)) {
                    if (details[0].equalsIgnoreCase("approved")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return false;  
    }

//    public static void main(String[] args) {
//        new StaffLogin();  // Open the Staff login window
//    }
}
