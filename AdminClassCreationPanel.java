import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

class AdminClassCreationPanel extends JPanel {
    private SystemManager systemManager;
    private JFrame parentFrame;
    private DefaultTableModel tableModel;
    private JTable table;
    
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
        
        JComboBox<String> lecturerBox = new JComboBox<>();
        java.util.List<User> lecturers = systemManager.getAllLecturers();
        for (User u : lecturers) {
           if (u instanceof Lecturer) {
             lecturerBox.addItem(u.getUserID() + " - " + u.getFullName());
    }
}
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(30, 1, 200, 5));
        
        // Schedule Type Selection
        JPanel scheduleTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup scheduleGroup = new ButtonGroup();
        JRadioButton recurringRadio = new JRadioButton("Recurring (Weekly)", true);
        JRadioButton oneTimeRadio = new JRadioButton("One-time");
        scheduleGroup.add(recurringRadio);
        scheduleGroup.add(oneTimeRadio);
        scheduleTypePanel.add(recurringRadio);
        scheduleTypePanel.add(oneTimeRadio);
        
        // Recurring Schedule Fields
        JPanel recurringPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        JComboBox<String> dayCombo = new JComboBox<>(daysOfWeek);
        JSpinner recurringTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor recurringTimeEditor = new JSpinner.DateEditor(recurringTimeSpinner, "HH:mm");
        recurringTimeSpinner.setEditor(recurringTimeEditor);
        recurringPanel.add(createLabeledRow("Day of Week:", dayCombo));
        recurringPanel.add(createLabeledRow("Time:", recurringTimeSpinner));
        
        // One-time Schedule Fields
        JPanel oneTimePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        JSpinner oneTimeTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor oneTimeTimeEditor = new JSpinner.DateEditor(oneTimeTimeSpinner, "HH:mm");
        oneTimeTimeSpinner.setEditor(oneTimeTimeEditor);
        oneTimePanel.add(createLabeledRow("Date:", dateSpinner));
        oneTimePanel.add(createLabeledRow("Time:", oneTimeTimeSpinner));
        oneTimePanel.setVisible(false);
        
        JTextField locationField = new JTextField();
        
        createPanel.add(createLabeledRow("Class ID:", classIdField));
        createPanel.add(Box.createVerticalStrut(4));
        createPanel.add(createLabeledRow("Class Name:", classNameField));
        createPanel.add(Box.createVerticalStrut(4));
        createPanel.add(createLabeledRow("Module:", moduleBox));
        createPanel.add(Box.createVerticalStrut(4));
        createPanel.add(createLabeledRow("Lecturer:", lecturerBox));
        createPanel.add(Box.createVerticalStrut(4));
        createPanel.add(createLabeledRow("Capacity:", capacitySpinner));
        createPanel.add(Box.createVerticalStrut(8));
        
        // Schedule section
        JPanel scheduleSection = new JPanel();
        scheduleSection.setLayout(new BoxLayout(scheduleSection, BoxLayout.Y_AXIS));
        scheduleSection.setBorder(BorderFactory.createTitledBorder("Class Schedule"));
        scheduleSection.add(scheduleTypePanel);
        scheduleSection.add(Box.createVerticalStrut(4));
        scheduleSection.add(recurringPanel);
        scheduleSection.add(oneTimePanel);
        createPanel.add(scheduleSection);
        
        createPanel.add(Box.createVerticalStrut(4));
        createPanel.add(createLabeledRow("Location:", locationField));
        createPanel.add(Box.createVerticalStrut(8));
        
        // Radio button listeners to toggle panels
        recurringRadio.addActionListener(e -> {
            recurringPanel.setVisible(true);
            oneTimePanel.setVisible(false);
            createPanel.revalidate();
            createPanel.repaint();
        });
        
        oneTimeRadio.addActionListener(e -> {
            recurringPanel.setVisible(false);
            oneTimePanel.setVisible(true);
            createPanel.revalidate();
            createPanel.repaint();
        });
        
        JPanel createBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton createBtn = new JButton("Create Class");
        createBtnPanel.add(createBtn);
        createPanel.add(createBtnPanel);
        
        // Middle section: All classes
        JPanel allClassesPanel = new JPanel(new BorderLayout());
        allClassesPanel.setBorder(BorderFactory.createTitledBorder("All Classes"));
        
        String[] cols = new String[] {"Class ID", "Class Name", "Module", "Capacity", "Schedule", "Location", "Lecturer"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        allClassesPanel.add(scroll, BorderLayout.CENTER);
        
        // Load initial data
        refreshTable();
        
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
                String location = locationField.getText().trim();
                
                String day = null;
                String time = null;
                
                // Get schedule based on selected type
                if (recurringRadio.isSelected()) {
                    // Recurring schedule
                    day = (String)dayCombo.getSelectedItem();
                    Date timeValue = (Date)recurringTimeSpinner.getValue();
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    time = timeFormat.format(timeValue);
                } else {
                    // One-time schedule
                    Date dateValue = (Date)dateSpinner.getValue();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    day = dateFormat.format(dateValue);
                    
                    Date timeValue = (Date)oneTimeTimeSpinner.getValue();
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    time = timeFormat.format(timeValue);
                }
                
                Lecturer selectedLecturer = null;
                if (lecturerBox.getSelectedIndex() >= 0 && lecturerBox.getItemCount() > 0) {
                    String selected = (String) lecturerBox.getSelectedItem();
                    String selectedLecturerID = selected.split(" - ")[0];
                    User u = systemManager.findUserByID(selectedLecturerID);
                    if (u instanceof Lecturer) {
                        selectedLecturer = (Lecturer) u;
                    }
                }
                ClassModule newClass = new ClassModule(
                    classId, 
                    className, 
                    moduleId, 
                    capacity, 
                    day,
                    time,
                    location.isEmpty() ? null : location,
                    selectedLecturer
                );
                
                systemManager.createClass(newClass);
                JOptionPane.showMessageDialog(parentFrame, "Class created successfully!");
                
                // Refresh table to show new class
                refreshTable();
                
                // Reset form
                classIdField.setText(systemManager.generateClassID());
                classNameField.setText("");
                if (moduleBox.getItemCount() > 0) {
                    moduleBox.setSelectedIndex(0);
                }
                capacitySpinner.setValue(30);
                dayCombo.setSelectedIndex(0);
                recurringTimeSpinner.setValue(new Date());
                dateSpinner.setValue(new Date());
                oneTimeTimeSpinner.setValue(new Date());
                locationField.setText("");
                recurringRadio.setSelected(true);
                recurringPanel.setVisible(true);
                oneTimePanel.setVisible(false);
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
                String className = (String)table.getValueAt(r, 1);
                int confirm = JOptionPane.showConfirmDialog(parentFrame, 
                    "Delete class '" + className + "' (" + classId + ")?", 
                    "Confirm", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    systemManager.deleteClass(classId);
                    JOptionPane.showMessageDialog(parentFrame, "Class deleted successfully!");
                    refreshTable();
                }
            }
        });
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        
        java.util.List<ClassModule> classes = systemManager.getAllClasses();
        for (ClassModule c : classes) {
            String lecName = "Unassigned";
            if (c.getLecturerID() != null) {
                User u = systemManager.findUserByID(c.getLecturerID());
                lecName = u != null ? u.getUsername() : "Not found";
            }
            
            // Format schedule display
            String schedule = "";
            if (c.getDay() != null && c.getTime() != null) {
                schedule = c.getDay() + ", " + c.getTime();
            } else if (c.getDay() != null) {
                schedule = c.getDay();
            } else if (c.getTime() != null) {
                schedule = c.getTime();
            } else {
                schedule = "N/A";
            }
            
            tableModel.addRow(new Object[] {
                c.getClassID(),
                c.getClassName(),
                c.getModuleID(),
                c.getCapacity(),
                schedule,
                c.getLocation() != null ? c.getLocation() : "N/A",
                lecName
            });
        }
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
