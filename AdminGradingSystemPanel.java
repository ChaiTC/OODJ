import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminGradingSystemPanel extends JPanel {
    private SystemManager systemManager;
    private JFrame parentFrame;
    private DefaultTableModel tableModel;
    private JTable table;
    private GradingSystem gradingSystem;
    
    public AdminGradingSystemPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Load grading system from system manager
        gradingSystem = systemManager.getGradingSystem();
        
        // Top section: Grading scale table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("APU Grading Scale (double-click to edit cells)"));
        
        String[] cols = new String[] {"Min %", "Max %", "Grade", "GPA", "Classification"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add cell editors for numeric columns so they can be edited directly
        // Column 0: Min % (Integer)
        JTextField minEditor = new JTextField();
        table.getColumnModel().getColumn(0).setCellEditor(new javax.swing.DefaultCellEditor(minEditor));
        
        // Column 1: Max % (Integer)
        JTextField maxEditor = new JTextField();
        table.getColumnModel().getColumn(1).setCellEditor(new javax.swing.DefaultCellEditor(maxEditor));
        
        // Column 2: Grade (String)
        JTextField gradeEditor = new JTextField();
        table.getColumnModel().getColumn(2).setCellEditor(new javax.swing.DefaultCellEditor(gradeEditor));
        
        // Column 3: GPA (Double) - THIS IS THE KEY ONE YOU NEED!
        JTextField gpaEditor = new JTextField();
        table.getColumnModel().getColumn(3).setCellEditor(new javax.swing.DefaultCellEditor(gpaEditor));
        
        // Column 4: Classification (String)
        JTextField classEditor = new JTextField();
        table.getColumnModel().getColumn(4).setCellEditor(new javax.swing.DefaultCellEditor(classEditor));
        
        JScrollPane scroll = new JScrollPane(table);
        
        // Load initial grading scale data from gradingSystem
        loadInitialGrades();
        
        // Add listener to detect cell edits and save changes
        tableModel.addTableModelListener(new javax.swing.event.TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent e) {
                if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                    saveGradingSystemToFile();
                }
            }
        });
        
        tablePanel.add(scroll, BorderLayout.CENTER);
        
        // Bottom section: Add/Edit grade
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setBorder(BorderFactory.createTitledBorder("Add/Edit Grade"));
        
        JSpinner minSpinner = new JSpinner(new SpinnerNumberModel(80, 0, 100, 1));
        JSpinner maxSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 100, 1));
        JTextField gradeField = new JTextField();
        JSpinner gpaSpinner = new JSpinner(new SpinnerNumberModel(4.00, 0.00, 4.00, 0.10));
        JTextField classificationField = new JTextField();
        
        editPanel.add(createLabeledRow("Min Score (%):", minSpinner));
        editPanel.add(Box.createVerticalStrut(4));
        editPanel.add(createLabeledRow("Max Score (%):", maxSpinner));
        editPanel.add(Box.createVerticalStrut(4));
        editPanel.add(createLabeledRow("Grade Letter:", gradeField));
        editPanel.add(Box.createVerticalStrut(4));
        editPanel.add(createLabeledRow("GPA Points:", gpaSpinner));
        editPanel.add(Box.createVerticalStrut(4));
        editPanel.add(createLabeledRow("Classification:", classificationField));
        editPanel.add(Box.createVerticalStrut(8));
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addBtn = new JButton("Add Grade");
        JButton saveChangesBtn = new JButton("Save Changes");
        JButton deleteBtn = new JButton("Delete Selected");
        btnPanel.add(addBtn);
        btnPanel.add(saveChangesBtn);
        btnPanel.add(deleteBtn);
        editPanel.add(btnPanel);
        
        add(tablePanel, BorderLayout.CENTER);
        add(editPanel, BorderLayout.SOUTH);
        
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int minScore = (Integer) minSpinner.getValue();
                    int maxScore = (Integer) maxSpinner.getValue();
                    String grade = gradeField.getText().trim();
                    double gpa = (Double) gpaSpinner.getValue();
                    String classification = classificationField.getText().trim();
                    
                    if (grade.isEmpty() || classification.isEmpty()) {
                        JOptionPane.showMessageDialog(parentFrame, "Please fill all fields");
                        return;
                    }
                    
                    if (minScore < 0 || maxScore > 100 || minScore > maxScore) {
                        JOptionPane.showMessageDialog(parentFrame, "Invalid score range");
                        return;
                    }
                    
                    // Add to table model
                    tableModel.addRow(new Object[] { minScore, maxScore, grade, gpa, classification });
                    
                    // Add to grading system with GPA
                    String gradeID = "G" + (gradingSystem.getGrades().size() + 1);
                    GradingScale gradingScale = new GradingScale(gradeID, grade, minScore, maxScore, classification, gpa);
                    gradingSystem.addGradingScale(gradingScale);
                    
                    // Save to file immediately
                    FileManager.saveGradingSystem(gradingSystem);
                    
                    // Clear fields
                    minSpinner.setValue(80);
                    maxSpinner.setValue(100);
                    gradeField.setText("");
                    gpaSpinner.setValue(4.00);
                    classificationField.setText("");
                    
                    JOptionPane.showMessageDialog(parentFrame, "Grade added and saved successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
                }
            }
        });
        
        saveChangesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGradingSystemToFile();
                JOptionPane.showMessageDialog(parentFrame, "All changes saved successfully!");
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Select a grade to delete");
                    return;
                }
                
                int confirm = JOptionPane.showConfirmDialog(parentFrame, "Delete this grade?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Remove from grading system
                    if (r < gradingSystem.getGrades().size()) {
                        gradingSystem.getGrades().remove(r);
                    }
                    // Remove from table model
                    tableModel.removeRow(r);
                    // Save changes immediately
                    saveGradingSystemToFile();
                    JOptionPane.showMessageDialog(parentFrame, "Grade deleted and saved successfully!");
                }
            }
        });
    }
    
    
    private void saveGradingSystemToFile() {
        try {
            // Create a NEW grading system from what's in the table (not syncing with existing object)
            GradingSystem newGradingSystem = new GradingSystem("GS001", "APU Grading System", 60.0);
            newGradingSystem.getGrades().clear();
            
            System.out.println("\n=== SAVING GRADING SYSTEM ===");
            System.out.println("Table has " + tableModel.getRowCount() + " rows");
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                try {
                    // Read each cell value
                    Object minObj = tableModel.getValueAt(i, 0);
                    Object maxObj = tableModel.getValueAt(i, 1);
                    Object gradeObj = tableModel.getValueAt(i, 2);
                    Object gpaObj = tableModel.getValueAt(i, 3);
                    Object classObj = tableModel.getValueAt(i, 4);
                    
                    // Convert to proper types - handle both String and Number types
                    int minScore = 0;
                    try {
                        minScore = (minObj instanceof Number) ? ((Number) minObj).intValue() : Integer.parseInt(minObj.toString());
                    } catch (Exception e) {
                        System.err.println("Error parsing min score: " + minObj);
                        continue;
                    }
                    
                    int maxScore = 0;
                    try {
                        maxScore = (maxObj instanceof Number) ? ((Number) maxObj).intValue() : Integer.parseInt(maxObj.toString());
                    } catch (Exception e) {
                        System.err.println("Error parsing max score: " + maxObj);
                        continue;
                    }
                    
                    double gpa = 0.0;
                    try {
                        gpa = (gpaObj instanceof Number) ? ((Number) gpaObj).doubleValue() : Double.parseDouble(gpaObj.toString());
                    } catch (Exception e) {
                        System.err.println("Error parsing GPA: " + gpaObj);
                        gpa = 0.0;
                    }
                    
                    String grade = (gradeObj != null) ? gradeObj.toString() : "";
                    String classification = (classObj != null) ? classObj.toString() : "";
                    
                    if (grade.isEmpty() || classification.isEmpty()) {
                        System.err.println("Skipping row " + i + " - empty grade or classification");
                        continue;
                    }
                    
                    System.out.println("Row " + i + ": " + grade + " [" + minScore + "-" + maxScore + "] GPA=" + gpa + " (" + classification + ")");
                    
                    String gradeID = "G" + (i + 1);
                    GradingScale gradingScale = new GradingScale(gradeID, grade, minScore, maxScore, classification, gpa);
                    newGradingSystem.addGradingScale(gradingScale);
                    
                } catch (Exception e) {
                    System.err.println("Error processing row " + i + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Save the new grading system to file
            FileManager.saveGradingSystem(newGradingSystem);
            // Update the internal reference
            this.gradingSystem = newGradingSystem;
            
            System.out.println("=== SAVED " + newGradingSystem.getGrades().size() + " GRADES ===\n");
            
        } catch (Exception ex) {
            System.err.println("Error saving grading system: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void loadInitialGrades() {
        tableModel.setRowCount(0);
        // Load grading scales from the gradingSystem object as initial data
        java.util.List<GradingScale> grades = gradingSystem.getGrades();
        for (GradingScale scale : grades) {
            // Store as Object array so each cell is independent
            Object[] row = new Object[5];
            row[0] = Integer.valueOf((int) scale.getMinPercentage());  // Min %
            row[1] = Integer.valueOf((int) scale.getMaxPercentage());  // Max %
            row[2] = scale.getGradeLetter();                           // Grade
            row[3] = Double.valueOf(scale.getGPA());                   // GPA
            row[4] = scale.getDescription();                           // Classification
            tableModel.addRow(row);
        }
    }
    
    private JPanel createLabeledRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(140, 24));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        row.add(lbl, BorderLayout.WEST);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }
}
