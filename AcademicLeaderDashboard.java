import javax.swing.*;
import java.awt.*;

public class AcademicLeaderDashboard extends JFrame {

    private final SystemManager systemManager;
    private final AcademicLeader leader;

    public AcademicLeaderDashboard(SystemManager systemManager, AcademicLeader leader) {
        this.systemManager = systemManager;
        this.leader = leader;
        initUI();
    }

    // ======================================================
    // MAIN UI
    // ======================================================
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

    // ======================================================
    // HEADER
    // ======================================================
    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setBackground(new Color(56, 142, 60));

        JLabel title = new JLabel(
                "Welcome, " + leader.getFullName() + " (Academic Leader)"
        );
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        header.add(title);
        return header;
    }

    // ======================================================
    // TABS
    // ======================================================
    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Edit Profile", buildEditProfilePanel());
        tabs.addTab("Module Management", buildModuleManagementPanel());
        tabs.addTab("Assign Lecturers", buildAssignLecturersPanel());
        tabs.addTab("View Reports", buildReportsPanel());

        return tabs;
    }

    // ======================================================
    // FOOTER
    // ======================================================
    private JPanel buildFooter() {
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.RED);
        logoutBtn.setForeground(Color.WHITE);

        logoutBtn.addActionListener(e -> {
            dispose();
            new AFSLoginFrame().setVisible(true);
        });

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.add(logoutBtn);
        return footer;
    }

    // ======================================================
    // EDIT PROFILE
    // ======================================================
    private JPanel buildEditProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JTextField nameField = new JTextField(leader.getFullName());
        JTextField emailField = new JTextField(leader.getEmail());
        JTextField deptField = new JTextField(leader.getDepartment());
        deptField.setEditable(false); // Improvement 3

        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Department:"));
        panel.add(deptField);

        panel.add(new JLabel("Leader ID:"));
        panel.add(new JLabel(leader.getLeaderID()));

        return panel;
    }

    // ======================================================
    // MODULE MANAGEMENT
    // ======================================================
    private JPanel buildModuleManagementPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField creditField = new JTextField("3");
        JTextField deptField = new JTextField(leader.getDepartment());
        deptField.setEditable(false);

        JButton createBtn = new JButton("Create Module");

        createBtn.addActionListener(e -> {
            if (codeField.getText().isEmpty() || nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            int credits;
            try {
                credits = Integer.parseInt(creditField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits must be a number");
                return;
            }

            Module module = new Module(
                    codeField.getText(),
                    nameField.getText(),
                    credits,
                    deptField.getText()
            );

            leader.createModule(module);
            JOptionPane.showMessageDialog(this, "Module created successfully");

            codeField.setText("");
            nameField.setText("");
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

    // ======================================================
    // ASSIGN LECTURERS (IMPROVEMENT 1)
    // ======================================================
    private JPanel buildAssignLecturersPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JComboBox<String> moduleBox = new JComboBox<>();
        JComboBox<String> lecturerBox = new JComboBox<>();

        if (leader.getManagedModules().isEmpty()) {
            moduleBox.addItem("No modules available");
        } else {
            leader.getManagedModules().forEach(
                    m -> moduleBox.addItem(m.getModuleCode() + " - " + m.getModuleName())
            );
        }

        lecturerBox.addItem("LEC001 - AIDEN");

        JButton assignBtn = new JButton("Assign Lecturer");
        assignBtn.addActionListener(e -> {
            if (leader.getManagedModules().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No modules to assign");
                return;
            }

            Lecturer lecturer = new Lecturer(
                    "LEC001",
                    "aiden",
                    "pass",
                    "aiden@college.edu",
                    "Aiden",
                    "0123456789"
            );

            leader.assignLecturer(lecturer);
            JOptionPane.showMessageDialog(this, "Lecturer assigned successfully");
        });

        panel.add(new JLabel("Select Module:"));
        panel.add(moduleBox);

        panel.add(new JLabel("Select Lecturer:"));
        panel.add(lecturerBox);

        panel.add(new JLabel());
        panel.add(assignBtn);

        return panel;
    }

    // ======================================================
    // VIEW REPORTS (IMPROVEMENT 2)
    // ======================================================
    private JPanel buildReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        String riskLevel =
                leader.getManagedModules().isEmpty() ? "HIGH RISK" : "LOW RISK";

        reportArea.setText(
                "Academic Reports\n\n" +
                "Department: " + leader.getDepartment() + "\n" +
                "Academic Leader: " + leader.getFullName() + "\n\n" +
                "Summary:\n" +
                "- Total Modules: " + leader.getManagedModules().size() + "\n" +
                "- Total Lecturers Assigned: " + leader.getAssignedLecturers().size() + "\n" +
                "- Academic Risk Level: " + riskLevel + "\n\n" +
                "Remarks:\n" +
                "This report is generated based on current in-memory academic data."
        );

        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        return panel;
    }
}
