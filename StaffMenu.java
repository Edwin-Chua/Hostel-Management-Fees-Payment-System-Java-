import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StaffMenu extends JFrame {
    private JButton updateAccountButton;
    private JButton makePaymentButton;
    private JButton generateReceiptButton;
    private JButton logoutButton;
    private String currentUsername;

    public StaffMenu(String username) {
        this.currentUsername = username;  
        initComponents();
    }

    private void initComponents() {
        setTitle("Staff Menu");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        updateAccountButton = new JButton("Update Account Details");
        makePaymentButton = new JButton("Make Payment for Resident");
        generateReceiptButton = new JButton("Generate Receipt for Resident");
        logoutButton = new JButton("Logout");

        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        updateAccountButton.setFont(buttonFont);
        makePaymentButton.setFont(buttonFont);
        generateReceiptButton.setFont(buttonFont);
        logoutButton.setFont(buttonFont);

        updateAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdateAccountDetails(currentUsername);  
                dispose();  
            }
        });

        makePaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MakePayment(currentUsername);  
                dispose();  
            }
        });

        generateReceiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GenerateReceipt(currentUsername);  
                dispose();  
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(StaffMenu.this, "Logging out...");
                dispose(); // Close the current window (Staff Menu)
                new Role(); // Open the Role window
            }
        });

        setLayout(new GridLayout(5, 1, 10, 10)); 
        add(updateAccountButton);
        add(makePaymentButton);
        add(generateReceiptButton);
        add(logoutButton);

        setVisible(true);
    }
}
