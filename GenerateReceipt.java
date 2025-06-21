import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class GenerateReceipt {
    private JFrame frame;
    private JTextField residentNameField;
    private JButton generateButton;
    private JButton backButton;
    private String currentUsername;

    public GenerateReceipt(String username) {
        this.currentUsername = username;
        frame = new JFrame("Generate Receipt");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JLabel titleLabel = new JLabel("Enter Resident Name to Generate Receipt");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(titleLabel);

        residentNameField = new JTextField(20);
        frame.add(residentNameField);

        generateButton = new JButton("Generate Receipt");
        generateButton.addActionListener(e -> generateReceipt());
        frame.add(generateButton);

        backButton = new JButton("Back");
        backButton.addActionListener(e -> goBackToStaffMenu());
        frame.add(backButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void generateReceipt() {
        String residentName = residentNameField.getText().trim();

        if (residentName.isEmpty() || !isResidentValid(residentName)) {
            JOptionPane.showMessageDialog(frame, "Resident not found! Please enter a valid resident name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] latestPayment = getLatestPaymentRecord(residentName);

        if (latestPayment == null) {
            JOptionPane.showMessageDialog(frame, "No payment records found for this resident.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userId = getResidentUserId(residentName);
        String roomType = latestPayment[1];  

        String receipt = generateReceiptString(userId, residentName, roomType, latestPayment[2], latestPayment[3], latestPayment[4]);

        JOptionPane.showMessageDialog(frame, receipt, "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isResidentValid(String residentName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("payment_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equalsIgnoreCase(residentName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String[] getLatestPaymentRecord(String residentName) {
        List<String[]> paymentRecords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("payment_records.txt"))) {
            String line;
            List<String> paymentRecord = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    paymentRecord.add(line.trim());
                }
                if (paymentRecord.size() == 5) {
                    if (paymentRecord.get(0).equalsIgnoreCase(residentName)) {
                        paymentRecords.add(paymentRecord.toArray(new String[0]));
                    }
                    paymentRecord.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (paymentRecords.isEmpty()) {
            return null;
        }

        return paymentRecords.get(paymentRecords.size() - 1);
    }

    private String getResidentUserId(String residentName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("resident.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\\|\\|");
                if (details.length > 3 && details[3].equalsIgnoreCase(residentName)) {
                    return details[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Not Found";
    }

    private String generateReceiptString(String userId, String residentName, String roomType, String duration, String totalAmount, String paymentMethod) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        return "       APU Hostel Fees\n\n" +
                "Date: " + dtf.format(now) + "\n" +
                "-----------------------------------\n" +
                "User ID: " + userId + "\n" +
                "Resident Name: " + residentName + "\n" +
                "Room Type: " + roomType + "\n" +
                "Duration Paid: " + duration + "\n" +
                "Total Amount: " + totalAmount + "\n" +
                "Payment Method: " + paymentMethod + "\n" +
                "-----------------------------------\n" +
                "Thank you for your payment!";
    }

    private void goBackToStaffMenu() {
        frame.dispose();
        new StaffMenu(currentUsername);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new GenerateReceipt("testUser"));
//    }
}
