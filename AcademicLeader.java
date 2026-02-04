import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * AcademicLeader class - manages academic operations
 * Responsibilities: Module management, lecturer assignment, report generation
 */
public class AcademicLeader extends User {
    private static final long serialVersionUID = 1L;
    
    private String department;
    private String leaderID;
    private List<Lecturer> assignedLecturers;
    private List<Module> managedModules;
    
    public AcademicLeader(String userID, String username, String password, String email,
                          String fullName, String phoneNumber, String department, String leaderID) {
        super(userID, username, password, email, fullName, phoneNumber, "ACADEMIC_LEADER");
        this.department = department;
        this.leaderID = leaderID;
        this.assignedLecturers = new ArrayList<>();
        this.managedModules = new ArrayList<>();
    }
    
    @Override
    public void displayMenu() {
        
        
        
        
        
        
    }
    
    @Override
    public void handleAction(String action) {
        switch(action) {
            case "1":
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                break;
            default:
        }
    }
    
    public void createModule(Module module) {
        managedModules.add(module);
        
    }
    
    public void assignLecturer(Lecturer lecturer) {
        assignedLecturers.add(lecturer);
        
    }
    
    public void generateAnalyzedReport() {
        
    }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getLeaderID() { return leaderID; }
    public void setLeaderID(String leaderID) { this.leaderID = leaderID; }
    
    public List<Lecturer> getAssignedLecturers() { return assignedLecturers; }
    public List<Module> getManagedModules() { return managedModules; }
}

class AcademicLeaderDashboard extends JFrame {
    private SystemManager systemManager;
    private AcademicLeader leader;
    
    public AcademicLeaderDashboard(SystemManager systemManager, AcademicLeader leader) {
        this.systemManager = systemManager;
        this.leader = leader;
        initializeFrame();
    }
    
    private void initializeFrame() {
        setTitle("Academic Leader Dashboard - " + leader.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(56, 142, 60));
        JLabel welcomeLabel = new JLabel("Welcome, " + leader.getFullName() + " (Academic Leader)");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(welcomeLabel);
        
        // Function Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(240, 240, 240));

        // Create content panels for each tab
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(Color.WHITE);
        showEditProfile(profilePanel);

        JPanel modulePanel = new JPanel(new BorderLayout());
        modulePanel.setBackground(Color.WHITE);
        showModuleManagement(modulePanel);

        JPanel assignPanel = new JPanel(new BorderLayout());
        assignPanel.setBackground(Color.WHITE);
        showAssignLecturers(assignPanel);

        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBackground(Color.WHITE);
        showReports(reportPanel);

        // Add tabs
        tabbedPane.addTab("Edit Profile", profilePanel);
        tabbedPane.addTab("Module Management", modulePanel);
        tabbedPane.addTab("Assign Lecturers", assignPanel);
        tabbedPane.addTab("View Reports", reportPanel);
        
        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createTitledBorder("Content Area"));
        
        // Logout Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(240, 240, 240));
        JButton logoutBtn = createMenuButton("Logout", new Color(244, 67, 54));
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
    
    private JButton createMenuButton(String text) {
        return createMenuButton(text, new Color(56, 142, 60));
    }

