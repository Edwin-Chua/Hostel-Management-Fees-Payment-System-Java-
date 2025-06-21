import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class UpdateAccountDetails extends JFrame {
    private JButton updateAccountButton, backButton;
    private String currentUsername;

    // Constructor now accepts the username
    public UpdateAccountDetails(String username) {
        this.currentUsername = username;  // Store the username passed from StaffMenu
        initComponents();
    }

    private void initComponents() {
        // Set up the window
        setTitle("Update Account Details");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set up layout and components
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Update Account Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Create a JTable to display user information in a tabular format
        String[] columnNames = {"No.", "Username", "Password", "Email", "Phone", "Role"};
        String[] userDetails = getUserDetails(currentUsername);
        Object[][] data = {
            {"1", currentUsername, "******", userDetails[0], userDetails[1], "Staff"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setEnabled(false);  // Disable editing the table

        JScrollPane tableScrollPane = new JScrollPane(table);
        add(tableScrollPane, BorderLayout.CENTER);

        // Buttons (Update Account Details, Back)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        updateAccountButton = new JButton("Update Account Details");
        updateAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUpdateForm();
            }
        });
        buttonPanel.add(updateAccountButton);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backButtonAction();
            }
        });
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Make window visible
        setVisible(true);
    }

    private void openUpdateForm() {
        // Open the form with editable fields for Username, Password, Email, Phone
        new UpdateAccountForm(currentUsername);
        dispose();  // Close the current screen
    }

    // Fetch the user details from the staff.txt file
    private String[] getUserDetails(String username) {
        String[] userDetails = new String[2];  // email, phone
        try (BufferedReader reader = new BufferedReader(new FileReader("staff.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\\|\\|");
                if (details.length >= 6 && details[2].equals(username)) {
                    userDetails[0] = details[4];  // Email
                    userDetails[1] = details[5];  // Phone number
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userDetails;
    }

    private void backButtonAction() {
        // Dispose the current UpdateAccountDetails window
        dispose();  
        // Redirect back to StaffMenu, passing the current username
        new StaffMenu(currentUsername);  
    }

    // Uncomment the main method for testing the class independently
//    public static void main(String[] args) {
//        new UpdateAccountDetails("edwin"); // Open Update Account Details window for "edwin"
//    }
}
