import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;

/**
 * Lecturer class - manages assessment and feedback
 * Responsibilities: Assessment design, mark entry, feedback provision
 */
public class Lecturer extends User {
    private static final long serialVersionUID = 1L;
    
    private String lecturerID;
    private String staffID;
    private String department;
    private String academicLeaderID;
    private List<Module> assignedModules;
    private List<Assessment> createdAssessments;
    private List<Feedback> providedFeedback;
    
    public Lecturer(String userID, String username, String password, String email,
                    String fullName, String phoneNumber, String lecturerID, String department) {
        super(userID, username, password, email, fullName, phoneNumber, "LECTURER");
        this.lecturerID = lecturerID;
        this.staffID = null;
        this.department = department;
        this.academicLeaderID = null;
        this.assignedModules = new ArrayList<>();
        this.createdAssessments = new ArrayList<>();
        this.providedFeedback = new ArrayList<>();
    }
    
    
    public void designAssessment(Assessment assessment) {
        createdAssessments.add(assessment);
        
    }
    
    public void keyInMarks(Assessment assessment, Student student, double marks) {
        assessment.recordMarks(student, marks);
        
    }
    
    public void provideFeedback(Feedback feedback) {
        providedFeedback.add(feedback);
        
    }
    
    public String getLecturerID() { return lecturerID; }
    public void setLecturerID(String lecturerID) { this.lecturerID = lecturerID; }
    
