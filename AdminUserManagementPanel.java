import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminUserManagementPanel extends JPanel {
    // Reference to system manager for user data operations
    private SystemManager systemManager;
    
    // Reference to parent frame for dialog positioning
    private JFrame parentFrame;
    
    // Table model and table for displaying user list
    private DefaultTableModel tableModel;
    private JTable table;
    
    // Search field for finding users
    private JTextField searchField;
    
    // Role filter dropdown for filtering by user type
    private JComboBox<String> roleFilter;
    
    // Lists for managing user data (all users and filtered subset)
    private java.util.List<User> allUsers;
    private java.util.List<User> filteredUsers;
    
    public AdminUserManagementPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    /**
     * Initializes the panel layout and all UI components.
     * Sets up the user table, search functionality, action buttons,
     * and the dynamic user form.
     */
    private void initializePanel() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // ==================== USER TABLE SETUP ====================
        // Create table with columns for user information
        // Table is non-editable - users must use Edit button
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
        
        // Load initial data from system manager
        refreshTable();
        
        // ==================== SEARCH AND FILTER SECTION ====================
        // Search and Action buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Search panel with search field and role filter
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);
        
        // Role filter - allows filtering by user type
        searchPanel.add(new JLabel("  Filter by Role:"));
        roleFilter = new JComboBox<>(new String[]{"All Roles", "STUDENT", "LECTURER", "ACADEMIC_LEADER", "ADMIN_STAFF"});
        searchPanel.add(roleFilter);
        
        // ==================== ACTION BUTTONS ====================
        // Action buttons for user management operations
        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createBtn = new JButton("Create User");
        JButton editBtn = new JButton("Edit User");
        JButton deleteBtn = new JButton("Delete User");
        JButton approveBtn = new JButton("Approve User");
        JButton rejectBtn = new JButton("Reject User");
        
        // Make all buttons uniform size for better UI consistency
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
        
        // ==================== USER FORM PANEL ====================
        // Form panel for creating/editing users (initially hidden)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("User Form"));
        formPanel.setVisible(false);
        
        // Form fields for user information
        // User type selector (Student, Lecturer, etc.)
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Student","Lecturer","Academic Leader","Admin Staff"});
        
        // User ID field - auto-generated, non-editable
        JTextField userIdField = new JTextField();
        userIdField.setEditable(false);
        
        // Basic user information fields
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField email = new JTextField();
        JTextField fullname = new JTextField();
        JTextField phone = new JTextField();
        
        // Additional user details
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(20, 15, 100, 1));
        
        // Department selection (varies based on user type)
        JComboBox<String> departmentBox = new JComboBox<>(new String[]{"IT", "Business", "Engineering", "Administration"});
        
        // Staff ID field - auto-generated for staff roles
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
        
        // Form action buttons (Save/Cancel)
        JPanel formBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        formBtnPanel.add(saveBtn);
        formBtnPanel.add(cancelBtn);
        formPanel.add(formBtnPanel);

        // Add panels to main layout
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        
        // ==================== SEARCH FUNCTIONALITY ====================
        // Search button - triggers search by username or ID
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        
        // Allow Enter key in search field to trigger search
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        
        // Clear button - resets search and shows all users
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                roleFilter.setSelectedIndex(0);
                refreshTable();
            }
        });
        
        // Role filter - triggers search when changed
        roleFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        // ==================== CREATE USER FUNCTIONALITY ====================
        // Create button - shows form with empty fields for new user
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
                saveBtn.setText("Create"); // Set button text to indicate create mode
                mainPanel.add(formPanel, BorderLayout.SOUTH);
                mainPanel.revalidate();
            }
        });
        
        // ==================== EDIT USER FUNCTIONALITY ====================
        // Edit button - loads selected user's data into form
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
                
                // Store current role for later comparison (to detect role changes)
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
                
                // Store original role as client property to detect changes during save
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
                
                // Show form in update mode
                formPanel.setVisible(true);
                saveBtn.setText("Update"); // Set button text to indicate update mode
                mainPanel.add(formPanel, BorderLayout.SOUTH);
                mainPanel.revalidate();
            }
        });
        
        // ==================== DELETE USER FUNCTIONALITY ====================
        // Delete button - removes user from system after confirmation
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
        
        // ==================== APPROVE USER FUNCTIONALITY ====================
        // Approve button - approves pending user registrations
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
        
        // ==================== REJECT USER FUNCTIONALITY ====================
        // Reject button - rejects pending user registrations with confirmation
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
        
        // ==================== SAVE BUTTON LOGIC ====================
        // Save button - handles both Create and Update operations
        // Includes comprehensive validation for all fields
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // Collect form data
                String type = (String)typeBox.getSelectedItem();
                String usern = username.getText().trim();
                String pass = new String(password.getPassword());
                String mail = email.getText().trim();
                String name = fullname.getText().trim();
                String phoneStr = phone.getText().trim();
                String gender = (String) genderBox.getSelectedItem();
                int age = (Integer) ageSpinner.getValue();
                String dept = (String)departmentBox.getSelectedItem();
                
                // ========== BASIC VALIDATION ==========
                // Check required fields
                if (usern.isEmpty() || name.isEmpty()) { 
                    JOptionPane.showMessageDialog(parentFrame, "Username and full name are required"); 
                    return; 
                }
                
                // Password validation - only required when creating or if changed
                if ("Create".equals(saveBtn.getText())) {
                    // Password must be at least 8 characters
                    if (pass.length() < 8) {
                        JOptionPane.showMessageDialog(parentFrame, "Password must be at least 8 characters long");
                        return;
                    }
                    // Password must contain at least one special character for security
                    if (!pass.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                        JOptionPane.showMessageDialog(parentFrame, "Password must contain at least one special character");
                        return;
                    }
                }
                
                // Email validation - must contain @ and domain
                if (!mail.matches(".*@.*\\..*")) {
                    JOptionPane.showMessageDialog(parentFrame, "Please enter a valid email address (must contain @ and domain)");
                    return;
                }

                // Phone number validation - optional but must be valid format if provided
                if (phoneStr != null && !phoneStr.isEmpty()) {
                    if (!phoneStr.matches("^[+]?[-\\d\\s]{7,20}$")) {
                        JOptionPane.showMessageDialog(parentFrame, "Please enter a valid phone number (digits, spaces, + or -; 7-20 characters)");
                        return;
                    }
                }
                
                try {
                    // ========== CREATE NEW USER ==========
                    if ("Create".equals(saveBtn.getText())) {
                        User newUser = null;
                        // Generate appropriate user ID based on type
                        String userId = systemManager.generateUserID(type);
                        userIdField.setText(userId);
                        
                        // Create user object based on selected type
                        if (type.equals("Student")) {
                            // Students get student ID and enrollment year
                            String enrollmentYear = String.valueOf(java.time.Year.now().getValue());
                            newUser = new Student(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, userId, enrollmentYear);
                        } else if (type.equals("Lecturer")) {
                            // Lecturers get staff ID and department assignment
                            String staffId = systemManager.generateStaffID();
                            staffIdField.setText(staffId);
                            Lecturer lec = new Lecturer(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, userId, dept);
                            lec.setStaffID(staffId);
                            newUser = lec;
                        } else if (type.equals("Academic Leader")) {
                            // Academic Leaders get staff ID and department management
                            String staffId = systemManager.generateStaffID();
                            staffIdField.setText(staffId);
                            AcademicLeader leader = new AcademicLeader(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, dept, userId);
                            leader.setStaffID(staffId);
                            newUser = leader;
                        } else {
                            // Admin Staff get staff ID and administration department
                            String staffId = systemManager.generateStaffID();
                            staffIdField.setText(staffId);
                            newUser = new AdminStaff(userId, usern, pass, mail, name, phoneStr.isEmpty()?"N/A":phoneStr, dept, staffId);
                        }
                        // Set additional user attributes
                        if (newUser != null) {
                            newUser.setGender(gender);
                            newUser.setAge(age);
                        }
                        // Register the user in the system
                        if (systemManager.registerUser(newUser)) {
                            JOptionPane.showMessageDialog(parentFrame, "User created successfully!");
                            formPanel.setVisible(false);
                            mainPanel.remove(formPanel);
                            refreshTable();
                        } else {
                            JOptionPane.showMessageDialog(parentFrame, "Username already exists");
                        }
                    } else if ("Update".equals(saveBtn.getText())) {
                        // ========== UPDATE EXISTING USER ==========
                        String userId = userIdField.getText();
                        User existingUser = systemManager.findUserByID(userId);
                        if (existingUser == null) {
                            JOptionPane.showMessageDialog(parentFrame, "User not found");
                            return;
                        }
                        
                        // Check if user role has been changed (requires conversion)
                        String originalRole = (String) typeBox.getClientProperty("originalRole");
                        String newRole = (String) typeBox.getSelectedItem();
                        String oldUserId = userId;
                        
                        // Check if role has been changed (requires user conversion)
                        if (originalRole != null && !originalRole.equals(newRole)) {
                            // ========== ROLE CONVERSION ==========
                            // Convert user to a different role type (e.g., Student to Lecturer)
                            // This generates a new user ID with the appropriate prefix
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
                                    // Update references to use the new user object
                                    existingUser = convertedUser;
                                    userId = convertedUser.getUserID();
                                    
                                    // Notify admin of the ID change
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
                        
                        // ========== UPDATE USER FIELDS ==========
                        // Update common fields that all user types have
                        existingUser.setUsername(usern);
                        // Only update password if a new one was provided
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
                        
                        // Update role-specific fields (department for staff roles)
                        if (existingUser instanceof Lecturer) {
                            ((Lecturer)existingUser).setDepartment(dept);
                        } else if (existingUser instanceof AcademicLeader) {
                            ((AcademicLeader)existingUser).setDepartment(dept);
                        } else if (existingUser instanceof AdminStaff) {
                            ((AdminStaff)existingUser).setDepartment(dept);
                        }
                        
                        // Save changes to file
                        systemManager.updateUser(existingUser);
                        JOptionPane.showMessageDialog(parentFrame, "User updated successfully!");
                        // Hide form and refresh table
                        formPanel.setVisible(false);
                        mainPanel.remove(formPanel);
                        refreshTable();
                    }
                } catch (Exception ex) {
                    // Handle any unexpected errors during save
                    JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
                }
            }
        });
        
        // ==================== TYPE BOX HANDLER ====================
        // Handle user type changes - updates ID and department options
        typeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String)typeBox.getSelectedItem();
                // When creating a new user, generate appropriate IDs
                if ("Create".equals(saveBtn.getText())) {
                    // Generate user ID with correct prefix (STU/LEC/AL/ADM)
                    userIdField.setText(systemManager.generateUserID(selectedType));
                    // Generate staff ID for non-student roles
                    if (!"Student".equals(selectedType)) {
                        staffIdField.setText(systemManager.generateStaffID());
                    } else {
                        staffIdField.setText("N/A");
                    }
                }
                
                // Update department options based on user type
                departmentBox.removeAllItems();
                if ("Admin Staff".equals(selectedType)) {
                    // Admin staff only in Administration department
                    departmentBox.addItem("Administration");
                } else {
                    // Other staff can be in various departments
                    departmentBox.addItem("IT");
                    departmentBox.addItem("Business");
                    departmentBox.addItem("Engineering");
                }
                departmentBox.setSelectedIndex(0);
            }
        });
        
        // ==================== CANCEL BUTTON ====================
        // Cancel button - hides form without saving
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                formPanel.setVisible(false);
                mainPanel.remove(formPanel);
                mainPanel.repaint();
            }
        });
    }
    
    /**
     * Refreshes the user table by loading all users from the system manager.
     * This method is called after any user modification (create, edit, delete, approve, reject).
     */
    private void refreshTable() {
        allUsers = systemManager.getAllUsers();
        filteredUsers = new ArrayList<>(allUsers);
        updateTableDisplay();
    }
    
    /**
     * Updates the table display with the current filtered user list.
     * Shows user ID, username, full name, email, role, and approval status.
     */
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
    
    /**
     * Performs a search based on the search field text and role filter.
     * Filters users by matching username or user ID (case-insensitive)
     * and by role if a specific role is selected.
     */
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
