import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class UpdateUserPopup extends JDialog {

    private JTextField passwordField, emailField, phoneField;
    private JComboBox<String> roomTypeField;
    private JLabel userIdLabel, usernameLabel;
    private JButton saveButton, cancelButton;
    private String userId, username, role;
    private SearchUpdateDelete parent;

    public UpdateUserPopup(SearchUpdateDelete parent, String userId, String username, String role) {
        super(parent, "Update User Information", true);
        this.parent = parent;
        this.userId = userId;
        this.username = username;
        this.role = role;

        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 20));

        userIdLabel = new JLabel("UserID: " + userId);
        usernameLabel = new JLabel("Username: " + username);

        passwordField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();

        // Dropdown menu for Resident room types
        String[] roomTypes = {"Single", "Twin", "Triple"};
        roomTypeField = new JComboBox<>(roomTypes);

        // Only allow room type selection for Residents
        if ("Manager".equals(role) || "Staff".equals(role)) {
            panel.add(new JLabel("Room Type:"));
            panel.add(new JLabel("-")); // Managers & Staff do not have room types
        } else {
            panel.add(new JLabel("Room Type:"));
            panel.add(roomTypeField); // Residents select from dropdown
        }

        saveButton = new JButton("Save & Exit");
        cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            if (validateFields()) {
                saveUserData();
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(userIdLabel);
        panel.add(usernameLabel);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(saveButton);
        panel.add(cancelButton);

        add(panel);
        setVisible(true);
    }

    private boolean validateFields() {
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Password Validation
        if (password.length() < 8 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*") || !password.matches(".*[@$%&].*")) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long, contain numbers, alphabets, and special characters (@ $ % & ).", "Password Validation", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Email Validation
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Email must contain '@' symbol.", "Email Validation", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Phone Validation
        if (!phone.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(this, "Phone number must only contain numbers.", "Phone Validation", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void saveUserData() {
        String fileName = getFileName();
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        System.out.println("Updating UserID: " + userId); // Debug log

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|\\|");

                System.out.println("Checking line: " + line); // Debugging log

                int userIdIndex = (role.equals("Resident")) ? 2 : 1;  // UserID index based on role
                int roomTypeIndex = (role.equals("Resident")) ? 1 : -1; // Room Type for Residents only
                int usernameIndex = (role.equals("Resident")) ? 3 : 2;
                int passwordIndex = (role.equals("Resident")) ? 4 : 3;
                int emailIndex = (role.equals("Resident")) ? 5 : 4;
                int phoneIndex = (role.equals("Resident")) ? 6 : 5;

                // Ensure the line has enough fields and matches the correct UserID
                if (data.length > phoneIndex && data[userIdIndex].trim().equals(userId.trim())) {
                    System.out.println("Match found, updating data...");

                    // Update Room Type if the user is a Resident
                    String newRoomType = (role.equals("Resident")) ? roomTypeField.getSelectedItem().toString() : data[1];

                    // Construct updated line while preserving field order
                    String updatedLine = data[0] + "||" +  // Status
                            (role.equals("Resident") ? newRoomType + "||" : data[1] + "||") + // Update Room Type if Resident
                            userId + "||" +  // UserID
                            username + "||" +  // Username
                            passwordField.getText().trim() + "||" +  // Password
                            emailField.getText().trim() + "||" +  // Email
                            phoneField.getText().trim();  // Phone

                    lines.add(updatedLine);
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (updated) {
            // Write back updated data
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (String updatedLine : lines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Ensure the table refreshes
            if (parent != null) {
                System.out.println("Reloading table after update..."); // Debug log
                parent.loadUsers();
            }
        } else {
            System.err.println("Error: User not found for update. Check UserID format.");
        }
    }

    private String getFileName() {
        return switch (role) {
            case "Manager" -> "manager.txt";
            case "Staff" -> "staff.txt";
            default -> "resident.txt";
        };
    }
}