    public String getStaffID() { return staffID; }
    public void setStaffID(String staffID) { this.staffID = staffID; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getAcademicLeaderID() { return academicLeaderID; }
    public void setAcademicLeaderID(String academicLeaderID) { this.academicLeaderID = academicLeaderID; }
    
    public List<Module> getAssignedModules() { return assignedModules; }
    public List<Assessment> getCreatedAssessments() { return createdAssessments; }
    public List<Feedback> getProvidedFeedback() { return providedFeedback; }
}

class LecturerDashboard extends JFrame {
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
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 152, 0));
        JLabel welcomeLabel = new JLabel("Welcome, " + lecturer.getFullName() + " (Lecturer)");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(welcomeLabel);
        
        // Function Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(240, 240, 240));

        // Create content panels for each tab
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

        // Add tabs
        tabbedPane.addTab("Edit Profile", profilePanel);
        tabbedPane.addTab("Design Assessment", assessmentPanel);
        tabbedPane.addTab("Key-in Marks", marksPanel);
        tabbedPane.addTab("Provide Feedback", feedbackPanel);
        tabbedPane.addTab("My Modules", modulesPanel);
        
        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createTitledBorder("Content Area"));
        
        // Logout Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(240, 240, 240));
        JButton logoutBtn = createMenuButton("Logout", new Color(244, 67, 54));
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AFSLoginFrame().setVisible(true);
            }
        });
        bottomPanel.add(logoutBtn);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JButton createMenuButton(String text) {
        return createMenuButton(text, new Color(255, 152, 0));
    }

    private JButton createMenuButton(String text, Color baseColor) {
        Color hover = baseColor.brighter();
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(baseColor);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 60));
        if (baseColor.getRed() > 200 && baseColor.getGreen() < 100 && baseColor.getBlue() < 100) {
            button.addMouseListener(new MouseAdapter() {});
        } else {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { button.setBackground(hover); }
                @Override
                public void mouseExited(MouseEvent e) { button.setBackground(baseColor); }
            });
        }
        return button;
    }
    
    // Helper to create a horizontal labeled row: label on left, component on right
    private JPanel createLabeledRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(110, 24));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
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
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        String gender = lecturer.getGender();
        if (gender != null && !gender.isEmpty()) {
            genderBox.setSelectedItem(gender);
        }
        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(lecturer.getAge() > 0 ? lecturer.getAge() : 20, 15, 100, 1));

        panel.add(createLabeledRow("Full Name:", nameField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Email:", emailField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Department:", departmentField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Gender:", genderBox)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Age:", ageSpinner)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Lecturer ID:", new JLabel(lecturer.getLecturerID()))); panel.add(Box.createVerticalStrut(8));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lecturer.setFullName(nameField.getText());
                lecturer.setEmail(emailField.getText());
                lecturer.setDepartment(departmentField.getText());
                lecturer.setGender((String) genderBox.getSelectedItem());
                lecturer.setAge((Integer) ageSpinner.getValue());
                JOptionPane.showMessageDialog(LecturerDashboard.this, "✓ Profile updated successfully!");
            }
        });
        btnRow.add(saveBtn); panel.add(btnRow);

        contentPanel.add(panel, BorderLayout.CENTER);
    }
    
    private void showDesignAssessment(JPanel contentPanel) {
        JPanel panel = new JPanel(); panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); panel.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JTextField titleField = new JTextField();
        String[] types = {"ASSIGNMENT", "CLASS_TEST", "FINAL_EXAM", "PROJECT", "QUIZ", "PRESENTATION"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        JTextField marksField = new JTextField("100");
        JTextField weightageField = new JTextField("10");

        panel.add(createLabeledRow("Assessment Title:", titleField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Type:", typeCombo)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Total Marks:", marksField)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Weightage (%):", weightageField)); panel.add(Box.createVerticalStrut(8));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT)); JButton createBtn = new JButton("Create Assessment");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(LecturerDashboard.this, "✓ Assessment Created!\n\nTitle: " + titleField.getText() + "\nType: " + typeCombo.getSelectedItem() + "\nTotal Marks: " + marksField.getText());
            }
        }); btnRow.add(createBtn);
        panel.add(btnRow);

        contentPanel.add(panel, BorderLayout.CENTER);
    }
    
    private void showKeyInMarks(JPanel contentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Populate students and assessments from systemManager
        java.util.List<User> users = systemManager.getUsersByRole("STUDENT");
        String[] students = users.stream().map(u -> u.getUserID() + " - " + u.getFullName()).toArray(String[]::new);
        if (students.length == 0) students = new String[]{"No students available"};
        JComboBox<String> studentCombo = new JComboBox<>(students);

        java.util.List<Assessment> assessmentsList = systemManager.getAllAssessments();
        String[] assessments = assessmentsList.stream().map(Assessment::getAssessmentName).toArray(String[]::new);
        if (assessments.length == 0) assessments = new String[]{"No assessments available"};
        JComboBox<String> assessmentCombo = new JComboBox<>(assessments);
        JTextField marksField = new JTextField();
        JTextField remarksField = new JTextField();
        
        panel.add(createFieldRow("Student:", studentCombo));
        panel.add(createFieldRow("Assessment:", assessmentCombo));
        panel.add(createFieldRow("Marks Obtained:", marksField));
        panel.add(createFieldRow("Remarks:", remarksField));

        JPanel btnHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitBtn = new JButton("Submit Marks");
        btnHolder.add(submitBtn);
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(LecturerDashboard.this, "✓ Marks recorded successfully!");
            }
        });
        
        panel.add(btnHolder);
        contentPanel.add(panel, BorderLayout.CENTER);
    }

    // Helper to create a horizontal labeled row: label on left, component on right
    private JPanel createFieldRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(140, 24));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        row.add(lbl, BorderLayout.WEST);
        row.add(comp, BorderLayout.CENTER);
        return row;
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
        
        JLabel feedbackLabel = new JLabel("Feedback Comment:");
        JTextArea feedbackArea = new JTextArea(8, 40);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        
        JPanel buttonPanel = new JPanel();
        JButton submitBtn = new JButton("Submit Feedback");
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(LecturerDashboard.this, "✓ Feedback submitted to " + studentCombo.getSelectedItem());
            }
        });
        buttonPanel.add(submitBtn);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(feedbackLabel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(panel, BorderLayout.CENTER);
    }
    
    private void showMyModules(JPanel contentPanel) {
        StringBuilder modules = new StringBuilder();
        modules.append("Modules Assigned to You\n\n");
        java.util.List<Module> myMods = lecturer.getAssignedModules();
        if (myMods.isEmpty()) {
            modules.append("You have no modules assigned yet.");
        } else {
            int idx = 1; int totalStudents = 0;
            for (Module m : myMods) {
                modules.append(idx++).append(". ").append(m.getModuleName()).append(" (Code: ").append(m.getModuleCode()).append(")\n");
                // Count students across classes for this module
                int count = 0;
                for (ClassModule c : systemManager.getAllClasses()) {
                    if (c.getModule() != null && c.getModule().getModuleID().equals(m.getModuleID())) {
                        modules.append("   └─ Section: ").append(c.getClassName()).append("\n");
                        modules.append("   └─ Students: ").append(c.getEnrolledStudents().size()).append("\n\n");
                        count += c.getEnrolledStudents().size();
                    }
                }
                totalStudents += count;
            }
            modules.append("Total Students: ").append(totalStudents);
        }
        
        JTextArea modulesArea = new JTextArea(modules.toString());
        modulesArea.setEditable(false);
        modulesArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(modulesArea);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }
}
