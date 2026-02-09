import java.util.Scanner;

/**
 * AFSApplication class - Main application with console interface
 */
public class AFSApplication {
    private SystemManager systemManager;
    private Scanner scanner;
    
    public AFSApplication() {
        this.systemManager = new SystemManager();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Main application loop
     */
    public void start() {
        
        boolean running = true;
        while (running) {
            if (systemManager.getCurrentUser() == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    /**
     * Display login menu
     */
    private void showLoginMenu() {
        
        
        
        
        
        String choice = scanner.nextLine().trim();
        switch(choice) {
            case "1":
                handleLogin();
                break;
            case "2":
                handleRegistration();
                break;
            case "3":
                
                System.exit(0);
                break;
            default:
                
        }
    }
    
    /**
     * Handle user login
     */
    private void handleLogin() {
        
        String username = scanner.nextLine().trim();
        String password = scanner.nextLine().trim();
        
        User user = systemManager.authenticateUser(username, password);
        if (user != null) {
            systemManager.setCurrentUser(user);
        }
    }
    
    /**
     * Handle user registration
     */
    private void handleRegistration() {
        String choice = scanner.nextLine().trim();
        
        String userID = scanner.nextLine().trim();
        String username = scanner.nextLine().trim();
        String password = scanner.nextLine().trim();
        String email = scanner.nextLine().trim();
        String fullName = scanner.nextLine().trim();
        String phoneNumber = scanner.nextLine().trim();
        
        User newUser = null;
        
        switch(choice) {
            case "1":
                String adminDept = scanner.nextLine().trim();
                String staffID = scanner.nextLine().trim();
                newUser = new AdminStaff(userID, username, password, email, fullName, phoneNumber, adminDept, staffID);
                break;
            case "2":
                String leaderDept = scanner.nextLine().trim();
                String leaderID = scanner.nextLine().trim();
                newUser = new AcademicLeader(userID, username, password, email, fullName, phoneNumber, leaderDept, leaderID);
                break;
            case "3":
                String lecturerID = scanner.nextLine().trim();
                String lecturerDept = scanner.nextLine().trim();
                newUser = new Lecturer(userID, username, password, email, fullName, phoneNumber, lecturerID, lecturerDept);
                break;
            case "4":
                String studentID = scanner.nextLine().trim();
                String enrollmentYear = scanner.nextLine().trim();
                newUser = new Student(userID, username, password, email, fullName, phoneNumber, studentID, enrollmentYear);
                break;
            default:
                
                return;
        }
        
        if (newUser != null) {
            if (systemManager.registerUser(newUser)) {
                
            }
        }
    }
    
    /**
     * Show main menu after login
     */
    private void showMainMenu() {
        User currentUser = systemManager.getCurrentUser();
        if (currentUser instanceof AdminStaff) {
            handleAdminMenu((AdminStaff) currentUser);
        } else if (currentUser instanceof AcademicLeader) {
            handleAcademicLeaderMenu((AcademicLeader) currentUser);
        } else if (currentUser instanceof Lecturer) {
            handleLecturerMenu((Lecturer) currentUser);
        } else if (currentUser instanceof Student) {
            handleStudentMenu((Student) currentUser);
        }
    }
    
    /**
     * Handle Admin Staff menu
     */
    private void handleAdminMenu(AdminStaff admin) {
        admin.displayMenu();
        String choice = scanner.nextLine().trim();
        
        switch(choice) {
            case "1":
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                handleCreateClass();
                break;
            case "5":
                break;
            case "6":
                systemManager.setCurrentUser(null);
                
                break;
            default:
                
        }
    }
    
    /**
     * Handle Academic Leader menu
     */
    private void handleAcademicLeaderMenu(AcademicLeader leader) {
        leader.displayMenu();
        String choice = scanner.nextLine().trim();
        
        switch(choice) {
            case "1":
                editProfile(leader);
                break;
            case "2":
                handleCreateModule();
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                systemManager.setCurrentUser(null);
                
                break;
            default:
                
        }
    }
    
    /**
     * Handle Lecturer menu
     */
    private void handleLecturerMenu(Lecturer lecturer) {
        lecturer.displayMenu();
        String choice = scanner.nextLine().trim();
        
        switch(choice) {
            case "1":
                editProfile(lecturer);
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                break;
            case "6":
                systemManager.setCurrentUser(null);
                
                break;
            default:
                
        }
    }
    
    /**
     * Handle Student menu
     */
    private void handleStudentMenu(Student student) {
        student.displayMenu();
        String choice = scanner.nextLine().trim();
        
        switch(choice) {
            case "1":
                editProfile(student);
                break;
            case "2":
                break;
            case "3":
                student.viewResults();
                break;
            case "4":
                break;
            case "5":
                break;
            case "6":
                systemManager.setCurrentUser(null);
                
                break;
            default:
                
        }
    }
    
    /**
     * Edit user profile
     */
    private void editProfile(User user) {
        
        
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }
        
        
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) {
            user.setPhoneNumber(phone);
        }
        
    }
    
    /**
     * Handle module creation
     */
    private void handleCreateModule() {
        
        String moduleID = scanner.nextLine().trim();
        String moduleName = scanner.nextLine().trim();
        String moduleCode = scanner.nextLine().trim();
        String description = scanner.nextLine().trim();
        int creditHours = Integer.parseInt(scanner.nextLine().trim());
        String department = scanner.nextLine().trim();
        
        Module module = new Module(moduleID, moduleName, moduleCode, description, creditHours, department);
        systemManager.createModule(module);
    }
    
    /**
     * Handle class creation
     */
    private void handleCreateClass() {
        
        String classID = scanner.nextLine().trim();
        String className = scanner.nextLine().trim();
        String moduleID = scanner.nextLine().trim();
        String semester = scanner.nextLine().trim();
        int capacity = Integer.parseInt(scanner.nextLine().trim());
        
        // Find module
        Module selectedModule = null;
        for (Module module : systemManager.getAllModules()) {
            if (module.getModuleID().equals(moduleID)) {
                selectedModule = module;
                break;
            }
        }
        
        if (selectedModule != null) {
            
        } else {
            
        }
    }
}
