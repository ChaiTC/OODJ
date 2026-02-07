import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminDashboard extends JFrame {
    private SystemManager systemManager;
    private AdminStaff admin;
    
    public AdminDashboard(SystemManager systemManager, AdminStaff admin) {
        this.systemManager = systemManager;
        this.admin = admin;
        initializeFrame();
    }
    
    private void initializeFrame() {
        setTitle("Admin Dashboard - " + admin.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(25, 118, 210));
        JLabel welcomeLabel = new JLabel("Welcome, " + admin.getFullName() + " (Admin Staff)");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(welcomeLabel);
        
        // Function Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(240, 240, 240));

        // Create content panels for each tab using dedicated panel classes
        AdminUserManagementPanel userPanel = new AdminUserManagementPanel(systemManager, this);
        AdminLecturerAssignmentPanel assignPanel = new AdminLecturerAssignmentPanel(systemManager, this);
        AdminGradingSystemPanel gradingPanel = new AdminGradingSystemPanel(systemManager, this);
        AdminClassCreationPanel classPanel = new AdminClassCreationPanel(systemManager, this);

        // Add tabs
        tabbedPane.addTab("User Management", userPanel);
        tabbedPane.addTab("Lecturer Assignment", assignPanel);
        tabbedPane.addTab("Grading System", gradingPanel);
        tabbedPane.addTab("Create Classes", classPanel);
        
        // Logout Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(240, 240, 240));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(244, 67, 54));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setOpaque(true);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AFSLoginFrame().setVisible(true);
            }
        });
        bottomPanel.add(logoutBtn);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
}
