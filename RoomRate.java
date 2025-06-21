import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRate extends JFrame {
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> roomTypeDropdown;
    private JTextField newRateField;
    private List<String[]> roomData;

    public RoomRate() {
        setTitle("Update Room Rate");
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Update Room Rates", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(70, 130, 180)); // Steel Blue
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(1300, 60));
        add(titleLabel, BorderLayout.NORTH);

        // Load room rates
        roomData = loadRoomRates();

        // Table setup
        String[] columnNames = {"Room Type", "Rate (RM)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        roomTable = new JTable(tableModel);

        // Set font size and row height
        roomTable.setFont(new Font("Arial", Font.PLAIN, 18));
        roomTable.setRowHeight(30);

        // Set table header font
        JTableHeader header = roomTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 32));
        header.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        header.setForeground(Color.WHITE);

        // Populate table
        for (String[] row : roomData) {
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(roomTable);
        add(scrollPane, BorderLayout.CENTER);

        // Main panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // Dropdown label
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel selectLabel = new JLabel("Select Room Type:");
        selectLabel.setFont(new Font("Arial", Font.BOLD, 28));
        inputPanel.add(selectLabel, gbc);

        // Dropdown menu for room types
        gbc.gridx = 1;
        roomTypeDropdown = new JComboBox<>();
        roomTypeDropdown.setFont(new Font("Arial", Font.PLAIN, 28));
        for (String[] row : roomData) {
            roomTypeDropdown.addItem(row[0]);
        }
        inputPanel.add(roomTypeDropdown, gbc);

        // Input field label
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel rateLabel = new JLabel("Enter New Rate:");
        rateLabel.setFont(new Font("Arial", Font.BOLD, 28));
        inputPanel.add(rateLabel, gbc);

        // Input field
        gbc.gridx = 1;
        newRateField = new JTextField(10);
        newRateField.setFont(new Font("Arial", Font.PLAIN, 28));
        inputPanel.add(newRateField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));

        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");

        // Button styling
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        Dimension buttonSize = new Dimension(200, 50);

        saveButton.setFont(buttonFont);
        saveButton.setPreferredSize(buttonSize);
        saveButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        saveButton.setForeground(Color.WHITE);

        backButton.setFont(buttonFont);
        backButton.setPreferredSize(buttonSize);
        backButton.setBackground(new Color(220, 20, 60)); // Crimson
        backButton.setForeground(Color.WHITE);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRoomRate();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close current window
                new ManagerMenu(); // Go back to Manager Menu
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        // Add input and button panels to layout
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private List<String[]> loadRoomRates() {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("room_rates.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    data.add(parts);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading room rates!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return data;
    }

    private void updateRoomRate() {
        String selectedRoom = (String) roomTypeDropdown.getSelectedItem();
        String newRate = newRateField.getText().trim();

        if (newRate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a new rate!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double formattedRate;
        try {
            formattedRate = Double.parseDouble(newRate);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid rate! Enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Format to 2 decimal places
        String formattedRateStr = String.format("%.2f", formattedRate);

        // Update in memory
        for (String[] row : roomData) {
            if (row[0].equals(selectedRoom)) {
                row[1] = formattedRateStr;
                break;
            }
        }

        // Update table
        tableModel.setRowCount(0);
        for (String[] row : roomData) {
            tableModel.addRow(row);
        }

        // Save to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("room_rates.txt"))) {
            for (String[] row : roomData) {
                writer.write(row[0] + ", " + row[1]);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Room rate updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving room rates!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//    public static void main(String[] args) {
//        new RoomRate();
//    }
}
