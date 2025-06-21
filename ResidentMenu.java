import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResidentMenu extends JFrame {

    private String currentUsername;  // Store the username of the logged-in resident

    public ResidentMenu(String username) {
        this.currentUsername = username;  // Initialize with the logged-in username
        initComponents();
    }

    private void initComponents() {
        // Set up the window
        setTitle("Resident Menu");
        setSize(1300, 750); // Set window size to be smaller
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons for menu options
        JButton updateAccountButton = new JButton("Update Account Details");
        JButton viewPaymentButton = new JButton("View Payment Records");
        JButton logoutButton = new JButton("Logout");

        // Set smaller font size for buttons
        updateAccountButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewPaymentButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));

        // Set preferred size for buttons to make them smaller
        updateAccountButton.setPreferredSize(new Dimension(400, 60));
        viewPaymentButton.setPreferredSize(new Dimension(400, 60));
        logoutButton.setPreferredSize(new Dimension(400, 60));

        updateAccountButton.setBackground(new Color(103, 171, 240));
        viewPaymentButton.setBackground(new Color(103, 171, 240));
        logoutButton.setBackground(new Color(103, 171, 240));

        // Add action listeners for the buttons
        updateAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pass the currentUsername to the UpdateAccountDetailsR constructor
                dispose(); // Close the current window (Resident Menu)
                new UpdateAccountDetailsR(currentUsername); // Open the UpdateAccount window
            }
        });

        viewPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the ViewPaymentRecords window when the button is clicked
                dispose(); // Close the current window (Resident Menu)
                new PaymentRecord(currentUsername); // Open the ViewPaymentRecords window
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Log out functionality
                System.out.println("Logging out...");
                dispose(); // Close the current window (Resident Menu)
                new Role(); // Open the Role window
            }
        });

        // Set layout to GridBagLayout to control spacing and centering
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Center the buttons with equal space
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding between buttons
        add(updateAccountButton, gbc);

        gbc.gridy = 1;
        add(viewPaymentButton, gbc);

        gbc.gridy = 2;
        add(logoutButton, gbc);

        setVisible(true);
    }

//    public static void main(String[] args) {
//        // Simulating a logged-in user with "derick" as the username
//        new ResidentMenu("derick");  // Open the Resident menu window with the current username
//    }
}
