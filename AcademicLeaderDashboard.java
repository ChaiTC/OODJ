import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AcademicLeaderDashboard extends JFrame {

    private final SystemManager systemManager;
    private final AcademicLeader leader;

    private JLabel headerLabel;
    private JComboBox<String> moduleBox;

    public AcademicLeaderDashboard(SystemManager systemManager, AcademicLeader leader) {
        this.systemManager = systemManager;
        this.leader = leader;
        initUI();
    }

    private void initUI() {
        setTitle("Academic Leader Dashboard - " + leader.getFullName());
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    // ================= HEADER =================
    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(56, 142, 60));

        headerLabel = new JLabel(
                "Welcome, " + leader.getFullName() + " (Academic Leader)"
        );
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));

        header.add(headerLabel);
        return header;
    }

    // ================= TABS =================
    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Edit Profile", buildEditProfilePanel());
        tabs.addTab("Module Management", buildModuleManagementPanel());
        tabs.addTab("Assign Lecturers", buildAssignLecturersPanel());
        tabs.addTab("View Reports", buildReportsPanel());

        return tabs;
    }

    // ================= FOOTER =================
    private JPanel buildFooter() {
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(244, 67, 54));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setOpaque(true);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setPreferredSize(new Dimension(120, 35));

        logoutBtn.addActionListener(e -> {
            dispose();
            new AFSLoginFrame(systemManager).setVisible(true);
        });

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(logoutBtn);
        return footer;
    }

    // ================= EDIT PROFILE =================
    private JPanel buildEditProfilePanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(leader.getFullName(), 20);
        JTextField emailField = new JTextField(leader.getEmail(), 20);
        JTextField deptField = new JTextField(leader.getDepartment(), 20);

        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        genderBox.setSelectedItem(leader.getGender());

        JSpinner ageSpinner = new JSpinner(
                new SpinnerNumberModel(leader.getAge(), 18, 100, 1)
        );

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        panel.add(deptField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        panel.add(genderBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        panel.add(ageSpinner, gbc);

        JButton saveBtn = new JButton("Save Profile");
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            leader.setFullName(nameField.getText());
            leader.setEmail(emailField.getText());
            leader.setDepartment(deptField.getText());
            leader.setGender((String) genderBox.getSelectedItem());
            leader.setAge((Integer) ageSpinner.getValue());

            headerLabel.setText(
                    "Welcome, " + leader.getFullName() + " (Academic Leader)"
            );

            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
        });

        return panel;
    }

    // ================= MODULE MANAGEMENT =================
    private JPanel buildModuleManagementPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField creditField = new JTextField("3");

        JComboBox<String> departmentBox = new JComboBox<>(
                new String[]{"IT", "Business", "Engineering"}
        );
        departmentBox.setSelectedItem(leader.getDepartment());

        JComboBox<String> moduleSelectBox = new JComboBox<>();
        List<Module> modules = systemManager.getAllModules();
        for (Module m : modules) {
            moduleSelectBox.addItem(m.getModuleCode() + " - " + m.getModuleName());
        }

        JButton createBtn = new JButton("Create Module");
        JButton updateBtn = new JButton("Update Module");
        JButton deleteBtn = new JButton("Delete Module");

        createBtn.addActionListener(e -> {
            try {
                String moduleID = systemManager.generateModuleID();
                Module module = new Module(
                        moduleID,
                        nameField.getText(),
                        codeField.getText(),
                        "No description",
                        Integer.parseInt(creditField.getText()),
                        (String) departmentBox.getSelectedItem()
                );
                systemManager.createModule(module);
                moduleSelectBox.addItem(module.getModuleCode() + " - " + module.getModuleName());
                JOptionPane.showMessageDialog(this, "Module created!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid input",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        updateBtn.addActionListener(e -> {
            int idx = moduleSelectBox.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "Select a module to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Module selected = modules.get(idx);
            try {
                selected.setModuleName(nameField.getText());
                selected.setModuleCode(codeField.getText());
                selected.setCreditHours(Integer.parseInt(creditField.getText()));
                selected.setDepartment((String) departmentBox.getSelectedItem());
                systemManager.createModule(selected); // Overwrites existing
                moduleSelectBox.insertItemAt(selected.getModuleCode() + " - " + selected.getModuleName(), idx);
                moduleSelectBox.removeItemAt(idx + 1);
                JOptionPane.showMessageDialog(this, "Module updated!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            int idx = moduleSelectBox.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "Select a module to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Module selected = modules.get(idx);
            try {
                modules.remove(selected);
                moduleSelectBox.removeItemAt(idx);
                JOptionPane.showMessageDialog(this, "Module deleted!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Delete failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        moduleSelectBox.addActionListener(e -> {
            int idx = moduleSelectBox.getSelectedIndex();
            if (idx >= 0 && idx < modules.size()) {
                Module m = modules.get(idx);
                codeField.setText(m.getModuleCode());
                nameField.setText(m.getModuleName());
                creditField.setText(String.valueOf(m.getCreditHours()));
                departmentBox.setSelectedItem(m.getDepartment());
            }
        });

        panel.add(new JLabel("Module Code:"));
        panel.add(codeField);
        panel.add(new JLabel("Module Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Credits:"));
        panel.add(creditField);
        panel.add(new JLabel("Department:"));
        panel.add(departmentBox);
        panel.add(new JLabel("Select Module:"));
        panel.add(moduleSelectBox);
        panel.add(new JLabel());
        panel.add(createBtn);
        panel.add(new JLabel());
        panel.add(updateBtn);
        panel.add(new JLabel());
        panel.add(deleteBtn);

        return panel;
    }

    // ================= ASSIGN LECTURERS =================
    private JPanel buildAssignLecturersPanel() {

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        moduleBox = new JComboBox<>();
        refreshModuleBox();

        JComboBox<String> lecturerBox = new JComboBox<>();
        for (User u : systemManager.getAllLecturers()) {
            lecturerBox.addItem(u.getUserID() + " - " + u.getFullName());
        }

        JButton assignBtn = new JButton("Assign Lecturer");

        assignBtn.addActionListener(e -> {
            try {
                List<Module> modules = systemManager.getAllModules();
                List<User> lecturers = systemManager.getAllLecturers();

                int modIdx = moduleBox.getSelectedIndex();
                int lecIdx = lecturerBox.getSelectedIndex();

                if (modIdx < 0 || lecIdx < 0) {
                    JOptionPane.showMessageDialog(this, "Please select both module and lecturer", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Module selectedModule = modules.get(modIdx);
                Lecturer selectedLecturer = (Lecturer) lecturers.get(lecIdx);

                // Add module to lecturer's assigned modules at runtime and persist
                if (!selectedLecturer.getAssignedModules().contains(selectedModule)) {
                    selectedLecturer.getAssignedModules().add(selectedModule);
                    // persist change to users file
                    systemManager.updateUser(selectedLecturer);
                    JOptionPane.showMessageDialog(this, "Lecturer assigned to module (saved)");
                } else {
                    JOptionPane.showMessageDialog(this, "Lecturer is already assigned to this module");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to assign lecturer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel("Select Module:"));
        panel.add(moduleBox);
        panel.add(new JLabel("Select Lecturer:"));
        panel.add(lecturerBox);
        panel.add(new JLabel());
        panel.add(assignBtn);

        return panel;
    }

    private void refreshModuleBox() {
        if (moduleBox == null) return;

        moduleBox.removeAllItems();
        List<Module> modules = systemManager.getAllModules();

        for (Module m : modules) {
            moduleBox.addItem(m.getModuleCode() + " - " + m.getModuleName());
        }
    }

    // ================= REPORTS PANEL =================
    private JPanel buildReportsPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setPreferredSize(new Dimension(180, 400));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Report Options"));

        Dimension buttonSize = new Dimension(160, 35);

        JButton userBtn = new JButton("User Summary");
        JButton classBtn = new JButton("Class Summary");
        JButton moduleBtn = new JButton("Module Summary");
        JButton assessBtn = new JButton("Assessment Summary");
        JButton enrollBtn = new JButton("Enrollment Report");

        JButton[] buttons = {userBtn, classBtn, moduleBtn, assessBtn, enrollBtn};

        for (JButton btn : buttons) {
            btn.setMaximumSize(buttonSize);
            btn.setMinimumSize(buttonSize);
            btn.setPreferredSize(buttonSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            optionsPanel.add(btn);
            optionsPanel.add(Box.createVerticalStrut(10));
        }

        JTextArea reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(reportArea);

        panel.add(optionsPanel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ========== USER REPORT ==========
        userBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            List<User> users = systemManager.getAllUsers();

            sb.append("=== USER SUMMARY REPORT ===\n\n");
            sb.append("Total Users: ").append(users.size()).append("\n\n");

            for (User u : users) {
                sb.append(u.getUserID())
                        .append(" - ")
                        .append(u.getFullName())
                        .append(" (")
                        .append(u.getRole())
                        .append(")\n");
            }

            reportArea.setText(sb.toString());
        });

        // ========== MODULE REPORT ==========
        moduleBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            List<Module> modules = systemManager.getAllModules();

            sb.append("=== MODULE SUMMARY REPORT ===\n\n");

            // For each module, also show any lecturers assigned to it
            List<User> allLecturers = systemManager.getAllLecturers();

            for (Module m : modules) {
                sb.append("Module ID: ").append(m.getModuleID()).append("\n");
                sb.append("Name: ").append(m.getModuleName()).append("\n");
                sb.append("Code: ").append(m.getModuleCode()).append("\n");
                sb.append("Credits: ").append(m.getCreditHours()).append("\n");

                // find lecturers assigned to this module
                StringBuilder lecNames = new StringBuilder();
                for (User u : allLecturers) {
                    if (u instanceof Lecturer) {
                        Lecturer L = (Lecturer) u;
                        for (Module am : L.getAssignedModules()) {
                            if (am != null && (am.getModuleID().equalsIgnoreCase(m.getModuleID()) || am.getModuleCode().equalsIgnoreCase(m.getModuleCode()))) {
                                if (lecNames.length() > 0) lecNames.append(", ");
                                lecNames.append(L.getFullName()).append(" (").append(L.getUserID()).append(")");
                                break;
                            }
                        }
                    }
                }

                sb.append("Assigned Lecturer(s): ").append(lecNames.length() > 0 ? lecNames.toString() : "Unassigned").append("\n\n");
            }

            reportArea.setText(sb.toString());
        });

        // ========== ASSESSMENT REPORT ==========
        assessBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            List<Assessment> assessments = systemManager.getAllAssessments();

            sb.append("=== ASSESSMENT SUMMARY REPORT ===\n\n");

            for (Assessment a : assessments) {
                sb.append("Assessment ID: ").append(a.getAssessmentID()).append("\n");
                sb.append("Title: ").append(a.getAssessmentName()).append("\n");
                sb.append("Type: ").append(a.getAssessmentType().getAssessmentType()).append("\n\n");
            }

            reportArea.setText(sb.toString());
        });

        // ========== CLASS REPORT ==========
        classBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            List<ClassModule> classes = systemManager.getAllClasses();

            sb.append("=== CLASS SUMMARY REPORT ===\n\n");
            sb.append("Total Classes: ").append(classes.size()).append("\n\n");

            for (ClassModule c : classes) {
                sb.append("Class ID: ").append(c.getClassID()).append("\n");
                sb.append("  Name: ").append(c.getClassName()).append("\n");
                sb.append("  Module: ").append(c.getModuleID()).append("\n");
                sb.append("  Capacity: ").append(c.getCapacity()).append("\n");
                sb.append("  Enrolled: ").append(c.getEnrolledStudents().size()).append(" students\n");
                sb.append("  Schedule: ").append(c.getDay() != null ? c.getDay() : "N/A").append(" at ")
                        .append(c.getTime() != null ? c.getTime() : "N/A").append("\n");
                sb.append("  Location: ").append(c.getLocation() != null ? c.getLocation() : "N/A").append("\n");
                sb.append("  Lecturer: ");
                if (c.getLecturerID() != null) {
                    User u = systemManager.findUserByID(c.getLecturerID());
                    sb.append(u != null ? u.getUsername() : "Not found");
                } else {
                    sb.append("Unassigned");
                }
                sb.append("\n\n");
            }

            reportArea.setText(sb.toString());
        });

        // ========== ENROLLMENT REPORT ==========
        enrollBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            List<ClassModule> classes = systemManager.getAllClasses();
            int totalEnrolled = 0;

            sb.append("=== ENROLLMENT REPORT ===\n\n");
            sb.append("--- Enrollment by Class ---\n");
            for (ClassModule c : classes) {
                int enrolled = c.getEnrolledStudents().size();
                sb.append(c.getClassID()).append(" (").append(c.getClassName()).append("): ")
                        .append(enrolled).append(" / ").append(c.getCapacity()).append(" students\n");
                totalEnrolled += enrolled;
            }

            sb.append("\nTotal Enrolled: ").append(totalEnrolled).append("\n\n");

            sb.append("--- Enrollment by Student ---\n");
            List<User> students = systemManager.getAllStudents();
            for (User u : students) {
                int classCount = 0;
                for (ClassModule c : classes) {
                    if (c.getEnrolledStudents().contains(u.getUserID())) classCount++;
                }
                sb.append(u.getUsername()).append(" (").append(u.getUserID()).append("): ")
                        .append(classCount).append(" classes\n");
            }

            reportArea.setText(sb.toString());
        });

        return panel;
    }
}
