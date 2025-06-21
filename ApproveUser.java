import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class ApproveUser extends JFrame {

    private JTable userTable;
    private JButton approveButton, backButton;
    private JComboBox<String> roleDropdown;
    private DefaultTableModel managerStaffTableModel, residentTableModel;

    public ApproveUser() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Approve User");
        setSize(1300, 750);  // Set window size
        setLocationRelativeTo(null);  // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Title panel for the "Approve User" label
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Approve User");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40)); // Title font size: 40px
        titlePanel.add(titleLabel);

        // Set title panel position explicitly at the top with additional space
        add(titlePanel, BorderLayout.NORTH);

        // Additional padding or space to make sure it appears correctly
        JPanel spacePanel = new JPanel();
        spacePanel.setPreferredSize(new Dimension(0, 50)); // Add vertical space before the table
        add(spacePanel, BorderLayout.NORTH);

        // Create the role dropdown panel and add to center
        JPanel rolePanel = new JPanel();
        rolePanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // Center align the role panel
        JLabel roleLabel = new JLabel("Role: ");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Role label font size: 18px
        roleDropdown = new JComboBox<>(new String[]{"Manager", "Staff", "Resident"});
        roleDropdown.setFont(new Font("Arial", Font.PLAIN, 18)); // Dropdown font size: 18px
        rolePanel.add(roleLabel);
        rolePanel.add(roleDropdown);

        // Add role panel below the title panel
        add(rolePanel, BorderLayout.NORTH);  // Ensure role dropdown is also aligned below the title

        // Create table models for Manager/Staff and Resident
        String[] managerStaffColumns = {"No.", "Status", "UserID", "Username", "Password", "Email", "Phone"};
        managerStaffTableModel = new DefaultTableModel(managerStaffColumns, 0);
        userTable = new JTable(managerStaffTableModel);

        String[] residentColumns = {"No.", "Status", "Room Type", "UserID", "Username", "Password", "Email", "Phone"};
        residentTableModel = new DefaultTableModel(residentColumns, 0);

        // Initially, load Manager/Staff data
        userTable.setModel(managerStaffTableModel);

        // Set table fonts and column background color
        userTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Data font size: 14px
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18)); // Column title font size: 18px
        userTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Add action listeners for the dropdown to update the table
        roleDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRole = (String) roleDropdown.getSelectedItem();
                loadUsers(selectedRole);
            }
        });

        // Approve button to approve selected users
        approveButton = new JButton("Approve User");
        approveButton.setFont(new Font("Arial", Font.PLAIN, 30)); // Button font size: 30px
        approveButton.setBackground(new Color(103, 171, 240));
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                approveUsers();
            }
        });

        // Back button to return to Manager Menu
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 30)); // Button font size: 30px
        backButton.setBackground(new Color(103, 171, 240));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window and return to Manager Menu
                new ManagerMenu(); // Open Manager Menu window
            }
        });

        // Panel for buttons with increased spacing
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));  // Increased spacing between buttons
        buttonPanel.add(approveButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Set background color for the page
        getContentPane().setBackground(new Color(245, 245, 245)); // Set page background to a light gray

        // Initial load for Managers
        loadUsers("Manager");

        setVisible(true);
    }



    private void loadUsers(String role) {
        // Clear existing data in the table
        if (role.equals("Resident")) {
            userTable.setModel(residentTableModel);  // Switch to Resident table model
        } else {
            userTable.setModel(managerStaffTableModel);  // Switch to Manager/Staff table model
        }

        // Clear the table first
        if (role.equals("Resident")) {
            residentTableModel.setRowCount(0);
        } else {
            managerStaffTableModel.setRowCount(0);
        }

        // Read the respective file based on the role
        try {
            BufferedReader reader;
            if (role.equals("Manager")) {
                reader = new BufferedReader(new FileReader("manager.txt"));
            } else if (role.equals("Staff")) {
                reader = new BufferedReader(new FileReader("staff.txt"));
            } else {
                reader = new BufferedReader(new FileReader("resident.txt"));
            }

            String line;
            int rowNum = 1;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|\\|");

                if (role.equals("Manager") || role.equals("Staff")) {
                    Object[] row = {rowNum++, data[0], data[1], data[2], data[3], data[4], data[5]};
                    managerStaffTableModel.addRow(row);
                } else if (role.equals("Resident")) {
                    // For Resident, include Room Type and UserID as separate columns
                    Object[] row = {rowNum++, data[0], data[1], data[2], data[3], data[4], data[5], data[6]};
                    residentTableModel.addRow(row);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void approveUsers() {
        int[] selectedRows = userTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one user to approve.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the selected role
        String selectedRole = (String) roleDropdown.getSelectedItem();

        // Determine the correct file based on the role
        String fileName = selectedRole.equals("Manager") ? "manager.txt" :
                          selectedRole.equals("Staff") ? "staff.txt" : "resident.txt";
        
        // Read the data and update the status to "approved"
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            // Update the selected users' status to "approved"
            for (int selectedRow : selectedRows) {
                String[] data = lines.get(selectedRow).split("\\|\\|");
                data[0] = "approved";  // Update status to "approved"
                lines.set(selectedRow, String.join("||", data));
            }

            // Write back the updated data to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();

            JOptionPane.showMessageDialog(this, "Selected users have been approved.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Reload users based on the selected role
            loadUsers(selectedRole);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        new ApproveUser();  // Open the Approve User window
//    }
}
