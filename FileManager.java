import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * FileManager class - handles all file I/O operations for data persistence
 *
 * This class is like a "librarian" for our data. It saves information to files
 * and loads it back when the program starts. Without this, all data would be lost
 * when you close the program!
 *
 * Files are stored in the "data/" folder:
 * - users.txt      (all user accounts)
 * - modules.txt    (course modules like CS101)
 * - classes.txt    (class sections)
 * - assessments.txt (assignments, tests)
 * - feedback.txt   (teacher comments)
 */
public class FileManager {

    // File paths - where we store our data
    private static final String DATA_DIR = "data/";           // Folder for all data files
    private static final String USERS_FILE = "data/users.txt";      // User accounts
    private static final String MODULES_FILE = "data/modules.txt";   // Course modules
    private static final String CLASSES_FILE = "data/classes.txt";   // Class sections
    private static final String ASSESSMENTS_FILE = "data/assessments.txt"; // Assignments/tests
    private static final String FEEDBACK_FILE = "data/feedback.txt"; // Feedback
    private static final String GRADING_FILE = "data/grading.txt";   // Grading system

    // Static block - runs once when class is first loaded
    static {
        // Make sure data directory exists
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs(); // Create the directory if it doesn't exist
        }
    }

    /**
     * Save a single user to the users file
     * Adds the user to the end of the file
     */
    public static void saveUser(User user) {
        try (FileWriter fw = new FileWriter(USERS_FILE, true);  // "true" = append mode
             BufferedWriter bw = new BufferedWriter(fw)) {

            // Convert user object to text format and write to file
            bw.write(serializeUser(user));
            bw.newLine(); // Add newline after each user

            System.out.println("✅ User saved successfully!");
        } catch (IOException e) {
            System.err.println("❌ Error saving user: " + e.getMessage());
        }
    }

    /**
     * Save all users to file (overwrite existing file)
     * Used when updating or deleting users
     */
    public static void saveAllUsers(List<User> users) {
        try (FileWriter fw = new FileWriter(USERS_FILE, false); // "false" = overwrite mode
             BufferedWriter bw = new BufferedWriter(fw)) {

            // Write each user on a separate line
            for (User user : users) {
                bw.write(serializeUser(user));
                bw.newLine();
            }

            System.out.println("✅ All users saved successfully!");
        } catch (IOException e) {
            System.err.println("❌ Error saving users: " + e.getMessage());
        }
    }
    
    /**
     * Load all users from file
     */
    public static List<User> loadAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                User user = deserializeUser(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found. Creating new file...");
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }
    
    /**
     * Save module to file
     */
    public static void saveModule(Module module) {
        try (FileWriter fw = new FileWriter(MODULES_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(serializeModule(module));
            bw.newLine();
            System.out.println("Module saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving module: " + e.getMessage());
        }
    }
    
    /**
     * Load all modules from file
     */
    public static List<Module> loadAllModules() {
        List<Module> modules = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MODULES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Module module = deserializeModule(line);
                if (module != null) {
                    modules.add(module);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Modules file not found. Creating new file...");
        } catch (IOException e) {
            System.err.println("Error loading modules: " + e.getMessage());
        }
        return modules;
    }

    /**
     * Save a single class (append)
     */
    public static void saveClass(ClassModule cls) {
        try (FileWriter fw = new FileWriter(CLASSES_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(serializeClass(cls));
            bw.newLine();
            System.out.println("Class saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving class: " + e.getMessage());
        }
    }

    /**
     * Overwrite and save all classes
     */
    public static void saveAllClasses(List<ClassModule> classes) {
        try (FileWriter fw = new FileWriter(CLASSES_FILE, false);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (ClassModule cls : classes) {
                bw.write(serializeClass(cls));
                bw.newLine();
            }
            System.out.println("All classes saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving classes: " + e.getMessage());
        }
    }

    /**
     * Load all classes from file. Requires module and user lists to map references.
     */
    public static List<ClassModule> loadAllClasses(List<Module> modules, List<User> users) {
        List<ClassModule> classes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CLASSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                ClassModule cls = deserializeClass(line, modules, users);
                if (cls != null) classes.add(cls);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Classes file not found. Creating new file...");
        } catch (IOException e) {
            System.err.println("Error loading classes: " + e.getMessage());
        }
        return classes;
    }
    
    /**
     * Save assessment to file
     */
    public static void saveAssessment(Assessment assessment) {
        try (FileWriter fw = new FileWriter(ASSESSMENTS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(serializeAssessment(assessment));
            bw.newLine();
            System.out.println("Assessment saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving assessment: " + e.getMessage());
        }
    }
    
    /**
     * Save feedback to file
     */
    public static void saveFeedback(Feedback feedback) {
        try (FileWriter fw = new FileWriter(FEEDBACK_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(serializeFeedback(feedback));
            bw.newLine();
            System.out.println("Feedback saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving feedback: " + e.getMessage());
        }
    }
    
    /**
     * Load all feedback from file
     */
    public static List<Feedback> loadAllFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FEEDBACK_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Feedback feedback = deserializeFeedback(line);
                if (feedback != null) {
                    feedbackList.add(feedback);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Feedback file not found. Creating new file...");
        } catch (IOException e) {
            System.err.println("Error loading feedback: " + e.getMessage());
        }
        return feedbackList;
    }
    
    // Serialization methods
    private static String serializeUser(User user) {
        String role = user.getRole();
        StringBuilder sb = new StringBuilder();
        sb.append(role).append("|")
          .append(user.getUserID()).append("|")
          .append(user.getUsername()).append("|")
          .append(user.getPassword()).append("|")
          .append(user.getEmail()).append("|")
          .append(user.getFullName()).append("|")
          .append(user.getPhoneNumber());
        
        if (user instanceof AdminStaff) {
            AdminStaff admin = (AdminStaff) user;
            sb.append("|").append(admin.getDepartment()).append("|").append(admin.getStaffID());
        } else if (user instanceof AcademicLeader) {
            AcademicLeader leader = (AcademicLeader) user;
            sb.append("|").append(leader.getDepartment()).append("|").append(leader.getLeaderID());
        } else if (user instanceof Lecturer) {
            Lecturer lecturer = (Lecturer) user;
            sb.append("|").append(lecturer.getDepartment()).append("|").append(lecturer.getLecturerID());
        } else if (user instanceof Student) {
            Student student = (Student) user;
            sb.append("|").append(student.getStudentID()).append("|").append(student.getEnrollmentYear());
        }
        return sb.toString();
    }
    
    private static String serializeModule(Module module) {
        return module.getModuleID() + "|" +
               module.getModuleName() + "|" +
               module.getModuleCode() + "|" +
               module.getDescription() + "|" +
               module.getCreditHours() + "|" +
               module.getDepartment();
    }
    
    private static String serializeAssessment(Assessment assessment) {
        return assessment.getAssessmentID() + "|" +
               assessment.getAssessmentName() + "|" +
               assessment.getModule().getModuleID() + "|" +
               assessment.getDueDate();
    }
    
    private static String serializeFeedback(Feedback feedback) {
        return feedback.getFeedbackID() + "|" +
               feedback.getAssessmentID() + "|" +
               feedback.getStudentID() + "|" +
               feedback.getLecturerID() + "|" +
               feedback.getFeedbackContent() + "|" +
               feedback.getSuggestedMarks();
    }
    
    private static String serializeGradingSystem(GradingSystem gradingSystem) {
        StringBuilder sb = new StringBuilder();
        sb.append(gradingSystem.getSystemID()).append("|")
          .append(gradingSystem.getSystemName()).append("|")
          .append(gradingSystem.getPassingPercentage());
        for (GradingScale grade : gradingSystem.getGrades()) {
            sb.append("|").append(grade.getGradeID()).append(",")
              .append(grade.getGradeLetter()).append(",")
              .append(grade.getMinPercentage()).append(",")
              .append(grade.getMaxPercentage()).append(",")
              .append(grade.getDescription());
        }
        return sb.toString();
    }
    
    // Deserialization methods
    private static User deserializeUser(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 7) return null;
        
        String role = parts[0];
        String userID = parts[1];
        String username = parts[2];
        String password = parts[3];
        String email = parts[4];
        String fullName = parts[5];
        String phoneNumber = parts[6];
        
        switch(role) {
            case "ADMIN_STAFF":
                if (parts.length >= 9) {
                    String department = parts[7];
                    String staffID = parts[8];
                    return new AdminStaff(userID, username, password, email, fullName, phoneNumber, department, staffID);
                }
                break;
            case "ACADEMIC_LEADER":
                if (parts.length >= 9) {
                    String department = parts[7];
                    String leaderID = parts[8];
                    return new AcademicLeader(userID, username, password, email, fullName, phoneNumber, department, leaderID);
                }
                break;
            case "LECTURER":
                if (parts.length >= 9) {
                    String lecturerID = parts[7];
                    String department = parts[8];
                    return new Lecturer(userID, username, password, email, fullName, phoneNumber, lecturerID, department);
                }
                break;
            case "STUDENT":
                if (parts.length >= 9) {
                    String studentID = parts[7];
                    String enrollmentYear = parts[8];
                    return new Student(userID, username, password, email, fullName, phoneNumber, studentID, enrollmentYear);
                }
                break;
        }
        return null;
    }
    
    private static Module deserializeModule(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 6) return null;
        
        return new Module(parts[0], parts[1], parts[2], parts[3], 
                         Integer.parseInt(parts[4]), parts[5]);
    }
    
    private static Feedback deserializeFeedback(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 6) return null;
        
        return new Feedback(parts[0], parts[1], parts[2], parts[3], 
                           parts[4], Double.parseDouble(parts[5]));
    }
    
    private static GradingSystem deserializeGradingSystem(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 3) return null;
        
        String systemID = parts[0];
        String systemName = parts[1];
        double passingPercentage = Double.parseDouble(parts[2]);
        
        GradingSystem gs = new GradingSystem(systemID, systemName, passingPercentage);
        gs.getGrades().clear(); // Clear default grades
        
        for (int i = 3; i < parts.length; i++) {
            String[] gradeParts = parts[i].split(",");
            if (gradeParts.length >= 5) {
                String gradeID = gradeParts[0];
                String gradeLetter = gradeParts[1];
                double min = Double.parseDouble(gradeParts[2]);
                double max = Double.parseDouble(gradeParts[3]);
                String desc = gradeParts[4];
                gs.getGrades().add(new GradingScale(gradeID, gradeLetter, min, max, desc));
            }
        }
        return gs;
    }

    private static String serializeClass(ClassModule cls) {
        // Format: classID|className|moduleID|lecturerID|semester|capacity|studentIDs(comma)
        StringBuilder sb = new StringBuilder();
        sb.append(cls.getClassID()).append("|")
          .append(cls.getClassName()).append("|")
          .append(cls.getModule() != null ? cls.getModule().getModuleID() : "").append("|")
          .append(cls.getLecturer() != null ? cls.getLecturer().getLecturerID() : "").append("|")
          .append(cls.getSemester()).append("|")
          .append(cls.getCapacity()).append("|");
        // enrolled students
        List<Student> enrolled = cls.getEnrolledStudents();
        for (int i = 0; i < enrolled.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(enrolled.get(i).getStudentID());
        }
        return sb.toString();
    }

    private static ClassModule deserializeClass(String data, List<Module> modules, List<User> users) {
        String[] parts = data.split("\\|");
        if (parts.length < 7) return null;
        String classID = parts[0];
        String className = parts[1];
        String moduleID = parts[2];
        String lecturerID = parts[3];
        String semester = parts[4];
        int capacity = 50;
        try { capacity = Integer.parseInt(parts[5]); } catch (Exception ignored) {}
        String studentCSV = parts[6];

        Module moduleObj = null;
        for (Module m : modules) if (m.getModuleID().equals(moduleID)) { moduleObj = m; break; }

        Lecturer lecturerObj = null;
        for (User u : users) if (u instanceof Lecturer && u.getUserID().equals(lecturerID)) { lecturerObj = (Lecturer) u; break; }

        // fallbacks
        if (moduleObj == null) moduleObj = new Module("", moduleID, "", "", 3, "N/A");
        if (lecturerObj == null) lecturerObj = new Lecturer("LEC000", "unassigned", "pass", "unassigned@apu.edu", "Unassigned", "N/A", "LEC000", "N/A");

        ClassModule cls = new ClassModule(classID, className, moduleObj, lecturerObj, semester, capacity);

        if (studentCSV != null && !studentCSV.isEmpty()) {
            String[] sids = studentCSV.split(",");
            for (String sid : sids) {
                for (User u : users) {
                    if (u instanceof Student && u.getUserID().equals(sid)) {
                        cls.enrollStudent((Student) u);
                        ((Student) u).registerClass(cls);
                        break;
                    }
                }
            }
        }

        return cls;
    }
    
    /**
     * Save grading system to file
     */
    public static void saveGradingSystem(GradingSystem gradingSystem) {
        try (FileWriter fw = new FileWriter(GRADING_FILE, false); // overwrite
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(serializeGradingSystem(gradingSystem));
            System.out.println("✅ Grading system saved successfully!");
        } catch (IOException e) {
            System.err.println("❌ Error saving grading system: " + e.getMessage());
        }
    }
    
    /**
     * Load grading system from file
     */
    public static GradingSystem loadGradingSystem() {
        try (BufferedReader br = new BufferedReader(new FileReader(GRADING_FILE))) {
            String line = br.readLine();
            if (line != null && !line.trim().isEmpty()) {
                return deserializeGradingSystem(line);
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist, return null
        } catch (IOException e) {
            System.err.println("❌ Error loading grading system: " + e.getMessage());
        }
        return null; // Return null if not found or error
    }
    
    /**
     * Clear all files - for system reset
     */
    public static void clearAllData() {
        try {
            new FileWriter(USERS_FILE).close();
            new FileWriter(MODULES_FILE).close();
            new FileWriter(ASSESSMENTS_FILE).close();
            new FileWriter(FEEDBACK_FILE).close();
            new FileWriter(GRADING_FILE).close();
            System.out.println("All data cleared!");
        } catch (IOException e) {
            System.err.println("Error clearing data: " + e.getMessage());
        }
    }
}
