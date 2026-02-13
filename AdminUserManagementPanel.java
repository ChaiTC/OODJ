import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminUserManagementPanel extends JPanel {
    private SystemManager systemManager;
    private JFrame parentFrame;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private JComboBox<String> roleFilter;
    private java.util.List<User> allUsers;
    private java.util.List<User> filteredUsers;
    
    public AdminUserManagementPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // User table with DefaultTableModel
        String[] cols = new String[] {"UserID", "Username", "Full Name", "Email", "Role", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        
        refreshTable();
        
        // Search and Action buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);
        
        // Role filter
        searchPanel.add(new JLabel("  Filter by Role:"));
        roleFilter = new JComboBox<>(new String[]{"All Roles", "STUDENT", "LECTURER", "ACADEMIC_LEADER", "ADMIN_STAFF"});
        searchPanel.add(roleFilter);
        
        // Action buttons
        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createBtn = new JButton("Create User");
        JButton editBtn = new JButton("Edit User");
        JButton deleteBtn = new JButton("Delete User");
        JButton approveBtn = new JButton("Approve User");
        JButton rejectBtn = new JButton("Reject User");
        JButton[] allButtons = new JButton[] {
            searchBtn, clearBtn, createBtn, editBtn, deleteBtn, approveBtn, rejectBtn
        };
        Dimension uniformSize = new Dimension(0, 0);
        for (JButton btn : allButtons) {
            Dimension pref = btn.getPreferredSize();
            if (pref.width > uniformSize.width) {
                uniformSize.width = pref.width;
            }
            if (pref.height > uniformSize.height) {
                uniformSize.height = pref.height;
            }
        }
        for (JButton btn : allButtons) {
            btn.setPreferredSize(new Dimension(uniformSize));
        }
        buttonBar.add(createBtn);
        buttonBar.add(editBtn);
        buttonBar.add(deleteBtn);
        buttonBar.add(approveBtn);
        buttonBar.add(rejectBtn);
        
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(buttonBar, BorderLayout.SOUTH);
        
        // Form panel (initially hidden)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("User Form"));
        formPanel.setVisible(false);
        
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Student","Lecturer","Academic Leader","Admin Staff"});
        JTextField userIdField = new JTextField();
        userIdField.setEditable(false);
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField email = new JTextField();
        JTextField fullname = new JTextField();
        JTextField phone = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(20, 15, 100, 1));
        JComboBox<String> departmentBox = new JComboBox<>(new String[]{"IT", "Business", "Engineering", "Administration"});
        JTextField staffIdField = new JTextField();
        staffIdField.setEditable(false);
        
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
        formPanel.add(createLabeledRow("Gender:", genderBox));
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(createLabeledRow("Age:", ageSpinner));
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

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        
        // Search functionality
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                roleFilter.setSelectedIndex(0);
                refreshTable();
            }
        });
        
        roleFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

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
                genderBox.setSelectedIndex(0);
                ageSpinner.setValue(20);
                departmentBox.removeAllItems();
                departmentBox.addItem("IT");
                departmentBox.addItem("Business");
                departmentBox.addItem("Engineering");
                departmentBox.setSelectedIndex(0);
                staffIdField.setText("");
                formPanel.setVisible(true);
                saveBtn.setText("Create");
                mainPanel.add(formPanel, BorderLayout.SOUTH);
                mainPanel.revalidate();
            }
        });
        
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) { 
                    JOptionPane.showMessageDialog(parentFrame, "Select a user to edit"); 
                    return; 
                }
                String userId = (String)table.getValueAt(r,0);
                User u = systemManager.findUserByID(userId);
                if (u == null) { 
                    JOptionPane.showMessageDialog(parentFrame, "User not found"); 
                    return; 
                }
                
                // Store current role for later comparison
                String currentRole = null;
                String currentDept = null;
                String currentStaffID = "";
                
                if (u instanceof Student) {
                    currentRole = "Student";
                    departmentBox.removeAllItems();
                    departmentBox.addItem("IT");
                    departmentBox.setSelectedItem("IT");
                    staffIdField.setText("N/A");
                } else if (u instanceof Lecturer) {
                    currentRole = "Lecturer";
                    Lecturer lec = (Lecturer) u;
                    currentDept = lec.getDepartment();
                    currentStaffID = lec.getStaffID();
                    departmentBox.removeAllItems();
                    departmentBox.addItem("IT");
                    departmentBox.addItem("Business");
                    departmentBox.addItem("Engineering");
                    departmentBox.setSelectedItem(lec.getDepartment());
                    staffIdField.setText(lec.getStaffID());
                } else if (u instanceof AcademicLeader) {
                    currentRole = "Academic Leader";
                    AcademicLeader leader = (AcademicLeader) u;
                    currentDept = leader.getDepartment();
                    currentStaffID = leader.getStaffID();
                    departmentBox.removeAllItems();
                    departmentBox.addItem("IT");
                    departmentBox.addItem("Business");
                    departmentBox.addItem("Engineering");
                    departmentBox.setSelectedItem(leader.getDepartment());
                    staffIdField.setText(leader.getStaffID());
                } else if (u instanceof AdminStaff) {
                    currentRole = "Admin Staff";
                    AdminStaff staff = (AdminStaff) u;
                    currentDept = staff.getDepartment();
                    currentStaffID = staff.getStaffID();
                    departmentBox.removeAllItems();
                    departmentBox.addItem("Administration");
                    departmentBox.setSelectedItem(staff.getDepartment());
                    staffIdField.setText(staff.getStaffID());
                }
                
                // Store original role as tag to detect changes
                typeBox.putClientProperty("originalRole", currentRole);
                typeBox.setSelectedItem(currentRole);
                
                // Allow typeBox to be changed (all options available)
                typeBox.setEnabled(true);
                
                userIdField.setText(u.getUserID());
                username.setText(u.getUsername());
                password.setText("");
                email.setText(u.getEmail());
                fullname.setText(u.getFullName());
                phone.setText(u.getPhoneNumber());
                String gender = u.getGender();
                if (gender == null || gender.isEmpty()) {
                    genderBox.setSelectedIndex(0);
                } else {
                    genderBox.setSelectedItem(gender);
                }
                ageSpinner.setValue(u.getAge() > 0 ? u.getAge() : 20);
                
                formPanel.setVisible(true);
                saveBtn.setText("Update");
                mainPanel.add(formPanel, BorderLayout.SOUTH);
                mainPanel.revalidate();
            }
        });
        
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) { 
                    JOptionPane.showMessageDialog(parentFrame, "Select a user to delete"); 
                    return; 
                }
                String userId = (String)table.getValueAt(r,0);
                String username = (String)table.getValueAt(r,1);
                int yn = JOptionPane.showConfirmDialog(parentFrame, "Delete this user " + username + "?","Confirm",JOptionPane.YES_NO_OPTION);
                if (yn == JOptionPane.YES_OPTION) {
                    systemManager.deleteUser(userId);
                    JOptionPane.showMessageDialog(parentFrame, "User deleted successfully!");
                    refreshTable();
                }
            }
        });
        
        approveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) { 
                    JOptionPane.showMessageDialog(parentFrame, "Select a user to approve"); 
                    return; 
                }
                String userId = (String)table.getValueAt(r,0);
                String username = (String)table.getValueAt(r,1);
                if (systemManager.approveUser(userId)) {
                    JOptionPane.showMessageDialog(parentFrame, "User " + username + " approved successfully!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Failed to approve user");
                }
            }
        });
        
        rejectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) { 
                    JOptionPane.showMessageDialog(parentFrame, "Select a user to reject"); 
                    return; 
                }
                String userId = (String)table.getValueAt(r,0);
                String username = (String)table.getValueAt(r,1);
                int yn = JOptionPane.showConfirmDialog(parentFrame, "Reject this user " + username + "?","Confirm",JOptionPane.YES_NO_OPTION);
                if (yn == JOptionPane.YES_OPTION) {
                    if (systemManager.rejectUser(userId)) {
                        JOptionPane.showMessageDialog(parentFrame, "User rejected successfully!");
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, "Failed to reject user");
                    }
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
                String gender = (String) genderBox.getSelectedItem();
                int age = (Integer) ageSpinner.getValue();
                String dept = (String)departmentBox.getSelectedItem();
                
                if (usern.isEmpty() || name.isEmpty()) { 
                    JOptionPane.showMessageDialog(parentFrame, "Username and full name are required"); 
                    return; 
                }
                
                if ("Create".equals(saveBtn.getText())) {
                    if (pass.length() < 8) {
                        JOptionPane.showMessageDialog(parentFrame, "Password must be at least 8 characters long");
                        return;
                    }
                    if (!pass.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                        JOptionPane.showMessageDialog(parentFrame, "Password must contain at least one special character");
                        return;
                    }
                }
                
                if (!mail.matches(".*@.*\\..*")) {
                    JOptionPane.showMessageDialog(parentFrame, "Please enter a valid email address (must contain @ and domain)");
                    return;
                }

                if (phoneStr != null && !phoneStr.isEmpty()) {
                    if (!phoneStr.matches("^[+]?[-\\d\\s]{7,20}$")) {
                        JOptionPane.showMessageDialog(parentFrame, "Please enter a valid phone number (digits, spaces, + or -; 7-20 characters)");
                        return;
                    }
                }
                
                try {
                    if ("Create".equals(saveBtn.getText())) {
                        User newUser = null;
                        String userId = systemManager.generateUserID(type);
                        userIdField.setText(userId);
                        
                        if (type.equals("Student")) {
                            String enrollmentYear = String.valueOf(java.time.Year.now().getValue());
                            newUser = new Student(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, userId, enrollmentYear);
                        } else if (type.equals("Lecturer")) {
                            String staffId = systemManager.generateStaffID();
                            staffIdField.setText(staffId);
                            Lecturer lec = new Lecturer(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, userId, dept);
                            lec.setStaffID(staffId);
                            newUser = lec;
                        } else if (type.equals("Academic Leader")) {
                            String staffId = systemManager.generateStaffID();
                            staffIdField.setText(staffId);
                            AcademicLeader leader = new AcademicLeader(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, dept, userId);
                            leader.setStaffID(staffId);
                            newUser = leader;
                        } else {
                            String staffId = systemManager.generateStaffID();
                            staffIdField.setText(staffId);
                            newUser = new AdminStaff(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, dept, staffId);
                        }
                        if (newUser != null) {
                            newUser.setGender(gender);
                            newUser.setAge(age);
                        }
                        if (systemManager.registerUser(newUser)) {
                            JOptionPane.showMessageDialog(parentFrame, "User created successfully!");
                            formPanel.setVisible(false);
                            mainPanel.remove(formPanel);
                            refreshTable();
                        } else {
                            JOptionPane.showMessageDialog(parentFrame, "Username already exists");
                        }
                    } else if ("Update".equals(saveBtn.getText())) {
                        String userId = userIdField.getText();
                        User existingUser = systemManager.findUserByID(userId);
                        if (existingUser == null) {
                            JOptionPane.showMessageDialog(parentFrame, "User not found");
                            return;
                        }
                        
                        String originalRole = (String) typeBox.getClientProperty("originalRole");
                        String newRole = (String) typeBox.getSelectedItem();
                        String oldUserId = userId;
                        
                        // Check if role has been changed
                        if (originalRole != null && !originalRole.equals(newRole)) {
                            // Role conversion needed
                            String staffId = staffIdField.getText().trim();
                            
                            if (systemManager.convertUserRole(userId, newRole, dept, staffId)) {
                                // Get the converted user (which now has a new ID)
                                // Search for the newly created user
                                java.util.List<User> allUsers = systemManager.getAllUsers();
                                User convertedUser = null;
                                for (User u : allUsers) {
                                    if (u.getUsername().equals(usern) && !u.getUserID().equals(oldUserId)) {
                                        convertedUser = u;
                                        break;
                                    }
                                }
                                
                                if (convertedUser != null) {
                                    existingUser = convertedUser;
                                    userId = convertedUser.getUserID();
                                    
                                    JOptionPane.showMessageDialog(parentFrame, 
                                        "Role conversion successful!\n\n" +
                                        "Old User ID: " + oldUserId + "\n" +
                                        "New User ID: " + userId + "\n" +
                                        "Role: " + newRole);
                                } else {
                                    JOptionPane.showMessageDialog(parentFrame, "Failed to convert user role");
                                    return;
                                }
                            } else {
                                JOptionPane.showMessageDialog(parentFrame, "Failed to convert user role");
                                return;
                            }
                        }
                        
                        // Update common fields
                        existingUser.setUsername(usern);
                        if (!pass.isEmpty()) {
                            if (pass.length() < 8) {
                                JOptionPane.showMessageDialog(parentFrame, "Password must be at least 8 characters long");
                                return;
                            }
                            if (!pass.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                                JOptionPane.showMessageDialog(parentFrame, "Password must contain at least one special character");
                                return;
                            }
                            existingUser.setPassword(pass);
                        }
                        existingUser.setEmail(mail);
                        existingUser.setFullName(name);
                        existingUser.setPhoneNumber(phoneStr.isEmpty()?"N/A":phoneStr);
                        existingUser.setGender(gender);
                        existingUser.setAge(age);
                        
                        // Update role-specific fields
                        if (existingUser instanceof Lecturer) {
                            ((Lecturer)existingUser).setDepartment(dept);
                        } else if (existingUser instanceof AcademicLeader) {
                            ((AcademicLeader)existingUser).setDepartment(dept);
                        } else if (existingUser instanceof AdminStaff) {
                            ((AdminStaff)existingUser).setDepartment(dept);
                        }
                        
                        systemManager.updateUser(existingUser);
                        JOptionPane.showMessageDialog(parentFrame, "User updated successfully!");
                        formPanel.setVisible(false);
                        mainPanel.remove(formPanel);
                        refreshTable();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
                }
            }
        });
        
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
                mainPanel.repaint();
            }
        });
    }
    
    private void refreshTable() {
        allUsers = systemManager.getAllUsers();
        filteredUsers = new ArrayList<>(allUsers);
        updateTableDisplay();
    }
    
    private void updateTableDisplay() {
        tableModel.setRowCount(0);
        for (User u : filteredUsers) {
            String status = u.isApproved() ? "Approved" : "Pending";
            tableModel.addRow(new Object[] { 
                u.getUserID(), 
                u.getUsername(), 
                u.getFullName(), 
                u.getEmail(), 
                u.getRole(),
                status
            });
        }
    }
    
    private void performSearch() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        String selectedRole = (String) roleFilter.getSelectedItem();
        
        filteredUsers = new ArrayList<>();
        for (User u : allUsers) {
            // Check search term
            boolean matchesSearch = searchTerm.isEmpty() || 
                                    u.getUserID().toLowerCase().contains(searchTerm) ||
                                    u.getFullName().toLowerCase().contains(searchTerm);
            
            // Check role filter
            boolean matchesRole = "All Roles".equals(selectedRole) || u.getRole().equals(selectedRole);
            
            if (matchesSearch && matchesRole) {
                filteredUsers.add(u);
            }
        }
        updateTableDisplay();
    }
    
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
}
