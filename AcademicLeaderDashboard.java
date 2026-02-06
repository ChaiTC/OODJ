import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AcademicLeaderDashboard extends JFrame {

    private final SystemManager systemManager;
    private final AcademicLeader leader;

    private JComboBox<String> moduleCombo; // shared between tabs
    private JTextArea reportArea;

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
    // 1️⃣ EDIT PROFILE (Improved Layout)
    // ======================================================
    private JPanel buildEditProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField nameField = new JTextField(25);
        nameField.setText(leader.getFullName());

        JTextField emailField = new JTextField(25);
        emailField.setText(leader.getEmail());

        JTextField deptField = new JTextField(25);
        deptField.setText(leader.getDepartment());

        addRow(panel, gbc, 0, "Full Name:", nameField);
        addRow(panel, gbc, 1, "Email:", emailField);
        addRow(panel, gbc, 2, "Department:", deptField);
        addRow(panel, gbc, 3, "Leader ID:", new JLabel(leader.getLeaderID()));

        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row,
                        String label, Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // ======================================================
    // 2️⃣ MODULE MANAGEMENT (Dynamic Update)
    // ======================================================
    private JPanel buildModuleManagementPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField codeField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField creditField = new JTextField("3", 5);
        JTextField deptField = new JTextField(leader.getDepartment(), 20);

        JButton createBtn = new JButton("Create Module");

        addRow(panel, gbc, 0, "Module Code:", codeField);
        addRow(panel, gbc, 1, "Module Name:", nameField);
        addRow(panel, gbc, 2, "Credits:", creditField);
        addRow(panel, gbc, 3, "Department:", deptField);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(createBtn, gbc);

        createBtn.addActionListener(e -> {
            if (codeField.getText().isEmpty() || nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            Module module = new Module(
                    codeField.getText(),
                    nameField.getText(),
                    Integer.parseInt(creditField.getText()),
                    deptField.getText()
            );

            leader.createModule(module);
            refreshModuleCombo();
            refreshReport();

            JOptionPane.showMessageDialog(this, "Module created successfully");
            codeField.setText("");
            nameField.setText("");
        });

        return panel;
    }

    // ======================================================
    // 3️⃣ ASSIGN LECTURERS (Live Module List)
    // ======================================================
    private JPanel buildAssignLecturersPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        moduleCombo = new JComboBox<>();
        refreshModuleCombo();

        JComboBox<String> lecturerCombo = new JComboBox<>();
        lecturerCombo.addItem("LEC001 - AIDEN");

        JButton assignBtn = new JButton("Assign Lecturer");

        addRow(panel, gbc, 0, "Select Module:", moduleCombo);
        addRow(panel, gbc, 1, "Select Lecturer:", lecturerCombo);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(assignBtn, gbc);

        assignBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Lecturer assigned (demo)")
        );

        return panel;
    }

    private void refreshModuleCombo() {
        if (moduleCombo == null) return;

        moduleCombo.removeAllItems();
        List<Module> modules = leader.getManagedModules();

        if (modules.isEmpty()) {
            moduleCombo.addItem("No modules available");
        } else {
            for (Module m : modules) {
                moduleCombo.addItem(m.getModuleCode() + " - " + m.getModuleName());
            }
        }
    }

    // ======================================================
    // 3️⃣ VIEW REPORTS (Real Data)
    // ======================================================
    private JPanel buildReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        refreshReport();

        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        return panel;
    }

    private void refreshReport() {
        int totalModules = leader.getManagedModules().size();

        String risk = totalModules == 0 ? "HIGH RISK" : "LOW RISK";

        reportArea.setText(
                "Academic Reports\n\n" +
                "Department: " + leader.getDepartment() + "\n" +
                "Academic Leader: " + leader.getFullName() + "\n\n" +
                "Summary:\n" +
                "- Total Modules: " + totalModules + "\n" +
                "- Academic Risk Level: " + risk + "\n"
        );
    }
}
