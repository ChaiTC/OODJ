import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.*;

public class AdminStaff extends User {
    private static final long serialVersionUID = 1L;
    private String department;
    private String staffID;
    
    public AdminStaff(String userID, String username, String password, String email,
                      String fullName, String phoneNumber, String department, String staffID) {
        super(userID, username, password, email, fullName, phoneNumber, "ADMIN_STAFF");
        this.department = department;
        this.staffID = staffID;
    }
    
    @Override
    public void displayMenu() {
    }
    
    @Override
    public void handleAction(String action) {
    }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getStaffID() { return staffID; }
    public void setStaffID(String staffID) { this.staffID = staffID; }
}
