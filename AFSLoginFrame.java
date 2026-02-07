import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;

public class AFSLoginFrame extends JFrame {
    private SystemManager systemManager;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    
    public AFSLoginFrame() {
        this.systemManager = new SystemManager();
        initializeFrame();
    }
    
    private void initializeFrame() {
        setTitle("Assessment Feedback System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(25, 118, 210));
        JLabel titleLabel = new JLabel("ASSESSMENT FEEDBACK SYSTEM");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel);
        
        // Login Panel — use labeled rows so label and field sit on the same line
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(new Color(240, 240, 240));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField.setPreferredSize(new Dimension(300, 32));
        usernameField.setMaximumSize(new Dimension(300, 32));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setPreferredSize(new Dimension(300, 32));
        passwordField.setMaximumSize(new Dimension(300, 32));

        loginPanel.add(createFieldRow("Username:", usernameField));
        loginPanel.add(Box.createVerticalStrut(12));
        loginPanel.add(createFieldRow("Password:", passwordField));
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setBackground(new Color(25, 118, 210));
        loginButton.setForeground(Color.WHITE);
        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        loginButton.setBorderPainted(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        buttonPanel.add(loginButton);
        
        // Registration link panel
        JPanel registerLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerLinkPanel.setBackground(new Color(240, 240, 240));
        
        JLabel notRegisteredLabel = new JLabel("Not registered yet? ");
        notRegisteredLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel registerLink = new JLabel("<html><u>Register Here</u></html>");
        registerLink.setFont(new Font("Arial", Font.PLAIN, 12));
        registerLink.setForeground(new Color(25, 118, 210));
        registerLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openRegistrationFrame();
            }
        });
        
        registerLinkPanel.add(notRegisteredLabel);
        registerLinkPanel.add(registerLink);
        
        // Status Label
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Combine panels
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.add(statusLabel);
        bottomPanel.add(Box.createVerticalStrut(5));
        bottomPanel.add(buttonPanel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(registerLinkPanel);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            return;
        }
        
        User user = systemManager.authenticateUser(username, password);
        if (user != null) {
            openDashboard(user);
            this.dispose();
        } else {
            statusLabel.setText("Invalid username or password!");
            passwordField.setText("");
        }
    }
    
    private void openRegistrationFrame() {
        new AFSRegistrationFrame(systemManager).setVisible(true);
    }

    // Helper to create a horizontal labeled row: label on left, component on right
    private JPanel createFieldRow(String labelText, JComponent field) {
        JPanel rowPanel = new JPanel(new BorderLayout(3, 0));
        rowPanel.setBackground(new Color(240, 240, 240));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(90, 32));
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        rowPanel.add(label, BorderLayout.WEST);
        rowPanel.add(field, BorderLayout.CENTER);

        return rowPanel;
    }
    
    private void openDashboard(User user) {
        if (user instanceof AdminStaff) {
            new AdminDashboard(systemManager, (AdminStaff) user).setVisible(true);
        } else if (user instanceof AcademicLeader) {
            new AcademicLeaderDashboard(systemManager, (AcademicLeader) user).setVisible(true);
        } else if (user instanceof Lecturer) {
            new LecturerDashboard(systemManager, (Lecturer) user).setVisible(true);
        } else if (user instanceof Student) {
            new StudentDashboard(systemManager, (Student) user).setVisible(true);
        }
    }
}
