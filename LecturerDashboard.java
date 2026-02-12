import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LecturerDashboard extends JFrame {
    private SystemManager systemManager;
    private Lecturer lecturer;

    public LecturerDashboard(SystemManager systemManager, Lecturer lecturer) {
        this.systemManager = systemManager;
        
        User refreshed = systemManager.findUserByID(lecturer.getUserID());
          if (refreshed instanceof Lecturer) {
          this.lecturer = (Lecturer) refreshed;

}   else {
    this.lecturer = lecturer;
}

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

        JPanel viewFeedbackPanel = new JPanel(new BorderLayout());
        viewFeedbackPanel.setBackground(Color.WHITE);
        showViewFeedback(viewFeedbackPanel);

tabbedPane.addTab("View Feedback", viewFeedbackPanel);

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
    panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

    java.util.List<ClassModule> myClasses = new ArrayList<>();
for (ClassModule c : systemManager.getAllClasses()) {
    if (c.getLecturerID() != null && c.getLecturerID().equals(lecturer.getUserID())) {
        myClasses.add(c);
    }
}

String[] classItems = myClasses.stream()
        .map(c -> c.getClassID() + " - " + c.getClassName())
        .toArray(String[]::new);
if (classItems.length == 0) classItems = new String[]{"No classes assigned"};
JComboBox<String> classCombo = new JComboBox<>(classItems);

panel.add(createLabeledRow("Class:", classCombo));
panel.add(Box.createVerticalStrut(6));


    java.util.List<Module> modules = lecturer.getAssignedModules();

    String[] moduleItems = modules.stream()
            .map(m -> m.getModuleID() + " - " + m.getModuleName())
            .toArray(String[]::new);
    if (moduleItems.length == 0) moduleItems = new String[]{"No modules available"};
    JComboBox<String> moduleCombo = new JComboBox<>(moduleItems);

    JTextField titleField = new JTextField();
    JComboBox<String> typeCombo = new JComboBox<>(new String[]{
            "ASSIGNMENT", "CLASS_TEST", "FINAL_EXAM"
    });
    JTextField marksField = new JTextField("100");
    JTextField weightageField = new JTextField("10");

    // Due date input 
    JTextField dueField = new JTextField("2026-02-20"); // yyyy-MM-dd

    panel.add(createLabeledRow("Module:", moduleCombo)); panel.add(Box.createVerticalStrut(6));
    panel.add(createLabeledRow("Assessment Title:", titleField)); panel.add(Box.createVerticalStrut(6));
    panel.add(createLabeledRow("Type:", typeCombo)); panel.add(Box.createVerticalStrut(6));
    panel.add(createLabeledRow("Total Marks:", marksField)); panel.add(Box.createVerticalStrut(6));
    panel.add(createLabeledRow("Weightage (%):", weightageField)); panel.add(Box.createVerticalStrut(6));
    panel.add(createLabeledRow("Due Date (yyyy-MM-dd):", dueField)); panel.add(Box.createVerticalStrut(10));

    JButton createBtn = new JButton("Create Assessment");
    createBtn.addActionListener(e -> {
        try {
            if (modules.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No modules assigned yet. Academic Leader must assign modules first.");
                return;
            }

            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Assessment title cannot be empty.");
                return;
            }

            double totalMarks = Double.parseDouble(marksField.getText().trim());
            double weightage = Double.parseDouble(weightageField.getText().trim());
            if (totalMarks <= 0 || weightage <= 0) {
                JOptionPane.showMessageDialog(this, "Total marks and weightage must be > 0.");
                return;
            }

            int idx = moduleCombo.getSelectedIndex();
            Module selectedModule = modules.get(Math.max(0, idx));

            String typeStr = (String) typeCombo.getSelectedItem();
            AssessmentType at = new AssessmentType(
                    systemManager.generateAssessmentTypeID(),
                    AssessmentType.Type.valueOf(typeStr),
                    weightage,
                    totalMarks
            );

            Date dueDate = parseDate(dueField.getText().trim()); 
            
            if (myClasses.isEmpty()) {
    JOptionPane.showMessageDialog(this, "No classes assigned to you. Ask admin/leader to assign you to a class.");
    return;
}
int cIdx = classCombo.getSelectedIndex();
ClassModule selectedClass = myClasses.get(Math.max(0, cIdx));
String classID = selectedClass.getClassID();
String assessmentID = systemManager.generateAssessmentID();
Assessment assessment = new Assessment(
        assessmentID,
        title,
        at,
        selectedModule,
        lecturer,
        classID,
        dueDate
);

 
            systemManager.createAssessment(assessment);

            JOptionPane.showMessageDialog(this,
                    "✓ Assessment saved!\n\n" +
                    "ID: " + assessmentID +
                    "\nModule: " + selectedModule.getModuleName() +
                    "\nTitle: " + title +
                    "\nType: " + typeStr +
                    "\nTotal Marks: " + totalMarks +
                    "\nWeightage: " + weightage
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Marks and weightage must be numbers.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error creating assessment: " + ex.getMessage());
        }
    });

    JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnRow.add(createBtn);
    panel.add(btnRow);

    contentPanel.add(panel, BorderLayout.CENTER);
}

private Date parseDate(String yyyyMmDd) {
    
    try {
        String[] p = yyyyMmDd.split("-");
        int y = Integer.parseInt(p[0]);
        int m = Integer.parseInt(p[1]) - 1; 
        int d = Integer.parseInt(p[2]);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, y);
        cal.set(Calendar.MONTH, m);
        cal.set(Calendar.DAY_OF_MONTH, d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    } catch (Exception e) {
        return null; 
    }
}

    private void showKeyInMarks(JPanel contentPanel) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    java.util.List<User> users = systemManager.getUsersByRole("STUDENT");
    java.util.List<Student> studentsList = new ArrayList<>();
    for (User u : users) if (u instanceof Student) studentsList.add((Student) u);

    String[] students = studentsList.stream()
            .map(s -> s.getStudentID() + " - " + s.getFullName())
            .toArray(String[]::new);
    if (students.length == 0) students = new String[]{"No students available"};
    JComboBox<String> studentCombo = new JComboBox<>(students);

    java.util.List<Assessment> allAssessments = systemManager.getAllAssessments();
    
    java.util.List<Assessment> myAssessments = new ArrayList<>();
    for (Assessment a : allAssessments) {
        if (a.getCreatedBy() != null && a.getCreatedBy().getUserID().equals(lecturer.getUserID())) {
            myAssessments.add(a);
        }
    }
    if (myAssessments.isEmpty()) myAssessments.addAll(allAssessments);

    String[] assessments = myAssessments.stream()
            .map(a -> a.getAssessmentID() + " - " + a.getAssessmentName())
            .toArray(String[]::new);
    if (assessments.length == 0) assessments = new String[]{"No assessments available"};
    JComboBox<String> assessmentCombo = new JComboBox<>(assessments);

    JTextField marksField = new JTextField();

    panel.add(createLabeledRow("Student:", studentCombo)); panel.add(Box.createVerticalStrut(8));
    panel.add(createLabeledRow("Assessment:", assessmentCombo)); panel.add(Box.createVerticalStrut(8));
    panel.add(createLabeledRow("Marks Obtained:", marksField)); panel.add(Box.createVerticalStrut(12));

    JButton submitBtn = new JButton("Submit Marks");
    submitBtn.addActionListener(e -> {
        try {
            if (studentsList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No students exist.");
                return;
            }
            if (myAssessments.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No assessments exist.");
                return;
            }

            int sIdx = studentCombo.getSelectedIndex();
            int aIdx = assessmentCombo.getSelectedIndex();

            Student studentObj = studentsList.get(Math.max(0, sIdx));
            Assessment assessmentObj = myAssessments.get(Math.max(0, aIdx));

            double marks = Double.parseDouble(marksField.getText().trim());

            
            assessmentObj.recordMarks(studentObj, marks);
            boolean ok = systemManager.updateAssessment(assessmentObj);

            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "✓ Marks saved!\n" +
                        "Student: " + studentObj.getStudentID() +
                        "\nAssessment: " + assessmentObj.getAssessmentName() +
                        "\nMarks: " + marks + "/" + assessmentObj.getTotalMarks()
                );
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save marks (assessment not found).");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Marks must be a number.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving marks: " + ex.getMessage());
        }
    });

    JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnRow.add(submitBtn);
    panel.add(btnRow);

    contentPanel.add(panel, BorderLayout.CENTER);
}


   private void showProvideFeedback(JPanel contentPanel) {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Students
    java.util.List<User> users = systemManager.getUsersByRole("STUDENT");
    java.util.List<Student> studentsList = new ArrayList<>();
    for (User u : users) if (u instanceof Student) studentsList.add((Student) u);

    String[] students = studentsList.stream()
            .map(s -> s.getStudentID() + " - " + s.getFullName())
            .toArray(String[]::new);
    if (students.length == 0) students = new String[]{"No students available"};
    JComboBox<String> studentCombo = new JComboBox<>(students);

    // Assessments
    java.util.List<Assessment> assessmentsList = systemManager.getAllAssessments();
    String[] assessments = assessmentsList.stream()
            .map(a -> a.getAssessmentID() + " - " + a.getAssessmentName())
            .toArray(String[]::new);
    if (assessments.length == 0) assessments = new String[]{"No assessments available"};
    JComboBox<String> assessmentCombo = new JComboBox<>(assessments);

    JTextField suggestedMarksField = new JTextField("0");

    JPanel topPanel = new JPanel(new GridLayout(3, 2, 8, 8));
    topPanel.add(new JLabel("Select Student:"));
    topPanel.add(studentCombo);
    topPanel.add(new JLabel("Assessment:"));
    topPanel.add(assessmentCombo);
    topPanel.add(new JLabel("Suggested Marks:"));
    topPanel.add(suggestedMarksField);

    JTextArea feedbackArea = new JTextArea(8, 40);
    feedbackArea.setLineWrap(true);
    feedbackArea.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(feedbackArea);

    JButton submitBtn = new JButton("Submit Feedback");
    submitBtn.addActionListener(e -> {
        try {
            if (studentsList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No students exist.");
                return;
            }
            if (assessmentsList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No assessments exist.");
                return;
            }

            Student studentObj = studentsList.get(Math.max(0, studentCombo.getSelectedIndex()));
            Assessment assessmentObj = assessmentsList.get(Math.max(0, assessmentCombo.getSelectedIndex()));

            String content = feedbackArea.getText().trim();
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Feedback content cannot be empty.");
                return;
            }

            double suggested = Double.parseDouble(suggestedMarksField.getText().trim());

            Feedback fb = new Feedback(
                    systemManager.generateFeedbackID(),
                    assessmentObj.getAssessmentID(),
                    studentObj.getStudentID(),
                    lecturer.getUserID(),
                    content,
                    suggested
            );
            fb.setFeedbackDate(new Date());
            fb.setDelivered(true);

            systemManager.createFeedback(fb); 

            JOptionPane.showMessageDialog(this,
                    "✓ Feedback saved!\n" +
                    "Student: " + studentObj.getStudentID() +
                    "\nAssessment: " + assessmentObj.getAssessmentName()
            );

            feedbackArea.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Suggested marks must be a number.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving feedback: " + ex.getMessage());
        }
    });

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(submitBtn);

    panel.add(topPanel, BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    contentPanel.add(panel, BorderLayout.CENTER);
}

