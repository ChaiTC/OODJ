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
        setSize(1000, 600);
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

        JPanel panel = new JPanel(new GridLayout(6, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

        JTextField nameField = new JTextField(leader.getFullName());
        JTextField emailField = new JTextField(leader.getEmail());
        JTextField phoneField = new JTextField(leader.getPhoneNumber());

        JButton saveBtn = new JButton("Save Changes");

        saveBtn.addActionListener(e -> {
            leader.setFullName(nameField.getText());
            leader.setEmail(emailField.getText());
            leader.setPhoneNumber(phoneField.getText());

            systemManager.updateUser(leader);

            JOptionPane.showMessageDialog(this,
                    "Profile updated successfully!");
        });

        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel());
        panel.add(saveBtn);

        return panel;
    }

    // ================= MODULE MANAGEMENT (RESTORED LOOK) =================
    private JPanel buildModuleManagementPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField codeField = new JTextField(25);
        JTextField nameField = new JTextField(25);
        JTextField creditField = new JTextField("3", 25);
        JTextField deptField = new JTextField(leader.getDepartment(), 25);

        JButton createBtn = new JButton("Create Module");
        createBtn.setPreferredSize(new Dimension(250, 45));

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Module Code:"), gbc);
        gbc.gridx = 1;
        panel.add(codeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Module Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Credits:"), gbc);
        gbc.gridx = 1;
        panel.add(creditField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        panel.add(deptField, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(createBtn, gbc);

        createBtn.addActionListener(e -> {

            if (codeField.getText().trim().isEmpty() ||
                    nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Module Code and Name are required.");
                return;
            }

            Module module = new Module(
                    systemManager.generateModuleID(),
                    nameField.getText(),
                    codeField.getText(),
                    "UNASSIGNED",
                    Integer.parseInt(creditField.getText()),
                    deptField.getText()
            );

            systemManager.createModule(module);
            refreshModuleBox();

            JOptionPane.showMessageDialog(this,
                    "Module created successfully!");

            codeField.setText("");
            nameField.setText("");
            creditField.setText("3");
        });

        return panel;
    }

    // ================= ASSIGN LECTURERS =================
    private JPanel buildAssignLecturersPanel() {

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        moduleBox = new JComboBox<>();
        refreshModuleBox();

        JComboBox<String> lecturerBox = new JComboBox<>();
        List<User> lecturers = systemManager.getAllLecturers();
        for (User u : lecturers) {
            lecturerBox.addItem(u.getUserID() + " - " + u.getFullName());
        }

        JButton assignBtn = new JButton("Assign Lecturer");

        assignBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Lecturer assigned successfully!")
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
        optionsPanel.setPreferredSize(new Dimension(220, 400));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Report Options"));

        Dimension buttonSize = new Dimension(190, 45);

        JButton userBtn = createReportButton("User Summary", buttonSize);
        JButton classBtn = createReportButton("Class Summary", buttonSize);
        JButton moduleBtn = createReportButton("Module Summary", buttonSize);
        JButton assessBtn = createReportButton("Assessment Summary", buttonSize);
        JButton enrollBtn = createReportButton("Enrollment Report", buttonSize);

        optionsPanel.add(userBtn);
        optionsPanel.add(Box.createVerticalStrut(12));
        optionsPanel.add(classBtn);
        optionsPanel.add(Box.createVerticalStrut(12));
        optionsPanel.add(moduleBtn);
        optionsPanel.add(Box.createVerticalStrut(12));
        optionsPanel.add(assessBtn);
        optionsPanel.add(Box.createVerticalStrut(12));
        optionsPanel.add(enrollBtn);

        JTextArea reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setEditable(false);

        panel.add(optionsPanel, BorderLayout.WEST);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        // ===== USER =====
        userBtn.addActionListener(e -> {
            List<User> users = systemManager.getAllUsers();
            StringBuilder sb = new StringBuilder();

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

        // ===== CLASS =====
        classBtn.addActionListener(e -> {
            List<ClassModule> classes = systemManager.getAllClasses();
            StringBuilder sb = new StringBuilder();

            sb.append("=== CLASS SUMMARY REPORT ===\n\n");

            if (classes.isEmpty()) {
                sb.append("No classes found.\n");
            } else {
                for (ClassModule c : classes) {
                    sb.append("Class ID: ").append(c.getClassID()).append("\n");
                    sb.append("Name: ").append(c.getClassName()).append("\n");
                    sb.append("Capacity: ").append(c.getCapacity()).append("\n");
                    sb.append("Enrolled: ").append(c.getEnrolledStudents().size()).append("\n\n");
                }
            }

            reportArea.setText(sb.toString());
        });

        // ===== ENROLLMENT =====
        enrollBtn.addActionListener(e -> {
            List<ClassModule> classes = systemManager.getAllClasses();
            StringBuilder sb = new StringBuilder();
            int total = 0;

            sb.append("=== ENROLLMENT REPORT ===\n\n");

            if (classes.isEmpty()) {
                sb.append("No enrollment data available.\n");
            } else {
                for (ClassModule c : classes) {
                    int enrolled = c.getEnrolledStudents().size();
                    sb.append(c.getClassName())
                            .append(": ")
                            .append(enrolled)
                            .append(" students\n");

                    total += enrolled;
                }

                sb.append("\nTotal Enrolled Students: ").append(total);
            }

            reportArea.setText(sb.toString());
        });

        return panel;
    }

    private JButton createReportButton(String text, Dimension size) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(size);
        btn.setPreferredSize(size);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }
}
