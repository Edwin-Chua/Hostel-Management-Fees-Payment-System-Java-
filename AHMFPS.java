import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

// Parent User class
// Parent User class
class User {
    protected String username;
    protected String password;
    protected String email;
    protected String phone;
    protected String fileName;

    public User(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileName() {
        return this.fileName;
    }


    public String login(Scanner sc) {
        System.out.println("Enter your name: ");
        String fullName = sc.nextLine().trim();

        System.out.println("Enter your password: ");
        String password = sc.nextLine().trim();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split("\\|\\|");

                // Check if the username and password match
                if (details.length >= 6 && details[2].equals(fullName) && details[3].equals(password)) {
                    // Check if the user is approved (status should be "approved")
                    if (details[0].equalsIgnoreCase("approved")) {
                        System.out.println("Login successful. Welcome, " + fullName + "!");
                        return details[1];  // Return UUID as the username
                    } else {
                        System.out.println("Login failed. Your account is still pending approval.");
                        return null;  // Return null if the status is "pending"
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        System.out.println("Login failed. Incorrect name or password.");
        return null;  // Return null if the credentials don't match
    }



}


// Manager class extending User
class Manager extends User {
    public Manager() {
        super("manager.txt");
    }

 // Manager menu after login
    public void menu(Scanner sc) {

        while (true) {
            System.out.println("1. Register User\n2. Approve User\n3. Search User\n4. Update User\n5. Delete User\n6. Update Room Rate\n7. Logout");
            String choice = sc.next();
            switch (choice) {
                case "1": SystemUtility.registerUser("manager.txt"); break;
                case "2": SystemUtility.approveUser("manager.txt"); break;
                case "3": SystemUtility.searchUser("manager.txt"); break;
                case "4": SystemUtility.updateUser("manager.txt", sc); break;
                case "5": SystemUtility.deleteUser("manager.txt", sc); break;
                case "6": SystemUtility.updateRoomRate(); break;
                case "7": return;  // Logout
                default: System.out.println("Invalid choice.");
            }
        }
    }


}

// Staff class extending User
class Staff extends User {
    public Staff() {
        super("staff.txt");
    }

    public void menu(Scanner sc) {
        while (true) {
            System.out.println("\n1. Register account");
            System.out.println("2. Update account details");
            System.out.println("3. Make payment for resident");
            System.out.println("4. Generate receipt for resident");
            System.out.println("5. Logout");

            String choice = sc.next().toLowerCase();

            switch (choice) {
                case "1":
                    SystemUtility.registerUser("staff.txt");  // Register account
                    break;

                case "2":
                    SystemUtility.updateUser("staff.txt",sc);  // Update account details
                    break;

                case "3":
                    SystemUtility.makePayment(sc); // Process payment for resident
                    break;

                case "4":
                    generateReceiptForResident(sc);  // Generate receipt for resident
                    break;

                case "5":
                    return;  // Logout

                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private void generateReceiptForResident(Scanner sc) {
        System.out.println("Enter the resident's name: ");
        String residentName = sc.nextLine().trim();

        // Find the resident's details and generate the receipt
        String residentUsername = getResidentUsernameByName(residentName);

        if (residentUsername == null) {
            System.out.println("Resident not found.");
            return;
        }

        // Assuming the resident has made a payment, we will retrieve payment details
        // Example: Getting the room type and payment details for the receipt
        String roomType = "Single";  // Replace this with actual logic to get room type
        int months = 6;  // Replace this with actual logic for months
        String monthCovered = "Jan-June";  // Replace this with actual logic for months covered
        double totalAmount = 1200.0;  // Replace with actual total amount logic

        // Now generate the receipt using SystemUtility.generateReceipt
        String receipt = SystemUtility.generateReceipt(residentUsername, "StaffUser123", roomType, months, monthCovered, totalAmount);

        // Display the generated receipt
        System.out.println("\nGenerated Receipt:\n" + receipt);
    }

    private String getResidentUsernameByName(String residentName) {
        // Logic to find the Resident's UUID/username by their name
        // This can involve searching through the resident.txt file or a database
        // For simplicity, we will assume it's located in a file called "resident.txt"
        try (BufferedReader br = new BufferedReader(new FileReader("resident.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split("\\|\\|");
                if (details[2].equalsIgnoreCase(residentName)) {
                    return details[1];  // Return the UUID of the resident
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return null;  // If not found
    }

}

// Resident class extending User
class Resident extends User {
    private String loggedInName; // Store the resident's name after login

    public Resident() {
        super("resident.txt");
    }

    @Override
    public String login(Scanner sc) {
        System.out.println("Enter your name: ");
        String fullName = sc.nextLine().trim();

        System.out.println("Enter your password: ");
        String password = sc.nextLine().trim();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split("\\|\\|");
                // Check indices for resident: fullName at [3], password at [4]
                if (details[3].equalsIgnoreCase(fullName) && details[4].equals(password)) {
                    if (details[0].equalsIgnoreCase("approved")) {
                        System.out.println("Login successful. Welcome, " + fullName + "!");
                        loggedInName = fullName;  // Store the logged-in resident's name
                        return details[2];  // Return UUID
                    } else {
                        System.out.println("Login failed. Your account is still pending approval.");
                        return null;  // Return null if the status is "pending"
                    }
                    
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        System.out.println("Login failed. Incorrect name or password.");
        return null;
    }

    public void menu(Scanner sc) {
        while (true) {
            System.out.println("\nResident Menu:");
            System.out.println("1. Register User");
            System.out.println("2. Update Account");
            System.out.println("3. View Payment Records");
            System.out.println("4. Logout");

            String choice = sc.next();
            sc.nextLine();  // Consume newline

            switch (choice) {
                case "1":
                    SystemUtility.registerUser(fileName);
                    break;
                case "2":
                    SystemUtility.updateResidentAccount(fileName, loggedInName, sc);
                    break;
                case "3":
                    SystemUtility.viewPaymentRecords(sc);
                    break;
                case "4":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}


// Utility class for common operations
class SystemUtility {
    // Register user if not already registered
public static void registerUser(String fileName) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter your full name (also your username): ");
    String fullName = sc.nextLine().trim();
    String username = UUID.randomUUID().toString();  // Generate a unique UUID for the username

    System.out.println("Enter password (at least 8 characters, including a number and special character): ");
    String password;
    while (true) {
        password = sc.nextLine().trim();
        
        // Check if the password meets the criteria
        if (password.length() >= 8 && 
            password.matches(".*[0-9].*") &&  // Must contain a number
            password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {  // Must contain a special character
            break;
        }
        System.out.println("Invalid password. Ensure it has at least 8 characters, a number, and a special character.");
    }

    // Email validation (based on role)
    String email = "";
    boolean validEmail = false;
    while (!validEmail) {
        System.out.println("Enter email: ");
        email = sc.nextLine().trim();
        validEmail = validateEmail(fileName, email);
        if (!validEmail) {
            System.out.println("Invalid email for " + fileName + ". Please try again.");
        }
    }

    System.out.println("Enter phone number: ");
    String phone = sc.nextLine();

    String status = "pending";  // Default status for new users
    if (fileName.equals("resident.txt")) {
        System.out.println("Enter room type (Single/Twin/Triple): ");
        String roomType = sc.nextLine();
        saveToFile(fileName, status + "||" + roomType + "||" + username + "||" + fullName + "||" + password + "||" + email + "||" + phone);
    } else {
        saveToFile(fileName, status + "||" + username + "||" + fullName + "||" + password + "||" + email + "||" + phone);
    }
    System.out.println("User registered successfully.");
}



    // Email validation method
// Validate email based on user role
    private static boolean validateEmail(String fileName, String email) {
        String domain = "";

        switch (fileName) {
            case "manager.txt":
                domain = "@managermail.com";
                break;
            case "staff.txt":
                domain = "@staffmail.com";
                break;
            case "resident.txt":
                domain = "@residentmail.com";
                break;
            default:
                break;
        }

        // Check if email ends with the correct domain
        return email.endsWith(domain);
    }



    private static void saveToFile(String fileName, String data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(data);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void approveUser(String fileName) {
       Scanner sc = new Scanner(System.in);
       try {
           // Read all lines from the respective files
           List<String> managerLines = Files.readAllLines(Paths.get("manager.txt"));
           List<String> staffLines = Files.readAllLines(Paths.get("staff.txt"));
           List<String> residentLines = Files.readAllLines(Paths.get("resident.txt"));

           boolean updated = false;
           System.out.println("\n--- Pending Users ---");

           // Display all pending users from the manager file
           for (String line : managerLines) {
               String[] details = line.split("\\|\\|");
               if (details[0].equalsIgnoreCase("pending")) {
                   System.out.println("Name: " + details[2] + " | Type: Manager");
               }
           }

           // Display all pending users from the staff file
           for (String line : staffLines) {
               String[] details = line.split("\\|\\|");
               if (details[0].equalsIgnoreCase("pending")) {
                   System.out.println("Name: " + details[2] + " | Type: Staff");
               }
           }

           // Display all pending users from the resident file
           for (String line : residentLines) {
               String[] details = line.split("\\|\\|");
               if (details[0].equalsIgnoreCase("pending")) {
                   System.out.println("Name: " + details[3] + " | Type: Resident");
               }
           }

           // Ask for the name of the user to approve
           System.out.println("\nEnter the full name of the user to approve: ");
           String nameToApprove = sc.nextLine().trim();

           // Approve the user in managerLines
           for (int i = 0; i < managerLines.size(); i++) {
               String[] details = managerLines.get(i).split("\\|\\|");
               if (details[0].equalsIgnoreCase("pending") && details[2].equalsIgnoreCase(nameToApprove)) {
                   details[0] = "approved";  // Change status to approved
                   managerLines.set(i, String.join("||", details));  // Update the line with approved status
                   updated = true;
                   System.out.println(nameToApprove + " (Manager) has been approved.");
                   break;
               }
           }

           // Approve the user in staffLines
           for (int i = 0; i < staffLines.size(); i++) {
               String[] details = staffLines.get(i).split("\\|\\|");
               if (details[0].equalsIgnoreCase("pending") && details[2].equalsIgnoreCase(nameToApprove)) {
                   details[0] = "approved";  // Change status to approved
                   staffLines.set(i, String.join("||", details));  // Update the line with approved status
                   updated = true;
                   System.out.println(nameToApprove + " (Staff) has been approved.");
                   break;
               }
           }

           // Approve the user in residentLines
           for (int i = 0; i < residentLines.size(); i++) {
               String[] details = residentLines.get(i).split("\\|\\|");
               if (details[0].equalsIgnoreCase("pending") && details[3].equalsIgnoreCase(nameToApprove)) {
                   details[0] = "approved";  // Change status to approved
                   residentLines.set(i, String.join("||", details));  // Update the line with approved status
                   updated = true;
                   System.out.println(nameToApprove + " (Resident) has been approved.");
                   break;
               }
           }

           // If there is an update, write the updated lines back to the file
           if (updated) {
               Files.write(Paths.get("manager.txt"), managerLines);
               Files.write(Paths.get("staff.txt"), staffLines);
               Files.write(Paths.get("resident.txt"), residentLines);
               System.out.println("User approval status updated.");
           } else {
               System.out.println("No matching user found to approve.");
           }

       } catch (IOException e) {
           System.out.println("Error processing the file: " + e.getMessage());
       }
   }




    public static void searchUser(String fileName) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the username to search for: ");
        String username = sc.nextLine().trim();

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            boolean userFound = false;

            for (String line : lines) {
                String[] details = line.split("\\|\\|");

                // Checking if the user exists based on the file type (manager, staff, resident)
                if (fileName.equals("manager.txt") || fileName.equals("staff.txt")) {
                    if (details[2].equalsIgnoreCase(username)) {
                        System.out.println("User found: ");
                        System.out.println("Status: " + details[0]);
                        System.out.println("Username: " + details[1]);
                        System.out.println("Name: " + details[2]);
                        System.out.println("Password: " + details[3]);
                        System.out.println("Email: " + details[4]);
                        System.out.println("Phone: " + details[5]);
                        userFound = true;
                        break;
                    }
                } else if (fileName.equals("resident.txt")) {
                    if (details[2].equalsIgnoreCase(username)) {
                        System.out.println("Resident found: ");
                        System.out.println("Status: " + details[0]);
                        System.out.println("Room Type: " + details[1]);
                        System.out.println("Username: " + details[2]);
                        System.out.println("Name: " + details[3]);
                        System.out.println("Password: " + details[4]);
                        System.out.println("Email: " + details[5]);
                        System.out.println("Phone: " + details[6]);
                        userFound = true;
                        break;
                    }
                }
            }

            if (!userFound) {
                System.out.println("No user found with the username: " + username);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }


    public static void updateUser(String fileName, Scanner sc) {
        System.out.println("Enter the username of the user you want to update: ");
        String username = sc.nextLine().trim();

        System.out.println("What would you like to update?");
        System.out.println("1. Password\n2. Email\n3. Phone Number\n");
        String choice = sc.nextLine().trim();

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            boolean updated = false;

            for (int i = 0; i < lines.size(); i++) {
                String[] details = lines.get(i).split("\\|\\|");

                // Check if the username matches
                if (fileName.equals("manager.txt") || fileName.equals("staff.txt")) {
                    if (details[2].equalsIgnoreCase(username)) {
                        updated = true;
                        System.out.println("User found: " + details[1] + " (" + details[2] + ")");

                        // Handle different updates based on user choice
                        switch (choice) {
                            case "1":
                                System.out.println("Enter new password: ");
                                String newPassword = sc.nextLine().trim();
                                details[3] = newPassword;
                                System.out.println("Password updated successfully.");
                                break;
                            case "2":
                                System.out.println("Enter new email: ");
                                String newEmail = sc.nextLine().trim();
                                details[4] = newEmail;
                                System.out.println("Email updated successfully.");
                                break;
                            case "3":
                                System.out.println("Enter new phone number: ");
                                String newPhone = sc.nextLine().trim();
                                details[5] = newPhone;
                                System.out.println("Phone number updated successfully.");
                                break;
                            default:
                                System.out.println("Invalid option.");
                                updated = false;
                                break;
                        }

                        // Save the updated line back to the list
                        lines.set(i, String.join("||", details));
                        break;
                    }
                } else if (fileName.equals("resident.txt")) {
                    if (details[2].equalsIgnoreCase(username)) {
                        updated = true;
                        System.out.println("Resident found: " + details[2] + " (" + details[3] + ")");

                        // Handle different updates based on user choice
                        switch (choice) {
                            case "1":
                                System.out.println("Enter new password: ");
                                String newPassword = sc.nextLine().trim();
                                details[4] = newPassword;
                                System.out.println("Password updated successfully.");
                                break;
                            case "2":
                                System.out.println("Enter new email: ");
                                String newEmail = sc.nextLine().trim();
                                details[5] = newEmail;
                                System.out.println("Email updated successfully.");
                                break;
                            case "3":
                                System.out.println("Enter new phone number: ");
                                String newPhone = sc.nextLine().trim();
                                details[6] = newPhone;
                                System.out.println("Phone number updated successfully.");
                                break;
                            case "4":
                                System.out.println("Enter new room type (Single/Twin/Triple): ");
                                String newRoomType = sc.nextLine().trim();
                                details[1] = newRoomType;
                                System.out.println("Room type updated successfully.");
                                break;
                            default:
                                System.out.println("Invalid option.");
                                updated = false;
                                break;
                        }

                        // Save the updated line back to the list
                        lines.set(i, String.join("||", details));
                        break;
                    }
                }
            }

            // If updates were made, write the modified list back to the file
            if (updated) {
                Files.write(Paths.get(fileName), lines);
                System.out.println("User updated successfully.");
            } else {
                System.out.println("No matching user found to update.");
            }
        } catch (IOException e) {
            System.out.println("Error processing the file: " + e.getMessage());
        }
    }


    public static void deleteUser(String fileName, Scanner sc) {
        System.out.println("Enter the username of the user you want to delete: ");
        String username = sc.nextLine().trim();

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            boolean deleted = false;

            for (int i = 0; i < lines.size(); i++) {
                String[] details = lines.get(i).split("\\|\\|");

                // For Manager and Staff, check if the username matches
                if ((fileName.equals("manager.txt") || fileName.equals("staff.txt")) && details[2].equalsIgnoreCase(username)) {
                    System.out.println("User found: " + details[1] + " (" + details[2] + ")");
                    lines.remove(i);  // Remove the user from the list
                    deleted = true;
                    break;
                }
                // For Resident, check if the username matches
                else if (fileName.equals("resident.txt") && details[2].equalsIgnoreCase(username)) {
                    System.out.println("Resident found: " + details[2] + " (" + details[3] + ")");
                    lines.remove(i);  // Remove the resident from the list
                    deleted = true;
                    break;
                }
            }

            // If a user was deleted, write the updated list back to the file
            if (deleted) {
                Files.write(Paths.get(fileName), lines);
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("No matching user found to delete.");
            }
        } catch (IOException e) {
            System.out.println("Error processing the file: " + e.getMessage());
        }
    }


    public static void updateRoomRate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter room type to update (Single/Twin/Triple): ");
        String roomType = sc.nextLine().trim();

        // Check for valid room type
        if (!(roomType.equalsIgnoreCase("Single") || roomType.equalsIgnoreCase("Twin") || roomType.equalsIgnoreCase("Triple"))) {
            System.out.println("Invalid room type. Please enter 'Single', 'Twin', or 'Triple'.");
            return;
        }

        System.out.println("Enter new rate for " + roomType + ": RM");
        double newRate;
        while (true) {
            try {
                newRate = Double.parseDouble(sc.nextLine().trim());
                if (newRate > 0) {
                    break;
                } else {
                    System.out.println("Rate must be greater than zero. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid rate. Please enter a valid number.");
            }
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get("room_rates.txt"));
            boolean updated = false;

            for (int i = 0; i < lines.size(); i++) {
                String[] details = lines.get(i).split("\\|");

                // Update the rate for the specified room type
                if (details[0].equalsIgnoreCase(roomType)) {
                    lines.set(i, roomType + "|" + newRate);
                    updated = true;
                    System.out.println("Room rate for " + roomType + " updated successfully.");
                    break;
                }
            }

            // If updates were made, write the modified list back to the file
            if (updated) {
                Files.write(Paths.get("room_rates.txt"), lines);
            } else {
                System.out.println("Room type not found. Please ensure you entered the correct type.");
            }
        } catch (IOException e) {
            System.out.println("Error updating room rates: " + e.getMessage());
        }
    }

    public static double getRoomRate(String roomType) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("room_rates.txt"));
            for (String line : lines) {
                String[] parts = line.split("\\|");
                if (parts[0].equalsIgnoreCase(roomType)) {
                    return Double.parseDouble(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading room rates: " + e.getMessage());
        }
        return 0.0; // Default if not found
    }


// Method to process the payment
    public static void makePayment(Scanner sc) {
        System.out.println("Enter the resident username for payment processing: ");
        String residentUsername = sc.nextLine().trim();

        System.out.println("Enter the payment amount (in RM): ");
        double amount;
        while (true) {
            try {
                amount = Double.parseDouble(sc.nextLine().trim());
                if (amount > 0) {
                    break;
                } else {
                    System.out.println("Payment amount must be greater than zero. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }

        System.out.println("Enter the month(s) covered (e.g., Jan-Feb, 1-3, or a single month like '1'): ");
        String monthCovered = sc.nextLine().trim();

        // Make sure you retrieve the staff username and room type from your system or database
        System.out.println("Enter the staff username processing this payment: ");
        String staffUser = sc.nextLine().trim();

        // Assuming roomType and months are available or need to be retrieved from user input or a database
        System.out.println("Enter room type (Single/Twin/Triple): ");
        String roomType = sc.nextLine().trim();

        System.out.println("Enter the number of months paid for: ");
        int months;
        while (true) {
            try {
                months = Integer.parseInt(sc.nextLine().trim());
                if (months > 0) {
                    break;
                } else {
                    System.out.println("Number of months must be greater than zero. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of months. Please enter a valid number.");
            }
        }

        // Assuming you have a method to get the room rate based on the room type (Single, Twin, etc.)
        double roomRate = getRoomRate(roomType);

        // Calculate total amount based on room rate and months
        double totalAmount = roomRate * months;

        try {
            // Generate the receipt and save the payment
            String receipt = generateReceipt(residentUsername, staffUser, roomType, months, monthCovered, totalAmount);
            savePaymentReceipt(receipt);
            System.out.println("Payment processed successfully. Receipt has been generated.");
        } catch (IOException e) {
            System.out.println("Error processing the payment: " + e.getMessage());
        }
    }



    // Method to generate a receipt for the payment
    public static String generateReceipt(String residentUsername, String staffUser, String roomType, int months, String monthCovered, double totalAmount) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        // Display shortened UUID or user's name
        String displayUsername = residentUsername.substring(0, 8);  // Display first 8 characters of UUID

        return "Title: APU Hostel Payment System | Date: " + dtf.format(now) +
               " | Resident Username: " + displayUsername +
               " | Staff Username: " + staffUser + " | Room Type: " + roomType +
               " | Months Paid: " + months + " | Month(s): " + monthCovered +
               " | Total Amount: RM" + totalAmount;
    }




    // Method to save payment receipt to payment records file
    private static void savePaymentReceipt(String receipt) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("payment_records.txt", true))) {
            bw.write(receipt);
            bw.newLine();
        }
    }

    public static void updateResidentAccount(String fileName, String loggedInName, Scanner sc) {
        System.out.println("\nUpdate Account:");

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            boolean updated = false;

            for (int i = 0; i < lines.size(); i++) {
                String[] details = lines.get(i).split("\\|\\|");

                if (details[3].equalsIgnoreCase(loggedInName)) {  // Match name from login
                    updated = true;
                    System.out.println("What would you like to update?");
                    System.out.println("1. Password\n2. Email\n3. Phone Number\n4. Room Type");
                    String option = sc.nextLine().trim();

                    switch (option) {
                        case "1":
                            System.out.println("Enter new password: ");
                            details[4] = sc.nextLine().trim();
                            System.out.println("Password updated successfully.");
                            break;
                        case "2":
                            System.out.println("Enter new email: ");
                            details[5] = sc.nextLine().trim();
                            System.out.println("Email updated successfully.");
                            break;
                        case "3":
                            System.out.println("Enter new phone number: ");
                            details[6] = sc.nextLine().trim();
                            System.out.println("Phone number updated successfully.");
                            break;
                        case "4":
                            System.out.println("Enter new room type (Single/Twin/Triple): ");
                            details[1] = sc.nextLine().trim();
                            System.out.println("Room type updated successfully.");
                            break;
                        default:
                            System.out.println("Invalid option.");
                            return;
                    }

                    // Update the line in the file
                    lines.set(i, String.join("||", details));
                    break;
                }
            }

            if (updated) {
                Files.write(Paths.get(fileName), lines);
                System.out.println("Account updated successfully.");
            } else {
                System.out.println("No matching record found.");
            }
        } catch (IOException e) {
            System.out.println("Error updating the account: " + e.getMessage());
        }
    }

    
    public static void viewPaymentRecords(Scanner sc) {
        System.out.println("Enter the resident username to view payment records: ");
        String residentUsername = sc.nextLine().trim();

        try {
            List<String> lines = Files.readAllLines(Paths.get("payment_records.txt"));
            boolean recordsFound = false;

            System.out.println("\n--- Payment Records for " + residentUsername + " ---");
            for (String line : lines) {
                if (line.contains("Resident Username: " + residentUsername)) {
                    System.out.println(line);  // Display the payment record
                    recordsFound = true;
                }
            }

            if (!recordsFound) {
                System.out.println("No payment records found for resident: " + residentUsername);
            }
        } catch (IOException e) {
            System.out.println("Error reading payment records: " + e.getMessage());
        }
    }

}

// Main class
public class AHMFPS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose role: 1. Manager 2. Staff 3. Resident");
        String role = sc.nextLine();

        User user = null;
        switch (role) {
            case "1":
                user = new Manager();
                break;
            case "2":
                user = new Staff();
                break;
            case "3":
                user = new Resident();
                break;
            default:
                System.out.println("Invalid role.");
                return;
        }

        // Show menu first
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    SystemUtility.registerUser(user.fileName);
                    break;
                case "2":
                    // If the user chooses to login
                    String loggedInUser = user.login(sc);
                    if (loggedInUser == null) {
                        System.out.println("Login failed. Exiting...");
                        return;  // If login fails, exit the program
                    } else {
                        // Proceed to the menu after successful login
                        if (user instanceof Manager) {
                            ((Manager) user).menu(sc);
                        } else if (user instanceof Staff) {
                            ((Staff) user).menu(sc);
                        } else if (user instanceof Resident) {
                            ((Resident) user).menu(sc);
                        }
                    }
                    break;
                case "3":
                    return;  // Exit the program
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Method to check if the user is registered (check the corresponding file)
    private static boolean isUserRegistered(User user) {
        // Check if the user is already registered by checking the corresponding file
        File file = new File(user.getFileName());
        return file.exists() && file.length() > 0;  // Ensure that the file is not empty
    }
}