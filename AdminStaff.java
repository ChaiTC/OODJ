import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.JTabbedPane;
import javax.swing.JPasswordField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminStaff extends User {
    private static final long serialVersionUID = 1L;
    private String department;
    private String staffID;
    
    public AdminStaff(String userID, String username, String password, String email,
                      String fullName, String phoneNumber, String department, String staffID) {
        super(userID, username, password, email, fullName, phoneNumber, "ADMIN_STAFF");
        this.department = department;
        this.staffID = staffID;
    }
    
    @Override
    public void displayMenu() {
        System.out.println("\n========== ADMIN STAFF MENU ==========");
        System.out.println("1. Create/Read/Update/Delete Users");
        System.out.println("2. Assign Lecturers to Academic Leaders");
        System.out.println("3. Define APU Grading System");
        System.out.println("4. Create New Classes");
        System.out.println("5. View System Reports");
        System.out.println("6. Logout");
        System.out.println("=====================================");
    }
    
    @Override
    public void handleAction(String action) {
        switch(action) {
            case "1":
                System.out.println("Opening User Management Module...");
                break;
            case "2":
                System.out.println("Opening Lecturer Assignment Module...");
                break;
            case "3":
                System.out.println("Opening Grading System Configuration...");
                break;
            case "4":
                System.out.println("Opening Class Creation Module...");
                break;
            case "5":
                System.out.println("Opening System Reports...");
                break;
            case "6":
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid action!");
        }
    }
    
    public void createUser(User user) {
        System.out.println("User " + user.getFullName() + " has been created by " + this.getFullName());
    }
    
    public void deleteUser(String userID) {
        System.out.println("User " + userID + " has been deleted by " + this.getFullName());
    }
    
    public void defineGradingSystem(GradingSystem gradingSystem) {
        System.out.println("Grading system has been updated by " + this.getFullName());
    }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getStaffID() { return staffID; }
    public void setStaffID(String staffID) { this.staffID = staffID; }
}

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
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
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

        // Create content panels for each tab
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(Color.WHITE);
        showUserManagement(userPanel);

        JPanel assignPanel = new JPanel(new BorderLayout());
        assignPanel.setBackground(Color.WHITE);
        showLecturerAssignment(assignPanel);

        JPanel gradingPanel = new JPanel(new BorderLayout());
        gradingPanel.setBackground(Color.WHITE);
        showGradingSystem(gradingPanel);

        JPanel classPanel = new JPanel(new BorderLayout());
        classPanel.setBackground(Color.WHITE);
        showCreateClass(classPanel);

        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBackground(Color.WHITE);
        showReports(reportPanel);

        // Add tabs
        tabbedPane.addTab("User Management", userPanel);
        tabbedPane.addTab("Lecturer Assignment", assignPanel);
        tabbedPane.addTab("Grading System", gradingPanel);
        tabbedPane.addTab("Create Classes", classPanel);
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
        return createMenuButton(text, new Color(25, 118, 210));
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
        // Hover effect (don't change color for logout/special buttons with red background)
        if (baseColor.getRed() > 200 && baseColor.getGreen() < 100 && baseColor.getBlue() < 100) {
            // Red button - no hover effect
            button.addMouseListener(new MouseAdapter() {});
        } else {
            // Normal button - apply hover effect
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
    
    private void showUserManagement(JPanel contentPanel) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // User table
        java.util.List<User> users = systemManager.getAllUsers();
        String[] cols = new String[] {"UserID", "Username", "Full Name", "Email", "Role"};
        Object[][] data = new Object[users.size()][];
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            data[i] = new Object[] { u.getUserID(), u.getUsername(), u.getFullName(), u.getEmail(), u.getRole() };
        }

        JTable table = new JTable(data, cols);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        
        // Action buttons
        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createBtn = new JButton("Create User");
        JButton editBtn = new JButton("Edit User");
        JButton deleteBtn = new JButton("Delete User");
        JButton toggleFormBtn = new JButton("Show Form");
        buttonBar.add(createBtn);
        buttonBar.add(editBtn);
        buttonBar.add(deleteBtn);
        buttonBar.add(toggleFormBtn);
        
        // Form panel (initially hidden)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("User Form"));
        formPanel.setVisible(false);
        
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Student","Lecturer","Academic Leader","Admin Staff"});
        JTextField userIdField = new JTextField();
        userIdField.setEditable(false); // Auto-generated
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField email = new JTextField();
        JTextField fullname = new JTextField();
        JTextField phone = new JTextField();
        JComboBox<String> departmentBox = new JComboBox<>(new String[]{"IT", "Business", "Engineering", "Administration"});
        JTextField staffIdField = new JTextField();
        staffIdField.setEditable(false); // Auto-generated
        
        formPanel.add(createLabeledRow("Type:", typeBox));
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(createLabeledRow("User ID:", userIdField));
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(createLabeledRow("Username:", username));
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(createLabeledRow("Password:", password));
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(createLabeledRow("Email:", email));
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(createLabeledRow("Full name:", fullname));
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(createLabeledRow("Phone Number:", phone));
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(createLabeledRow("Department:", departmentBox));
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(createLabeledRow("Staff ID:", staffIdField));
        formPanel.add(Box.createVerticalStrut(8));
        
        JPanel formBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        formBtnPanel.add(saveBtn);
        formBtnPanel.add(cancelBtn);
        formPanel.add(formBtnPanel);

        // Place table and form in BorderLayout for flexibility
        mainPanel.add(buttonBar, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);
        // Form will be added to SOUTH when shown
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                typeBox.setSelectedIndex(0);
                userIdField.setText("");
                username.setText("");
                password.setText("");
                email.setText("");
                fullname.setText("");
                phone.setText("");
                // Update department options for default user type (Student)
                departmentBox.removeAllItems();
                departmentBox.addItem("IT");
                departmentBox.addItem("Business");
                departmentBox.addItem("Engineering");
                departmentBox.setSelectedIndex(0);
                staffIdField.setText("");
                formPanel.setVisible(true);
                saveBtn.setText("Create");
                mainPanel.add(formPanel, BorderLayout.SOUTH);
                toggleFormBtn.setText("Hide Form");
                contentPanel.revalidate();
            }
        });
        
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) { 
                    JOptionPane.showMessageDialog(AdminDashboard.this, "Select a user to edit"); 
                    return; 
                }
                String userId = (String)table.getValueAt(r,0);
                User u = systemManager.findUserByID(userId);
                if (u == null) { 
                    JOptionPane.showMessageDialog(AdminDashboard.this, "User not found"); 
                    return; 
                }
                
                // Set form values
                if (u instanceof Student) {
                    typeBox.setSelectedItem("Student");
                    userIdField.setText(u.getUserID());
                    departmentBox.setSelectedItem("IT"); // Default for students
                    staffIdField.setText("N/A");
                    phone.setText(u.getPhoneNumber());
                } else if (u instanceof Lecturer) {
                    typeBox.setSelectedItem("Lecturer");
                    Lecturer lec = (Lecturer) u;
                    userIdField.setText(u.getUserID());
                    departmentBox.setSelectedItem(lec.getDepartment());
                    staffIdField.setText(lec.getLecturerID());
                } else if (u instanceof AcademicLeader) {
                    typeBox.setSelectedItem("Academic Leader");
                    AcademicLeader leader = (AcademicLeader) u;
                    userIdField.setText(u.getUserID());
                    departmentBox.setSelectedItem(leader.getDepartment());
                    staffIdField.setText(leader.getLeaderID());
                } else if (u instanceof AdminStaff) {
                    typeBox.setSelectedItem("Admin Staff");
                    AdminStaff staff = (AdminStaff) u;
                    userIdField.setText(u.getUserID());
                    departmentBox.setSelectedItem(staff.getDepartment());
                    staffIdField.setText(staff.getStaffID());
                    phone.setText(u.getPhoneNumber());
                }
                
                username.setText(u.getUsername());
                password.setText(""); // Don't show password
                email.setText(u.getEmail());
                fullname.setText(u.getFullName());
                phone.setText(u.getPhoneNumber());
                
                formPanel.setVisible(true);
                saveBtn.setText("Update");
                mainPanel.add(formPanel, BorderLayout.SOUTH);
                toggleFormBtn.setText("Hide Form");
                contentPanel.revalidate();
            }
        });
        
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) { 
                    JOptionPane.showMessageDialog(AdminDashboard.this, "Select a user to delete"); 
                    return; 
                }
                String userId = (String)table.getValueAt(r,0);
                int yn = JOptionPane.showConfirmDialog(AdminDashboard.this, "Delete user " + userId + "?","Confirm",JOptionPane.YES_NO_OPTION);
                if (yn == JOptionPane.YES_OPTION) {
                    systemManager.deleteUser(userId);
                    JOptionPane.showMessageDialog(AdminDashboard.this, "User deleted successfully!");
                    showUserManagement(contentPanel); // Refresh
                }
            }
        });
        
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String type = (String)typeBox.getSelectedItem();
                String usern = username.getText().trim();
                String pass = new String(password.getPassword());
                String mail = email.getText().trim();
                String name = fullname.getText().trim();
                String phoneStr = phone.getText().trim();
                String dept = (String)departmentBox.getSelectedItem();
                
                if (usern.isEmpty() || name.isEmpty()) { 
                    JOptionPane.showMessageDialog(AdminDashboard.this, "Username and full name are required"); 
                    return; 
                }
                
                // Password validation for new users
                if ("Create".equals(saveBtn.getText())) {
                    if (pass.length() < 8) {
                        JOptionPane.showMessageDialog(AdminDashboard.this, "Password must be at least 8 characters long");
                        return;
                    }
                    if (!pass.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                        JOptionPane.showMessageDialog(AdminDashboard.this, "Password must contain at least one special character");
                        return;
                    }
                }
                
                // Email validation
                if (!mail.matches(".*@.*\\..*")) {
                    JOptionPane.showMessageDialog(AdminDashboard.this, "Please enter a valid email address (must contain @ and domain)");
                    return;
                }

                        // Phone validation (optional) - allow digits, spaces, + and -; length between 7 and 20
                        if (phoneStr != null && !phoneStr.isEmpty()) {
                            if (!phoneStr.matches("^[+]?[-\\d\\s]{7,20}$")) {
                                JOptionPane.showMessageDialog(AdminDashboard.this, "Please enter a valid phone number (digits, spaces, + or -; 7-20 characters)");
                                return;
                            }
                        }
                
                try {
                    if ("Create".equals(saveBtn.getText())) {
                        User newUser = null;
                        String userId = systemManager.generateUserID(type);
                        userIdField.setText(userId);
                        
                        if (type.equals("Student")) {
                            newUser = new Student(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, userId, "2024");
                        } else if (type.equals("Lecturer")) {
                            String staffId = systemManager.generateStaffID();
                            staffIdField.setText(staffId);
                            newUser = new Lecturer(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, staffId, dept);
                        } else if (type.equals("Academic Leader")) {
                            String staffId = systemManager.generateStaffID();
                            staffIdField.setText(staffId);
                            newUser = new AcademicLeader(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, dept, staffId);
                        } else {
                            String staffId = systemManager.generateStaffID();
                            staffIdField.setText(staffId);
                            newUser = new AdminStaff(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, dept, staffId);
                        }
                        if (systemManager.registerUser(newUser)) {
                            JOptionPane.showMessageDialog(AdminDashboard.this, "User created successfully!\n\nUser ID: " + userId + "\nStaff ID: " + (type.equals("Student") ? "N/A" : staffIdField.getText()));
                            formPanel.setVisible(false);
                            mainPanel.remove(formPanel);
                            toggleFormBtn.setText("Show Form");
                            showUserManagement(contentPanel); 
                        } else {
                            JOptionPane.showMessageDialog(AdminDashboard.this, "Username already exists");
                        }
                    } else {
                        int r = table.getSelectedRow();
                        String userId = (String)table.getValueAt(r,0);
                        User u = systemManager.findUserByID(userId);
                        u.setUsername(usern);
                        if (!pass.isEmpty()) {
                            if (pass.length() < 8) {
                                JOptionPane.showMessageDialog(AdminDashboard.this, "Password must be at least 8 characters long");
                                return;
                            }
                            if (!pass.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                                JOptionPane.showMessageDialog(AdminDashboard.this, "Password must contain at least one special character");
                                return;
                            }
                            u.setPassword(pass);
                        }
                        u.setEmail(mail);
                        u.setFullName(name);
                        u.setPhoneNumber(phoneStr);
                        
                        // Update department and staff ID for staff types
                        if (u instanceof Lecturer) {
                            ((Lecturer)u).setDepartment(dept);
                        } else if (u instanceof AcademicLeader) {
                            ((AcademicLeader)u).setDepartment(dept);
                        } else if (u instanceof AdminStaff) {
                            ((AdminStaff)u).setDepartment(dept);
                        }
                        
                        systemManager.updateUser(u);
                        JOptionPane.showMessageDialog(AdminDashboard.this, "User updated successfully!");
                        formPanel.setVisible(false);
                        mainPanel.remove(formPanel);
                        toggleFormBtn.setText("Hide Form");
                        showUserManagement(contentPanel); // Refresh
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AdminDashboard.this, "Error: " + ex.getMessage());
                }
            }
        });
        
        // Auto-generate IDs when type is selected
        typeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String)typeBox.getSelectedItem();
                if ("Create".equals(saveBtn.getText())) {
                    userIdField.setText(systemManager.generateUserID(selectedType));
                    if (!"Student".equals(selectedType)) {
                        staffIdField.setText(systemManager.generateStaffID());
                    } else {
                        staffIdField.setText("N/A");
                    }
                }
                
                // Update department options based on user type
                departmentBox.removeAllItems();
                if ("Admin Staff".equals(selectedType)) {
                    departmentBox.addItem("Administration");
                } else {
                    departmentBox.addItem("IT");
                    departmentBox.addItem("Business");
                    departmentBox.addItem("Engineering");
                }
                departmentBox.setSelectedIndex(0);
            }
        });
        
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                formPanel.setVisible(false);
                mainPanel.remove(formPanel);
                toggleFormBtn.setText("Show Form");
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        
        toggleFormBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (formPanel.isVisible()) {
                    formPanel.setVisible(false);
                    mainPanel.remove(formPanel);
                    toggleFormBtn.setText("Show Form");
                } else {
                    formPanel.setVisible(true);
                    mainPanel.add(formPanel, BorderLayout.SOUTH);
                    toggleFormBtn.setText("Hide Form");
                }
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        
        
    }

    private JPanel createFieldRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(140, 24));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        row.add(lbl, BorderLayout.WEST);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }
    
    private void showLecturerAssignment(JPanel contentPanel) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Current assignments table
        String[] cols = new String[] {"Module Code", "Module Name", "Lecturer", "Class ID"};
        java.util.List<ClassModule> classes = systemManager.getAllClasses();
        Object[][] data = new Object[classes.size()][];
        for (int i = 0; i < classes.size(); i++) {
            ClassModule cm = classes.get(i);
            data[i] = new Object[] { 
                cm.getModule().getModuleCode(), 
                cm.getModule().getModuleName(),
                cm.getLecturer().getFullName(),
                cm.getClassID()
            };
        }
        
        JTable table = new JTable(data, cols);
        JScrollPane scroll = new JScrollPane(table);
        
        // Assignment form (label and field on same line)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Assign Lecturer to Module"));
        
        java.util.List<Module> modules = systemManager.getAllModules();
        java.util.List<User> lecturers = systemManager.getUsersByRole("LECTURER");
        
        JComboBox<String> moduleBox = new JComboBox<>();
        if (modules.isEmpty()) {
            moduleBox.addItem("No modules available");
        } else {
            for (Module m : modules) {
                moduleBox.addItem(m.getModuleCode() + " - " + m.getModuleName());
            }
        }
        
        JComboBox<String> lecturerBox = new JComboBox<>();
        if (lecturers.isEmpty()) {
            lecturerBox.addItem("No lecturers available");
        } else {
            for (User u : lecturers) {
                lecturerBox.addItem(u.getUserID() + " - " + u.getFullName());
            }
        }
        
        formPanel.add(createFieldRow("Select Module:", moduleBox));
        formPanel.add(createFieldRow("Select Lecturer:", lecturerBox));
        
        JButton assignBtn = new JButton("Assign Lecturer");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(assignBtn);
        
        assignBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modules.isEmpty() || lecturers.isEmpty()) { 
                    JOptionPane.showMessageDialog(AdminDashboard.this, "No modules or lecturers available"); 
                    return; 
                }
                String modSel = (String) moduleBox.getSelectedItem();
                String lecSel = (String) lecturerBox.getSelectedItem();
                
                if (modSel == null || lecSel == null) {
                    JOptionPane.showMessageDialog(AdminDashboard.this, "Please select both module and lecturer");
                    return;
                }
                
                String modCode = modSel.split(" - ")[0];
                String lecID = lecSel.split(" - ")[0];
                
                Module chosen = null;
                for (Module m : modules) if (m.getModuleCode().equals(modCode)) { chosen = m; break; }
                User u = systemManager.findUserByID(lecID);
                
                if (chosen != null && u instanceof Lecturer) {
                    Lecturer lec = (Lecturer) u;
                    ClassModule cm = new ClassModule(systemManager.generateUserID("Class"), "Auto-" + chosen.getModuleCode(), chosen, lec, "2024-1", 60);
                    systemManager.createClass(cm);
                    JOptionPane.showMessageDialog(AdminDashboard.this, 
                        "✓ Lecturer Assigned!\n\n" +
                        "Module: " + chosen.getModuleCode() + " - " + chosen.getModuleName() + "\n" +
                        "Lecturer: " + lec.getFullName());
                    showLecturerAssignment(contentPanel); // Refresh
                } else {
                    JOptionPane.showMessageDialog(AdminDashboard.this, "Assignment failed - check selections.");
                }
            }
        });
        
        mainPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        contentPanel.add(mainPanel, BorderLayout.CENTER);
    }
    
    private void showGradingSystem(JPanel contentPanel) {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel("APU Grading System Configuration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Grading form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Get current grading system
        GradingSystem gradingSystem = systemManager.getGradingSystem();
        List<GradingScale> grades = gradingSystem.getGrades();

        // Passing percentage field
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.add(new JLabel("Passing Percentage:"));
        JTextField passField = new JTextField(String.valueOf(gradingSystem.getPassingPercentage()), 5);
        passPanel.add(passField);
        passPanel.add(new JLabel("%"));
        formPanel.add(passPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Grade editing fields
        List<JTextField> gradeLetterFields = new ArrayList<>();
        List<JTextField> minPercentFields = new ArrayList<>();
        List<JTextField> maxPercentFields = new ArrayList<>();
        List<JTextField> descriptionFields = new ArrayList<>();

        for (GradingScale grade : grades) {
            JPanel gradePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            // Grade letter
            gradePanel.add(new JLabel("Grade:"));
            JTextField gradeLetterField = new JTextField(grade.getGradeLetter(), 3);
            gradeLetterFields.add(gradeLetterField);
            gradePanel.add(gradeLetterField);

            // Min percentage
            gradePanel.add(new JLabel("Min %:"));
            JTextField minField = new JTextField(String.valueOf(grade.getMinPercentage()), 3);
            minPercentFields.add(minField);
            gradePanel.add(minField);

            // Max percentage
            gradePanel.add(new JLabel("Max %:"));
            JTextField maxField = new JTextField(String.valueOf(grade.getMaxPercentage()), 3);
            maxPercentFields.add(maxField);
            gradePanel.add(maxField);

            // Description
            gradePanel.add(new JLabel("Description:"));
            JTextField descField = new JTextField(grade.getDescription(), 15);
            descriptionFields.add(descField);
            gradePanel.add(descField);

            formPanel.add(gradePanel);
        }

        // Save button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save Grading System");
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));
        saveButton.setBackground(new Color(56, 142, 60));
        saveButton.setForeground(Color.WHITE);
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Update passing percentage
                    double newPassPercent = Double.parseDouble(passField.getText().trim());
                    gradingSystem.setPassingPercentage(newPassPercent);

                    // Update each grade
                    for (int i = 0; i < grades.size(); i++) {
                        GradingScale grade = grades.get(i);

                        String newLetter = gradeLetterFields.get(i).getText().trim();
                        double newMin = Double.parseDouble(minPercentFields.get(i).getText().trim());
                        double newMax = Double.parseDouble(maxPercentFields.get(i).getText().trim());
                        String newDesc = descriptionFields.get(i).getText().trim();

                        grade.setGradeLetter(newLetter);
                        grade.setMinPercentage(newMin);
                        grade.setMaxPercentage(newMax);
                        grade.setDescription(newDesc);
                    }

                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "✓ Grading system updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Save to file
                    FileManager.saveGradingSystem(gradingSystem);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Error: Please enter valid numbers for percentages.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Error updating grading system: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(saveButton);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(mainPanel, BorderLayout.CENTER);
    }
    
    private void showCreateClass(JPanel contentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Get modules and lecturers
        List<Module> modules = systemManager.getAllModules();
        List<User> users = systemManager.getAllUsers();
        List<Lecturer> lecturers = new ArrayList<>();
        for (User u : users) {
            if ("LECTURER".equals(u.getRole())) {
                lecturers.add((Lecturer) u);
            }
        }

        JComboBox<Module> moduleCombo = new JComboBox<>(modules.toArray(new Module[0]));
        moduleCombo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Module) {
                    setText(((Module) value).getModuleCode() + " - " + ((Module) value).getModuleName());
                }
                return this;
            }
        });

        JComboBox<Lecturer> lecturerCombo = new JComboBox<>(lecturers.toArray(new Lecturer[0]));
        lecturerCombo.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Lecturer) {
                    setText(((Lecturer) value).getFullName());
                }
                return this;
            }
        });

        JTextField classNameField = new JTextField();
        JTextField semesterField = new JTextField();
        JTextField capacityField = new JTextField();

        panel.add(createLabeledRow("Module:", moduleCombo));
        panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Lecturer:", lecturerCombo));
        panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Class Name:", classNameField));
        panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Semester:", semesterField));
        panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Capacity:", capacityField));
        panel.add(Box.createVerticalStrut(8));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createBtn = new JButton("Create");
        btnRow.add(createBtn);
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Module selectedModule = (Module) moduleCombo.getSelectedItem();
                Lecturer selectedLecturer = (Lecturer) lecturerCombo.getSelectedItem();
                String className = classNameField.getText().trim();
                String semester = semesterField.getText().trim();
                String capacityText = capacityField.getText().trim();

                if (selectedModule == null || selectedLecturer == null || className.isEmpty() || semester.isEmpty() || capacityText.isEmpty()) {
                    JOptionPane.showMessageDialog(AdminDashboard.this, "Please fill all fields!");
                    return;
                }

                int capacity;
                try {
                    capacity = Integer.parseInt(capacityText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AdminDashboard.this, "Capacity must be a number!");
                    return;
                }

                // Generate classID
                int classCount = systemManager.getAllClasses().size() + 1;
                String classID = "CLS" + String.format("%03d", classCount);

                ClassModule newClass = new ClassModule(classID, className, selectedModule, selectedLecturer, semester, capacity);
                systemManager.createClass(newClass);

                JOptionPane.showMessageDialog(AdminDashboard.this, 
                    "✓ Class created successfully!\n\n" +
                    "Class ID: " + classID + "\n" +
                    "Module: " + selectedModule.getModuleCode() + "\n" +
                    "Class: " + className + "\n" +
                    "Lecturer: " + selectedLecturer.getFullName() + "\n" +
                    "Semester: " + semester + "\n" +
                    "Capacity: " + capacity);

                // Clear fields
                classNameField.setText("");
                semesterField.setText("");
                capacityField.setText("");
            }
        });

        panel.add(btnRow);
        
        contentPanel.add(panel, BorderLayout.CENTER);
    }
    
    private void showReports(JPanel contentPanel) {
        StringBuilder report = new StringBuilder();
        report.append("System Reports\n\n");
        report.append("Total Users: ").append(systemManager.getAllUsers().size()).append("\n");
        report.append("Total Modules: ").append(systemManager.getAllModules().size()).append("\n");
        report.append("Total Classes: ").append(systemManager.getAllClasses().size()).append("\n");
        report.append("Total Assessments: ").append(systemManager.getAllAssessments().size()).append("\n");
        report.append("Total Feedback Records: ").append(systemManager.getAllFeedback().size()).append("\n");
        report.append("System Status: ✓ Active\n");
        report.append("Last Updated: ").append(new java.util.Date()).append("\n");
        
        JTextArea reportArea = new JTextArea(report.toString());
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
}
