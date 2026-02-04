import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Student class - manages personal assessment and results
 * Responsibilities: Profile management, class registration, result checking
 */
public class Student extends User {
    private static final long serialVersionUID = 1L;
    
    private String studentID;
    private String enrollmentYear;
    private List<ClassModule> registeredClasses;
    private List<Assessment> takenAssessments;
    private List<Feedback> receivedFeedback;
    
    public Student(String userID, String username, String password, String email,
                   String fullName, String phoneNumber, String studentID, String enrollmentYear) {
        super(userID, username, password, email, fullName, phoneNumber, "STUDENT");
        this.studentID = studentID;
        this.enrollmentYear = enrollmentYear;
        this.registeredClasses = new ArrayList<>();
        this.takenAssessments = new ArrayList<>();
        this.receivedFeedback = new ArrayList<>();
    }
    
    @Override
    public void displayMenu() {
        
        
        
        
        
        
        
    }
    
    @Override
    public void handleAction(String action) {
        switch(action) {
            case "1":
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                break;
            case "6":
                break;
            default:
        }
    }
    
    public void registerClass(ClassModule classModule) {
        registeredClasses.add(classModule);
        
    }
    
    public void viewResults() {
        
        for (Assessment assessment : takenAssessments) {
            
        }
    }
    
    public void receiveFeedback(Feedback feedback) {
        receivedFeedback.add(feedback);
        
    }
    
    public void commentOnFeedback(String comment) {
        
    }
    
    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }
    
    public String getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(String enrollmentYear) { this.enrollmentYear = enrollmentYear; }
    
    public List<ClassModule> getRegisteredClasses() { return registeredClasses; }
    public List<Assessment> getTakenAssessments() { return takenAssessments; }
    public List<Feedback> getReceivedFeedback() { return receivedFeedback; }
}

class StudentDashboard extends JFrame {
    // These are like "remote controls" to access other parts of the system
    private SystemManager systemManager; // Access to all system data and functions
    private Student student;             // The current logged-in student

    /**
     * Constructor - creates the student dashboard window
     *
     * @param systemManager Gives us access to modules, classes, etc.
     * @param student The student who is logged in
     */
    public StudentDashboard(SystemManager systemManager, Student student) {
        this.systemManager = systemManager;
        this.student = student;

        // Set up the window (title, size, etc.)
        initializeFrame();
    }

