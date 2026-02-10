import java.util.*;

/**
 * SystemManager class - manages overall system operations
 *
 * This is like the "brain" of our application. It handles:
 * - User accounts (students, lecturers, admins)
 * - Course modules and classes
 * - Assessments and feedback
 * - Data loading and saving
 *
 * Think of it as a central manager that coordinates everything!
 */
public class SystemManager {

    // These are like storage boxes for different types of data
    private List<User> users;           // All user accounts (students, lecturers, admins)
    private List<Module> modules;       // All course modules (like CS101, MATH101)
    private List<ClassModule> classes;  // All class sections (like CS101-SectionA)
    private List<Assessment> assessments; // All assignments, tests, projects
    private List<Feedback> feedbackList; // All feedback given to students
    private GradingSystem gradingSystem; // How we calculate grades
    private User currentUser;          // Who is currently logged in

    /**
     * Constructor - runs when we create a SystemManager
     * Sets up empty lists and loads data from files
     */
    public SystemManager() {
        // Initialize empty lists (like empty shopping carts)
        this.users = new ArrayList<>();
        this.modules = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.assessments = new ArrayList<>();
        this.feedbackList = new ArrayList<>();

        // Create default grading system (60% to pass)
        this.gradingSystem = new GradingSystem("GS001", "APU Grading System", 60);

        // No one is logged in yet
        this.currentUser = null;

        // Load all saved data from files
        loadAllData();
    }

    /**
     * Load all data from files
     * This is called when the system starts up
     */
    public void loadAllData() {
        // Load users first (students, lecturers, admins)
        users = FileManager.loadAllUsers();

        // Load course modules (like CS101, MATH101)
        modules = FileManager.loadAllModules();

        // Load classes last - needs modules and users to be loaded first
        // because classes reference modules and lecturers
        classes = FileManager.loadAllClasses(modules, users);

        // Load assessments
        assessments = FileManager.loadAllAssessments(modules, users);
        // Load feedback data
        feedbackList = FileManager.loadAllFeedback();
        // Load grading system if exists
        GradingSystem loadedGrading = FileManager.loadGradingSystem();
        if (loadedGrading != null) {
            this.gradingSystem = loadedGrading;
        }

    }
    
    /**
     * User registration - creates a new user account
     *
     * @param user The new user to register
     * @return true if registration successful, false if username already exists
     */
    public boolean registerUser(User user) {
        // Check if username is already taken (like checking if email already exists)
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {

                return false; // Can't register with duplicate username
            }
        }

        // Username is available, add the user
        users.add(user);

        // Save to file so it persists between program runs
        FileManager.saveUser(user);


