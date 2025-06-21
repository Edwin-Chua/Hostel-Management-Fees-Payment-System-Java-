import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

public class PaymentRecord extends JFrame {
    private String currentUsername;  // Store the username of the logged-in resident
    private JTable paymentTable;
    
    public PaymentRecord(String username) {
        this.currentUsername = username;  // Initialize with the logged-in username
        initComponents();
    }

    private void initComponents() {
        setTitle("Payment Records");
        setSize(800, 400); // Set window size
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create column names
        String[] columns = {"Resident ID", "Unit", "Month", "Total Paid", "Payment Method"};
        
        // Create a table with default model
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        paymentTable = new JTable(tableModel);

        // Load payment records for the current user
        loadPaymentRecords(tableModel);
        
        // Add the table to a JScrollPane for scroll functionality
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create and style the "Back" button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(103, 171, 240));
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(e -> goBackToResidentMenu());
        add(backButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Load payment records for the current user from the payment_records.txt file
    private void loadPaymentRecords(DefaultTableModel tableModel) {
    try (BufferedReader reader = new BufferedReader(new FileReader("payment_records.txt"))) {
        String line;
        String[] paymentDetails = new String[5];
        int lineCount = 0;
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();  // Remove leading/trailing whitespaces
            if (line.isEmpty()) continue;  // Skip empty lines
            
            paymentDetails[lineCount] = line;  // Store the line in the corresponding index
            lineCount++;

            // Once we have 5 lines (1 record), we process the record
            if (lineCount == 5) {
                // Debugging output to verify the values
                System.out.println("Record for " + paymentDetails[0] + ": " +
                        paymentDetails[0] + ", " + paymentDetails[1] + ", " + 
                        paymentDetails[2] + ", " + paymentDetails[3] + ", " + paymentDetails[4]);
                
                if (paymentDetails[0].equalsIgnoreCase(currentUsername)) {
                    // Add the payment record as a row in the table
                    String[] record = {paymentDetails[0], paymentDetails[1], paymentDetails[2], paymentDetails[3], paymentDetails[4]};
                    tableModel.addRow(record);
                }
                lineCount = 0;  // Reset for the next record
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error loading payment records.", "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();  // Print the stack trace for debugging
    }
}


    private void goBackToResidentMenu() {
        dispose(); // Close the current window
        new ResidentMenu(currentUsername); // Go back to the ResidentMenu
    }

//    public static void main(String[] args) {
//        // Simulating a logged-in user with "derick" as the username
//        new PaymentRecord("derick");  // Open the PaymentRecord window with the current username
//    }
}
