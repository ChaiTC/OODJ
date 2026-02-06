import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * AcademicLeaderDashboard
 * UI layer for Academic Leader role
 */
public class AcademicLeaderDashboard extends JFrame {

    private final SystemManager systemManager;
    private final AcademicLeader leader;

    private JTextArea reportArea;
    private JComboBox<String> moduleBox;
    private JComboBox<String> lecturerBox;

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
        setSize(950, 620);
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
        header.setPreferredSize(new Dimension(950, 55));

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

        tabs.addTab("Edit Profile", buildProfilePanel());
        tabs.addTab("Module Management", buildModulePanel());
        tabs.addTab("Assign Lecturers", buildAssignPanel());
        tabs.addTab("View Reports", buildReportsPanel());

        // Refresh reports whenever tab is selected
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 3) {
                refreshReport();
            }
        });

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
    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JTextField nameField = new JTextField(leader.getFullName());
        JTextField emailField = new JTextField(leader.getEmail());
        JTextField deptField = new JTextField(leader.getDepartment());

        deptField.setEditable(false);

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
    private JPanel buildModulePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField creditField = new JTextField("3");
        JTextField deptField = new JTextField(leader.getDepartment());
        deptField.setEditable(false);

        JButton createBtn = new JButton("Create Module");

        createBtn.addActionListener(e -> {
            if (codeField.getText().isEmpty() || nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Module code and name are required",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int credits = Integer.parseInt(creditField.getText());

                Module module = new Module(
                        codeField.getText(),
                        nameField.getText(),
                        credits,
                        deptField.getText()
                );

                leader.createModule(module);

                // Optional persistence
                FileManager.saveModules(leader.getManagedModules());

                refreshModuleDropdown();

                JOptionPane.showMessageDialog(this,
                        "Module created successfully");

                codeField.setText("");
                nameField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Credits must be a number",
                        "Input Error",
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

    // ======================================================
    // ASSIGN LECTURERS
    // ======================================================
    private JPanel buildAssignPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        moduleBox = new JComboBox<>();
        lecturerBox = new JComboBox<>();

        refreshModuleDropdown();
        refreshLecturerDropdown();

        JButton assignBtn = new JButton("Assign Lecturer");

        assignBtn.addActionListener(e -> {
            if (moduleBox.getItemCount() == 0 || lecturerBox.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No module or lecturer available");
                return;
            }

            int moduleIndex = moduleBox.getSelectedIndex();
            int lecturerIndex = lecturerBox.getSelectedIndex();

            Module module = leader.getManagedModules().get(moduleIndex);
            Lecturer lecturer = (Lecturer)
                    systemManager.getUsersByRole("LECTURER").get(lecturerIndex);

            module.assignLecturer(lecturer);
            leader.assignLecturer(lecturer);

            JOptionPane.showMessageDialog(this,
                    "Lecturer assigned to " + module.getModuleCode());
        });

        panel.add(new JLabel("Select Module:"));
        panel.add(moduleBox);
        panel.add(new JLabel("Select Lecturer:"));
        panel.add(lecturerBox);
        panel.add(new JLabel());
        panel.add(assignBtn);

        return panel;
    }

    private void refreshModuleDropdown() {
        if (moduleBox == null) return;

        moduleBox.removeAllItems();

        List<Module> modules = leader.getManagedModules();
        for (Module m : modules) {
            moduleBox.addItem(m.getModuleCode() + " - " + m.getModuleName());
        }
    }

    private void refreshLecturerDropdown() {
        if (lecturerBox == null) return;

        lecturerBox.removeAllItems();

        List<User> lecturers = systemManager.getUsersByRole("LECTURER");
        for (User u : lecturers) {
            lecturerBox.addItem(u.getUserID() + " - " + u.getFullName());
        }
    }

    // ======================================================
    // VIEW REPORTS
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
        List<Assessment> assessments = systemManager.getAllAssessments();
        GradingScale scale = systemManager.getGradingScale();

        reportArea.setText(
                leader.generateAcademicReport(assessments, scale)
        );
    }
}
