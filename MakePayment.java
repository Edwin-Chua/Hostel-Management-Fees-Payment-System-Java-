import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MakePayment {
    private JFrame frame;
    private JTextField residentIDField;
    private JComboBox<String> roomTypeDropdown, monthDropdown, paymentMethodDropdown;
    private JLabel rentalUnitFeeLabel, totalLabel;
    private final Map<String, Double> roomRates;
    private String currentUsername;

    public MakePayment(String username) {
        this.currentUsername = username;
        roomRates = new HashMap<>();
        roomRates.put("Single", 500.00);  
        roomRates.put("Twin", 700.00);    
        roomRates.put("Triple", 900.00);  

        frame = new JFrame("Make Payment for Resident");
        frame.setSize(800, 600);  
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Make Payment for Resident", JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 140, 0)); // Orange
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        addLabelAndField(mainPanel, gbc, "Resident name:", residentIDField = new JTextField(25), 0);
        addLabelAndCombo(mainPanel, gbc, "Room Type:", roomTypeDropdown = createRoomTypeDropdown(), 1);
        roomTypeDropdown.addActionListener(e -> updateRentalUnitFee());

        addLabelAndField(mainPanel, gbc, "Rental Unit Fees:", rentalUnitFeeLabel = createStyledLabel("RM0.00"), 2);
        addLabelAndCombo(mainPanel, gbc, "Duration:", monthDropdown = createMonthDropdown(), 3);

        addLabelAndCombo(mainPanel, gbc, "Payment Method:", paymentMethodDropdown = createPaymentDropdown(), 4);
        addLabelAndField(mainPanel, gbc, "Total Payment:", totalLabel = createStyledLabel("RM0.00"), 5);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton calculateButton = createStyledButton("Calculate Total", new Color(0, 102, 204));
        JButton confirmButton = createStyledButton("Confirm & Save", new Color(0, 153, 51));
        JButton backButton = createStyledButton("Back", new Color(204, 0, 0));

        calculateButton.addActionListener(e -> calculateTotal());
        confirmButton.addActionListener(e -> confirmPayment());
        backButton.addActionListener(e -> backButtonAction());

        buttonPanel.add(calculateButton);
        buttonPanel.add(confirmButton);
        buttonPanel.add(backButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int y) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(label, gbc);

        field.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void addLabelAndCombo(JPanel panel, GridBagConstraints gbc, String labelText, JComboBox<String> comboBox, int y) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(label, gbc);

        comboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        panel.add(comboBox, gbc);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(new Color(34, 139, 34)); // Green
        return label;
    }

    private JComboBox<String> createRoomTypeDropdown() {
        return new JComboBox<>(new String[]{"Single", "Twin", "Triple"});
    }

    private JComboBox<String> createMonthDropdown() {
        return new JComboBox<>(new String[]{"1 month", "2 months", "3 months", "4 months", "5 months", "6 months",
                                            "7 months", "8 months", "9 months", "10 months", "11 months", "12 months"});
    }

    private JComboBox<String> createPaymentDropdown() {
        return new JComboBox<>(new String[]{"Online Banking", "Credit Card", "Debit Card"});
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setPreferredSize(new Dimension(200, 40));
        return button;
    }

    private void updateRentalUnitFee() {
        String selectedRoomType = (String) roomTypeDropdown.getSelectedItem();
        Double roomRate = roomRates.getOrDefault(selectedRoomType, 0.0);
        rentalUnitFeeLabel.setText(String.format("RM%.2f", roomRate));
    }

    private void calculateTotal() {
        double roomRate = Double.parseDouble(rentalUnitFeeLabel.getText().replace("RM", ""));
        int months = monthDropdown.getSelectedIndex() + 1; 

        totalLabel.setText(String.format("RM%.2f", roomRate * months));
    }

    private void confirmPayment() {
        String residentName = residentIDField.getText().trim();
        String roomType = (String) roomTypeDropdown.getSelectedItem(); // Get selected room type

        // Check if the resident and room type are valid
        if (!isResidentValid(residentName, roomType)) {
            JOptionPane.showMessageDialog(frame, "Resident/Room type does not match! Please enter a valid input.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("payment_records.txt", true))) {
            writer.write(residentName);  
            writer.newLine();
            writer.write(roomType);  // Use the room type selected
            writer.newLine();
            writer.write((String) monthDropdown.getSelectedItem());
            writer.newLine();
            writer.write(totalLabel.getText());
            writer.newLine();
            writer.write((String) paymentMethodDropdown.getSelectedItem());
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(frame, "Payment recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        resetFields();
    }

    private void resetFields() {
        residentIDField.setText("");
        roomTypeDropdown.setSelectedIndex(0);
        rentalUnitFeeLabel.setText("RM0.00");
        totalLabel.setText("RM0.00");
        paymentMethodDropdown.setSelectedIndex(0);
    }

    private boolean isResidentValid(String residentName, String roomType) {
        try (BufferedReader reader = new BufferedReader(new FileReader("resident.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\\|\\|");
                // Check if the resident name matches
                if (details[3].equalsIgnoreCase(residentName)) {
                    // Check if the room type matches
                    if (details[1].equalsIgnoreCase(roomType)) {
                        return true;  // Resident and room type are valid
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;  // Either resident or room type doesn't match
    }

    private void backButtonAction() {
        frame.dispose();  // Close the current MakePayment page
        new StaffMenu(currentUsername);  // Redirect to StaffMenu
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new MakePayment("exampleUser"));
//    }
}
