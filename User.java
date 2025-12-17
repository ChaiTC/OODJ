import java.io.Serializable;
import java.util.Date;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userID;        // Unique ID like "STU001" or "LEC001"
    private String username;      // Login username (must be unique)
    private String password;      // Login password
    private String email;         // Email address
    private String fullName;      // Full display name
    private String phoneNumber;   // Phone number
    private String role;          // "STUDENT", "LECTURER", "ACADEMIC_LEADER", or "ADMIN_STAFF"
    private Date createdDate;     // When account was created
    private boolean isActive;     // Is account active (can login) or disabled

    /**
     * Constructor - creates a new User
     * This runs when we create any type of user (Student, Lecturer, etc.)
     */
    public User(String userID, String username, String password, String email,
                String fullName, String phoneNumber, String role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.createdDate = new Date();  // Current date/time
        this.isActive = true;           // New accounts start as active
    }

    public abstract void displayMenu();
    public abstract void handleAction(String action);

    // ===== GETTER METHODS =====
    // These let other classes read the user's information
    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }
    public Date getCreatedDate() { return createdDate; }
    public boolean isActive() { return isActive; }

    // ===== SETTER METHODS =====
    // These let other classes change the user's information
    public void setUserID(String userID) { this.userID = userID; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setRole(String role) { this.role = role; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
}
