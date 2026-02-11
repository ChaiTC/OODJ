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

    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Edit Profile", buildEditProfilePanel());
        tabs.addTab("Module Management", buildModuleManagementPanel());
        tabs.addTab("Assign Lecturers", buildAssignLecturersPanel());
        tabs.addTab("View Reports", buildReportsPanel());

        return tabs;
    }

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

    // ================= MODULE MANAGEMENT =================
    private JPanel buildModuleManagementPanel() {

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField creditField = new JTextField("3");

        JComboBox<String> departmentBox = new JComboBox<>(
                new String[]{"IT", "Business", "Engineering"}
        );
        departmentBox.setSelectedItem(leader.getDepartment());

        JButton createBtn = new JButton("Create Module");

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
                refreshModuleBox();

                JOptionPane.showMessageDialog(this, "Module created!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid input",
                        "Error",
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
        panel.add(departmentBox);
        panel.add(new JLabel());
        panel.add(createBtn);

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

        // USER REPORT
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

        // MODULE REPORT
        moduleBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            List<Module> modules = systemManager.getAllModules();

            sb.append("=== MODULE SUMMARY REPORT ===\n\n");

            for (Module m : modules) {
                sb.append("Module ID: ").append(m.getModuleID()).append("\n");
                sb.append("Name: ").append(m.getModuleName()).append("\n");
                sb.append("Code: ").append(m.getModuleCode()).append("\n");
                sb.append("Credits: ").append(m.getCreditHours()).append("\n\n");
            }

            reportArea.setText(sb.toString());
        });

        // CLASS REPORT
        classBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            List<ClassModule> classes = systemManager.getAllClasses();

            sb.append("=== CLASS SUMMARY REPORT ===\n\n");

            if (classes.isEmpty()) {
                sb.append("No classes available.\n");
            } else {
                for (ClassModule c : classes) {
                    sb.append("Class ID: ").append(c.getClassID()).append("\n");
                    sb.append("Class Name: ").append(c.getClassName()).append("\n");
                    sb.append("Module: ").append(c.getModule().getModuleName()).append("\n");
                    sb.append("Lecturer: ")
                      .append(c.getLecturer() != null ? c.getLecturer().getFullName() : "UNASSIGNED")
                      .append("\n");
                    sb.append("Capacity: ").append(c.getCapacity()).append("\n");
                    sb.append("Enrolled Students: ").append(c.getEnrolledStudents().size()).append("\n\n");
                }
            }

            reportArea.setText(sb.toString());
        });

        // ENROLLMENT REPORT
        enrollBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            List<ClassModule> classes = systemManager.getAllClasses();

            sb.append("=== ENROLLMENT REPORT ===\n\n");

            if (classes.isEmpty()) {
                sb.append("No classes found.\n");
            } else {
                for (ClassModule c : classes) {
                    sb.append("Class: ").append(c.getClassName())
                      .append(" (").append(c.getClassID()).append(")\n");

                    if (c.getEnrolledStudents().isEmpty()) {
                        sb.append("   No students enrolled.\n");
                    } else {
                        for (Student s : c.getEnrolledStudents()) {
                            sb.append("   - ")
                              .append(s.getStudentID())
                              .append(" | ")
                              .append(s.getFullName())
                              .append("\n");
                        }
                    }
                    sb.append("\n");
                }
            }

            reportArea.setText(sb.toString());
        });

        return panel;
    }
}
