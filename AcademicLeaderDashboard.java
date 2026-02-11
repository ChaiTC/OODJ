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
        logoutBtn.setPreferredSize(new Dimension(120, 35));

        logoutBtn.addActionListener(e -> {
            dispose();
            new AFSLoginFrame(systemManager).setVisible(true);
        });

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(logoutBtn);
        return footer;
    }

    // ================= MODULE MANAGEMENT (RESTORED ORIGINAL LOOK) =================
    private JPanel buildModuleManagementPanel() {

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField creditField = new JTextField("3");
        JTextField deptField = new JTextField(leader.getDepartment());

        JButton createBtn = new JButton("Create Module");

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

        // LEFT SIDE BUTTON PANEL
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setPreferredSize(new Dimension(200, 400));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Report Options"));

        Dimension buttonSize = new Dimension(180, 40);

        JButton userBtn = createReportButton("User Summary", buttonSize);
        JButton classBtn = createReportButton("Class Summary", buttonSize);
        JButton moduleBtn = createReportButton("Module Summary", buttonSize);
        JButton assessBtn = createReportButton("Assessment Summary", buttonSize);
        JButton enrollBtn = createReportButton("Enrollment Report", buttonSize);

        optionsPanel.add(userBtn);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(classBtn);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(moduleBtn);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(assessBtn);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(enrollBtn);

        JTextArea reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setEditable(false);

        panel.add(optionsPanel, BorderLayout.WEST);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        // ================= USER REPORT =================
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

        // ================= CLASS REPORT =================
        classBtn.addActionListener(e -> {
            List<ClassModule> classes = systemManager.getAllClasses();
            StringBuilder sb = new StringBuilder();

            sb.append("=== CLASS SUMMARY REPORT ===\n\n");
            sb.append("Total Classes: ").append(classes.size()).append("\n\n");

            for (ClassModule c : classes) {
                sb.append("Class ID: ").append(c.getClassID()).append("\n");
                sb.append("Name: ").append(c.getClassName()).append("\n");
                sb.append("Module: ").append(c.getModuleID()).append("\n");
                sb.append("Capacity: ").append(c.getCapacity()).append("\n");
                sb.append("Enrolled: ").append(c.getEnrolledStudents().size()).append("\n\n");
            }

            reportArea.setText(sb.toString());
        });

        // ================= MODULE REPORT =================
        moduleBtn.addActionListener(e -> {
            List<Module> modules = systemManager.getAllModules();
            StringBuilder sb = new StringBuilder();

            sb.append("=== MODULE SUMMARY REPORT ===\n\n");

            for (Module m : modules) {
                sb.append("Module ID: ").append(m.getModuleID()).append("\n");
                sb.append("Name: ").append(m.getModuleName()).append("\n");
                sb.append("Code: ").append(m.getModuleCode()).append("\n");
                sb.append("Credits: ").append(m.getCreditHours()).append("\n\n");
            }

            reportArea.setText(sb.toString());
        });

        // ================= ASSESSMENT REPORT =================
        assessBtn.addActionListener(e -> {
            List<Assessment> assessments = systemManager.getAllAssessments();
            StringBuilder sb = new StringBuilder();

            sb.append("=== ASSESSMENT SUMMARY REPORT ===\n\n");

            for (Assessment a : assessments) {
                sb.append("Assessment ID: ").append(a.getAssessmentID()).append("\n");
                sb.append("Title: ").append(a.getAssessmentName()).append("\n");
                sb.append("Type: ").append(a.getAssessmentType().getAssessmentType()).append("\n");
                sb.append("Module: ").append(a.getModule().getModuleCode()).append("\n\n");
            }

            reportArea.setText(sb.toString());
        });

        // ================= ENROLLMENT REPORT =================
        enrollBtn.addActionListener(e -> {
            List<ClassModule> classes = systemManager.getAllClasses();
            StringBuilder sb = new StringBuilder();
            int total = 0;

            sb.append("=== ENROLLMENT REPORT ===\n\n");

            for (ClassModule c : classes) {
                int enrolled = c.getEnrolledStudents().size();
                sb.append(c.getClassID())
                        .append(" - ")
                        .append(c.getClassName())
                        .append(": ")
                        .append(enrolled)
                        .append(" students\n");

                total += enrolled;
            }

            sb.append("\nTotal Enrolled Students: ").append(total);

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
