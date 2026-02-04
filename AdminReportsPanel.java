import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminReportsPanel extends JPanel {
    private SystemManager systemManager;
    private JFrame parentFrame;
    
    public AdminReportsPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Left section: Report options
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Report Options"));
        optionsPanel.setPreferredSize(new Dimension(150, 300));
        
        JButton userReportBtn = new JButton("User Summary");
        userReportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.add(userReportBtn);
        optionsPanel.add(Box.createVerticalStrut(8));
        
        JButton classReportBtn = new JButton("Class Summary");
        classReportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.add(classReportBtn);
        optionsPanel.add(Box.createVerticalStrut(8));
        
        JButton moduleReportBtn = new JButton("Module Summary");
        moduleReportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.add(moduleReportBtn);
        optionsPanel.add(Box.createVerticalStrut(8));
        
        JButton assessmentReportBtn = new JButton("Assessment Summary");
        assessmentReportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.add(assessmentReportBtn);
        optionsPanel.add(Box.createVerticalStrut(8));
        
        JButton enrollmentReportBtn = new JButton("Enrollment Report");
        enrollmentReportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsPanel.add(enrollmentReportBtn);
        optionsPanel.add(Box.createVerticalGlue());
        
        // Right section: Report display
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBorder(BorderFactory.createTitledBorder("Report Display"));
        
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scroll = new JScrollPane(reportArea);
        reportPanel.add(scroll, BorderLayout.CENTER);
        
        add(optionsPanel, BorderLayout.WEST);
        add(reportPanel, BorderLayout.CENTER);
        
        userReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder report = new StringBuilder();
                report.append("=== USER SUMMARY REPORT ===\n");
                report.append("Generated: ").append(new java.util.Date()).append("\n\n");
                
                java.util.List<User> users = systemManager.getAllUsers();
                Map<String, Integer> roleCount = new HashMap<>();
                for (User u : users) {
                    String role = u.getRole();
                    roleCount.put(role, roleCount.getOrDefault(role, 0) + 1);
                }
                
                report.append("Total Users: ").append(users.size()).append("\n");
                for (String role : roleCount.keySet()) {
                    report.append("  ").append(role).append(": ").append(roleCount.get(role)).append("\n");
                }
                
                report.append("\n--- User List ---\n");
                for (User u : users) {
                    report.append("ID: ").append(u.getUserID()).append(" | ");
                    report.append("Username: ").append(u.getUsername()).append(" | ");
                    report.append("Name: ").append(u.getFullName()).append(" | ");
                    report.append("Role: ").append(u.getRole()).append("\n");
                }
                
                reportArea.setText(report.toString());
            }
        });
        
        classReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder report = new StringBuilder();
                report.append("=== CLASS SUMMARY REPORT ===\n");
                report.append("Generated: ").append(new java.util.Date()).append("\n\n");
                
                java.util.List<ClassModule> classes = systemManager.getAllClasses();
                report.append("Total Classes: ").append(classes.size()).append("\n\n");
                
                report.append("--- Class Details ---\n");
                for (ClassModule c : classes) {
                    report.append("Class ID: ").append(c.getClassID()).append("\n");
                    report.append("  Name: ").append(c.getClassName()).append("\n");
                    report.append("  Module: ").append(c.getModuleID()).append("\n");
                    report.append("  Capacity: ").append(c.getCapacity()).append("\n");
                    report.append("  Enrolled: ").append(c.getEnrolledStudents().size()).append(" students\n");
                    report.append("  Schedule: ").append(c.getDay() != null ? c.getDay() : "N/A").append(" at ")
                           .append(c.getTime() != null ? c.getTime() : "N/A").append("\n");
                    report.append("  Location: ").append(c.getLocation() != null ? c.getLocation() : "N/A").append("\n");
                    report.append("  Lecturer: ");
                    if (c.getLecturerID() != null) {
                        User u = systemManager.findUserByID(c.getLecturerID());
                        report.append(u != null ? u.getUsername() : "Not found");
                    } else {
                        report.append("Unassigned");
                    }
                    report.append("\n\n");
                }
                
                reportArea.setText(report.toString());
            }
        });
        
        moduleReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder report = new StringBuilder();
                report.append("=== MODULE SUMMARY REPORT ===\n");
                report.append("Generated: ").append(new java.util.Date()).append("\n\n");
                
                java.util.List<Module> modules = systemManager.getAllModules();
                report.append("Total Modules: ").append(modules.size()).append("\n\n");
                
                report.append("--- Module Details ---\n");
                for (Module m : modules) {
                    report.append("Module ID: ").append(m.getModuleID()).append("\n");
                    report.append("  Name: ").append(m.getModuleName()).append("\n");
                    report.append("  Code: ").append(m.getModuleCode()).append("\n");
                    report.append("  Credits: ").append(m.getCredits()).append("\n");
                    report.append("  Classes: ");
                    java.util.List<ClassModule> classes = systemManager.getAllClasses();
                    int classCount = 0;
                    for (ClassModule c : classes) {
                        if (c.getModuleID().equals(m.getModuleID())) classCount++;
                    }
                    report.append(classCount).append("\n\n");
                }
                
                reportArea.setText(report.toString());
            }
        });
        
        assessmentReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder report = new StringBuilder();
                report.append("=== ASSESSMENT SUMMARY REPORT ===\n");
                report.append("Generated: ").append(new java.util.Date()).append("\n\n");
                
                java.util.List<Assessment> assessments = systemManager.getAllAssessments();
                report.append("Total Assessments: ").append(assessments.size()).append("\n\n");
                
                report.append("--- Assessment Details ---\n");
                for (Assessment a : assessments) {
                    report.append("Assessment ID: ").append(a.getAssessmentID()).append("\n");
                    report.append("  Class: ").append(a.getClassID()).append("\n");
                    report.append("  Title: ").append(a.getTitle()).append("\n");
                    report.append("  Type: ").append(a.getAssessmentType()).append("\n");
                    report.append("  Status: ").append(a.getStatus()).append("\n");
                    report.append("  Marks: ").append(a.getTotalMarks()).append("\n");
                    report.append("  Students Graded: ").append(a.getStudentMarks().size()).append("\n\n");
                }
                
                reportArea.setText(report.toString());
            }
        });
        
        enrollmentReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder report = new StringBuilder();
                report.append("=== ENROLLMENT REPORT ===\n");
                report.append("Generated: ").append(new java.util.Date()).append("\n\n");
                
                java.util.List<ClassModule> classes = systemManager.getAllClasses();
                int totalEnrolled = 0;
                
                report.append("--- Enrollment by Class ---\n");
                for (ClassModule c : classes) {
                    report.append(c.getClassID()).append(" (").append(c.getClassName()).append("): ");
                    int enrolled = c.getEnrolledStudents().size();
                    report.append(enrolled).append(" / ").append(c.getCapacity()).append(" students\n");
                    totalEnrolled += enrolled;
                }
                
                report.append("\nTotal Enrolled: ").append(totalEnrolled).append("\n");
                
                report.append("\n--- Enrollment by Student ---\n");
                java.util.List<User> students = systemManager.getAllStudents();
                for (User u : students) {
                    report.append(u.getUsername()).append(" (").append(u.getUserID()).append("): ");
                    int classCount = 0;
                    for (ClassModule c : classes) {
                        if (c.getEnrolledStudents().contains(u.getUserID())) classCount++;
                    }
                    report.append(classCount).append(" classes\n");
                }
                
                reportArea.setText(report.toString());
            }
        });
    }
}
