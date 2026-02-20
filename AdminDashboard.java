import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminDashboard extends JFrame {
    // Reference to the system manager for data operations
    private SystemManager systemManager;
    
    // Reference to the currently logged-in admin user
    private AdminStaff admin;
    public AdminDashboard(SystemManager systemManager, AdminStaff admin) {
        this.systemManager = systemManager;
        this.admin = admin;
        initializeFrame();
    }
    
    /**
     * Initializes the dashboard frame and all its components.
     * Sets up the header, tabbed pane with different management panels,
     * and the logout button.
     */
    private void initializeFrame() {
        // Set up window properties
        setTitle("Admin Dashboard - " + admin.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Create main container panel with proper spacing
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ==================== HEADER SECTION ====================
        // Blue header banner displaying welcome message
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(25, 118, 210));
        JLabel welcomeLabel = new JLabel("Welcome, " + admin.getFullName() + " (Admin Staff)");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(welcomeLabel);
        
        // ==================== TABBED PANE SECTION ====================
        // Create tabbed interface for different admin functions
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(240, 240, 240));

        // Create content panels for each tab using dedicated panel classes
        // Each panel handles a specific admin function
        AdminUserManagementPanel userPanel = new AdminUserManagementPanel(systemManager, this);
        AdminLecturerAssignmentPanel assignPanel = new AdminLecturerAssignmentPanel(systemManager, this);
        AdminGradingSystemPanel gradingPanel = new AdminGradingSystemPanel(systemManager, this);
        AdminClassCreationPanel classPanel = new AdminClassCreationPanel(systemManager, this);

        // Add tabs with descriptive titles
        tabbedPane.addTab("User Management", userPanel);
        tabbedPane.addTab("Lecturer Assignment", assignPanel);
        tabbedPane.addTab("Grading System", gradingPanel);
        tabbedPane.addTab("Create Classes", classPanel);
        
        // ==================== LOGOUT SECTION ====================
        // Bottom panel with logout button aligned to the right
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(240, 240, 240));
        JButton logoutBtn = new JButton("Logout");
        // Style the logout button with red color to indicate exit action
        logoutBtn.setBackground(new Color(244, 67, 54));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setOpaque(true);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        // Add action listener to handle logout - returns to login screen
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close this dashboard and show login frame
                dispose();
                new AFSLoginFrame(systemManager).setVisible(true);
            }
        });
        bottomPanel.add(logoutBtn);
        
        // ==================== ASSEMBLE ALL COMPONENTS ====================
        // Add all sections to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
}
