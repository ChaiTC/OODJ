import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminLecturerAssignmentPanel extends JPanel {
    private SystemManager systemManager;
    private JFrame parentFrame;
    
    public AdminLecturerAssignmentPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // Top section: Assignment form
        JPanel assignPanel = new JPanel();
        assignPanel.setLayout(new BoxLayout(assignPanel, BoxLayout.Y_AXIS));
        assignPanel.setBorder(BorderFactory.createTitledBorder("Assign Lecturer to Class"));
        
        JComboBox<String> lecturerBox = new JComboBox<>();
        java.util.List<User> lecturers = systemManager.getAllLecturers();
        for (User u : lecturers) {
            lecturerBox.addItem(u.getUserID() + " (" + u.getUsername() + ")");
        }
        
        JComboBox<String> classBox = new JComboBox<>();
        java.util.List<ClassModule> classes = systemManager.getAllClasses();
        for (ClassModule c : classes) {
            classBox.addItem(c.getClassID() + " (" + c.getClassName() + ")");
        }
        
        assignPanel.add(createLabeledRow("Select Lecturer:", lecturerBox));
        assignPanel.add(Box.createVerticalStrut(8));
        assignPanel.add(createLabeledRow("Select Class:", classBox));
        assignPanel.add(Box.createVerticalStrut(8));
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton assignBtn = new JButton("Assign Lecturer");
        btnPanel.add(assignBtn);
        assignPanel.add(btnPanel);
        
        // Middle section: Current assignments
        JPanel currentPanel = new JPanel(new BorderLayout());
        currentPanel.setBorder(BorderFactory.createTitledBorder("Current Assignments"));
        
        String[] cols = new String[] {"Class ID", "Class Name", "Lecturer ID", "Lecturer Name"};
        java.util.List<ClassModule> allClasses = systemManager.getAllClasses();
        Object[][] data = new Object[allClasses.size()][];
        for (int i = 0; i < allClasses.size(); i++) {
            ClassModule c = allClasses.get(i);
            String lecId = c.getLecturerID() != null ? c.getLecturerID() : "Unassigned";
            String lecName = "Unassigned";
            if (c.getLecturerID() != null) {
                User u = systemManager.findUserByID(c.getLecturerID());
                lecName = u != null ? u.getUsername() : "Not found";
            }
            data[i] = new Object[] { c.getClassID(), c.getClassName(), lecId, lecName };
        }
        
        JTable table = new JTable(data, cols);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        currentPanel.add(scroll, BorderLayout.CENTER);
        
        // Bottom section: Remove assignment
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton removeBtn = new JButton("Remove Assignment");
        removePanel.add(removeBtn);
        
        add(assignPanel, BorderLayout.NORTH);
        add(currentPanel, BorderLayout.CENTER);
        add(removePanel, BorderLayout.SOUTH);
        
        assignBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lecturerBox.getItemCount() == 0 || classBox.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Please ensure there are lecturers and classes available");
                    return;
                }
                String lecSelection = (String)lecturerBox.getSelectedItem();
                String classSelection = (String)classBox.getSelectedItem();
                String lecId = lecSelection.substring(0, lecSelection.indexOf(" "));
                String classId = classSelection.substring(0, classSelection.indexOf(" "));
                
                ClassModule c = systemManager.findClassByID(classId);
                if (c != null) {
                    c.setLecturerID(lecId);
                    systemManager.updateClass(c);
                    JOptionPane.showMessageDialog(parentFrame, "Lecturer assigned successfully!");
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Class not found");
                }
            }
        });
        
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Select an assignment to remove");
                    return;
                }
                String classId = (String)table.getValueAt(r, 0);
                ClassModule c = systemManager.findClassByID(classId);
                if (c != null) {
                    c.setLecturerID(null);
                    systemManager.updateClass(c);
                    JOptionPane.showMessageDialog(parentFrame, "Assignment removed successfully!");
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Class not found");
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
