import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminUserManagementPanel extends JPanel {
    private SystemManager systemManager;
    private JFrame parentFrame;
    
    public AdminUserManagementPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        
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
        buttonBar.add(createBtn);
        buttonBar.add(editBtn);
        buttonBar.add(deleteBtn);
        
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

        mainPanel.add(buttonBar, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

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
                
                if (u instanceof Student) {
                    typeBox.setSelectedItem("Student");
                    userIdField.setText(u.getUserID());
                    departmentBox.setSelectedItem("IT");
                    staffIdField.setText("N/A");
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
                }
                
                username.setText(u.getUsername());
                password.setText("");
                email.setText(u.getEmail());
                fullname.setText(u.getFullName());
                phone.setText(u.getPhoneNumber());
                
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
                int yn = JOptionPane.showConfirmDialog(parentFrame, "Delete user " + userId + "?","Confirm",JOptionPane.YES_NO_OPTION);
                if (yn == JOptionPane.YES_OPTION) {
                    systemManager.deleteUser(userId);
                    JOptionPane.showMessageDialog(parentFrame, "User deleted successfully!");
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
                            JOptionPane.showMessageDialog(parentFrame, "User created successfully!");
                            formPanel.setVisible(false);
                            mainPanel.remove(formPanel);
                        } else {
                            JOptionPane.showMessageDialog(parentFrame, "Username already exists");
                        }
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
