import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Role extends JFrame {
    
    private JButton managerButton;
    private JButton staffButton;
    private JButton residentButton;
    private JLabel titleLabel;
    
    public Role() {
        initComponents();
    }

    private void initComponents() {
        // Set up the window
        setTitle("Select Role");
        setSize(1300, 750); // Set window size to a reasonable value
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create the title label and customize it
        titleLabel = new JLabel("APU Hostel Management Fees Payment System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40)); // Set the title font
        titleLabel.setForeground(Color.BLACK);  // Set the title text color
        
        // Create buttons for selecting role
        managerButton = new JButton("Manager");
        staffButton = new JButton("Staff");
        residentButton = new JButton("Resident");

        // Customizing the buttons: set size, colors, and fonts
        managerButton.setPreferredSize(new Dimension(500, 130)); // Button size (width, height)
        staffButton.setPreferredSize(new Dimension(500, 130));
        residentButton.setPreferredSize(new Dimension(500, 130));
        
        managerButton.setBackground(new Color(103, 171, 240));  // Light Blue
        staffButton.setBackground(new Color(103, 171, 240));    // Light Blue
        residentButton.setBackground(new Color(103, 171, 240)); // Light Blue

        managerButton.setForeground(Color.WHITE); // Text color (white)
        staffButton.setForeground(Color.WHITE);
        residentButton.setForeground(Color.WHITE);

        // Custom font for buttons (size 16, bold)
        Font buttonFont = new Font("Arial", Font.BOLD, 32);
        managerButton.setFont(buttonFont);
        staffButton.setFont(buttonFont);
        residentButton.setFont(buttonFont);

        // Add action listeners to buttons
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Manager login page when Manager button is clicked
                new ManagerLogin(); 
                dispose(); // Close the current window
            }
        });

        staffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StaffLogin(); 
                dispose(); // Close the current window
            }
        });

        residentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Resident login page when Resident button is clicked
                new ResidentLogin();
                dispose(); // Close the current window
            }
        });

        // Set layout and add components (title + buttons)
        setLayout(new GridBagLayout()); // Use GridBagLayout for centering
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Add title label above Manager button
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;  // Make the title span across all columns
        gbc.insets = new Insets(10, 10, 10, 10);  // Adds padding
        add(titleLabel, gbc);
        
        // Center buttons in the layout
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;  // Reset gridwidth to 1 for buttons
        add(managerButton, gbc);

        gbc.gridy = 2;  // Move to the next row for the next button
        add(staffButton, gbc);

        gbc.gridy = 3;  // Move to the next row for the last button
        add(residentButton, gbc);

        // Make the window visible
        setVisible(true);
    }

    public static void main(String[] args) {
        // Create and display the role selection page
        new Role();
    }
}
