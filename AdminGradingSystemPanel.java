import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminGradingSystemPanel extends JPanel {
    private SystemManager systemManager;
    private JFrame parentFrame;
    
    public AdminGradingSystemPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Top section: Grading scale
        JPanel scalePanel = new JPanel();
        scalePanel.setLayout(new BoxLayout(scalePanel, BoxLayout.Y_AXIS));
        scalePanel.setBorder(BorderFactory.createTitledBorder("Grading Scale Configuration"));
        
        GradingScale scale = systemManager.getGradingScale();
        
        JTextField aField = new JTextField();
        aField.setText(String.valueOf(scale.getAMarkPercentage()));
        JTextField bField = new JTextField();
        bField.setText(String.valueOf(scale.getBMarkPercentage()));
        JTextField cField = new JTextField();
        cField.setText(String.valueOf(scale.getCMarkPercentage()));
        JTextField dField = new JTextField();
        dField.setText(String.valueOf(scale.getDMarkPercentage()));
        JTextField fField = new JTextField();
        fField.setText(String.valueOf(scale.getFMarkPercentage()));
        
        scalePanel.add(createLabeledRow("Grade A (%):", aField));
        scalePanel.add(Box.createVerticalStrut(4));
        scalePanel.add(createLabeledRow("Grade B (%):", bField));
        scalePanel.add(Box.createVerticalStrut(4));
        scalePanel.add(createLabeledRow("Grade C (%):", cField));
        scalePanel.add(Box.createVerticalStrut(4));
        scalePanel.add(createLabeledRow("Grade D (%):", dField));
        scalePanel.add(Box.createVerticalStrut(4));
        scalePanel.add(createLabeledRow("Grade F (%):", fField));
        scalePanel.add(Box.createVerticalStrut(8));
        
        JButton updateScaleBtn = new JButton("Update Scale");
        JPanel scaleBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scaleBtnPanel.add(updateScaleBtn);
        scalePanel.add(scaleBtnPanel);
        
        // Middle section: Assessment types
        JPanel typePanel = new JPanel(new BorderLayout());
        typePanel.setBorder(BorderFactory.createTitledBorder("Assessment Types"));
        
        String[] cols = new String[] {"Type ID", "Type Name", "Weight (%)"};
        java.util.List<AssessmentType> types = systemManager.getAllAssessmentTypes();
        Object[][] data = new Object[types.size()][];
        for (int i = 0; i < types.size(); i++) {
            AssessmentType t = types.get(i);
            data[i] = new Object[] { t.getTypeID(), t.getTypeName(), t.getWeight() };
        }
        
        JTable table = new JTable(data, cols);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        typePanel.add(scroll, BorderLayout.CENTER);
        
        // Bottom section: Assessment type management
        JPanel typeMgmtPanel = new JPanel();
        typeMgmtPanel.setLayout(new BoxLayout(typeMgmtPanel, BoxLayout.Y_AXIS));
        typeMgmtPanel.setBorder(BorderFactory.createTitledBorder("Manage Assessment Types"));
        
        JTextField typeNameField = new JTextField();
        JSpinner weightSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 5));
        
        typeMgmtPanel.add(createLabeledRow("Type Name:", typeNameField));
        typeMgmtPanel.add(Box.createVerticalStrut(4));
        typeMgmtPanel.add(createLabeledRow("Weight (%):", weightSpinner));
        typeMgmtPanel.add(Box.createVerticalStrut(8));
        
        JPanel typeBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton createTypeBtn = new JButton("Create Type");
        JButton deleteTypeBtn = new JButton("Delete Selected Type");
        typeBtnPanel.add(createTypeBtn);
        typeBtnPanel.add(deleteTypeBtn);
        typeMgmtPanel.add(typeBtnPanel);
        
        add(scalePanel, BorderLayout.NORTH);
        add(typePanel, BorderLayout.CENTER);
        add(typeMgmtPanel, BorderLayout.SOUTH);
        
        updateScaleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double a = Double.parseDouble(aField.getText());
                    double b = Double.parseDouble(bField.getText());
                    double c = Double.parseDouble(cField.getText());
                    double d = Double.parseDouble(dField.getText());
                    double f = Double.parseDouble(fField.getText());
                    
                    if (a < 0 || b < 0 || c < 0 || d < 0 || f < 0 || a > 100 || b > 100 || c > 100 || d > 100 || f > 100) {
                        JOptionPane.showMessageDialog(parentFrame, "Percentages must be between 0 and 100");
                        return;
                    }
                    
                    GradingScale newScale = new GradingScale(a, b, c, d, f);
                    systemManager.setGradingScale(newScale);
                    JOptionPane.showMessageDialog(parentFrame, "Grading scale updated successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Please enter valid numbers");
                }
            }
        });
        
        createTypeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = typeNameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(parentFrame, "Please enter a type name");
                    return;
                }
                int weight = (Integer)weightSpinner.getValue();
                AssessmentType type = new AssessmentType(systemManager.generateAssessmentTypeID(), name, weight);
                systemManager.addAssessmentType(type);
                JOptionPane.showMessageDialog(parentFrame, "Assessment type created successfully!");
                typeNameField.setText("");
                weightSpinner.setValue(0);
            }
        });
        
        deleteTypeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Select a type to delete");
                    return;
                }
                String typeId = (String)table.getValueAt(r, 0);
                int confirm = JOptionPane.showConfirmDialog(parentFrame, "Delete this assessment type?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    systemManager.deleteAssessmentType(typeId);
                    JOptionPane.showMessageDialog(parentFrame, "Assessment type deleted successfully!");
                }
            }
        });
    }
    
    private JPanel createLabeledRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(150, 24));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        row.add(lbl, BorderLayout.WEST);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }
}
