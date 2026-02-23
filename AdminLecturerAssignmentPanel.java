import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminLecturerAssignmentPanel extends JPanel {
    // Reference to system manager for data operations
    private SystemManager systemManager;
    private JFrame parentFrame;
    private DefaultTableModel tableModel;
    private JTable table;
    
    public AdminLecturerAssignmentPanel(SystemManager systemManager, JFrame parentFrame) {
        this.systemManager = systemManager;
        this.parentFrame = parentFrame;
        initializePanel();
    }
    
    /**
     * Initializes the panel layout and all UI components.
     * Creates the assignment form, current assignments table, and action buttons.
     */
    private void initializePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        // ==================== ASSIGNMENT FORM SECTION ====================
        // Top section: Form for creating new lecturer-to-leader assignments
        JPanel assignPanel = new JPanel();
        assignPanel.setLayout(new BoxLayout(assignPanel, BoxLayout.Y_AXIS));
        assignPanel.setBorder(BorderFactory.createTitledBorder("Assign Lecturer to Academic Leader"));
        
        // Dropdown for selecting a lecturer
        JComboBox<String> lecturerBox = new JComboBox<>();
        java.util.List<User> lecturers = systemManager.getAllLecturers();
        for (User u : lecturers) {
            lecturerBox.addItem(u.getUserID() + " (" + u.getUsername() + ")");
        }
        
        // Dropdown for selecting an academic leader
        JComboBox<String> leaderBox = new JComboBox<>();
        java.util.List<User> leaders = systemManager.getAllAcademicLeaders();
        for (User u : leaders) {
            leaderBox.addItem(u.getUserID() + " (" + u.getUsername() + ")");
        }
        
        assignPanel.add(createLabeledRow("Select Lecturer:", lecturerBox));
        assignPanel.add(Box.createVerticalStrut(8));
        assignPanel.add(createLabeledRow("Select Academic Leader:", leaderBox));
        assignPanel.add(Box.createVerticalStrut(8));
        
        // Button to perform the assignment
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton assignBtn = new JButton("Assign to Leader");
        btnPanel.add(assignBtn);
        assignPanel.add(btnPanel);
        
        // ==================== CURRENT ASSIGNMENTS TABLE ====================
        // Middle section: Table displaying all current lecturer-leader assignments
        JPanel currentPanel = new JPanel(new BorderLayout());
        currentPanel.setBorder(BorderFactory.createTitledBorder("Current Lecturer-Leader Assignments"));
        
        // Table columns: Lecturer info and their assigned leader
        String[] cols = new String[] {"Lecturer ID", "Lecturer Name", "Academic Leader ID", "Leader Name"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        
        // Load initial assignment data
        refreshTable();
        
        currentPanel.add(scroll, BorderLayout.CENTER);
        
        // ==================== REMOVE ASSIGNMENT SECTION ====================
        // Bottom section: Button to remove selected assignment
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton removeBtn = new JButton("Remove Assignment");
        removePanel.add(removeBtn);
        
        add(assignPanel, BorderLayout.NORTH);
        add(currentPanel, BorderLayout.CENTER);
        add(removePanel, BorderLayout.SOUTH);
        
        // ==================== ASSIGN BUTTON HANDLER ====================
        // Handle assignment of lecturer to academic leader
        assignBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate that both dropdowns have selections
                if (lecturerBox.getItemCount() == 0 || leaderBox.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Please ensure there are lecturers and academic leaders available");
                    return;
                }
                // Extract user IDs from the combo box selections
                String lecSelection = (String)lecturerBox.getSelectedItem();
                String leaderSelection = (String)leaderBox.getSelectedItem();
                // Parse IDs (format: "ID (Username)")
                String lecId = lecSelection.substring(0, lecSelection.indexOf(" "));
                String leaderId = leaderSelection.substring(0, leaderSelection.indexOf(" "));
                
                // Check if lecturer is already assigned
                if (systemManager.isLecturerAlreadyAssigned(lecId)) {
                    int confirm = JOptionPane.showConfirmDialog(parentFrame,
                        "This lecturer is already assigned to another leader.\nDo you want to reassign them?",
                        "Reassign Lecturer",
                        JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                
                // Perform the assignment in the system
                boolean assigned = systemManager.assignLecturerToLeader(lecId, leaderId);
                if (!assigned) {
                    JOptionPane.showMessageDialog(parentFrame, "Failed to assign lecturer. Please try again.");
                    return;
                }
                JOptionPane.showMessageDialog(parentFrame, "Lecturer assigned to academic leader successfully!");
                // Refresh table to show new assignment
                refreshTable();
            }
        });
        
        // ==================== REMOVE BUTTON HANDLER ====================
        // Handle removal of lecturer-leader assignment
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Select an assignment to remove");
                    return;
                }
                // Get lecturer ID from selected row
                String lecId = (String)table.getValueAt(r, 0);
                // Remove the assignment (sets lecturer's academicLeaderID to null)
                systemManager.unassignLecturerFromLeader(lecId);
                JOptionPane.showMessageDialog(parentFrame, "Assignment removed successfully!");
                // Refresh table to reflect change
                refreshTable();
            }
        });
    }
    
    /**
     * Refreshes the assignments table by loading all lecturers and their leader assignments.
     * Shows "Unassigned" for lecturers without an academic leader.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        java.util.List<User> allLecturers = systemManager.getAllLecturers();
        for (User u : allLecturers) {
            Lecturer lec = (Lecturer) u;
            // Get assigned leader's information or show "Unassigned"
            String leaderId = lec.getAcademicLeaderID() != null ? lec.getAcademicLeaderID() : "Unassigned";
            String leaderName = "Unassigned";
            if (lec.getAcademicLeaderID() != null) {
                User leader = systemManager.findUserByID(lec.getAcademicLeaderID());
                leaderName = leader != null ? leader.getUsername() : "Not found";
            }
            // Add row to table
            tableModel.addRow(new Object[] { 
                lec.getUserID(), 
                lec.getUsername(), 
                leaderId, 
                leaderName 
            });
        }
    }
    

    private JPanel createLabeledRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(180, 24));
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        row.add(lbl, BorderLayout.WEST);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }
}

