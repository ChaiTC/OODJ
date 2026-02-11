import java.io.*;
import java.util.*;

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
    private static final String ANNOUNCEMENTS_FILE = "data/announcements.txt"; // Announcements

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


        } catch (IOException e) {

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


        } catch (IOException e) {

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

        } catch (IOException e) {

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

        } catch (IOException e) {

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

        } catch (IOException e) {

        }
        return modules;
    }

    /**
     * Overwrite and save all modules
     */
    public static void saveAllModules(List<Module> modules) {
        try (FileWriter fw = new FileWriter(MODULES_FILE, false);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Module module : modules) {
                bw.write(serializeModule(module));
                bw.newLine();
            }
        } catch (IOException e) {
            // Optionally log error
        }
    }

    /**
     * Save a single class (append)
     */
    public static void saveClass(ClassModule cls) {
        try (FileWriter fw = new FileWriter(CLASSES_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(serializeClass(cls));
            bw.newLine();

        } catch (IOException e) {

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

        } catch (IOException e) {

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

        } catch (IOException e) {

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

        } catch (IOException e) {

        }
    }

    public static void saveAllAssessments(List<Assessment> assessments) {
    try (FileWriter fw = new FileWriter(ASSESSMENTS_FILE, false);
         BufferedWriter bw = new BufferedWriter(fw)) {

        for (Assessment a : assessments) {
            bw.write(serializeAssessment(a));
            bw.newLine();
        }

    } catch (IOException e) {
        System.err.println("Error saving all assessments: " + e.getMessage());
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

        } catch (IOException e) {

        }
    }

    /**
     * Save all feedback to file (overwrite)
     */
    public static void saveAllFeedback(List<Feedback> feedbackList) {
        try (FileWriter fw = new FileWriter(FEEDBACK_FILE, false);
             BufferedWriter bw = new BufferedWriter(fw)) {

            for (Feedback f : feedbackList) {
                bw.write(serializeFeedback(f));
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error saving all feedback: " + e.getMessage());
        }
    }
    
    /**
     * Load all assessments from file
     */
    public static List<Assessment> loadAllAssessments(List<Module> modules, List<User> users) {
        List<Assessment> assessments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ASSESSMENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Assessment assessment = deserializeAssessment(line, modules, users);
                if (assessment != null) {
                    assessments.add(assessment);
                }
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return assessments;
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

        } catch (IOException e) {

        }
        return feedbackList;
    }
    
    /**
     * Save a single announcement to the announcements file (append)
     */
    public static void saveAnnouncement(Announcement ann) {
        try (FileWriter fw = new FileWriter(ANNOUNCEMENTS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(serializeAnnouncement(ann));
            bw.newLine();
        } catch (IOException e) {
            // Optionally log error
        }
    }

    /**
     * Overwrite and save all announcements
     */
    public static void saveAllAnnouncements(List<Announcement> announcements) {
        try (FileWriter fw = new FileWriter(ANNOUNCEMENTS_FILE, false);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Announcement ann : announcements) {
                bw.write(serializeAnnouncement(ann));
                bw.newLine();
            }
        } catch (IOException e) {
            // Optionally log error
        }
    }

    /**
     * Load all announcements from file
     */
    public static List<Announcement> loadAllAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ANNOUNCEMENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Announcement ann = deserializeAnnouncement(line);
                if (ann != null) announcements.add(ann);
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist, return empty list
        } catch (IOException e) {
            // Optionally log error
        }
        return announcements;
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
                    .append(user.getPhoneNumber()).append("|")
                    .append(user.getGender() != null ? user.getGender() : "N/A").append("|")
                    .append(user.getAge()).append("|")
                    .append(user.isActive()).append("|")
                    .append(user.isApproved());
        
        if (user instanceof AdminStaff) {
            AdminStaff admin = (AdminStaff) user;
            sb.append("|").append(admin.getDepartment()).append("|").append(admin.getStaffID());
        } else if (user instanceof AcademicLeader) {
            AcademicLeader leader = (AcademicLeader) user;
            sb.append("|").append(leader.getDepartment()).append("|").append(leader.getStaffID() != null ? leader.getStaffID() : leader.getLeaderID());
        } else if (user instanceof Lecturer) {
            Lecturer lecturer = (Lecturer) user;
            sb.append("|").append(lecturer.getDepartment()).append("|").append(lecturer.getStaffID() != null ? lecturer.getStaffID() : lecturer.getLecturerID());
            String leaderID = lecturer.getAcademicLeaderID();
            sb.append("|").append(leaderID != null ? leaderID : "UNASSIGNED");
            // append assigned module IDs (comma separated)
            List<Module> assigned = lecturer.getAssignedModules();
            sb.append("|");
            if (assigned != null && !assigned.isEmpty()) {
                for (int i = 0; i < assigned.size(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append(assigned.get(i).getModuleID());
                }
            }
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
    
    private static String serializeAssessment(Assessment a) {
    StringBuilder sb = new StringBuilder();

    long createdMillis = (a.getCreatedDate() != null) ? a.getCreatedDate().getTime() : 0;
    long dueMillis = (a.getDueDate() != null) ? a.getDueDate().getTime() : 0;

    sb.append(a.getAssessmentID()).append("|")
      .append(a.getAssessmentName()).append("|")
      .append(a.getAssessmentType().getAssessmentType().name()).append("|")
      .append(a.getAssessmentType().getWeightage()).append("|")
      .append(a.getAssessmentType().getTotalMarks()).append("|")
      .append(a.getModule().getModuleID()).append("|")
      .append(a.getCreatedBy() != null ? a.getCreatedBy().getUserID() : "").append("|")
      .append(createdMillis).append("|")
      .append(dueMillis).append("|")
      .append(a.getClassID() != null ? a.getClassID() : "").append("|");

    // marks: studentID=mark,studentID=mark
    int count = 0;
    for (Map.Entry<String, Double> e : a.getStudentMarks().entrySet()) {
        if (count > 0) sb.append(",");
        sb.append(e.getKey()).append("=").append(e.getValue());
        count++;
    }

    return sb.toString();
}

    
    private static String serializeFeedback(Feedback feedback) {
        return feedback.getFeedbackID() + "|" +
               feedback.getAssessmentID() + "|" +
               feedback.getStudentID() + "|" +
               feedback.getLecturerID() + "|" +
               feedback.getFeedbackContent() + "|" +
               feedback.getSuggestedMarks();
    }
    
    private static String serializeAnnouncement(Announcement ann) {
        return ann.getAnnouncementID() + "|" +
               ann.getTitle().replace("|", "/") + "|" +
               ann.getContent().replace("|", "/") + "|" +
               ann.getSenderID() + "|" +
               ann.getTargetRole() + "|" +
               ann.getCreatedDate().getTime();
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
              .append(grade.getDescription()).append(",")
              .append(grade.getGPA());
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
        String gender = "N/A";
        int age = 0;
        boolean isActive = true;
        boolean isApproved = false;
        boolean hasGenderAge = parts.length >= 9 && (
            "Male".equals(parts[7]) || "Female".equals(parts[7]) || "N/A".equals(parts[7])
        );
        if (hasGenderAge) {
            gender = parts[7];
            try { age = Integer.parseInt(parts[8]); } catch (Exception ignored) {}
        }
        int idx = hasGenderAge ? 9 : 7;
        
        // Check for isActive and isApproved fields (new format)
        if (parts.length > idx && ("true".equals(parts[idx]) || "false".equals(parts[idx]))) {
            try { isActive = Boolean.parseBoolean(parts[idx]); } catch (Exception ignored) {}
            if (parts.length > idx + 1) {
                try { isApproved = Boolean.parseBoolean(parts[idx + 1]); } catch (Exception ignored) {}
                idx += 2;
            } else {
                idx += 1;
            }
        }
        
        switch(role) {
            case "ADMIN_STAFF":
                if (parts.length >= idx + 2) {
                    String department = parts[idx];
                    String staffID = parts[idx + 1];
                    AdminStaff admin = new AdminStaff(userID, username, password, email, fullName, phoneNumber, department, staffID);
                    admin.setGender(gender);
                    admin.setAge(age);
                    admin.setActive(isActive);
                    admin.setApproved(isApproved);
                    return admin;
                }
                break;
            case "ACADEMIC_LEADER":
                if (parts.length >= idx + 2) {
                    String department = parts[idx];
                    String staffID = parts[idx + 1];
                    AcademicLeader leader = new AcademicLeader(userID, username, password, email, fullName, phoneNumber, department, userID);
                    leader.setStaffID(staffID);
                    leader.setGender(gender);
                    leader.setAge(age);
                    leader.setActive(isActive);
                    leader.setApproved(isApproved);
                    return leader;
                }
                break;
            case "LECTURER":
                if (parts.length >= idx + 2) {
                    String department = parts[idx];
                    String staffID = parts[idx + 1];
                    Lecturer lec = new Lecturer(userID, username, password, email, fullName, phoneNumber, userID, department);
                    lec.setStaffID(staffID);
                    lec.setGender(gender);
                    lec.setAge(age);
                    lec.setActive(isActive);
                    lec.setApproved(isApproved);
                    // Handle academicLeaderID field (backward compatible)
                    if (parts.length >= idx + 3 && !parts[idx + 2].equals("UNASSIGNED")) {
                        lec.setAcademicLeaderID(parts[idx + 2]);
                    }
                    // Handle assigned module IDs (optional, backward compatible)
                    if (parts.length >= idx + 4 && parts[idx + 3] != null && !parts[idx + 3].isEmpty()) {
                        String mods = parts[idx + 3];
                        String[] mids = mods.split(",");
                        for (String mid : mids) {
                            if (mid != null && !mid.trim().isEmpty()) {
                                // create placeholder Module with only ID; will be reconciled later
                                lec.getAssignedModules().add(new Module(mid.trim(), "Unknown Module", "", "Unknown", 0, ""));
                            }
                        }
                    }
                    return lec;
                }
                break;
            case "STUDENT":
                if (parts.length >= idx + 2) {
                    String studentID = parts[idx];
                    String enrollmentYear = parts[idx + 1];
                    Student student = new Student(userID, username, password, email, fullName, phoneNumber, studentID, enrollmentYear);
                    student.setGender(gender);
                    student.setAge(age);
                    student.setActive(isActive);
                    student.setApproved(isApproved);
                    return student;
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
    
    private static Announcement deserializeAnnouncement(String data) {
        try {
            String[] parts = data.split("\\|", -1);
            if (parts.length < 6) return null;
            String id = parts[0];
            String title = parts[1];
            String content = parts[2];
            String senderID = parts[3];
            String target = parts[4];
            long millis = Long.parseLong(parts[5]);
            Announcement ann = new Announcement(id, title, content, senderID, target);
            java.lang.reflect.Field f = ann.getClass().getDeclaredField("createdDate");
            f.setAccessible(true);
            f.set(ann, new java.util.Date(millis));
            return ann;
        } catch (Exception e) {
            return null;
        }
    }
    
    private static Assessment deserializeAssessment(String data, List<Module> modules, List<User> users) {
    try {
        String[] parts = data.split("\\|", -1);
        if (parts.length < 11) return null;

        String assessmentID = parts[0];
        String name = parts[1];
        String typeName = parts[2];
        double weightage = Double.parseDouble(parts[3]);
        double totalMarks = Double.parseDouble(parts[4]);
        String moduleID = parts[5];
        String lecturerUserID = parts[6];
        long createdMillis = Long.parseLong(parts[7]);
        long dueMillis = Long.parseLong(parts[8]);
        String classID = parts[9];
        String marksCSV = parts[10];

        // Find module
       // Find module (support both moduleID and moduleCode)
Module module = null;
for (Module m : modules) {
    if (m.getModuleID().equalsIgnoreCase(moduleID) ||
        m.getModuleCode().equalsIgnoreCase(moduleID)) {
        module = m;
        break;
    }
}
if (module == null) return null;


        // Find lecturer (createdBy)
        Lecturer lecturer = null;
        if (lecturerUserID != null && !lecturerUserID.isEmpty()) {
            for (User u : users) {
                if (u instanceof Lecturer && u.getUserID().equals(lecturerUserID)) {
                    lecturer = (Lecturer) u;
                    break;
                }
            }
        }

        AssessmentType at = new AssessmentType(
                "ATX",
                AssessmentType.Type.valueOf(typeName.toUpperCase()),
                weightage,
                totalMarks
        );

        Assessment a = new Assessment(
                assessmentID,
                name,
                at,
                module,
                lecturer,
                classID,
                (dueMillis == 0 ? null : new Date(dueMillis))
        );

        a.setCreatedDate(createdMillis == 0 ? new Date() : new Date(createdMillis));

        // Restore marks
        if (marksCSV != null && !marksCSV.isEmpty()) {
            String[] entries = marksCSV.split(",");
            for (String entry : entries) {
                String[] kv = entry.split("=");
                if (kv.length == 2) {
                    a.addStudentMark(kv[0], Double.parseDouble(kv[1]));
                }
            }
        }

        return a;

    } catch (Exception e) {
        return null;
    }
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
                double gpa = 0.0;
                if (gradeParts.length >= 6) {
                    try {
                        gpa = Double.parseDouble(gradeParts[5]);
                    } catch (Exception ignored) {}
                }
                gs.getGrades().add(new GradingScale(gradeID, gradeLetter, min, max, desc, gpa));
            }
        }
        return gs;
    }

    private static String serializeClass(ClassModule cls) {
        // Format: classID|className|moduleID|lecturerID|day|time|location|capacity|studentIDs(comma)
        StringBuilder sb = new StringBuilder();
        sb.append(cls.getClassID()).append("|")
          .append(cls.getClassName()).append("|")
          .append(cls.getModuleID()).append("|")
          .append(cls.getLecturerID() != null ? cls.getLecturerID() : "").append("|")
          .append(cls.getDay() != null ? cls.getDay() : "").append("|")
          .append(cls.getTime() != null ? cls.getTime() : "").append("|")
          .append(cls.getLocation() != null ? cls.getLocation() : "").append("|")
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
    try {
        String[] parts = data.split("\\|", -1);
        if (parts.length < 8) return null;

        String classID = parts[0];
        String className = parts[1];
        String moduleID = parts[2];
        String lecturerID = parts[3];
        String day = parts[4];
        String time = parts[5];
        String location = parts[6];
        int capacity = Integer.parseInt(parts[7]);
        String studentCSV = parts.length > 8 ? parts[8] : "";

        ClassModule cls = new ClassModule(
                classID, className, moduleID,
                capacity, day, time, location, null
        );

        // assign lecturer
        if (lecturerID != null && !lecturerID.isEmpty()) {
            for (User u : users) {
                if (u instanceof Lecturer &&
                        u.getUserID().equals(lecturerID)) {
                    cls.setLecturer((Lecturer) u);
                    break;
                }
            }
        }

        // enrolled students
        if (studentCSV != null && !studentCSV.isEmpty()) {
            String[] ids = studentCSV.split(",");
            for (String sid : ids) {
                for (User u : users) {
                    if (u instanceof Student) {
                        Student s = (Student) u;
                        if (s.getStudentID().equals(sid)) {
                            cls.enrollStudent(s);
                            break;
                        }
                    }
                }
            }
        }

        return cls;

    } catch (Exception e) {
        return null;
    }
}


    
    /**
     * Save grading system to file
     */
    public static void saveGradingSystem(GradingSystem gradingSystem) {
        try (FileWriter fw = new FileWriter(GRADING_FILE, false); // overwrite
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(serializeGradingSystem(gradingSystem));
            bw.flush();

        } catch (IOException e) {
            System.err.println("Error saving grading system to file: " + e.getMessage());
            e.printStackTrace();
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

        } catch (IOException e) {

        }
    }
    
    /**
     * Save grading scale
     */
    public static void saveGradingScale(Object scale) {
        // Stub implementation
    }
    
    /**
     * Save assessment type
     */
    public static void saveAssessmentType(Object type) {
        // Stub implementation
    }
}