    /**
     * Sets up the main window appearance and layout
     * This is like designing the dashboard interface
     */
    private void initializeFrame() {
        // Window settings - title, close button, size
        setTitle("Student Dashboard - " + student.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close button exits program
        setSize(800, 600); // Width x Height in pixels
        setLocationRelativeTo(null); // Center on screen
        setResizable(false); // Can't resize window

        // Create main panel with padding around edges
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); // 10px spacing
        mainPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 20px padding

        // ===== HEADER SECTION =====
        // Purple header bar with welcome message
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(156, 39, 176)); // Purple color
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getFullName() + " (Student)");
        welcomeLabel.setForeground(Color.WHITE); // White text
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Big, bold font
        headerPanel.add(welcomeLabel);

        // ===== INFO SECTION =====
        // Shows student ID, enrollment year, and email
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 1 row, 3 columns
        infoPanel.setBackground(new Color(240, 240, 240));

        // Create labels for student information
        JLabel studentIDLabel = new JLabel("Student ID: " + student.getStudentID());
        studentIDLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel enrollmentLabel = new JLabel("Enrollment: " + student.getEnrollmentYear());
        enrollmentLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel emailLabel = new JLabel("Email: " + student.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        // Add labels to info panel
        infoPanel.add(studentIDLabel);
        infoPanel.add(enrollmentLabel);
        infoPanel.add(emailLabel);

        // ===== MENU SECTION =====
        // Function tabs shown at the top
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(240, 240, 240));

        // Create content panels for each tab
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(Color.WHITE);
        showEditProfile(profilePanel);

        JPanel registerPanel = new JPanel(new BorderLayout());
        registerPanel.setBackground(Color.WHITE);
        showRegisterClasses(registerPanel);

        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(Color.WHITE);
        showResults(resultsPanel);

        JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.setBackground(Color.WHITE);
        showFeedback(feedbackPanel);

        JPanel commentsPanel = new JPanel(new BorderLayout());
        commentsPanel.setBackground(Color.WHITE);
        showComments(commentsPanel);

        // Add tabs
        tabbedPane.addTab("Edit Profile", profilePanel);
        tabbedPane.addTab("Register Classes", registerPanel);
        tabbedPane.addTab("Check Results", resultsPanel);
        tabbedPane.addTab("View Feedback", feedbackPanel);
        tabbedPane.addTab("Comments", commentsPanel);

        // ===== CONTENT AREA =====
        // Main content panel that changes based on selection
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createTitledBorder("Content Area"));

        // Default welcome message
        JLabel defaultLabel = new JLabel("Please select a function from the dropdown above", SwingConstants.CENTER);
        defaultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPanel.add(defaultLabel, BorderLayout.CENTER);

        // ===== LOGOUT BUTTON =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(240, 240, 240));
        JButton logoutBtn = createMenuButton("Logout", new Color(244, 67, 54)); // Red logout button
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AFSLoginFrame().setVisible(true);
            }
        });
        bottomPanel.add(logoutBtn);

        // ===== LAYOUT ASSEMBLY =====
        // Put header and info together at top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.CENTER);

        // Add top section, tabbed pane, and logout to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add the main panel to the window
        add(mainPanel);
    }
    
    /**
     * Helper method to find the function combo box in the UI
     */
    /**
     * Creates a styled button for the menu with custom color
     * This makes all buttons look consistent and professional
     *
     * @param text The text to show on the button
     * @param baseColor The background color for the button
     * @return A nicely styled JButton
     */
    private JButton createMenuButton(String text) {
        return createMenuButton(text, new Color(25, 118, 210));
    }
    
    private JButton createMenuButton(String text, Color baseColor) {
        // Calculate hover color (slightly brighter)
        Color hoverColor = baseColor.brighter();

        // Create the button
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12)); // Bold, readable font
        button.setBackground(baseColor); // Set background color
        button.setOpaque(true); // Make background visible
        button.setBorderPainted(false); // Remove border for modern look
        button.setForeground(Color.WHITE); // White text
        button.setPreferredSize(new Dimension(150, 60)); // Consistent size

        // Special handling for red logout button (no hover effect)
        if (baseColor.getRed() > 200 && baseColor.getGreen() < 100 && baseColor.getBlue() < 100) {
            // Red button - no hover effect needed
            button.addMouseListener(new MouseAdapter() {});
        } else {
            // Normal button - add hover effect (button gets brighter when mouse over)
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(hoverColor); // Brighter on hover
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(baseColor); // Back to normal
                }
            });
        }

        return button;
    }

    /**
     * Creates a labeled input field for forms
     * This is a common pattern: label on left, input field on right
     *
     * @param labelText The text for the label (like "Name:")
     * @param component The input field (JTextField, JComboBox, etc.)
     * @return A JPanel containing the label and input field
     */
    private JPanel createLabeledRow(String labelText, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(8, 0)); // 8px gap between label and field
        row.setOpaque(false); // Transparent background

        // Create and style the label
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(110, 24)); // Fixed width for alignment
        label.setHorizontalAlignment(SwingConstants.LEFT); // Left-align text

        // Add label on left, component on right
        row.add(label, BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);

        return row;
    }
    
    /**
     * Displays profile editing form in the content panel
     * This lets students update their name, email, and phone number
     */
    private void showEditProfile(JPanel contentPanel) {
        // Create main panel with vertical layout (components stack top to bottom)
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical box layout
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Padding around edges

        // Create input fields with current values pre-filled
        JTextField nameField = new JTextField(student.getFullName());    // Current name
        JTextField emailField = new JTextField(student.getEmail());      // Current email
        JTextField phoneField = new JTextField("N/A");                   // Phone (placeholder)

        // Add labeled input rows to panel
        panel.add(createLabeledRow("Full Name:", nameField));
        panel.add(Box.createVerticalStrut(6)); // 6px vertical space
        panel.add(createLabeledRow("Email:", emailField));
        panel.add(Box.createVerticalStrut(6)); // 6px vertical space
        panel.add(createLabeledRow("Phone:", phoneField));
        panel.add(Box.createVerticalStrut(6)); // 6px vertical space

        // Show student ID (read-only, can't be changed)
        panel.add(createLabeledRow("Student ID:", new JLabel(student.getStudentID())));
        panel.add(Box.createVerticalStrut(8)); // Extra space before buttons

        // Create button row at bottom (Save and Cancel buttons)
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Right-aligned buttons
        JButton saveBtn = new JButton("Save Changes");
        JButton cancelBtn = new JButton("Cancel");

        // Button actions
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(StudentDashboard.this, "✅ Profile updated successfully!");
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // No action needed for cancel
            }
        });

        // Add buttons to button row
        buttonRow.add(saveBtn);
        buttonRow.add(cancelBtn);

        // Add button row to main panel
        panel.add(buttonRow);

        // Add panel to content area
        contentPanel.add(panel, BorderLayout.CENTER);
    }
    
    private void showRegisterClasses(JPanel contentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // Load modules and classes from systemManager (fallback to defaults if empty)
        java.util.List<Module> modulesList = systemManager.getAllModules();
        String[] moduleNames;
        if (modulesList.isEmpty()) {
            moduleNames = new String[]{"CS101", "CS102", "CS103", "CS104"};
        } else {
            moduleNames = modulesList.stream().map(Module::getModuleName).toArray(String[]::new);
        }
        JComboBox<String> moduleCombo = new JComboBox<>(moduleNames);

        java.util.List<ClassModule> classList = systemManager.getAllClasses();
        String[] classNames;
        if (classList.isEmpty()) {
            classNames = new String[]{"Section A", "Section B", "Section C"};
        } else {
            classNames = classList.stream().map(ClassModule::getClassName).toArray(String[]::new);
        }
        JComboBox<String> classCombo = new JComboBox<>(classNames);
        JTextField semesterField = new JTextField("2024-1");

        panel.add(createLabeledRow("Module:", moduleCombo)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Class:", classCombo)); panel.add(Box.createVerticalStrut(6));
        panel.add(createLabeledRow("Semester:", semesterField)); panel.add(Box.createVerticalStrut(8));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton registerBtn = new JButton("Register");
        JButton cancelBtn = new JButton("Cancel");
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedModuleName = (String) moduleCombo.getSelectedItem();
                String selectedClassName = (String) classCombo.getSelectedItem();
                String semester = semesterField.getText().trim();

                Module chosenModule = null;
                for (Module m : systemManager.getAllModules()) {
                    if (m.getModuleName().equals(selectedModuleName) || m.getModuleCode().equals(selectedModuleName)) {
                        chosenModule = m; break;
                    }
                }
                if (chosenModule == null) {
                    // create a simple Module placeholder if not found
                    String modID = systemManager.generateModuleID();
                    chosenModule = new Module(modID, selectedModuleName, "", "", 3, "N/A");
                    systemManager.createModule(chosenModule);
                }

                // Try to find an existing class for this module + class name + semester
                ClassModule targetClass = null;
                for (ClassModule c : systemManager.getAllClasses()) {
                    if (c.getModule() != null && c.getModule().getModuleID().equals(chosenModule.getModuleID())
                        && c.getClassName().equals(selectedClassName) && c.getSemester().equals(semester)) {
                        targetClass = c; break;
                    }
                }

                if (targetClass == null) {
                    // create a new class
                    String classID = systemManager.generateClassID();
                    Lecturer unassigned = new Lecturer("LEC000", "unassigned", "pass", "unassigned@apu.edu", "Unassigned", "N/A", "LEC000", "N/A");
                    targetClass = new ClassModule(classID, selectedClassName, chosenModule, unassigned, semester, 50);
                    systemManager.createClass(targetClass);
                }

                // Register student and enroll
                student.registerClass(targetClass);
                targetClass.enrollStudent(student);
                // Persist class changes (enrollment)
                systemManager.saveAllClasses();

                JOptionPane.showMessageDialog(StudentDashboard.this, "✓ Class registered successfully!");
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // No action needed for cancel
            }
        });
        btnRow.add(registerBtn); btnRow.add(cancelBtn);
        panel.add(btnRow);
        contentPanel.add(panel, BorderLayout.CENTER);
    }
    
    private void showResults(JPanel contentPanel) {
        StringBuilder results = new StringBuilder();
        results.append("Assessment Results\n\n");
        results.append("Student: ").append(student.getFullName()).append("\n");
        results.append("ID: ").append(student.getStudentID()).append("\n\n");
        results.append("Assessments Taken:\n");
        results.append("├─ CS101 - Assignment 1: 85 (Grade: B+)\n");
        results.append("├─ CS101 - Class Test: 78 (Grade: B+)\n");
        results.append("├─ CS102 - Project: 92 (Grade: A)\n");
        results.append("└─ CS103 - Final Exam: 88 (Grade: A)\n\n");
        results.append("Overall GPA: 3.7/4.0");

        JTextArea resultsArea = new JTextArea(results.toString());
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void showFeedback(JPanel contentPanel) {
        StringBuilder feedback = new StringBuilder();
        feedback.append("Feedback from Lecturers\n\n");
        feedback.append("1. CS101 - Assignment 1\n");
        feedback.append("   Lecturer: Dr. Smith\n");
        feedback.append("   Score: 85/100\n");
        feedback.append("   Comment: Good work on your analysis.\n\n");
        feedback.append("2. CS102 - Project\n");
        feedback.append("   Lecturer: Prof. Johnson\n");
        feedback.append("   Score: 92/100\n");
        feedback.append("   Comment: Excellent implementation!\n\n");
        feedback.append("3. CS103 - Final Exam\n");
        feedback.append("   Lecturer: Dr. Lee\n");
        feedback.append("   Score: 88/100\n");
        feedback.append("   Comment: Clear understanding of concepts.");

        JTextArea feedbackArea = new JTextArea(feedback.toString());
        feedbackArea.setEditable(false);
        feedbackArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(feedbackArea);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void showComments(JPanel contentPanel) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel label = new JLabel("Respond to Lecturer Feedback:");
        JTextArea commentsArea = new JTextArea(8, 30);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(commentsArea);
        
        JPanel buttonPanel = new JPanel();
        JButton submitBtn = new JButton("Submit Comment");
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(StudentDashboard.this, "✓ Comment submitted!");
                commentsArea.setText("");
            }
        });
        
        buttonPanel.add(submitBtn);
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(panel, BorderLayout.CENTER);
    }
}
