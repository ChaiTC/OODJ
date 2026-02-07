import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;

/**
 * AFSRegistrationFrame - GUI Registration frame
 */
public class AFSRegistrationFrame extends JFrame {
    private SystemManager systemManager;
    private JComboBox<String> userTypeCombo, departmentCombo, genderCombo;
    private JTextField userIDField, usernameField, emailField, fullNameField, phoneField;
    private JTextField staffIdField, enrollmentField;
    private JPasswordField passwordField;
    private JSpinner ageSpinner;
    private JLabel departmentLabel, staffIdLabel;
    private JLabel usernameError, passwordError, emailError, fullNameError, phoneError;
    
    public AFSRegistrationFrame(SystemManager systemManager) {
        this.systemManager = systemManager;
        initializeFrame();
    }
    
    private void initializeFrame() {
        setTitle("Assessment Feedback System - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(56, 142, 60));
        JLabel titleLabel = new JLabel("Register New User");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);
        
        // Registration Panel
        JPanel registrationPanel = new JPanel();
        registrationPanel.setLayout(new BoxLayout(registrationPanel, BoxLayout.Y_AXIS));
        registrationPanel.setBackground(new Color(240, 240, 240));
        registrationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // User Type
        userTypeCombo = new JComboBox<>(new String[]{"Student", "Lecturer", "Academic Leader", "Admin Staff"});
        registrationPanel.add(createFieldRow("User Type:", userTypeCombo, null));
        
        // User ID (auto-generated)
        userIDField = new JTextField();
        userIDField.setEditable(false);
        userIDField.setBackground(new Color(220, 220, 220));
        registrationPanel.add(createFieldRow("User ID:", userIDField, null));
        
        // Username
        usernameField = new JTextField();
        usernameError = new JLabel();
        registrationPanel.add(createFieldRow("Username:", usernameField, usernameError));
        
        // Password
        passwordField = new JPasswordField();
        passwordError = new JLabel();
        registrationPanel.add(createFieldRow("Password:", passwordField, passwordError));
        
        // Email
        emailField = new JTextField();
        emailError = new JLabel();
        registrationPanel.add(createFieldRow("Email:", emailField, emailError));
        
        // Full Name
        fullNameField = new JTextField();
        fullNameError = new JLabel();
        registrationPanel.add(createFieldRow("Full Name:", fullNameField, fullNameError));
        
        // Phone Number
        phoneField = new JTextField();
        phoneError = new JLabel();
        registrationPanel.add(createFieldRow("Phone Number:", phoneField, phoneError));

        // Gender
        genderCombo = new JComboBox<>(new String[]{"Male", "Female"});
        registrationPanel.add(createFieldRow("Gender:", genderCombo, null));

        // Age
        ageSpinner = new JSpinner(new SpinnerNumberModel(20, 15, 100, 1));
        registrationPanel.add(createFieldRow("Age:", ageSpinner, null));
        
        // Department (dropdown)
        departmentLabel = new JLabel("Department:");
        departmentCombo = new JComboBox<>();
        registrationPanel.add(createFieldRow("Department:", departmentCombo, null));
        
        // Staff ID (auto-generated for staff types)
        staffIdLabel = new JLabel("Staff ID:");
        staffIdField = new JTextField();
        staffIdField.setEditable(false);
        staffIdField.setBackground(new Color(220, 220, 220));
        registrationPanel.add(createFieldRow("Staff ID:", staffIdField, null));
        
        // Enrollment Year (for students only)
        String currentYear = String.valueOf(java.time.Year.now().getValue());
        enrollmentField = new JTextField(currentYear);
        registrationPanel.add(createFieldRow("Enrollment Year:", enrollmentField, null));
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));
        registerButton.setBackground(new Color(56, 142, 60));
        registerButton.setForeground(Color.WHITE);
        registerButton.setOpaque(true);
        registerButton.setContentAreaFilled(true);
        registerButton.setBorderPainted(false);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setOpaque(true);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setBorderPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AFSRegistrationFrame.this.dispose();
            }
        });
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(registrationPanel), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Add listeners after all components are initialized
        userTypeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFieldsVisibility();
                generateUserID();
            }
        });
        
        // Initialize field visibility and generate initial IDs
        updateFieldsVisibility();
        generateUserID();
    }
    
    private JPanel createFieldRow(String labelText, JComponent field, JLabel errorLabel) {
        JPanel rowPanel = new JPanel(new BorderLayout(5, 2));
        rowPanel.setBackground(new Color(240, 240, 240));
        
        JPanel fieldPanel = new JPanel(new BorderLayout(5, 0));
        fieldPanel.setBackground(new Color(240, 240, 240));
        
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(120, 25));
        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(field, BorderLayout.CENTER);
        
        rowPanel.add(fieldPanel, BorderLayout.NORTH);
        
        if (errorLabel != null) {
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            errorLabel.setVisible(false);
            rowPanel.add(errorLabel, BorderLayout.SOUTH);
        }
        
        return rowPanel;
    }
    
    private void generateUserID() {
        String userType = (String) userTypeCombo.getSelectedItem();
        String generatedID = systemManager.generateUserID(userType);
        userIDField.setText(generatedID);
        
        // Generate staff ID for non-student types
        if (!"Student".equals(userType)) {
            String staffID = systemManager.generateStaffID();
            staffIdField.setText(staffID);
        } else {
            staffIdField.setText("N/A");
        }
    }
    
    private void updateFieldsVisibility() {
        String userType = (String) userTypeCombo.getSelectedItem();
        
        // All user types need department and staff ID
        departmentLabel.setVisible(true);
        departmentCombo.setVisible(true);
        staffIdLabel.setVisible(true);
        staffIdField.setVisible(true);
        
        // Set department options based on user type
        departmentCombo.removeAllItems();
        if (userType.equals("Admin Staff")) {
            departmentCombo.addItem("Administration");
            departmentCombo.setSelectedIndex(0);
        } else {
            departmentCombo.addItem("IT");
            departmentCombo.addItem("Business");
            departmentCombo.addItem("Engineering");
            departmentCombo.setSelectedIndex(0);
        }
        
        // Set appropriate labels
        if (userType.equals("Student")) {
            staffIdLabel.setText("Student ID:");
            staffIdField.setText("N/A"); // Students don't have separate staff ID
        } else {
            staffIdLabel.setText("Staff ID:");
            // Staff ID will be auto-generated
        }
    }
    
    private void handleRegistration() {
        // Clear all error messages
        usernameError.setText("");
        usernameError.setVisible(false);
        passwordError.setText("");
        passwordError.setVisible(false);
        emailError.setText("");
        emailError.setVisible(false);
        fullNameError.setText("");
        fullNameError.setVisible(false);
        phoneError.setText("");
        phoneError.setVisible(false);
        
        String userID = userIDField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String department = (String) departmentCombo.getSelectedItem();
        String staffId = staffIdField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        int age = (Integer) ageSpinner.getValue();
        
        // Basic field validation
        if (username.isEmpty()) {
            usernameError.setText("Username is required!");
            usernameError.setVisible(true);
            return;
        }
        if (password.isEmpty()) {
            passwordError.setText("Password is required!");
            passwordError.setVisible(true);
            return;
        }
        if (email.isEmpty()) {
            emailError.setText("Email is required!");
            emailError.setVisible(true);
            return;
        }
        if (fullName.isEmpty()) {
            fullNameError.setText("Full Name is required!");
            fullNameError.setVisible(true);
            return;
        }
        if (phone.isEmpty()) {
            phoneError.setText("Phone Number is required!");
            phoneError.setVisible(true);
            return;
        }
        
        // Password validation
        if (password.length() < 8) {
            passwordError.setText("Password must be at least 8 characters long!");
            passwordError.setVisible(true);
            return;
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            passwordError.setText("Password must contain at least one special character!");
            passwordError.setVisible(true);
            return;
        }
        
        // Email validation
        if (!email.matches(".*@.*\\..*")) {
            emailError.setText("Please enter a valid email address (must contain @ and domain)!");
            emailError.setVisible(true);
            return;
        }
        
        User newUser = null;
        String userType = (String) userTypeCombo.getSelectedItem();
        
        try {
            if (userType.equals("Student")) {
                String enrollmentYear = enrollmentField.getText().trim();
                newUser = new Student(userID, username, password, email, fullName, phone, userID, enrollmentYear);
            } else if (userType.equals("Lecturer")) {
                newUser = new Lecturer(userID, username, password, email, fullName, phone, staffId, department);
            } else if (userType.equals("Academic Leader")) {
                newUser = new AcademicLeader(userID, username, password, email, fullName, phone, department, staffId);
            } else if (userType.equals("Admin Staff")) {
                newUser = new AdminStaff(userID, username, password, email, fullName, phone, department, staffId);
            }
            
            if (newUser != null) {
                newUser.setGender(gender);
                newUser.setAge(age);
            }

            if (newUser != null && systemManager.registerUser(newUser)) {
                JOptionPane.showMessageDialog(this, 
                    "Registration successful!\n\n" +
                    "User ID: " + userID + "\n" +
                    "Staff ID: " + ("Student".equals(userType) ? "N/A" : staffId) + "\n\n" +
                    "You can now login with your credentials.", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed! Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
