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
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║     ASSESSMENT FEEDBACK SYSTEM (AFS) - ASIA PACIFIC UNIVERSITY   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        
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
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           AFS LOGIN / REGISTER           ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        
        String choice = scanner.nextLine().trim();
        switch(choice) {
            case "1":
                handleLogin();
                break;
            case "2":
                handleRegistration();
                break;
            case "3":
                System.out.println("Thank you for using AFS. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }
    
    /**
     * Handle user login
     */
    private void handleLogin() {
        System.out.println("\n========== LOGIN ==========");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
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
        System.out.println("\n========== REGISTER NEW USER ==========");
        System.out.println("Select User Type:");
        System.out.println("1. Admin Staff");
        System.out.println("2. Academic Leader");
        System.out.println("3. Lecturer");
        System.out.println("4. Student");
        System.out.print("Enter choice: ");
        
        String choice = scanner.nextLine().trim();
        
        System.out.print("User ID: ");
        String userID = scanner.nextLine().trim();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine().trim();
        
        User newUser = null;
        
        switch(choice) {
            case "1":
                System.out.print("Department: ");
                String adminDept = scanner.nextLine().trim();
                System.out.print("Staff ID: ");
                String staffID = scanner.nextLine().trim();
                newUser = new AdminStaff(userID, username, password, email, fullName, phoneNumber, adminDept, staffID);
                break;
            case "2":
                System.out.print("Department: ");
                String leaderDept = scanner.nextLine().trim();
                System.out.print("Leader ID: ");
                String leaderID = scanner.nextLine().trim();
                newUser = new AcademicLeader(userID, username, password, email, fullName, phoneNumber, leaderDept, leaderID);
                break;
            case "3":
                System.out.print("Lecturer ID: ");
                String lecturerID = scanner.nextLine().trim();
                System.out.print("Department: ");
                String lecturerDept = scanner.nextLine().trim();
                newUser = new Lecturer(userID, username, password, email, fullName, phoneNumber, lecturerID, lecturerDept);
                break;
            case "4":
                System.out.print("Student ID: ");
                String studentID = scanner.nextLine().trim();
                System.out.print("Enrollment Year: ");
                String enrollmentYear = scanner.nextLine().trim();
                newUser = new Student(userID, username, password, email, fullName, phoneNumber, studentID, enrollmentYear);
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        if (newUser != null) {
            if (systemManager.registerUser(newUser)) {
                System.out.println("Registration successful! You can now login.");
            }
        }
    }
    
    /**
     * Show main menu after login
     */
    private void showMainMenu() {
        User currentUser = systemManager.getCurrentUser();
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  Welcome, " + String.format("%-25s", currentUser.getFullName()) + "║");
        System.out.println("║  Role: " + String.format("%-30s", currentUser.getRole()) + "║");
        System.out.println("╚════════════════════════════════════════╝");
        
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
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine().trim();
        
        switch(choice) {
            case "1":
                System.out.println("Opening User Management...");
                break;
            case "2":
                System.out.println("Opening Lecturer Assignment...");
                break;
            case "3":
                System.out.println("Grading System configured.");
                break;
            case "4":
                handleCreateClass();
                break;
            case "5":
                System.out.println("Opening System Reports...");
                break;
            case "6":
                systemManager.setCurrentUser(null);
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
    
    /**
     * Handle Academic Leader menu
     */
    private void handleAcademicLeaderMenu(AcademicLeader leader) {
        leader.displayMenu();
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine().trim();
        
        switch(choice) {
            case "1":
                editProfile(leader);
                break;
            case "2":
                handleCreateModule();
                break;
            case "3":
                System.out.println("Opening Lecturer Assignment...");
                break;
            case "4":
                System.out.println("Opening Report Analysis...");
                break;
            case "5":
                systemManager.setCurrentUser(null);
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
    
    /**
     * Handle Lecturer menu
     */
    private void handleLecturerMenu(Lecturer lecturer) {
        lecturer.displayMenu();
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine().trim();
        
        switch(choice) {
            case "1":
                editProfile(lecturer);
                break;
            case "2":
                System.out.println("Opening Assessment Design...");
                break;
            case "3":
                System.out.println("Opening Mark Entry...");
                break;
            case "4":
                System.out.println("Opening Feedback Management...");
                break;
            case "5":
                System.out.println("Displaying assigned modules...");
                break;
            case "6":
                systemManager.setCurrentUser(null);
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
    
    /**
     * Handle Student menu
     */
    private void handleStudentMenu(Student student) {
        student.displayMenu();
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine().trim();
        
        switch(choice) {
            case "1":
                editProfile(student);
                break;
            case "2":
                System.out.println("Opening Class Registration...");
                break;
            case "3":
                student.viewResults();
                break;
            case "4":
                System.out.println("Displaying feedback...");
                break;
            case "5":
                System.out.println("Opening Feedback Comments...");
                break;
            case "6":
                systemManager.setCurrentUser(null);
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
    
    /**
     * Edit user profile
     */
    private void editProfile(User user) {
        System.out.println("\n========== EDIT PROFILE ==========");
        System.out.println("Current Email: " + user.getEmail());
        System.out.print("New Email (or press Enter to keep): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }
        
        System.out.println("Current Phone: " + user.getPhoneNumber());
        System.out.print("New Phone (or press Enter to keep): ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) {
            user.setPhoneNumber(phone);
        }
        System.out.println("Profile updated!");
    }
    
    /**
     * Handle module creation
     */
    private void handleCreateModule() {
        System.out.println("\n========== CREATE MODULE ==========");
        System.out.print("Module ID: ");
        String moduleID = scanner.nextLine().trim();
        System.out.print("Module Name: ");
        String moduleName = scanner.nextLine().trim();
        System.out.print("Module Code: ");
        String moduleCode = scanner.nextLine().trim();
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        System.out.print("Credit Hours: ");
        int creditHours = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();
        
        Module module = new Module(moduleID, moduleName, moduleCode, description, creditHours, department);
        systemManager.createModule(module);
    }
    
    /**
     * Handle class creation
     */
    private void handleCreateClass() {
        System.out.println("\n========== CREATE CLASS ==========");
        System.out.print("Class ID: ");
        String classID = scanner.nextLine().trim();
        System.out.print("Class Name: ");
        String className = scanner.nextLine().trim();
        System.out.print("Select Module (Enter Module ID): ");
        String moduleID = scanner.nextLine().trim();
        System.out.print("Semester: ");
        String semester = scanner.nextLine().trim();
        System.out.print("Capacity: ");
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
            System.out.println("Class created successfully!");
        } else {
            System.out.println("Module not found!");
        }
    }
}
