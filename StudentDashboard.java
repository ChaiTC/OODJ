import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class StudentDashboard extends JFrame {
    private final SystemManager systemManager;
    private final Student student;
    private JTextArea classesArea;
    private JTextArea resultsArea;
    private DefaultListModel<Feedback> feedbackModel;
    private JList<Feedback> feedbackList;
    private JTextArea feedbackDetailsArea;
    private JTextArea commentArea;

    public StudentDashboard(SystemManager systemManager, Student student) {
        this.systemManager = systemManager;
        this.student = student;
        initializeFrame();
        refreshAll();
    }

    private void initializeFrame() {
        setTitle("Student Dashboard - " + student.getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(156, 39, 176));
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getFullName() + " (Student)");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.add(makeInfoLabel("Student ID: " + student.getStudentID()));
        infoPanel.add(makeInfoLabel("Enrollment: " + student.getEnrollmentYear()));
        infoPanel.add(makeInfoLabel("Email: " + student.getEmail()));

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.CENTER);

       
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Edit Profile", buildEditProfileTab());
        tabs.addTab("My Classes", buildMyClassesTab());
        tabs.addTab("Results", buildResultsTab());
        tabs.addTab("Feedback", buildFeedbackTab());

        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(240, 240, 240));
        JButton logoutBtn = createButton("Logout", new Color(244, 67, 54));
        logoutBtn.addActionListener(e -> {
            dispose();
            new AFSLoginFrame(systemManager).setVisible(true);
        });
        bottomPanel.add(logoutBtn);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel makeInfoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        return lbl;
    }

    private JButton createButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(baseColor);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(160, 40));

        Color hover = baseColor.brighter();
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { button.setBackground(baseColor); }
        });
        return button;
    }

    private JPanel labeledRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(120, 26));
        row.add(lbl, BorderLayout.WEST);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }

    

    private JPanel buildEditProfileTab() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JTextField nameField = new JTextField(student.getFullName());
        JTextField emailField = new JTextField(student.getEmail());
        JTextField phoneField = new JTextField(student.getPhoneNumber());

        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "N/A"});
        if (student.getGender() != null && !student.getGender().isEmpty()) {
            genderBox.setSelectedItem(student.getGender());
        }

        JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(
                student.getAge() > 0 ? student.getAge() : 20, 15, 100, 1
        ));

        panel.add(labeledRow("Full Name:", nameField)); panel.add(Box.createVerticalStrut(8));
        panel.add(labeledRow("Email:", emailField)); panel.add(Box.createVerticalStrut(8));
        panel.add(labeledRow("Phone:", phoneField)); panel.add(Box.createVerticalStrut(8));
        panel.add(labeledRow("Gender:", genderBox)); panel.add(Box.createVerticalStrut(8));
        panel.add(labeledRow("Age:", ageSpinner)); panel.add(Box.createVerticalStrut(8));
        panel.add(labeledRow("Student ID:", new JLabel(student.getStudentID())));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setOpaque(false);

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(e -> {
            student.setFullName(nameField.getText().trim());
            student.setEmail(emailField.getText().trim());
            student.setPhoneNumber(phoneField.getText().trim());
            student.setGender((String) genderBox.getSelectedItem());
            student.setAge((Integer) ageSpinner.getValue());

            
            systemManager.updateUser(student);

            JOptionPane.showMessageDialog(this, "✓ Profile updated and saved!");
        });

        btnRow.add(saveBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnRow);

        return panel;
    }

    private JPanel buildMyClassesTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        classesArea = new JTextArea();
        classesArea.setEditable(false);
        classesArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshClasses());

        panel.add(new JScrollPane(classesArea), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        top.add(refreshBtn);
        panel.add(top, BorderLayout.NORTH);

        return panel;
    }

    private JPanel buildResultsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshResults());

        panel.add(new JScrollPane(resultsArea), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        top.add(refreshBtn);
        panel.add(top, BorderLayout.NORTH);

        return panel;
    }

    private JPanel buildFeedbackTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        
        feedbackModel = new DefaultListModel<>();
        feedbackList = new JList<>(feedbackModel);
        feedbackList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        
        feedbackDetailsArea = new JTextArea();
        feedbackDetailsArea.setEditable(false);
        feedbackDetailsArea.setLineWrap(true);
        feedbackDetailsArea.setWrapStyleWord(true);

        commentArea = new JTextArea(4, 20);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);

        JButton addCommentBtn = new JButton("Add Comment");
        addCommentBtn.addActionListener(e -> addCommentToSelectedFeedback());

        feedbackList.addListSelectionListener(e -> showSelectedFeedbackDetails());

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);

        rightPanel.add(new JLabel("Feedback Details:"));
        rightPanel.add(Box.createVerticalStrut(6));
        rightPanel.add(new JScrollPane(feedbackDetailsArea));
        rightPanel.add(Box.createVerticalStrut(10));

        rightPanel.add(new JLabel("Your Comment:"));
        rightPanel.add(Box.createVerticalStrut(6));
        rightPanel.add(new JScrollPane(commentArea));
        rightPanel.add(Box.createVerticalStrut(8));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setOpaque(false);
        btnRow.add(addCommentBtn);
        rightPanel.add(btnRow);

       
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshFeedback());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        top.add(refreshBtn);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(feedbackList), BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);

       
        panel.getComponent(1).setPreferredSize(new Dimension(320, 0));

        return panel;
    }


    private void refreshAll() {
        refreshClasses();
        refreshResults();
        refreshFeedback();
    }

    private List<ClassModule> getMyClasses() {
        List<ClassModule> mine = new ArrayList<>();
        for (ClassModule c : systemManager.getAllClasses()) {
            
            for (Student s : c.getEnrolledStudents()) {
                if (s.getUserID().equals(student.getUserID())) {
                    mine.add(c);
                    break;
                }
            }
        }
        return mine;
    }

    private void refreshClasses() {
        List<ClassModule> mine = getMyClasses();
        StringBuilder sb = new StringBuilder();
        sb.append("My Registered Classes\n\n");
        if (mine.isEmpty()) {
            sb.append("No classes registered yet.\n");
            sb.append("Ask Admin or your academic leader to create classes, then register via the system.\n");
        } else {
            for (ClassModule c : mine) {
                sb.append("- ").append(c.getClassName())
                  .append(" | Module: ").append(c.getModule() != null ? c.getModule().getModuleName() : c.getModuleID())
                  .append(" | Lecturer: ").append(c.getLecturerID() != null ? c.getLecturerID() : "UNASSIGNED")
                  .append("\n");
            }
        }
        classesArea.setText(sb.toString());
    }

    private void refreshResults() {
        String sid = student.getStudentID();
        List<ClassModule> mine = getMyClasses();

        List<String> myModuleIDs = new ArrayList<>();
        for (ClassModule c : mine) {
            if (c.getModule() != null) myModuleIDs.add(c.getModule().getModuleID());
            else if (c.getModuleID() != null) myModuleIDs.add(c.getModuleID());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Assessment Results\n\n");
        sb.append("Student: ").append(student.getFullName()).append("\n");
        sb.append("Student ID: ").append(sid).append("\n\n");

        boolean foundAny = false;

        for (Assessment a : systemManager.getAllAssessments()) {
            
            String aModuleId = (a.getModule() != null) ? a.getModule().getModuleID() : null;
            if (!myModuleIDs.isEmpty() && aModuleId != null && !myModuleIDs.contains(aModuleId)) {
                continue;
            }

             foundAny = true;

    sb.append("- ")
      .append(a.getModule() != null ? a.getModule().getModuleName() : "Module")
      .append(" | ")
      .append(a.getAssessmentName())
      .append(" | Due: ")
      .append(a.getDueDate() != null ? a.getDueDate() : "N/A")
      .append("\n");

    Double mark = a.getStudentMarks(sid);
    if (mark != null) {
        sb.append("   Marks: ")
          .append(mark)
          .append("/")
          .append(a.getTotalMarks())
          .append("\n");
    } else {
        sb.append("   Marks: (not released yet)\n");
    }

    sb.append("\n");
}

if (!foundAny) {
    sb.append("No assessments found in the system.\n");
}
    

        if (!foundAny) {
            sb.append("No marks recorded yet.\n");
            sb.append("Once your lecturer keys in marks, they will appear here.\n");
        }

        resultsArea.setText(sb.toString());
    }

    private void refreshFeedback() {
        feedbackModel.clear();
        List<Feedback> list = systemManager.getStudentFeedback(student.getStudentID());

        if (list.isEmpty()) {
            
            feedbackDetailsArea.setText("No feedback received yet.\nOnce lecturers submit feedback, it will appear here.");
            return;
        }

        for (Feedback f : list) feedbackModel.addElement(f);
        feedbackList.setSelectedIndex(0);
    }

    private void showSelectedFeedbackDetails() {
        Feedback selected = feedbackList.getSelectedValue();
        if (selected == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Feedback ID: ").append(selected.getFeedbackID()).append("\n");
        sb.append("Assessment ID: ").append(selected.getAssessmentID()).append("\n");
        sb.append("Lecturer ID: ").append(selected.getLecturerID()).append("\n");
        sb.append("Suggested Marks: ").append(selected.getSuggestedMarks()).append("\n\n");
        sb.append("Feedback:\n").append(selected.getFeedbackContent()).append("\n\n");

        String existingComment = selected.getComments();
        sb.append("Student Comment:\n").append(existingComment != null ? existingComment : "(none)").append("\n");

        feedbackDetailsArea.setText(sb.toString());
        commentArea.setText(existingComment != null ? existingComment : "");
    }

    private void addCommentToSelectedFeedback() {
        Feedback selected = feedbackList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a feedback item first.");
            return;
        }

        String comment = commentArea.getText().trim();
        if (comment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Comment cannot be empty.");
            return;
        }

        selected.addStudentComment(comment);

        JOptionPane.showMessageDialog(this, "Comment saved.\n(If you want persistence, extend feedback.txt format.)");
        showSelectedFeedbackDetails();
    }

    private List<String> getMyClassIDs() {
    List<String> ids = new ArrayList<>();
    for (ClassModule c : systemManager.getAllClasses()) {
        for (Student s : c.getEnrolledStudents()) {
            if (s.getUserID().equals(student.getUserID())) {
                ids.add(c.getClassID());
                break;
            }
        }
    }
    return ids;
}

}

