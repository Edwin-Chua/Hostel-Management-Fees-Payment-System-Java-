import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class ManagerMenu extends JFrame {

    public ManagerMenu() {
        initComponents();
    }

    private void initComponents() {
        // Set up the window
        setTitle("Manager Menu");
        setSize(1300, 750); // Set window size to be smaller
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons for menu options
        JButton approveButton = new JButton("Approve User");
        JButton userActionButton = new JButton("Search Update Delete User");
        JButton rateButton = new JButton("Update Room Rate");
        JButton logoutButton = new JButton("Logout");

        // Set smaller font size for buttons
        approveButton.setFont(new Font("Arial", Font.BOLD, 16));
        userActionButton.setFont(new Font("Arial", Font.BOLD, 16));
        rateButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));

        // Set preferred size for buttons to make them smaller
        approveButton.setPreferredSize(new Dimension(400, 60));
        userActionButton.setPreferredSize(new Dimension(400, 60));
        rateButton.setPreferredSize(new Dimension(400, 60));
        logoutButton.setPreferredSize(new Dimension(400, 60));

        approveButton.setBackground(new Color(103, 171, 240));
        userActionButton.setBackground(new Color(103, 171, 240));
        rateButton.setBackground(new Color(103, 171, 240));
        logoutButton.setBackground(new Color(103, 171, 240));

        // Add action listeners for the buttons
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the ApproveUser window when the button is clicked
                dispose(); // Close the current window (Manager Menu)
                new ApproveUser(); // Open the ApproveUser window
            }
        });

        // Combined action button
        userActionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the ManagerMenu window and open SearchUpdateDelete window
                dispose(); // Close the current window (Manager Menu)
                new SearchUpdateDelete(); // Open the SearchUpdateDelete window
            }
        });
        
        rateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the Update Room Rate window when the button is clicked
                dispose(); // Close the current window (Manager Menu)
                new RoomRate(); // Open the RoomRate window
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Log out functionality
                System.out.println("Logging out...");
                dispose(); // Close the current window (Manager Menu)
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
        add(approveButton, gbc);

        gbc.gridy = 1;
        add(userActionButton, gbc);

        gbc.gridy = 2;
        add(rateButton, gbc);

        gbc.gridy = 3;
        add(logoutButton, gbc);

        setVisible(true);
    }

//    public static void main(String[] args) {
//        new ManagerMenu();  // Open the Manager menu window
//    }
}
