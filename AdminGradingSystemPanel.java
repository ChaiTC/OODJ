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
    
    public AdminGradingSystemPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Top section: Grading scale table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("APU Grading Scale"));
        
        String[] cols = new String[] {"Min %", "Max %", "Grade", "GPA", "Classification"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        
        // Load initial grading scale data only once
        loadInitialGrades();
        
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
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
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
                    
                    // Add to table model (note: in a real app, you'd persist this)
                    tableModel.addRow(new Object[] { minScore, maxScore, grade, gpa, classification });
                    
                    // Clear fields
                    minSpinner.setValue(80);
                    maxSpinner.setValue(100);
                    gradeField.setText("");
                    gpaSpinner.setValue(4.00);
                    classificationField.setText("");
                    
                    JOptionPane.showMessageDialog(parentFrame, "Grade added successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
                }
            }
        });
        
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Select a grade to edit");
                    return;
                }
                
                int minScore = ((Number) tableModel.getValueAt(r, 0)).intValue();
                int maxScore = ((Number) tableModel.getValueAt(r, 1)).intValue();
                String grade = (String) tableModel.getValueAt(r, 2);
                double gpa = ((Number) tableModel.getValueAt(r, 3)).doubleValue();
                String classification = (String) tableModel.getValueAt(r, 4);
                
                minSpinner.setValue(minScore);
                maxSpinner.setValue(maxScore);
                gradeField.setText(grade);
                gpaSpinner.setValue(gpa);
                classificationField.setText(classification);
                
                JOptionPane.showMessageDialog(parentFrame, "Edit the values above and click 'Add Grade' to save changes");
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
                    tableModel.removeRow(r);
                    JOptionPane.showMessageDialog(parentFrame, "Grade deleted successfully!");
                }
            }
        });
    }
    
    private void refreshTable() {
        // Don't reload defaults - keep user's changes in memory
        // This method is kept for backward compatibility but doesn't clear data
    }
    
    private void loadInitialGrades() {
        tableModel.setRowCount(0);
        // Load grading scales from system manager
        // For now, we'll load the default ones
        tableModel.addRow(new Object[] { 80, 100, "A+", 4.00, "Distinction" });
        tableModel.addRow(new Object[] { 75, 79, "A", 3.70, "Distinction" });
        tableModel.addRow(new Object[] { 70, 74, "B+", 3.30, "Credit" });
        tableModel.addRow(new Object[] { 65, 69, "B", 3.00, "Credit" });
        tableModel.addRow(new Object[] { 60, 64, "C+", 2.70, "Pass" });
        tableModel.addRow(new Object[] { 55, 59, "C", 2.30, "Pass" });
        tableModel.addRow(new Object[] { 50, 54, "C-", 2.00, "Pass" });
        tableModel.addRow(new Object[] { 40, 49, "D", 1.70, "Fail (Marginal)" });
        tableModel.addRow(new Object[] { 30, 39, "F+", 1.30, "Fail" });
        tableModel.addRow(new Object[] { 20, 29, "F", 1.00, "Fail" });
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
