import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class AdminLecturerAssignmentPanel extends JPanel {
    private SystemManager systemManager;
    private JFrame parentFrame;
    private DefaultTableModel tableModel;
    private JTable table;
    
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
        assignPanel.setBorder(BorderFactory.createTitledBorder("Assign Lecturer to Academic Leader"));
        
        JComboBox<String> lecturerBox = new JComboBox<>();
        java.util.List<User> lecturers = systemManager.getAllLecturers();
        for (User u : lecturers) {
            lecturerBox.addItem(u.getUserID() + " (" + u.getUsername() + ")");
        }
        
        JComboBox<String> leaderBox = new JComboBox<>();
        java.util.List<User> leaders = systemManager.getAllAcademicLeaders();
        for (User u : leaders) {
            leaderBox.addItem(u.getUserID() + " (" + u.getUsername() + ")");
        }
        
        assignPanel.add(createLabeledRow("Select Lecturer:", lecturerBox));
        assignPanel.add(Box.createVerticalStrut(8));
        assignPanel.add(createLabeledRow("Select Academic Leader:", leaderBox));
        assignPanel.add(Box.createVerticalStrut(8));
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton assignBtn = new JButton("Assign to Leader");
        btnPanel.add(assignBtn);
        assignPanel.add(btnPanel);
        
        // Middle section: Current assignments
        JPanel currentPanel = new JPanel(new BorderLayout());
        currentPanel.setBorder(BorderFactory.createTitledBorder("Current Lecturer-Leader Assignments"));
        
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
        
        refreshTable();
        
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
                if (lecturerBox.getItemCount() == 0 || leaderBox.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(parentFrame, "Please ensure there are lecturers and academic leaders available");
                    return;
                }
                String lecSelection = (String)lecturerBox.getSelectedItem();
                String leaderSelection = (String)leaderBox.getSelectedItem();
                String lecId = lecSelection.substring(0, lecSelection.indexOf(" "));
                String leaderId = leaderSelection.substring(0, leaderSelection.indexOf(" "));
                
                systemManager.assignLecturerToLeader(lecId, leaderId);
                JOptionPane.showMessageDialog(parentFrame, "Lecturer assigned to academic leader successfully!");
                refreshTable();
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
                String lecId = (String)table.getValueAt(r, 0);
                systemManager.unassignLecturerFromLeader(lecId);
                JOptionPane.showMessageDialog(parentFrame, "Assignment removed successfully!");
                refreshTable();
            }
        });
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        java.util.List<User> allLecturers = systemManager.getAllLecturers();
        for (User u : allLecturers) {
            Lecturer lec = (Lecturer) u;
            String leaderId = lec.getAcademicLeaderID() != null ? lec.getAcademicLeaderID() : "Unassigned";
            String leaderName = "Unassigned";
            if (lec.getAcademicLeaderID() != null) {
                User leader = systemManager.findUserByID(lec.getAcademicLeaderID());
                leaderName = leader != null ? leader.getUsername() : "Not found";
            }
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

