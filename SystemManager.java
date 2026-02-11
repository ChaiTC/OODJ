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
        currentUser = null;

        loadAllData();
    }

    public void loadAllData() {
        users = FileManager.loadAllUsers();
        modules = FileManager.loadAllModules();
        classes = FileManager.loadAllClasses(modules, users);
        assessments = FileManager.loadAllAssessments(modules, users);
        feedbackList = FileManager.loadAllFeedback();

        GradingSystem loaded = FileManager.loadGradingSystem();
        if (loaded != null) {
            gradingSystem = loaded;
        }
    }

    // ================= USERS =================

    public boolean registerUser(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
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

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public List<User> getUsersByRole(String role) {
        List<User> result = new ArrayList<>();
        for (User u : users) {
            if (u.getRole().equalsIgnoreCase(role)) {
                result.add(u);
            }
        }
        return result;
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

    public boolean deleteUser(String id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserID().equals(id)) {
                users.remove(i);
                FileManager.saveAllUsers(users);
                return true;
            }
        }
        return false;
    }

    public boolean approveUser(String id) {
        User u = findUserByID(id);
        if (u != null) {
            u.setApproved(true);
            FileManager.saveAllUsers(users);
            return true;
        }
        return false;
    }

    public boolean rejectUser(String id) {
        User u = findUserByID(id);
        if (u != null) {
            u.setActive(false);
            u.setApproved(false);
            FileManager.saveAllUsers(users);
            return true;
        }
        return false;
    }

    public List<User> getPendingUsers() {
        List<User> list = new ArrayList<>();
        for (User u : users) {
            if (!u.isApproved()) list.add(u);
        }
        return list;
    }

    // ================= MODULES =================

    public void createModule(Module m) {
        modules.add(m);
        FileManager.saveModule(m);
    }

    public List<Module> getAllModules() {
        return new ArrayList<>(modules);
    }

    // ================= CLASSES =================

    public void createClass(ClassModule c) {
        classes.add(c);
        FileManager.saveClass(c);
    }

    public List<ClassModule> getAllClasses() {
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

    public void deleteClass(String id) {
        classes.removeIf(c -> c.getClassID().equals(id));
        FileManager.saveAllClasses(classes);
    }

    // ================= ASSESSMENTS =================

    public void createAssessment(Assessment a) {
        assessments.add(a);
        FileManager.saveAssessment(a);
    }

    public List<Assessment> getAllAssessments() {
        return new ArrayList<>(assessments);
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

    // ================= FEEDBACK =================

    public void createFeedback(Feedback f) {
        feedbackList.add(f);
        FileManager.saveFeedback(f);
    }

    public List<Feedback> getAllFeedback() {
        return new ArrayList<>(feedbackList);
    }

    // ================= ASSIGNMENTS =================

    public void assignLecturerToLeader(String lecID, String leaderID) {
        User u = findUserByID(lecID);
        if (u instanceof Lecturer) {
            ((Lecturer) u).setAcademicLeaderID(leaderID);
            updateUser(u);
        }
    }

    public void unassignLecturerFromLeader(String lecID) {
        User u = findUserByID(lecID);
        if (u instanceof Lecturer) {
            ((Lecturer) u).setAcademicLeaderID(null);
            updateUser(u);
        }
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

    // ================= ID GENERATORS =================

    public String generateUserID(String role) {
        String prefix = "USR";
        if (role.equalsIgnoreCase("ADMIN_STAFF")) prefix = "ADM";
        if (role.equalsIgnoreCase("ACADEMIC_LEADER")) prefix = "ACL";
        if (role.equalsIgnoreCase("LECTURER")) prefix = "LEC";
        if (role.equalsIgnoreCase("STUDENT")) prefix = "STU";

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
        return String.format("%s%03d", prefix, max + 1);
    }

    public String generateStaffID() {
        int max = 0;
        for (User u : users) {
            String id = null;
            if (u instanceof AdminStaff)
                id = ((AdminStaff) u).getStaffID();
            if (u instanceof Lecturer)
                id = ((Lecturer) u).getStaffID();
            if (u instanceof AcademicLeader)
                id = ((AcademicLeader) u).getStaffID();

            if (id != null && id.startsWith("STF")) {
                try {
                    int num = Integer.parseInt(id.substring(3));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return String.format("STF%03d", max + 1);
    }

    public String generateAssessmentTypeID() {
        return "AT" + String.format("%03d", 1);
    }

    public String generateModuleID() {
        int max = 0;
        for (Module m : modules) {
            if (m.getModuleID().startsWith("MOD")) {
                try {
                    int num = Integer.parseInt(
                            m.getModuleID().substring(3));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return String.format("MOD%03d", max + 1);
    }

    public String generateClassID() {
        int max = 0;
        for (ClassModule c : classes) {
            if (c.getClassID().startsWith("CL")) {
                try {
                    int num = Integer.parseInt(
                            c.getClassID().substring(2));
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
        }
        return String.format("CL%03d", max + 1);
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
