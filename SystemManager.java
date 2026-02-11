import java.util.*;

public class SystemManager {

    private List<User> users;
    private List<Module> modules;
    private List<ClassModule> classes;
    private List<Assessment> assessments;
    private List<Feedback> feedbackList;
    private GradingSystem gradingSystem;
    private User currentUser;

    public SystemManager() {
        users = new ArrayList<>();
        modules = new ArrayList<>();
        classes = new ArrayList<>();
        assessments = new ArrayList<>();
        feedbackList = new ArrayList<>();
        gradingSystem = new GradingSystem("GS001", "APU Grading System", 60);
        loadAllData();
    }

    /* ================= LOAD DATA ================= */

    public void loadAllData() {
        users = FileManager.loadAllUsers();
        modules = FileManager.loadAllModules();
        classes = FileManager.loadAllClasses(modules, users);
        assessments = FileManager.loadAllAssessments(modules, users);
        feedbackList = FileManager.loadAllFeedback();

        GradingSystem loaded = FileManager.loadGradingSystem();
        if (loaded != null) gradingSystem = loaded;
    }

    /* ================= GETTERS ================= */

    public List<User> getAllUsers() { return new ArrayList<>(users); }
    public List<Module> getAllModules() { return new ArrayList<>(modules); }
    public List<ClassModule> getAllClasses() { return new ArrayList<>(classes); }
    public List<Assessment> getAllAssessments() { return new ArrayList<>(assessments); }
    public List<Feedback> getAllFeedback() { return new ArrayList<>(feedbackList); }
    public GradingSystem getGradingSystem() { return gradingSystem; }

    public User getCurrentUser() { return currentUser; }

    /* ================= USER ================= */

    public boolean registerUser(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) return false;
        }
        users.add(user);
        FileManager.saveUser(user);
        return true;
    }

    public User authenticateUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) &&
                u.getPassword().equals(password) &&
                u.isApproved() &&
                u.isActive()) {
                currentUser = u;
                return u;
            }
        }
        return null;
    }

    public User findUserByID(String id) {
        for (User u : users) {
            if (u.getUserID().equals(id)) return u;
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

    public boolean approveUser(String userID) {
        User u = findUserByID(userID);
        if (u != null) {
            u.setApproved(true);
            FileManager.saveAllUsers(users);
            return true;
        }
        return false;
    }

    public boolean rejectUser(String userID) {
        User u = findUserByID(userID);
        if (u != null) {
            u.setActive(false);
            u.setApproved(false);
            FileManager.saveAllUsers(users);
            return true;
        }
        return false;
    }

    public List<User> getStudentFeedbackUsers() {
        return getUsersByRole("STUDENT");
    }

    public List<User> getUsersByRole(String role) {
        List<User> list = new ArrayList<>();
        for (User u : users) {
            if (u.getRole().equals(role)) list.add(u);
        }
        return list;
    }

    public List<User> getAllLecturers() { return getUsersByRole("LECTURER"); }
    public List<User> getAllStudents() { return getUsersByRole("STUDENT"); }
    public List<User> getAllAcademicLeaders() { return getUsersByRole("ACADEMIC_LEADER"); }

    /* ================= MODULE ================= */

    public void createModule(Module m) {
        modules.add(m);
        FileManager.saveModule(m);
    }

    /* ================= CLASS ================= */

    public void createClass(ClassModule c) {
        classes.add(c);
        FileManager.saveClass(c);
    }

    public void saveAllClasses() {
        FileManager.saveAllClasses(classes);
    }

    /* ================= ASSESSMENT ================= */

    public void createAssessment(Assessment a) {
        assessments.add(a);
        FileManager.saveAssessment(a);
    }

    public boolean updateAssessment(Assessment updated) {
        for (int i = 0; i < assessments.size(); i++) {
            if (assessments.get(i).getAssessmentID()
                    .equals(updated.getAssessmentID())) {
                assessments.set(i, updated);
                FileManager.saveAllAssessments(assessments);
                return true;
            }
        }
        return false;
    }

    /* ================= FEEDBACK ================= */

    public void createFeedback(Feedback f) {
        feedbackList.add(f);
        FileManager.saveFeedback(f);
    }

    public List<Feedback> getStudentFeedback(String studentID) {
        List<Feedback> list = new ArrayList<>();
        for (Feedback f : feedbackList) {
            if (f.getStudentID().equals(studentID)) {
                list.add(f);
            }
        }
        return list;
    }

    /* ================= ID GENERATORS ================= */

    public String generateUserID(String role) {
        String prefix = switch (role.toUpperCase()) {
            case "ADMIN", "ADMIN_STAFF" -> "ADM";
            case "LECTURER" -> "LEC";
            case "STUDENT" -> "STU";
            case "ACADEMIC_LEADER" -> "ACL";
            default -> "USR";
        };

        int max = 0;
        for (User u : users) {
            if (u.getUserID().startsWith(prefix)) {
                try {
                    int num = Integer.parseInt(
                            u.getUserID().substring(prefix.length()));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return prefix + String.format("%03d", max + 1);
    }

    public String generateStaffID() {
        int max = 0;
        for (User u : users) {
            String id = null;
            if (u instanceof AdminStaff) id = ((AdminStaff) u).getStaffID();
            if (u instanceof Lecturer) id = ((Lecturer) u).getStaffID();
            if (u instanceof AcademicLeader) id = ((AcademicLeader) u).getStaffID();

            if (id != null && id.startsWith("STF")) {
                try {
                    int num = Integer.parseInt(id.substring(3));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return "STF" + String.format("%03d", max + 1);
    }

    public String generateModuleID() {
        int max = 0;
        for (Module m : modules) {
            if (m.getModuleID().startsWith("MOD")) {
                try {
                    int num = Integer.parseInt(m.getModuleID().substring(3));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return "MOD" + String.format("%03d", max + 1);
    }

    public String generateClassID() {
        int max = 0;
        for (ClassModule c : classes) {
            if (c.getClassID().startsWith("CL")) {
                try {
                    int num = Integer.parseInt(c.getClassID().substring(2));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return "CL" + String.format("%03d", max + 1);
    }

    public String generateAssessmentID() {
        int max = 0;
        for (Assessment a : assessments) {
            if (a.getAssessmentID().startsWith("ASM")) {
                try {
                    int num = Integer.parseInt(a.getAssessmentID().substring(3));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return "ASM" + String.format("%03d", max + 1);
    }

    public String generateFeedbackID() {
        int max = 0;
        for (Feedback f : feedbackList) {
            if (f.getFeedbackID().startsWith("FB")) {
                try {
                    int num = Integer.parseInt(f.getFeedbackID().substring(2));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return "FB" + String.format("%03d", max + 1);
    }

    public String generateAssessmentTypeID() {
        return "AT" + String.format("%03d", getAllAssessmentTypes().size() + 1);
    }

    /* ================= ASSESSMENT TYPES ================= */

    public List<AssessmentType> getAllAssessmentTypes() {
        List<AssessmentType> list = new ArrayList<>();
        list.add(new AssessmentType("AT001", AssessmentType.Type.ASSIGNMENT, 20, 100));
        list.add(new AssessmentType("AT002", AssessmentType.Type.CLASS_TEST, 30, 100));
        list.add(new AssessmentType("AT003", AssessmentType.Type.FINAL_EXAM, 50, 100));
        return list;
    }
}
