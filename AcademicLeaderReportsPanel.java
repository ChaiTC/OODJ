import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AcademicLeaderReportsPanel extends JPanel {
    private SystemManager systemManager;
    private JFrame parentFrame;
    
    public AcademicLeaderReportsPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Left section: Report options
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Report Options"));
        optionsPanel.setPreferredSize(new Dimension(150, 300));
        
        JButton userReportBtn = new JButton("User Summary");
        userReportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.add(userReportBtn);
        optionsPanel.add(Box.createVerticalStrut(8));
        
        JButton classReportBtn = new JButton("Class Summary");
        classReportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.add(classReportBtn);
        optionsPanel.add(Box.createVerticalStrut(8));
        
        JButton moduleReportBtn = new JButton("Module Summary");
        moduleReportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.add(moduleReportBtn);
        optionsPanel.add(Box.createVerticalStrut(8));
        
        JButton assessmentReportBtn = new JButton("Assessment Summary");
        assessmentReportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.add(assessmentReportBtn);
        optionsPanel.add(Box.createVerticalStrut(8));
        
        JButton enrollmentReportBtn = new JButton("Enrollment Report");
        enrollmentReportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.add(enrollmentReportBtn);
        optionsPanel.add(Box.createVerticalGlue());

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.add(refreshBtn);
        optionsPanel.add(Box.createVerticalStrut(8));

        final JButton[] selectedReportButton = new JButton[1];
        
        // Right section: Report display
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBorder(BorderFactory.createTitledBorder("Report Display"));
        
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scroll = new JScrollPane(reportArea);
        reportPanel.add(scroll, BorderLayout.CENTER);
        
        add(optionsPanel, BorderLayout.WEST);
        add(reportPanel, BorderLayout.CENTER);
        
        userReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedReportButton[0] = userReportBtn;
                StringBuilder report = new StringBuilder();
                report.append("=== USER SUMMARY REPORT ===\n");
                report.append("Generated: ").append(new java.util.Date()).append("\n\n");
                
                java.util.List<User> users = systemManager.getAllUsers();
                Map<String, Integer> roleCount = new HashMap<>();
                for (User u : users) {
                    String role = u.getRole();
                    roleCount.put(role, roleCount.getOrDefault(role, 0) + 1);
                }
                
                report.append("Total Users: ").append(users.size()).append("\n");
                for (String role : roleCount.keySet()) {
                    report.append("  ").append(role).append(": ").append(roleCount.get(role)).append("\n");
                }
                
                report.append("\n--- User List ---\n");
                for (User u : users) {
                    report.append("ID: ").append(u.getUserID()).append(" | ");
                    report.append("Username: ").append(u.getUsername()).append(" | ");
                    report.append("Name: ").append(u.getFullName()).append(" | ");
                    report.append("Role: ").append(u.getRole()).append("\n");
                }
                
                reportArea.setText(report.toString());
            }
        });
        
        classReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedReportButton[0] = classReportBtn;
                StringBuilder report = new StringBuilder();
                report.append("=== CLASS SUMMARY REPORT ===\n");
                report.append("Generated: ").append(new java.util.Date()).append("\n\n");
                
                java.util.List<ClassModule> classes = systemManager.getAllClasses();
                report.append("Total Classes: ").append(classes.size()).append("\n\n");
                
                report.append("--- Class Details ---\n");
                for (ClassModule c : classes) {
                    report.append("Class ID: ").append(c.getClassID()).append("\n");
                    report.append("  Name: ").append(c.getClassName()).append("\n");
                    report.append("  Module: ").append(c.getModuleID()).append("\n");
                    report.append("  Capacity: ").append(c.getCapacity()).append("\n");
                    report.append("  Enrolled: ").append(c.getEnrolledStudents().size()).append(" students\n");
                    report.append("  Schedule: ").append(c.getDay() != null ? c.getDay() : "N/A").append(" at ")
                           .append(c.getTime() != null ? c.getTime() : "N/A").append("\n");
                    report.append("  Location: ").append(c.getLocation() != null ? c.getLocation() : "N/A").append("\n");
                    report.append("  Lecturer: ");
                    if (c.getLecturerID() != null) {
                        User u = systemManager.findUserByID(c.getLecturerID());
                        report.append(u != null ? u.getUsername() : "Not found");
                    } else {
                        report.append("Unassigned");
                    }
                    report.append("\n\n");
                }
                
                reportArea.setText(report.toString());
            }
        });
        
        moduleReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedReportButton[0] = moduleReportBtn;
                showModuleManagementPanel();
            }
        });
        
        assessmentReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedReportButton[0] = assessmentReportBtn;
                StringBuilder report = new StringBuilder();
                report.append("=== ASSESSMENT SUMMARY REPORT ===\n");
                report.append("Generated: ").append(new java.util.Date()).append("\n\n");
                
                java.util.List<Assessment> assessments = systemManager.getAllAssessments();
                report.append("Total Assessments: ").append(assessments.size()).append("\n\n");
                
                report.append("--- Assessment Details ---\n");
                for (Assessment a : assessments) {
                    report.append("Assessment ID: ").append(a.getAssessmentID()).append("\n");
                    report.append("  Class: ").append(a.getClassID()).append("\n");
                    report.append("  Title: ").append(a.getTitle()).append("\n");
                    report.append("  Type: ").append(a.getAssessmentType()).append("\n");
                    report.append("  Status: ").append(a.getStatus()).append("\n");
                    report.append("  Marks: ").append(a.getTotalMarks()).append("\n");
                    report.append("  Students Graded: ").append(a.getStudentMarks().size()).append("\n\n");
                }
                
                reportArea.setText(report.toString());
            }
        });
        
        enrollmentReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedReportButton[0] = enrollmentReportBtn;
                StringBuilder report = new StringBuilder();
                report.append("=== ENROLLMENT REPORT ===\n");
                report.append("Generated: ").append(new java.util.Date()).append("\n\n");
                
                java.util.List<ClassModule> classes = systemManager.getAllClasses();
                int totalEnrolled = 0;
                
                report.append("--- Enrollment by Class ---\n");
                for (ClassModule c : classes) {
                    report.append(c.getClassID()).append(" (").append(c.getClassName()).append("): ");
                    int enrolled = c.getEnrolledStudents().size();
                    report.append(enrolled).append(" / ").append(c.getCapacity()).append(" students\n");
                    totalEnrolled += enrolled;
                }
                
                report.append("\nTotal Enrolled: ").append(totalEnrolled).append("\n");
                
                report.append("\n--- Enrollment by Student ---\n");
                java.util.List<User> students = systemManager.getAllStudents();
                for (User u : students) {
                    report.append(u.getUsername()).append(" (").append(u.getUserID()).append("): ");
                    int classCount = 0;
                    for (ClassModule c : classes) {
                        if (c.getEnrolledStudents().contains(u.getUserID())) classCount++;
                    }
                    report.append(classCount).append(" classes\n");
                }
                
                reportArea.setText(report.toString());
            }
        });

        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                systemManager.loadAllData();

                if (selectedReportButton[0] != null) {
                    selectedReportButton[0].doClick();
                } else {
                    reportArea.setText("Data refreshed. Select a report to view latest information.");
                }
            }
        });
    }

    private void showModuleManagementPanel() {
        java.util.List<Module> modules = systemManager.getAllModules();
        
        JDialog dialog = new JDialog(parentFrame, "Module Management", true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new BorderLayout(8, 8));
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Create table
        String[] columns = {"Module ID", "Module Name", "Code", "Credits", "Assigned Lecturer", "Action"};
        Object[][] data = new Object[modules.size()][6];
        
        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            
            // Find assigned lecturers (can be multiple)
            StringBuilder lecturerNames = new StringBuilder();
            lecturerNames.append("Not Assigned");
            java.util.List<User> lecturers = systemManager.getAllLecturers();
            java.util.List<String> assignedLecturers = new ArrayList<>();
            
            for (User u : lecturers) {
                if (u instanceof Lecturer) {
                    Lecturer lec = (Lecturer) u;
                    // Compare by module ID instead of object reference
                    for (Module assignedMod : lec.getAssignedModules()) {
                        if (assignedMod.getModuleID().equals(m.getModuleID())) {
                            assignedLecturers.add(lec.getFullName());
                            break;
                        }
                    }
                }
            }
            
            if (!assignedLecturers.isEmpty()) {
                lecturerNames = new StringBuilder(String.join(", ", assignedLecturers));
            }
            
            data[i][0] = m.getModuleID();
            data[i][1] = m.getModuleName();
            data[i][2] = m.getModuleCode();
            data[i][3] = m.getCreditHours();
            data[i][4] = lecturerNames.toString();
            data[i][5] = "Edit / Delete";
        }
        
        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only Action column is "editable"
            }
        };
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 5) {
                    JButton btn = new JButton("Edit");
                    btn.addActionListener(e -> {
                        showModuleEditDialog(modules.get(row), dialog);
                    });
                    return btn;
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        bottomPanel.add(closeBtn);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
    
    private void showModuleEditDialog(Module module, JDialog parent) {
        JDialog editDialog = new JDialog(parent, "Edit Module - " + module.getModuleID(), true);
        editDialog.setSize(500, 400);
        editDialog.setLocationRelativeTo(parent);
        editDialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Module ID (read-only)
        JTextField moduleIDField = new JTextField(module.getModuleID(), 20);
        moduleIDField.setEditable(false);
        
        // Module Name
        JTextField nameField = new JTextField(module.getModuleName(), 20);
        
        // Module Code
        JTextField codeField = new JTextField(module.getModuleCode(), 20);
        
        // Credits
        JSpinner creditsSpinner = new JSpinner(
            new SpinnerNumberModel(module.getCreditHours(), 1, 10, 1)
        );
        
        // Assigned Lecturer
        JComboBox<String> lecturerCombo = new JComboBox<>();
        lecturerCombo.addItem("Not Assigned");
        
        java.util.List<User> lecturers = systemManager.getAllLecturers();
        String currentLecturer = "Not Assigned";
        
        for (User u : lecturers) {
            if (u instanceof Lecturer) {
                Lecturer lec = (Lecturer) u;
                lecturerCombo.addItem(lec.getFullName() + " (" + lec.getUserID() + ")");
                
                if (lec.getAssignedModules().contains(module)) {
                    currentLecturer = lec.getFullName() + " (" + lec.getUserID() + ")";
                }
            }
        }
        
        if (!currentLecturer.equals("Not Assigned")) {
            lecturerCombo.setSelectedItem(currentLecturer);
        }
        
        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        editDialog.add(new JLabel("Module ID:"), gbc);
        gbc.gridx = 1;
        editDialog.add(moduleIDField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        editDialog.add(new JLabel("Module Name:"), gbc);
        gbc.gridx = 1;
        editDialog.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        editDialog.add(new JLabel("Module Code:"), gbc);
        gbc.gridx = 1;
        editDialog.add(codeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        editDialog.add(new JLabel("Credits:"), gbc);
        gbc.gridx = 1;
        editDialog.add(creditsSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        editDialog.add(new JLabel("Assigned Lecturer:"), gbc);
        gbc.gridx = 1;
        editDialog.add(lecturerCombo, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        
        JButton saveBtn = new JButton("Save");
        JButton deleteBtn = new JButton("Delete");
        JButton cancelBtn = new JButton("Cancel");
        
        saveBtn.addActionListener(e -> {
            module.setModuleName(nameField.getText());
            module.setModuleCode(codeField.getText());
            module.setCreditHours((Integer) creditsSpinner.getValue());
            
            if (systemManager.updateModule(module)) {
                JOptionPane.showMessageDialog(editDialog, "Module updated successfully!");
                editDialog.dispose();
                parent.dispose();
                showModuleManagementPanel();
            } else {
                JOptionPane.showMessageDialog(editDialog, "Error updating module", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(editDialog,
                "Are you sure you want to delete this module?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (systemManager.deleteModule(module.getModuleID())) {
                    JOptionPane.showMessageDialog(editDialog, "Module deleted successfully!");
                    editDialog.dispose();
                    parent.dispose();
                    showModuleManagementPanel();
                } else {
                    JOptionPane.showMessageDialog(editDialog, "Error deleting module", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cancelBtn.addActionListener(e -> editDialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        editDialog.add(buttonPanel, gbc);
        
        editDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        editDialog.setVisible(true);
    }
}
