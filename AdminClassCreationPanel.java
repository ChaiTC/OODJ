import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminClassCreationPanel extends JPanel {
    private SystemManager systemManager;
    private JFrame parentFrame;
    
    public AdminClassCreationPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Top section: Create class form
        JPanel createPanel = new JPanel();
        createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.Y_AXIS));
        createPanel.setBorder(BorderFactory.createTitledBorder("Create New Class"));
        
        JTextField classIdField = new JTextField();
        classIdField.setEditable(false);
        JTextField classNameField = new JTextField();
        
        JComboBox<String> moduleBox = new JComboBox<>();
        java.util.List<Module> modules = systemManager.getAllModules();
        for (Module m : modules) {
            moduleBox.addItem(m.getModuleID() + " (" + m.getModuleName() + ")");
        }
        
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(30, 1, 200, 5));
        JTextField dayField = new JTextField();
        dayField.setToolTipText("e.g., Monday");
        JTextField timeField = new JTextField();
        timeField.setToolTipText("e.g., 09:00");
        JTextField locationField = new JTextField();
        
        createPanel.add(createLabeledRow("Class ID:", classIdField));
        createPanel.add(Box.createVerticalStrut(4));
        createPanel.add(createLabeledRow("Class Name:", classNameField));
        createPanel.add(Box.createVerticalStrut(4));
        createPanel.add(createLabeledRow("Module:", moduleBox));
        createPanel.add(Box.createVerticalStrut(4));
        createPanel.add(createLabeledRow("Capacity:", capacitySpinner));
        createPanel.add(Box.createVerticalStrut(4));
        createPanel.add(createLabeledRow("Day:", dayField));
        createPanel.add(Box.createVerticalStrut(4));
        createPanel.add(createLabeledRow("Time:", timeField));
        createPanel.add(Box.createVerticalStrut(4));
        createPanel.add(createLabeledRow("Location:", locationField));
        createPanel.add(Box.createVerticalStrut(8));
        
        JPanel createBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton createBtn = new JButton("Create Class");
        createBtnPanel.add(createBtn);
        createPanel.add(createBtnPanel);
        
        // Middle section: All classes
        JPanel allClassesPanel = new JPanel(new BorderLayout());
        allClassesPanel.setBorder(BorderFactory.createTitledBorder("All Classes"));
        
        String[] cols = new String[] {"Class ID", "Class Name", "Module", "Capacity", "Day", "Time", "Location", "Lecturer"};
        java.util.List<ClassModule> classes = systemManager.getAllClasses();
        Object[][] data = new Object[classes.size()][];
        for (int i = 0; i < classes.size(); i++) {
            ClassModule c = classes.get(i);
            String lecName = "Unassigned";
            if (c.getLecturerID() != null) {
                User u = systemManager.findUserByID(c.getLecturerID());
                lecName = u != null ? u.getUsername() : "Not found";
            }
            data[i] = new Object[] { 
                c.getClassID(), 
                c.getClassName(), 
                c.getModuleID(),
                c.getCapacity(),
                c.getDay() != null ? c.getDay() : "N/A",
                c.getTime() != null ? c.getTime() : "N/A",
                c.getLocation() != null ? c.getLocation() : "N/A",
                lecName
            };
        }
        
        JTable table = new JTable(data, cols);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        allClassesPanel.add(scroll, BorderLayout.CENTER);
        
        // Bottom section: Delete class
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteBtn = new JButton("Delete Selected Class");
        deletePanel.add(deleteBtn);
        
        add(createPanel, BorderLayout.NORTH);
        add(allClassesPanel, BorderLayout.CENTER);
        add(deletePanel, BorderLayout.SOUTH);
        
        classIdField.setText(systemManager.generateClassID());
        
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String classId = classIdField.getText().trim();
                String className = classNameField.getText().trim();
                
                if (className.isEmpty()) {
                    JOptionPane.showMessageDialog(parentFrame, "Please enter a class name");
                    return;
                }
                
                if (moduleBox.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Please ensure there are modules available");
                    return;
                }
                
                String moduleSelection = (String)moduleBox.getSelectedItem();
                String moduleId = moduleSelection.substring(0, moduleSelection.indexOf(" "));
                
                int capacity = (Integer)capacitySpinner.getValue();
                String day = dayField.getText().trim();
                String time = timeField.getText().trim();
                String location = locationField.getText().trim();
                
                ClassModule newClass = new ClassModule(
                    classId, 
                    className, 
                    moduleId, 
                    capacity, 
                    day.isEmpty() ? null : day,
                    time.isEmpty() ? null : time,
                    location.isEmpty() ? null : location,
                    null
                );
                
                systemManager.createClass(newClass);
                JOptionPane.showMessageDialog(parentFrame, "Class created successfully!");
                
                // Reset form
                classIdField.setText(systemManager.generateClassID());
                classNameField.setText("");
                moduleBox.setSelectedIndex(0);
                capacitySpinner.setValue(30);
                dayField.setText("");
                timeField.setText("");
                locationField.setText("");
            }
        });
        
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Select a class to delete");
                    return;
                }
                String classId = (String)table.getValueAt(r, 0);
                int confirm = JOptionPane.showConfirmDialog(parentFrame, "Delete class " + classId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    systemManager.deleteClass(classId);
                    JOptionPane.showMessageDialog(parentFrame, "Class deleted successfully!");
                }
            }
        });
    }
    
    private JPanel createLabeledRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(120, 24));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        row.add(lbl, BorderLayout.WEST);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }
}
