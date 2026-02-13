import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

class AdminClassCreationPanel extends JPanel {
    // Reference to system manager for data operations
    private SystemManager systemManager;
    
    // Reference to parent frame for dialog positioning
    private JFrame parentFrame;
    
    // Table components for displaying all classes
    private DefaultTableModel tableModel;
    private JTable table;
    public AdminClassCreationPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    /**
     * Initializes the panel layout and all UI components.
     * Sets up the class creation form with schedule options and the classes table.
     */
    private void initializePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // ==================== CLASS CREATION FORM ====================
        // Top section: Form for creating new classes
        JPanel createPanel = new JPanel();
        createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.Y_AXIS));
        createPanel.setBorder(BorderFactory.createTitledBorder("Create New Class"));
        
        // Form fields
        // Class ID field - auto-generated, non-editable
        JTextField classIdField = new JTextField();
        classIdField.setEditable(false);
        JTextField classNameField = new JTextField();
        
        // Module selection dropdown (shows moduleID and name)
        JComboBox<String> moduleBox = new JComboBox<>();
        java.util.List<Module> modules = systemManager.getAllModules();
        for (Module m : modules) {
            moduleBox.addItem(m.getModuleID() + " (" + m.getModuleName() + ")");
        }
        
        // Lecturer selection dropdown
        JComboBox<String> lecturerBox = new JComboBox<>();
        java.util.List<User> lecturers = systemManager.getAllLecturers();
        for (User u : lecturers) {
           if (u instanceof Lecturer) {
             lecturerBox.addItem(u.getUserID() + " - " + u.getFullName());
    }
}
        // Capacity spinner (controls max number of students)
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(30, 1, 200, 5));
        
        // ==================== SCHEDULE TYPE SELECTION ====================
        // Radio buttons to choose between recurring or one-time schedule
        JPanel scheduleTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup scheduleGroup = new ButtonGroup();
        JRadioButton recurringRadio = new JRadioButton("Recurring (Weekly)", true);
        JRadioButton oneTimeRadio = new JRadioButton("One-time");
        scheduleGroup.add(recurringRadio);
        scheduleGroup.add(oneTimeRadio);
        scheduleTypePanel.add(recurringRadio);
        scheduleTypePanel.add(oneTimeRadio);
        
        // ==================== RECURRING SCHEDULE FIELDS ====================
        // For classes that repeat weekly (e.g., "Every Monday at 10:00")
        JPanel recurringPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        JComboBox<String> dayCombo = new JComboBox<>(daysOfWeek);
        JSpinner recurringTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor recurringTimeEditor = new JSpinner.DateEditor(recurringTimeSpinner, "HH:mm");
        recurringTimeSpinner.setEditor(recurringTimeEditor);
        recurringPanel.add(createLabeledRow("Day of Week:", dayCombo));
        recurringPanel.add(createLabeledRow("Time:", recurringTimeSpinner));
        
        // ==================== ONE-TIME SCHEDULE FIELDS ====================
        // For classes that occur only once (e.g., special events, exams)
        JPanel oneTimePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        JSpinner oneTimeTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor oneTimeTimeEditor = new JSpinner.DateEditor(oneTimeTimeSpinner, "HH:mm");
        oneTimeTimeSpinner.setEditor(oneTimeTimeEditor);
        oneTimePanel.add(createLabeledRow("Date:", dateSpinner));
        oneTimePanel.add(createLabeledRow("Time:", oneTimeTimeSpinner));
        oneTimePanel.setVisible(false);  // Hidden by default (recurring is default)
        
        // Location/room field
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
        
        // ==================== SCHEDULE SECTION ====================
        // Container for schedule type selection and schedule input fields
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
        
        // ==================== RADIO BUTTON LISTENERS ====================
        // Toggle between recurring and one-time schedule panels
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
        
        // ==================== ALL CLASSES TABLE ====================
        // Middle section: Table showing all existing classes
        JPanel allClassesPanel = new JPanel(new BorderLayout());
        allClassesPanel.setBorder(BorderFactory.createTitledBorder("All Classes"));
        
        // Table columns for class information
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
        
        // Load initial class data
        refreshTable();
        
        // ==================== DELETE SECTION ====================
        // Bottom section: Button to delete selected class
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteBtn = new JButton("Delete Selected Class");
        deletePanel.add(deleteBtn);
        
        add(createPanel, BorderLayout.NORTH);
        add(allClassesPanel, BorderLayout.CENTER);
        add(deletePanel, BorderLayout.SOUTH);
        
        // Set initial class ID
        classIdField.setText(systemManager.generateClassID());
        
        // ==================== CREATE BUTTON HANDLER ====================
        // Handle class creation with validation
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Collect form data
                String classId = classIdField.getText().trim();
                String className = classNameField.getText().trim();
                
                // Validate class name
                if (className.isEmpty()) {
                    JOptionPane.showMessageDialog(parentFrame, "Please enter a class name");
                    return;
                }
                
                // Validate that there are modules available
                if (moduleBox.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Please ensure there are modules available");
                    return;
                }
                
                // Extract module ID from selection (format: "MOD001 (Module Name)")
                String moduleSelection = (String)moduleBox.getSelectedItem();
                String moduleId = moduleSelection.substring(0, moduleSelection.indexOf(" "));
                
                int capacity = (Integer)capacitySpinner.getValue();
                String location = locationField.getText().trim();
                
                // Validate location
                if (location.isEmpty()) {
                    JOptionPane.showMessageDialog(parentFrame, "Please enter a location");
                    return;
                }
                
                // Variables to store schedule information
                String day = null;
                String time = null;
                
                // ========== GET SCHEDULE BASED ON SELECTED TYPE ==========
                if (recurringRadio.isSelected()) {
                    // Recurring schedule: day of week + time
                    day = (String)dayCombo.getSelectedItem();
                    Date timeValue = (Date)recurringTimeSpinner.getValue();
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    time = timeFormat.format(timeValue);
                } else {
                    // One-time schedule: specific date + time
                    Date dateValue = (Date)dateSpinner.getValue();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    day = dateFormat.format(dateValue);
                    
                    Date timeValue = (Date)oneTimeTimeSpinner.getValue();
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    time = timeFormat.format(timeValue);
                }
                
                // ========== GET SELECTED LECTURER ==========
                Lecturer selectedLecturer = null;
                if (lecturerBox.getSelectedIndex() >= 0 && lecturerBox.getItemCount() > 0) {
                    String selected = (String) lecturerBox.getSelectedItem();
                    String selectedLecturerID = selected.split(" - ")[0];
                    User u = systemManager.findUserByID(selectedLecturerID);
                    if (u instanceof Lecturer) {
                        selectedLecturer = (Lecturer) u;
                    }
                }
                // Create new ClassModule object with all the collected data
                ClassModule newClass = new ClassModule(
                    classId, 
                    className, 
                    moduleId, 
                    capacity, 
                    day,
                    time,
                    location,
                    selectedLecturer
                );
                
                // Save class to system
                systemManager.createClass(newClass);
                JOptionPane.showMessageDialog(parentFrame, "Class created successfully!");
                
                // Refresh table to show new class
                refreshTable();
                
                // ========== RESET FORM TO DEFAULT VALUES ==========
                // Ready for creating another class
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
        
        // ==================== DELETE BUTTON HANDLER ====================
        // Handle class deletion with confirmation
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Select a class to delete");
                    return;
                }
                // Get class details from selected row
                String classId = (String)table.getValueAt(r, 0);
                String className = (String)table.getValueAt(r, 1);
                // Confirm deletion with user
                int confirm = JOptionPane.showConfirmDialog(parentFrame, 
                    "Delete class '" + className + "' (" + classId + ")?", 
                    "Confirm", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Delete class from system
                    systemManager.deleteClass(classId);
                    JOptionPane.showMessageDialog(parentFrame, "Class deleted successfully!");
                    // Refresh table to reflect deletion
                    refreshTable();
                }
            }
        });
    }
    
    /**
     * Refreshes the classes table by loading all classes from the system manager.
     * Displays class information including schedule and assigned lecturer.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        
        java.util.List<ClassModule> classes = systemManager.getAllClasses();
        for (ClassModule c : classes) {
            // Get lecturer name or show "Unassigned"
            String lecName = "Unassigned";
            if (c.getLecturerID() != null) {
                User u = systemManager.findUserByID(c.getLecturerID());
                lecName = u != null ? u.getUsername() : "Not found";
            }
            
            // Format schedule display (handles both recurring and one-time schedules)
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
            
            // Add row to table with all class information
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