private void showViewFeedback(JPanel contentPanel) {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    DefaultListModel<Feedback> model = new DefaultListModel<>();
    JList<Feedback> list = new JList<>(model);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JTextArea details = new JTextArea();
    details.setEditable(false);
    details.setLineWrap(true);
    details.setWrapStyleWord(true);

    JButton refreshBtn = new JButton("Refresh");
    refreshBtn.addActionListener(e -> {
        model.clear();
        details.setText("");


        for (Feedback f : systemManager.getAllFeedback()) {
            
            if (f.getLecturerID() != null && f.getLecturerID().equals(lecturer.getUserID())) {
                model.addElement(f);
            }
        }

        if (!model.isEmpty()) list.setSelectedIndex(0);
        else details.setText("No feedback available for you yet.");
    });

    list.addListSelectionListener(e -> {
        Feedback f = list.getSelectedValue();
        if (f == null) return;

        String comment = (f.getComments() == null || f.getComments().trim().isEmpty())
                ? "(none yet)"
                : f.getComments();

        StringBuilder sb = new StringBuilder();
        sb.append("Feedback ID: ").append(f.getFeedbackID()).append("\n");
        sb.append("Assessment ID: ").append(f.getAssessmentID()).append("\n");
        sb.append("Student ID: ").append(f.getStudentID()).append("\n");
        sb.append("Suggested Marks: ").append(f.getSuggestedMarks()).append("\n\n");

        sb.append("Feedback Content:\n").append(f.getFeedbackContent()).append("\n\n");

        sb.append("Student Comment:\n").append(comment).append("\n");

        details.setText(sb.toString());
    });

    JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    top.add(refreshBtn);

    panel.add(top, BorderLayout.NORTH);
    panel.add(new JScrollPane(list), BorderLayout.WEST);
    panel.add(new JScrollPane(details), BorderLayout.CENTER);

    panel.getComponent(1).setPreferredSize(new Dimension(280, 0));

    contentPanel.add(panel, BorderLayout.CENTER);

    refreshBtn.doClick();
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
