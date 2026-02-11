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
        this.users = new ArrayList<>();
        this.modules = new ArrayList<>();
        this.classes = new ArrayList<>();
        this.assessments = new ArrayList<>();
        this.feedbackList = new ArrayList<>();

        this.gradingSystem = new GradingSystem("GS001", "APU Grading System", 60);
        this.currentUser = null;

        loadAllData();
    }

    public void loadAllData() {
        users = FileManager.loadAllUsers();
        modules = FileManager.loadAllModules();
        classes = FileManager.loadAllClasses(modules, users);
        assessments = FileManager.loadAllAssessments(modules, users);
        feedbackList = FileManager.loadAllFeedback();

        GradingSystem loadedGrading = FileManager.loadGradingSystem();
        if (loadedGrading != null) {
            this.gradingSystem = loadedGrading;
        }
    }

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
            if (user.getUsername().equals(username) &&
                user.getPassword().equals(password)) {

                if (!user.isApproved()) return null;
                if (!user.isActive()) return null;

                currentUser = user;
                return user;
            }
        }
        return null;
    }

    public void createModule(Module module) {
        modules.add(module);
        FileManager.saveModule(module);
    }

    public void createClass(ClassModule classModule) {
        classes.add(classModule);
        FileManager.saveClass(classModule);
    }

    public void saveAllClasses() {
        FileManager.saveAllClasses(this.classes);
    }

    public List<Module> getAllModules() {
        return new ArrayList<>(modules);
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

    public void createAssessment(Assessment assessment) {
        assessments.add(assessment);
        FileManager.saveAssessment(assessment);
    }

    public void createFeedback(Feedback feedback) {
        feedbackList.add(feedback);
        FileManager.saveFeedback(feedback);
    }

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
        }

        int max = 0;
        for (User u : users) {
            if (u.getUserID() != null && u.getUserID().startsWith(prefix)) {
                try {
                    int v = Integer.parseInt(u.getUserID().substring(prefix.length()));
                    if (v > max) max = v;
                } catch (Exception ignored) {}
            }
        }

        return String.format("%s%03d", prefix, max + 1);
    }

    public String generateStaffID() {
        String prefix = "STF";
        int max = 0;

        for (User u : users) {
            String staffId = null;

            if (u instanceof AdminStaff)
                staffId = ((AdminStaff) u).getStaffID();
            else if (u instanceof Lecturer)
                staffId = ((Lecturer) u).getStaffID();
            else if (u instanceof AcademicLeader)
                staffId = ((AcademicLeader) u).getStaffID();

            if (staffId != null && staffId.startsWith(prefix)) {
                try {
                    int v = Integer.parseInt(staffId.substring(prefix.length()));
                    if (v > max) max = v;
                } catch (Exception ignored) {}
            }
        }

        return String.format("%s%03d", prefix, max + 1);
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

    // ======== RESTORED MISSING METHODS ========

    public boolean deleteClass(String classID) {
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getClassID().equals(classID)) {
                classes.remove(i);
                FileManager.saveAllClasses(classes);
                return true;
            }
        }
        return false;
    }

    public boolean approveUser(String userID) {
        User user = findUserByID(userID);
        if (user != null) {
            user.setApproved(true);
            FileManager.saveAllUsers(users);
            return true;
        }
        return false;
    }

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

    public String generateAssessmentTypeID() {
        return "AT" + String.format("%03d", getAllAssessmentTypes().size() + 1);
    }

    public List<AssessmentType> getAllAssessmentTypes() {
        List<AssessmentType> types = new ArrayList<>();
        types.add(new AssessmentType("AT001", AssessmentType.Type.ASSIGNMENT, 20, 100));
        types.add(new AssessmentType("AT002", AssessmentType.Type.CLASS_TEST, 30, 100));
        types.add(new AssessmentType("AT003", AssessmentType.Type.FINAL_EXAM, 50, 100));
        return types;
    }

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
