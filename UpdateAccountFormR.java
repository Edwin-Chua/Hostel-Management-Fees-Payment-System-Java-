import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class UpdateAccountFormR extends JFrame {
    private JTextField emailField, phoneField;
    private JPasswordField passwordField;
    private JComboBox<String> roomTypeComboBox; // For room type selection
    private JButton okButton, cancelButton;
    private String currentUsername;

    // Constructor now accepts the username
    public UpdateAccountFormR(String username) {
        this.currentUsername = username;
        initComponents();
    }

    private void initComponents() {
        // Set up the window
        setTitle("Update Account Form");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Layout for the form
        setLayout(new GridLayout(6, 2, 10, 10));

        // Add fields for Username (non-editable), Password, Email, Phone, and Room Type
        add(new JLabel("Username: "));
        JTextField usernameField = new JTextField(currentUsername);
        usernameField.setEditable(false);  // Make username non-editable
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        add(passwordField);

        add(new JLabel("Email:"));
        emailField = new JTextField(20);
        add(emailField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField(20);
        add(phoneField);

        // Room Type dropdown
        add(new JLabel("Room Type: "));
        roomTypeComboBox = new JComboBox<>(new String[]{"Single", "Twin", "Triple"});
        add(roomTypeComboBox);

        // Buttons (OK, Cancel)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    saveAccountDetails();
                }
            }
        });
        buttonPanel.add(okButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When cancel is clicked, close this form and return to UpdateAccountDetailsR
                dispose();  // Close the current UpdateAccountFormR
                new UpdateAccountDetailsR(currentUsername);  // Redirect to UpdateAccountDetailsR
            }
        });
        buttonPanel.add(cancelButton);

        add(buttonPanel);

        // Make window visible
        setVisible(true);
    }

    private boolean validateFields() {
        String newPassword = new String(passwordField.getPassword()).trim();
        String newEmail = emailField.getText().trim();
        String newPhone = phoneField.getText().trim();
        String newRoomType = (String) roomTypeComboBox.getSelectedItem();

        // Password Validation
        if (newPassword.length() < 8 || !newPassword.matches(".*[a-zA-Z].*") || !newPassword.matches(".*\\d.*") || !newPassword.matches(".*[@$%&].*")) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long, contain numbers, alphabets, and special characters (@ $ % & ).", "Password Validation", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Email Validation
        if (!newEmail.contains("@")) {
            JOptionPane.showMessageDialog(this, "Email must contain '@' symbol.", "Email Validation", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Phone Validation
        if (!newPhone.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(this, "Phone number must only contain numbers.", "Phone Validation", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void saveAccountDetails() {
        String newPassword = new String(passwordField.getPassword()).trim();
        String newEmail = emailField.getText().trim();
        String newPhone = phoneField.getText().trim();
        String newRoomType = (String) roomTypeComboBox.getSelectedItem();

        try {
            // Read the existing resident.txt file and update the information
            File file = new File("resident.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder updatedContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\\|\\|");

                if (details[3].equals(currentUsername)) {
                    details[4] = newPassword;  // Update password
                    details[5] = newEmail;     // Update email
                    details[6] = newPhone;     // Update phone
                    details[1] = newRoomType;  // Update room type
                }

                updatedContent.append(String.join("||", details)).append("\n");
            }
            reader.close();

            // Write the updated content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(updatedContent.toString());
            writer.close();

            JOptionPane.showMessageDialog(this, "Account details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();  // Close the form after saving
            new UpdateAccountDetailsR(currentUsername);  // Redirect to UpdateAccountDetailsR page

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating account details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//    public static void main(String[] args) {
//        new UpdateAccountFormR("test");  // Open Update Account form for "test"
//    }
}
