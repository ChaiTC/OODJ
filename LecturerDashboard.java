import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LecturerDashboard extends JFrame {
    private SystemManager systemManager;
    private Lecturer lecturer;

    public LecturerDashboard(SystemManager systemManager, Lecturer lecturer) {
        this.systemManager = systemManager;
        this.lecturer = lecturer;
        initializeFrame();
    }

    private void initializeFrame() {
        setTitle("Lecturer Dashboard - " + lecturer.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 152, 0));
        JLabel welcomeLabel = new JLabel("Welcome, " + lecturer.getFullName() + " (Lecturer)");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(welcomeLabel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(240, 240, 240));

        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(Color.WHITE);
        showEditProfile(profilePanel);

        JPanel assessmentPanel = new JPanel(new BorderLayout());
        assessmentPanel.setBackground(Color.WHITE);
        showDesignAssessment(assessmentPanel);

        JPanel marksPanel = new JPanel(new BorderLayout());
        marksPanel.setBackground(Color.WHITE);
        showKeyInMarks(marksPanel);

        JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.setBackground(Color.WHITE);
        showProvideFeedback(feedbackPanel);

        JPanel modulesPanel = new JPanel(new BorderLayout());
        modulesPanel.setBackground(Color.WHITE);
        showMyModules(modulesPanel);

        tabbedPane.addTab("Edit Profile", profilePanel);
        tabbedPane.addTab("Design Assessment", assessmentPanel);
        tabbedPane.addTab("Key-in Marks", marksPanel);
        tabbedPane.addTab("Provide Feedback", feedbackPanel);
        tabbedPane.addTab("My Modules", modulesPanel);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(240, 240, 240));
        JButton logoutBtn = createMenuButton("Logout", new Color(244, 67, 54));
        logoutBtn.addActionListener(e -> {
            dispose();
            new AFSLoginFrame(systemManager).setVisible(true);
        });
        bottomPanel.add(logoutBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createMenuButton(String text, Color baseColor) {
        Color hover = baseColor.brighter();
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(baseColor);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40));
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { button.setBackground(baseColor); }
        });
        return button;
    }

    private JPanel createLabeledRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(140, 24));
        row.add(lbl, BorderLayout.WEST);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }

    private void showEditProfile(JPanel contentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JTextField nameField = new JTextField(lecturer.getFullName());
        JTextField emailField = new JTextField(lecturer.getEmail());
        JTextField departmentField = new JTextField(lecturer.getDepartment());
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "N/A"});

        String gender = lecturer.getGender();
        if (gender != null && !gender.isEmpty()) genderBox.setSelectedItem(gender);

        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(
                lecturer.getAge() > 0 ? lecturer.getAge() : 20, 15, 100, 1
        ));

        panel.add(createLabeledRow("Full Name:", nameField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Email:", emailField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Department:", departmentField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Gender:", genderBox)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Age:", ageSpinner)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Lecturer ID:", new JLabel(lecturer.getLecturerID())));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(e -> {
            lecturer.setFullName(nameField.getText());
            lecturer.setEmail(emailField.getText());
            lecturer.setDepartment(departmentField.getText());
            lecturer.setGender((String) genderBox.getSelectedItem());
            lecturer.setAge((Integer) ageSpinner.getValue());

            
            systemManager.updateUser(lecturer);

            JOptionPane.showMessageDialog(this, "✓ Profile updated and saved!");
        });
        btnRow.add(saveBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnRow);

        contentPanel.add(panel, BorderLayout.CENTER);
    }

    private void showDesignAssessment(JPanel contentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JTextField titleField = new JTextField();
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{
                "ASSIGNMENT", "CLASS_TEST", "FINAL_EXAM", "PROJECT", "QUIZ", "PRESENTATION"
        });
        JTextField marksField = new JTextField("100");
        JTextField weightageField = new JTextField("10");

        panel.add(createLabeledRow("Assessment Title:", titleField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Type:", typeCombo)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Total Marks:", marksField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Weightage (%):", weightageField)); panel.add(Box.createVerticalStrut(8));

        JButton createBtn = new JButton("Create Assessment");
        createBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "✓ Assessment Created!\n\nTitle: " + titleField.getText() +
                            "\nType: " + typeCombo.getSelectedItem() +
                            "\nTotal Marks: " + marksField.getText());
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.add(createBtn);
        panel.add(btnRow);

        contentPanel.add(panel, BorderLayout.CENTER);
    }

    private void showKeyInMarks(JPanel contentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        java.util.List<User> users = systemManager.getUsersByRole("STUDENT");
        String[] students = users.stream().map(u -> u.getUserID() + " - " + u.getFullName()).toArray(String[]::new);
        if (students.length == 0) students = new String[]{"No students available"};
        JComboBox<String> studentCombo = new JComboBox<>(students);

        java.util.List<Assessment> assessmentsList = systemManager.getAllAssessments();
        String[] assessments = assessmentsList.stream().map(a -> a.getAssessmentID() + " - " + a.getAssessmentName()).toArray(String[]::new);
        if (assessments.length == 0) assessments = new String[]{"No assessments available"};
        JComboBox<String> assessmentCombo = new JComboBox<>(assessments);

        JTextField marksField = new JTextField();

        panel.add(createLabeledRow("Student:", studentCombo)); panel.add(Box.createVerticalStrut(8));
        panel.add(createLabeledRow("Assessment:", assessmentCombo)); panel.add(Box.createVerticalStrut(8));
        panel.add(createLabeledRow("Marks Obtained:", marksField)); panel.add(Box.createVerticalStrut(12));

        JButton submitBtn = new JButton("Submit Marks");
        submitBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "✓ Marks recorded (UI only for now).");
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.add(submitBtn);
        panel.add(btnRow);

        contentPanel.add(panel, BorderLayout.CENTER);
    }

    private void showProvideFeedback(JPanel contentPanel) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        java.util.List<User> users = systemManager.getUsersByRole("STUDENT");
        String[] students = users.stream().map(u -> u.getUserID() + " - " + u.getFullName()).toArray(String[]::new);
        if (students.length == 0) students = new String[]{"No students available"};
        JComboBox<String> studentCombo = new JComboBox<>(students);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Student:"));
        topPanel.add(studentCombo);

        JTextArea feedbackArea = new JTextArea(8, 40);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);

        JButton submitBtn = new JButton("Submit Feedback");
        submitBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "✓ Feedback submitted to " + studentCombo.getSelectedItem());
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(submitBtn);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(panel, BorderLayout.CENTER);
    }

    private void showMyModules(JPanel contentPanel) {
        JTextArea modulesArea = new JTextArea();
        modulesArea.setEditable(false);
        modulesArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder sb = new StringBuilder();
        sb.append("Modules Assigned to You\n\n");

        java.util.List<Module> myMods = lecturer.getAssignedModules();
        if (myMods.isEmpty()) {
            sb.append("You have no modules assigned yet.\n");
            sb.append("(Admin/Leader must assign lecturer to module/class first)");
        } else {
            for (Module m : myMods) {
                sb.append("- ").append(m.getModuleName()).append(" (").append(m.getModuleCode()).append(")\n");
            }
        }

        modulesArea.setText(sb.toString());
        contentPanel.add(new JScrollPane(modulesArea), BorderLayout.CENTER);
    }
}
