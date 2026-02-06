import javax.swing.*;
import java.awt.*;

public class AcademicLeaderDashboard extends JFrame {

    private final SystemManager systemManager;
    private final AcademicLeader leader;

    private JLabel headerLabel;

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
        logoutBtn.setBackground(Color.RED);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setPreferredSize(new Dimension(110, 35));

        logoutBtn.addActionListener(e -> {
            dispose();
            new AFSLoginFrame().setVisible(true);
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
        panel.add(new JLabel("Leader ID:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(leader.getLeaderID()), gbc);

        JButton saveBtn = new JButton("Save Profile");
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            leader.setFullName(nameField.getText());
            leader.setEmail(emailField.getText());
            leader.setDepartment(deptField.getText());

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
            Module module = new Module(
                    codeField.getText(),
                    nameField.getText(),
                    "UNASSIGNED",
                    "UNASSIGNED",
                    Integer.parseInt(creditField.getText()),
                    deptField.getText()
            );
            leader.createModule(module);
            JOptionPane.showMessageDialog(this, "Module created");
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
        assignBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Lecturer assigned (demo)")
        );

        panel.add(new JLabel("Select Module:"));
        panel.add(moduleBox);
        panel.add(new JLabel("Select Lecturer:"));
        panel.add(lecturerBox);
        panel.add(new JLabel());
        panel.add(assignBtn);

        return panel;
    }

    // ================= REPORTS =================
    private JPanel buildReportsPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    JTextArea area = new JTextArea();
    area.setEditable(false);
    area.setFont(new Font("Monospaced", Font.PLAIN, 13));

    area.setText(
        "Academic Reports\n\n" +
        "Department: " + leader.getDepartment() + "\n" +
        "Academic Leader: " + leader.getFullName() + "\n\n" +
        "Summary:\n" +
        "- Total Modules: " + leader.getManagedModules().size() + "\n" +
        "- Academic Risk Level: HIGH RISK\n"
    );

    JScrollPane scrollPane = new JScrollPane(area);
    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
}
