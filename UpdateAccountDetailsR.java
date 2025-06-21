import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class UpdateAccountDetailsR extends JFrame {
    private JButton updateAccountButton, backButton;
    private String currentUsername;

    // Constructor now accepts the username
    public UpdateAccountDetailsR(String username) {
        this.currentUsername = username;  // Store the username passed from ResidentMenu
        initComponents();
    }

    private void initComponents() {
        // Set up the window
        setTitle("Update Account Details");
        setSize(1300, 750);  // Set the window size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set up layout and components
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Update Account Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));  // Larger font size for the title
        add(titleLabel, BorderLayout.NORTH);

        // Create a JTable to display user information in a tabular format
        String[] columnNames = {"No.", "Username", "Password", "Email", "Phone", "Room Type"};
        String[] userDetails = getUserDetails(currentUsername);
        Object[][] data = {
            {"1", currentUsername, "******", userDetails[0], userDetails[1], userDetails[2]}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setEnabled(false);  // Disable editing the table

        // Set the font size of the table content
        table.setFont(new Font("Arial", Font.PLAIN, 18));  // Data font size

        // Set background color for the table
        table.setBackground(new Color(240, 240, 240));

        // Adjust the column title font size
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 26));  // Column title font size

        JScrollPane tableScrollPane = new JScrollPane(table);
        add(tableScrollPane, BorderLayout.CENTER);

        // Buttons (Update Account Details, Back)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        updateAccountButton = new JButton("Update Account Details");
        updateAccountButton.setFont(new Font("Arial", Font.BOLD, 20));  // Larger font size for button
        updateAccountButton.setPreferredSize(new Dimension(300, 50));  // Make the button longer
        updateAccountButton.setBackground(new Color(103, 171, 240));  // Button color
        updateAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUpdateForm();
            }
        });
        buttonPanel.add(updateAccountButton);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setPreferredSize(new Dimension(200, 50));  // Set the size of the button
        backButton.setBackground(new Color(255, 99, 71));  // Button color
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close current window
                new ResidentMenu(currentUsername); // Go back to Manager Menu
            }
        });


        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Make window visible
        setVisible(true);
    }

    private void openUpdateForm() {
        // Open the form with editable fields for Username, Password, Email, Phone
        new UpdateAccountFormR(currentUsername); // Open the Update Account form for residents
        dispose();  // Close the current screen
    }

    // Fetch the user details from the resident.txt file
    private String[] getUserDetails(String username) {
        String[] userDetails = new String[3];  // email, phone, roomType
        try (BufferedReader reader = new BufferedReader(new FileReader("resident.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\\|\\|");
                if (details.length >= 7 && details[3].equals(username)) {
                    userDetails[0] = details[4];  // Email
                    userDetails[1] = details[6];  // Phone number
                    userDetails[2] = details[1];  // Room type
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userDetails;
    }

//    public static void main(String[] args) {
//        new UpdateAccountDetailsR("test");  // Open Update Account Details window for "test"
//    }
}
