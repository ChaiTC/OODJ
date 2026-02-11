import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AcademicLeaderDashboard extends JFrame {

    private final SystemManager systemManager;
    private final AcademicLeader leader;

    private JComboBox<String> moduleBox;

    public AcademicLeaderDashboard(SystemManager systemManager, AcademicLeader leader) {
        this.systemManager = systemManager;
        this.leader = leader;
        initUI();
    }

    private void initUI() {
        setTitle("Academic Leader Dashboard - " + leader.getFullName());
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    // ================= HEADER =================
    private JPanel buildHeader() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(56, 142, 60));

        JLabel label = new JLabel(
                "Welcome, " + leader.getFullName() + " (Academic Leader)"
        );
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        panel.add(label);
        return panel;
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
        JButton logout = new JButton("Logout");
        logout.setBackground(new Color(244, 67, 54));
        logout.setForeground(Color.WHITE);

        logout.addActionListener(e -> {
            dispose();
            new AFSLoginFrame(systemManager).setVisible(true);
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(logout);
        return panel;
    }

    // ================= EDIT PROFILE =================
    private JPanel buildEditProfilePanel() {

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

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

    // ================= MODULE MANAGEMENT =================
    private JPanel buildModuleManagementPanel() {

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField creditField = new JTextField("3");
        JTextField deptField = new JTextField(leader.getDepartment());

        JButton createBtn = new JButton("Create Module");

        createBtn.addActionListener(e -> {

            if (codeField.getText().trim().isEmpty() ||
                    nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Module Code and Name required!");
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
        panel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

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

    // ================= REPORTS =================
    private JPanel buildReportsPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel left = new JPanel(new GridLayout(5, 1, 5, 5));
        left.setBorder(BorderFactory.createTitledBorder("Report Options"));

        JButton userBtn = new JButton("User Summary");
        JButton classBtn = new JButton("Class Summary");
        JButton moduleBtn = new JButton("Module Summary");
        JButton assessBtn = new JButton("Assessment Summary");
        JButton enrollBtn = new JButton("Enrollment Report");

        left.add(userBtn);
        left.add(classBtn);
        left.add(moduleBtn);
        left.add(assessBtn);
        left.add(enrollBtn);

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        panel.add(left, BorderLayout.WEST);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        // USER REPORT
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

        // CLASS REPORT
        classBtn.addActionListener(e -> {
            List<ClassModule> classes = systemManager.getAllClasses();
            StringBuilder sb = new StringBuilder();

            sb.append("=== CLASS SUMMARY REPORT ===\n\n");
            sb.append("Total Classes: ").append(classes.size()).append("\n\n");

            for (ClassModule c : classes) {
                sb.append("Class ID: ").append(c.getClassID()).append("\n");
                sb.append("Name: ").append(c.getClassName()).append("\n");
                sb.append("Capacity: ").append(c.getCapacity()).append("\n\n");
            }

            reportArea.setText(sb.toString());
        });

        // ENROLLMENT REPORT
        enrollBtn.addActionListener(e -> {
            List<ClassModule> classes = systemManager.getAllClasses();
            StringBuilder sb = new StringBuilder();
            int total = 0;

            sb.append("=== ENROLLMENT REPORT ===\n\n");

            for (ClassModule c : classes) {
                int count = c.getEnrolledStudents().size();
                sb.append(c.getClassID())
                        .append(" - ")
                        .append(count)
                        .append(" students\n");

                total += count;
            }

            sb.append("\nTotal Enrolled Students: ").append(total);

            reportArea.setText(sb.toString());
        });

        return panel;
    }
}
