import java.util.*;

public class SystemManager {

    private List<User> users;
    private List<Module> modules;
    private List<ClassModule> classes;
    private List<Assessment> assessments;
    private List<Feedback> feedbackList;
    private GradingSystem gradingSystem;
    private User currentUser;

    // ================= CONSTRUCTOR =================
    public SystemManager() {

        users = new ArrayList<>();
        modules = new ArrayList<>();
        classes = new ArrayList<>();
        assessments = new ArrayList<>();
        feedbackList = new ArrayList<>();

        gradingSystem = new GradingSystem("GS001", "APU Grading System", 60);
        currentUser = null;

        loadAllData();
    }

    // ================= LOAD DATA =================
    public void loadAllData() {

        users = FileManager.loadAllUsers();
        if (users == null) users = new ArrayList<>();

        modules = FileManager.loadAllModules();
        if (modules == null) modules = new ArrayList<>();

        classes = FileManager.loadAllClasses(modules, users);
        if (classes == null) classes = new ArrayList<>();

        assessments = FileManager.loadAllAssessments(modules, users);
        if (assessments == null) assessments = new ArrayList<>();

        feedbackList = FileManager.loadAllFeedback();
        if (feedbackList == null) feedbackList = new ArrayList<>();

        GradingSystem loadedGrading = FileManager.loadGradingSystem();
        if (loadedGrading != null) {
            gradingSystem = loadedGrading;
        }
    }

    // ================= USER MANAGEMENT =================
    public boolean registerUser(User user) {

        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                return false;
            }
        }

        users.add(user);
        FileManager.saveUser(user);
        return true;
    }

    public User authenticateUser(String username, String password) {

        for (User user : users) {

            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {

                if (!user.isApproved()) return null;
                if (!user.isActive()) return null;

                currentUser = user;
                return user;
            }
        }
        return null;
    }

    public User findUserByID(String userID) {
        for (User u : users) {
            if (u.getUserID().equals(userID)) return u;
        }
        return null;
    }

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

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public List<User> getUsersByRole(String role) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals(role)) {
                result.add(user);
            }
        }
        return result;
    }

    public List<User> getAllLecturers() {
        return getUsersByRole("LECTURER");
    }

    public List<User> getAllStudents() {
        return getUsersByRole("STUDENT");
    }

    public List<User> getAllAcademicLeaders() {
        return getUsersByRole("ACADEMIC_LEADER");
    }

    // ================= MODULE MANAGEMENT =================
    public void createModule(Module module) {
        modules.add(module);
        FileManager.saveModule(module);
    }

    public List<Module> getAllModules() {
        return new ArrayList<>(modules);
    }

    public String generateModuleID() {
        String prefix = "MOD";
        int max = 0;

        for (Module m : modules) {
            if (m.getModuleID() != null && m.getModuleID().startsWith(prefix)) {
                try {
                    int v = Integer.parseInt(m.getModuleID().substring(prefix.length()));
                    if (v > max) max = v;
                } catch (Exception ignored) {}
            }
        }
        return String.format("%s%03d", prefix, max + 1);
    }

    // ================= CLASS MANAGEMENT =================
    public void createClass(ClassModule classModule) {

        if (classes == null) classes = new ArrayList<>();

        classes.add(classModule);

        // safer save (overwrite full list)
        FileManager.saveAllClasses(classes);
    }

    public List<ClassModule> getAllClasses() {
        if (classes == null) classes = new ArrayList<>();
        return new ArrayList<>(classes);
    }

    public void updateClass(ClassModule updated) {
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getClassID().equals(updated.getClassID())) {
                classes.set(i, updated);
                FileManager.saveAllClasses(classes);
                return;
            }
        }
    }

    public void deleteClass(String classID) {
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getClassID().equals(classID)) {
                classes.remove(i);
                FileManager.saveAllClasses(classes);
                return;
            }
        }
    }

    public String generateClassID() {
        String prefix = "CL";
        int max = 0;

        for (ClassModule c : classes) {
            if (c.getClassID() != null && c.getClassID().startsWith(prefix)) {
                try {
                    int v = Integer.parseInt(c.getClassID().substring(prefix.length()));
                    if (v > max) max = v;
                } catch (Exception ignored) {}
            }
        }
        return String.format("%s%03d", prefix, max + 1);
    }

    // ================= ASSESSMENT MANAGEMENT =================
    public void createAssessment(Assessment assessment) {
        assessments.add(assessment);
        FileManager.saveAssessment(assessment);
    }

    public List<Assessment> getAllAssessments() {
        return new ArrayList<>(assessments);
    }

    public boolean updateAssessment(Assessment updated) {
        for (int i = 0; i < assessments.size(); i++) {
            if (assessments.get(i).getAssessmentID().equals(updated.getAssessmentID())) {
                assessments.set(i, updated);
                FileManager.saveAllAssessments(assessments);
                return true;
            }
        }
        return false;
    }

    public String generateAssessmentID() {
        String prefix = "ASM";
        int max = 0;

        for (Assessment a : assessments) {
            if (a.getAssessmentID() != null && a.getAssessmentID().startsWith(prefix)) {
                try {
                    int v = Integer.parseInt(a.getAssessmentID().substring(prefix.length()));
                    if (v > max) max = v;
                } catch (Exception ignored) {}
            }
        }
        return String.format("%s%03d", prefix, max + 1);
    }

    // ================= FEEDBACK =================
    public void createFeedback(Feedback feedback) {
        feedbackList.add(feedback);
        FileManager.saveFeedback(feedback);
    }

    public List<Feedback> getAllFeedback() {
        return new ArrayList<>(feedbackList);
    }

    public List<Feedback> getStudentFeedback(String studentID) {
        List<Feedback> result = new ArrayList<>();
        for (Feedback f : feedbackList) {
            if (f.getStudentID().equals(studentID)) {
                result.add(f);
            }
        }
        return result;
    }

    public String generateFeedbackID() {
        String prefix = "FB";
        int max = 0;

        for (Feedback f : feedbackList) {
            if (f.getFeedbackID() != null && f.getFeedbackID().startsWith(prefix)) {
                try {
                    int v = Integer.parseInt(f.getFeedbackID().substring(prefix.length()));
                    if (v > max) max = v;
                } catch (Exception ignored) {}
            }
        }
        return String.format("%s%03d", prefix, max + 1);
    }

    // ================= GETTERS =================
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { currentUser = user; }
    public GradingSystem getGradingSystem() { return gradingSystem; }
}
