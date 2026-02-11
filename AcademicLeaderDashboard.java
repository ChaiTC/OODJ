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
        setSize(900, 600);
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
        logoutBtn.setPreferredSize(new Dimension(110, 35));

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
        if (leader.getGender() != null && !leader.getGender().isEmpty()) {
            genderBox.setSelectedItem(leader.getGender());
        }

        JSpinner ageSpinner = new JSpinner(
                new SpinnerNumberModel(
                        leader.getAge() > 0 ? leader.getAge() : 20,
                        15, 100, 1
                )
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

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Leader ID:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(leader.getLeaderID()), gbc);

        JButton saveBtn = new JButton("Save Profile");
        gbc.gridx = 1; gbc.gridy = 6;
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

            JOptionPane.showMessageDialog(this, "Profile updated successfully");
        });

        return panel;
    }

    // ================= MODULE MANAGEMENT =================
    private JPanel buildModuleManagementPanel() {

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField creditField = new JTextField("3");
        JTextField deptField = new JTextField(leader.getDepartment());

        JButton createBtn = new JButton("Create Module");

        createBtn.addActionListener(e -> {

            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String credits = creditField.getText().trim();

            if (code.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Module Code and Name are required",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Module module = new Module(
                        code,
                        name,
                        "UNASSIGNED",
                        "UNASSIGNED",
                        Integer.parseInt(credits),
                        deptField.getText()
                );

                systemManager.createModule(module);
                leader.createModule(module);

                refreshModuleBox();

                JOptionPane.showMessageDialog(this,
                        "Module created successfully!");

                codeField.setText("");
                nameField.setText("");
                creditField.setText("3");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Credits must be a valid number",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel("Module Code:"));
        panel.add(codeField);
        panel.add(new JLabel("Module Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Credits:"));
        panel.add(creditField);
        panel.add(new JLabel("Department:"));
        panel.add(deptField);
        panel.add(new JLabel());
        panel.add(createBtn);

        return panel;
    }

    // ================= ASSIGN LECTURERS =================
    private JPanel buildAssignLecturersPanel() {

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        moduleBox = new JComboBox<>();
        refreshModuleBox();

        JComboBox<String> lecturerBox = new JComboBox<>();
        lecturerBox.addItem("LEC001 - AIDEN");

        JButton assignBtn = new JButton("Assign Lecturer");
        assignBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Lecturer assigned (demo)")
        );

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

        if (modules == null || modules.isEmpty()) {
            moduleBox.addItem("No modules available");
        } else {
            for (Module m : modules) {
                moduleBox.addItem(
                        m.getModuleCode() + " - " + m.getModuleName()
                );
            }
        }
    }

    // ================= REPORTS =================
    // ================= REPORTS =================
private JPanel buildReportsPanel() {

    JPanel panel = new JPanel(new BorderLayout(8, 8));
    panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
    optionsPanel.setBorder(BorderFactory.createTitledBorder("Report Options"));
    optionsPanel.setPreferredSize(new Dimension(170, 300));

    JButton userReportBtn = new JButton("User Summary");
    JButton classReportBtn = new JButton("Class Summary");
    JButton moduleReportBtn = new JButton("Module Summary");
    JButton assessmentReportBtn = new JButton("Assessment Summary");
    JButton enrollmentReportBtn = new JButton("Enrollment Report");

    optionsPanel.add(userReportBtn);
    optionsPanel.add(Box.createVerticalStrut(8));
    optionsPanel.add(classReportBtn);
    optionsPanel.add(Box.createVerticalStrut(8));
    optionsPanel.add(moduleReportBtn);
    optionsPanel.add(Box.createVerticalStrut(8));
    optionsPanel.add(assessmentReportBtn);
    optionsPanel.add(Box.createVerticalStrut(8));
    optionsPanel.add(enrollmentReportBtn);

    JTextArea reportArea = new JTextArea();
    reportArea.setEditable(false);
    reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

    panel.add(optionsPanel, BorderLayout.WEST);
    panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

    // ================= USER REPORT =================
    userReportBtn.addActionListener(e -> {
        StringBuilder report = new StringBuilder();
        List<User> users = systemManager.getAllUsers();

        report.append("=== USER SUMMARY REPORT ===\n\n");
        report.append("Total Users: ").append(users.size()).append("\n\n");

        for (User u : users) {
            report.append(u.getUserID())
                  .append(" - ")
                  .append(u.getFullName())
                  .append(" (")
                  .append(u.getRole())
                  .append(")\n");
        }

        reportArea.setText(report.toString());
    });

    // ================= CLASS REPORT =================
    classReportBtn.addActionListener(e -> {
        StringBuilder report = new StringBuilder();
        List<ClassModule> classes = systemManager.getAllClasses();

        report.append("=== CLASS SUMMARY REPORT ===\n\n");
        report.append("Total Classes: ").append(classes.size()).append("\n\n");

        for (ClassModule c : classes) {
            report.append("Class ID: ").append(c.getClassID()).append("\n");
            report.append("Name: ").append(c.getClassName()).append("\n");
            report.append("Module: ").append(c.getModuleID()).append("\n");
            report.append("Capacity: ").append(c.getCapacity()).append("\n");
            report.append("Enrolled: ").append(c.getEnrolledStudents().size()).append("\n\n");
        }

        reportArea.setText(report.toString());
    });

    // ================= MODULE REPORT =================
    moduleReportBtn.addActionListener(e -> {
        StringBuilder report = new StringBuilder();
        List<Module> modules = systemManager.getAllModules();

        report.append("=== MODULE SUMMARY REPORT ===\n\n");
        report.append("Total Modules: ").append(modules.size()).append("\n\n");

        for (Module m : modules) {
            report.append("Module ID: ").append(m.getModuleID()).append("\n");
            report.append("Name: ").append(m.getModuleName()).append("\n");
            report.append("Code: ").append(m.getModuleCode()).append("\n");
            report.append("Credits: ").append(m.getCredits()).append("\n\n");
        }

        reportArea.setText(report.toString());
    });

    // ================= ASSESSMENT REPORT =================
    assessmentReportBtn.addActionListener(e -> {
        StringBuilder report = new StringBuilder();
        List<Assessment> assessments = systemManager.getAllAssessments();

        report.append("=== ASSESSMENT SUMMARY REPORT ===\n\n");
        report.append("Total Assessments: ").append(assessments.size()).append("\n\n");

        for (Assessment a : assessments) {
            report.append("Assessment ID: ").append(a.getAssessmentID()).append("\n");
            report.append("Class: ").append(a.getClassID()).append("\n");
            report.append("Title: ").append(a.getTitle()).append("\n");
            report.append("Type: ").append(a.getAssessmentType()).append("\n");
            report.append("Status: ").append(a.getStatus()).append("\n\n");
        }

        reportArea.setText(report.toString());
    });

    // ================= ENROLLMENT REPORT =================
    enrollmentReportBtn.addActionListener(e -> {
        StringBuilder report = new StringBuilder();
        List<ClassModule> classes = systemManager.getAllClasses();

        report.append("=== ENROLLMENT REPORT ===\n\n");

        int total = 0;
        for (ClassModule c : classes) {
            int enrolled = c.getEnrolledStudents().size();
            report.append(c.getClassID())
                  .append(" - ")
                  .append(c.getClassName())
                  .append(": ")
                  .append(enrolled)
                  .append(" students\n");
            total += enrolled;
        }

        report.append("\nTotal Enrolled Students: ").append(total);

        reportArea.setText(report.toString());
    });

    return panel;
}
}