        return true;
    }

    /**
     * User login - checks username and password
     *
     * @param username The username to check
     * @param password The password to check
     * @return User object if login successful, null if failed
     */
    public User authenticateUser(String username, String password) {
        // Loop through all users to find matching username and password
        for (User user : users) {
            // Check if this user matches the login attempt
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {

                // Found matching user, now check if account is approved by admin
                if (!user.isApproved()) {
                    // Account not approved yet
                    return null;
                }

                // Check if account is active
                if (user.isActive()) {
                    // Login successful!
                    currentUser = user;

                    return user;
                } else {
                    // Account is disabled
                    return null;
                }
            }
        }

        // No matching username/password found

        return null;
    }
    
    /**
     * Create new module
     */
    public void createModule(Module module) {
        modules.add(module);
        FileManager.saveModule(module);

    }
    
    /**
     * Create new class
     */
    public void createClass(ClassModule classModule) {
        classes.add(classModule);
        FileManager.saveClass(classModule);

    }

    /**
     * Persist all classes to disk (overwrite)
     */
    public void saveAllClasses() {
        FileManager.saveAllClasses(this.classes);
    }
    
    /**
     * Get all modules
     */
    public List<Module> getAllModules() {
        return new ArrayList<>(modules);
    }
    
    /**
     * Get all users by role
     */
    public List<User> getUsersByRole(String role) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals(role)) {
                result.add(user);
            }
        }
        return result;
    }
    
    /**
     * Create and save assessment
     */
    public void createAssessment(Assessment assessment) {
        assessments.add(assessment);
        FileManager.saveAssessment(assessment);

    }
    
    /**
     * Create and save feedback
     */
    public void createFeedback(Feedback feedback) {
        feedbackList.add(feedback);
        FileManager.saveFeedback(feedback);

    }
    
    /**
     * Get feedback for student
     */
    public List<Feedback> getStudentFeedback(String studentID) {
        List<Feedback> studentFeedback = new ArrayList<>();
        for (Feedback f : feedbackList) {
            if (f.getStudentID().equals(studentID)) {
                studentFeedback.add(f);
            }
        }
        return studentFeedback;
    }
    
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }
    
    public GradingSystem getGradingSystem() { return gradingSystem; }
    
    public List<User> getAllUsers() { return new ArrayList<>(users); }
    public List<ClassModule> getAllClasses() { return new ArrayList<>(classes); }
    public List<Assessment> getAllAssessments() { return new ArrayList<>(assessments); }
    public List<Feedback> getAllFeedback() { return new ArrayList<>(feedbackList); }

    /**
     * Find a user by their user ID
     */
    public User findUserByID(String userID) {
        for (User u : users) {
            if (u.getUserID().equals(userID)) return u;
        }
        return null;
    }

    /**
     * Update an existing user (by userID). Returns true if updated.
     */
    public boolean updateUser(User updated) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserID().equals(updated.getUserID())) {
                users.set(i, updated);
                FileManager.saveAllUsers(users);
                return true;
            }
        }
        return false;
    }

    /**
     * Delete a user by ID
     */
    public boolean deleteUser(String userID) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserID().equals(userID)) {
                users.remove(i);
                FileManager.saveAllUsers(users);
                return true;
            }
        }
        return false;
    }

    /**
     * Generate a user ID based on role with prefix and sequential numbering
     */
    public String generateUserID(String role) {
        String prefix = "USR";
        switch (role.toLowerCase()) {
            case "admin staff":
            case "admin_staff":
            case "admin":
                prefix = "ADM"; break;
            case "academic leader":
            case "academic_leader":
            case "leader":
                prefix = "ACL"; break;
            case "lecturer":
                prefix = "LEC"; break;
            case "student":
                prefix = "STU"; break;
            default:
                prefix = "USR"; break;
        }
        int max = 0;
        for (User u : users) {
            if (u.getUserID() != null && u.getUserID().startsWith(prefix)) {
                try {
                    String num = u.getUserID().substring(prefix.length());
                    int v = Integer.parseInt(num);
                    if (v > max) max = v;
                } catch (Exception ignored) {}
            }
        }
        int next = max + 1;
        return String.format("%s%03d", prefix, next);
    }

    /**
     * Generate a staff ID like STF001
     * Checks all staff types: AdminStaff, Lecturer, AcademicLeader
     */
    public String generateStaffID() {
        String prefix = "STF";
        int max = 0;
        for (User u : users) {
            String staffId = null;
            
            if (u instanceof AdminStaff) {
                staffId = ((AdminStaff) u).getStaffID();
            } else if (u instanceof Lecturer) {
                staffId = ((Lecturer) u).getStaffID();
            } else if (u instanceof AcademicLeader) {
                staffId = ((AcademicLeader) u).getStaffID();
            }
            
            if (staffId != null && staffId.startsWith(prefix)) {
                try {
                    String num = staffId.substring(prefix.length());
                    int v = Integer.parseInt(num);
                    if (v > max) max = v;
                } catch (Exception ignored) {}
            }
        }
        int next = max + 1;
        return String.format("%s%03d", prefix, next);
    }

    /**
     * Generate a module ID like MOD001
     */
    public String generateModuleID() {
        String prefix = "MOD";
        int max = 0;
        for (Module m : modules) {
            if (m.getModuleID() != null && m.getModuleID().startsWith(prefix)) {
                try {
                    String num = m.getModuleID().substring(prefix.length());
                    int v = Integer.parseInt(num);
                    if (v > max) max = v;
                } catch (Exception ignored) {}
            }
        }
        int next = max + 1;
        return String.format("%s%03d", prefix, next);
    }

    /**
     * Generate a class ID like CL001
     */
    public String generateClassID() {
        String prefix = "CL";
        int max = 0;
        for (ClassModule c : classes) {
            if (c.getClassID() != null && c.getClassID().startsWith(prefix)) {
                try {
                    String num = c.getClassID().substring(prefix.length());
                    int v = Integer.parseInt(num);
                    if (v > max) max = v;
                } catch (Exception ignored) {}
            }
        }
        int next = max + 1;
        return String.format("%s%03d", prefix, next);
    }
    
    /**
     * Get all lecturers
     */
    public List<User> getAllLecturers() {
        return getUsersByRole("LECTURER");
    }
    
    /**
     * Get all students
     */
    public List<User> getAllStudents() {
        return getUsersByRole("STUDENT");
    }
    
    /**
     * Get all academic leaders
     */
    public List<User> getAllAcademicLeaders() {
        return getUsersByRole("ACADEMIC_LEADER");
    }
    
    /**
     * Assign a lecturer to an academic leader
     */
    public void assignLecturerToLeader(String lecturerID, String leaderID) {
        User user = findUserByID(lecturerID);
        if (user instanceof Lecturer) {
            Lecturer lec = (Lecturer) user;
            lec.setAcademicLeaderID(leaderID);
            updateUser(lec);
        }
    }
    
    /**
     * Unassign a lecturer from their academic leader
     */
    public void unassignLecturerFromLeader(String lecturerID) {
        User user = findUserByID(lecturerID);
        if (user instanceof Lecturer) {
            Lecturer lec = (Lecturer) user;
            lec.setAcademicLeaderID(null);
            updateUser(lec);
        }
    }
    
    /**
     * Find a class by its ID
     */
    public ClassModule findClassByID(String classID) {
        for (ClassModule c : classes) {
            if (c.getClassID().equals(classID)) return c;
        }
        return null;
    }
    
    /**
     * Update an existing class
     */
    public void updateClass(ClassModule updated) {
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getClassID().equals(updated.getClassID())) {
                classes.set(i, updated);
                FileManager.saveAllClasses(classes);
                return;
            }
        }
    }
    
    /**
     * Delete a class by ID
     */
    public void deleteClass(String classID) {
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getClassID().equals(classID)) {
                classes.remove(i);
                FileManager.saveAllClasses(classes);
                return;
            }
        }
    }
    
    /**
     * Get the grading scale
     */
    public GradingScale getGradingScale() {
        return new GradingScale("GR001", "A", 80, 100, "Excellent");
    }
    
    /**
     * Set the grading scale
     */
    public void setGradingScale(GradingScale scale) {
        // Save to file
        FileManager.saveGradingScale(scale);
    }
    
    /**
     * Get all assessment types
     */
    public List<AssessmentType> getAllAssessmentTypes() {
        List<AssessmentType> types = new ArrayList<>();
        types.add(new AssessmentType("AT001", AssessmentType.Type.ASSIGNMENT, 20, 100));
        types.add(new AssessmentType("AT002", AssessmentType.Type.CLASS_TEST, 30, 100));
        types.add(new AssessmentType("AT003", AssessmentType.Type.FINAL_EXAM, 50, 100));
        return types;
    }
    
    /**
     * Generate an assessment type ID
     */
    public String generateAssessmentTypeID() {
        return "AT" + String.format("%03d", getAllAssessmentTypes().size() + 1);
    }
    
    /**
     * Add an assessment type
     */
    public void addAssessmentType(AssessmentType type) {
        // This would normally save to persistent storage
        FileManager.saveAssessmentType(type);
    }
    
    /**
     * Delete an assessment type
     */
    public void deleteAssessmentType(String typeID) {
        // This would normally delete from persistent storage
    }
    
    /**
     * Approve a pending user account
     */
    public boolean approveUser(String userID) {
        User user = findUserByID(userID);
        if (user != null) {
            user.setApproved(true);
            FileManager.saveAllUsers(users);
            return true;
        }
        return false;
    }
    
    /**
     * Reject a pending user account (disable it)
     */
    public boolean rejectUser(String userID) {
        User user = findUserByID(userID);
        if (user != null) {
            user.setActive(false);
            user.setApproved(false);
            FileManager.saveAllUsers(users);
            return true;
        }
        return false;
    }
    
    /**
     * Get list of pending users (not yet approved)
     */
    public List<User> getPendingUsers() {
        List<User> pending = new ArrayList<>();
        for (User user : users) {
            if (!user.isApproved()) {
                pending.add(user);
            }
        }
        return pending;
    }
}
