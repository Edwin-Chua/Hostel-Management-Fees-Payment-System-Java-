import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class SearchUpdateDelete extends JFrame {

    private JTable userTable;
    private JButton searchButton, updateButton, deleteButton, backButton;
    private JTextField searchField;
    private DefaultTableModel tableModel;

    public SearchUpdateDelete() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Search, Update, Delete User");
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Search, Update, Delete User");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        searchField = new JTextField(30);
        searchButton = new JButton("Search User");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 20));
        searchButton.setBackground(new Color(103, 171, 240));

        searchButton.addActionListener(e -> filterTable());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"No.", "Status", "Room Type", "UserID", "Username", "Password", "Email", "Phone", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        add(tableScrollPane, BorderLayout.CENTER);

        updateButton = new JButton("Update User");
        updateButton.setFont(new Font("Arial", Font.PLAIN, 20));
        updateButton.setBackground(new Color(103, 171, 240));
        updateButton.addActionListener(e -> updateUser());

        deleteButton = new JButton("Delete User");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 20));
        deleteButton.setBackground(new Color(103, 171, 240));
        deleteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(SearchUpdateDelete.this, "Please select a user to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the userId and role
            String userId = (String) userTable.getValueAt(selectedRow, 3);
            String role = (String) userTable.getValueAt(selectedRow, 8);

            // Determine the filename based on the role
            String fileName = "";
            if ("Manager".equals(role)) {
                fileName = "manager.txt";
            } else if ("Staff".equals(role)) {
                fileName = "staff.txt";
            } else if ("Resident".equals(role)) {
                fileName = "resident.txt";
            }

            // Delete the user from the file
            try {
                deleteUserFromFile(fileName, userId);
                // Reload the users to refresh the table
                loadUsers();
                JOptionPane.showMessageDialog(SearchUpdateDelete.this, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(SearchUpdateDelete.this, "Error deleting user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });


        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.setBackground(new Color(103, 171, 240));
        backButton.addActionListener(e -> {
            new ManagerMenu();  // Launch ManagerMenu window
            dispose();          // Close the current window
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUsers();
        setVisible(true);
    }
    
    

    public void loadUsers() {
        tableModel.setRowCount(0);
        try {
            loadUserFile("manager.txt", "Manager");
            loadUserFile("staff.txt", "Staff");
            loadUserFile("resident.txt", "Resident");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUserFile(String fileName, String role) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        int rowNum = 1;

        while ((line = reader.readLine()) != null) {
            String[] data = line.split("\\|\\|");

            if ((role.equals("Manager") || role.equals("Staff")) && data.length < 6) {
                System.err.println("Skipping malformed line: " + line);
                continue;
            }

            if (role.equals("Resident") && data.length < 7) {
                System.err.println("Skipping malformed line: " + line);
                continue;
            }

            String roomType = role.equals("Resident") ? data[1] : "-";
            String userId = role.equals("Resident") ? data[2] : data[1];
            String username = role.equals("Resident") ? data[3] : data[2];
            String password = role.equals("Resident") ? data[4] : data[3];
            String email = role.equals("Resident") ? data[5] : data[4];
            String phone = role.equals("Resident") ? data[6] : data[5];
            String status = data[0];

            Object[] row = {rowNum++, status, roomType, userId, username, password, email, phone, role};
            tableModel.addRow(row);
        }
        reader.close();
    }

    private void filterTable() {
        String query = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        userTable.setRowSorter(sorter);
        RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter(query);
        sorter.setRowFilter(rf);
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userId = (String) userTable.getValueAt(selectedRow, 3);
        String username = (String) userTable.getValueAt(selectedRow, 4);
        String role = (String) userTable.getValueAt(selectedRow, 8);

        new UpdateUserPopup(this, userId, username, role);
    }
    
    private void deleteUserFromFile(String fileName, String userId) throws IOException {
        File inputFile = new File(fileName);
        File tempFile = new File(fileName + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|\\|");
                if (!data[1].equals(userId)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }

        // Replace the original file with the updated one
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Failed to rename temp file to original.");
            }
        } else {
            System.err.println("Failed to delete original file.");
        }
    }


//    public static void main(String[] args) {
//        new SearchUpdateDelete();
//    }
}