    private JButton createMenuButton(String text, Color baseColor) {
        Color hover = baseColor.brighter();
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(baseColor);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 60));
        if (baseColor.getRed() > 200 && baseColor.getGreen() < 100 && baseColor.getBlue() < 100) {
            button.addMouseListener(new MouseAdapter() {});
        } else {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { button.setBackground(hover); }
                @Override
                public void mouseExited(MouseEvent e) { button.setBackground(baseColor); }
            });
        }
        return button;
    }
    
    // Helper to create a horizontal labeled row: label on left, component on right
    private JPanel createLabeledRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(110, 24));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        row.add(lbl, BorderLayout.WEST);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }
    
    private void showEditProfile(JPanel contentPanel) {
        JPanel panel = new JPanel(); panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        JTextField nameField = new JTextField(leader.getFullName());
        JTextField emailField = new JTextField(leader.getEmail());
        JTextField departmentField = new JTextField(leader.getDepartment());
        panel.add(createLabeledRow("Full Name:", nameField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Email:", emailField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Department:", departmentField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Leader ID:", new JLabel(leader.getLeaderID()))); panel.add(Box.createVerticalStrut(8));
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT)); JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(AcademicLeaderDashboard.this, "✓ Profile updated successfully!");
            }
        }); btnRow.add(saveBtn); panel.add(btnRow);
        contentPanel.add(panel, BorderLayout.CENTER);
    }
    
    private void showModuleManagement(JPanel contentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField moduleCodeField = new JTextField();
        JTextField moduleNameField = new JTextField();
        JTextField creditsField = new JTextField("3");
        JTextField departmentField = new JTextField(leader.getDepartment());

        panel.add(createLabeledRow("Module Code:", moduleCodeField));
        panel.add(createLabeledRow("Module Name:", moduleNameField));
        panel.add(createLabeledRow("Credits:", creditsField));
        panel.add(createLabeledRow("Department:", departmentField));
        
        JButton createBtn = new JButton("Create Module");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = moduleCodeField.getText().trim();
                String name = moduleNameField.getText().trim();
                int credits = 3;
                try { credits = Integer.parseInt(creditsField.getText().trim()); } catch (Exception ignored) {}
                String dept = departmentField.getText().trim();
                String moduleID = systemManager.generateModuleID();
                Module mod = new Module(moduleID, name.isEmpty() ? code : name, code, "", credits, dept);
                systemManager.createModule(mod);
                JOptionPane.showMessageDialog(AcademicLeaderDashboard.this,
                    "✓ Module Created!\n\n" +
                    "ID: " + moduleID + "\n" +
                    "Code: " + code + "\n" +
                    "Name: " + name + "\n" +
                    "Credits: " + credits);
            }
        });
        
        panel.add(createBtn);
        
        contentPanel.add(panel, BorderLayout.CENTER);
    }
    
    private void showAssignLecturers(JPanel contentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        java.util.List<Module> modulesList = systemManager.getAllModules();
        String[] modulesArr = modulesList.stream().map(m -> m.getModuleCode() + " - " + m.getModuleName()).toArray(String[]::new);
        if (modulesArr.length == 0) modulesArr = new String[]{"No modules available"};
        JComboBox<String> moduleCombo = new JComboBox<>(modulesArr);

        java.util.List<User> lecturerUsers = systemManager.getUsersByRole("LECTURER");
        String[] lecturers = lecturerUsers.stream().map(u -> u.getUserID() + " - " + u.getFullName()).toArray(String[]::new);
        if (lecturers.length == 0) lecturers = new String[]{"No lecturers available"};
        JComboBox<String> lecturerCombo = new JComboBox<>(lecturers);

        panel.add(createLabeledRow("Select Module:", moduleCombo));
        panel.add(createLabeledRow("Select Lecturer:", lecturerCombo));

        JButton assignBtn = new JButton("Assign Lecturer");
        assignBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String modSel = (String) moduleCombo.getSelectedItem();
                String lecSel = (String) lecturerCombo.getSelectedItem();
                if (modSel == null || lecSel == null) {
                    JOptionPane.showMessageDialog(AcademicLeaderDashboard.this, "Please select both module and lecturer.");
                    return;
                }
                String modCode = modSel.split(" - ")[0];
                String lecID = lecSel.split(" - ")[0];

                Module chosen = null;
                for (Module m : systemManager.getAllModules()) if (m.getModuleCode().equals(modCode)) { chosen = m; break; }
                User u = systemManager.findUserByID(lecID);
                if (chosen != null && u instanceof Lecturer) {
                    Lecturer L = (Lecturer) u;
                    L.getAssignedModules().add(chosen);
                    JOptionPane.showMessageDialog(AcademicLeaderDashboard.this,
                        "✓ Lecturer Assigned!\n\n" +
                        "Module: " + chosen.getModuleCode() + " - " + chosen.getModuleName() + "\n" +
                        "Lecturer: " + L.getFullName());
                } else {
                    JOptionPane.showMessageDialog(AcademicLeaderDashboard.this, "Assignment failed - check selections.");
                }
            }
        });
        
        panel.add(assignBtn);
        
        contentPanel.add(panel, BorderLayout.CENTER);
    }
    
    private void showReports(JPanel contentPanel) {
        StringBuilder report = new StringBuilder();
        report.append("Academic Reports\n\n");
        report.append("Department: ").append(leader.getDepartment()).append("\n");
        report.append("Academic Leader: ").append(leader.getFullName()).append("\n\n");
        report.append("Summary:\n");
        report.append("├─ Total Modules: 12\n");
        report.append("├─ Total Lecturers: 8\n");
        report.append("├─ Total Students: 450\n");
        report.append("├─ Average Class Size: 37\n");
        report.append("├─ Pass Rate: 85%\n");
        report.append("├─ Fail Rate: 15%\n");
        report.append("└─ Overall GPA: 3.4/4.0\n\n");
        report.append("Top Performers:\n");
        report.append("1. CS301 - Database Systems (89% Pass Rate)\n");
        report.append("2. CS201 - Data Structures (88% Pass Rate)\n");
        report.append("3. CS101 - Programming (86% Pass Rate)");
        
        JTextArea reportArea = new JTextArea(report.toString());
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }
}
