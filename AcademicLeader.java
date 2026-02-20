import java.util.ArrayList;
import java.util.List;

/**
 * AcademicLeader
 * - Manages modules
 * - Assigns lecturers
 * - Performs academic analytics
 */
public class AcademicLeader extends User {

    private static final long serialVersionUID = 1L;

    private String department;
    private String leaderID;
    private String staffID;
    private List<Lecturer> assignedLecturers;
    private List<Module> managedModules;

    // ======================================================
    // CONSTRUCTOR
    // ======================================================
    public AcademicLeader(String userID,
                          String username,
                          String password,
                          String email,
                          String fullName,
                          String phoneNumber,
                          String department,
                          String leaderID) {
        super(userID, username, password, email, fullName, phoneNumber, "ACADEMIC_LEADER");
        this.department = department;
        this.leaderID = leaderID;
        this.staffID = null;
        this.assignedLecturers = new ArrayList<>();
        this.managedModules = new ArrayList<>();
    }

    // ======================================================
    // CORE FUNCTIONS
    // ======================================================
    public void createModule(Module module) {
        if (module != null) {
            managedModules.add(module);
        }
    }

    public void assignLecturer(Lecturer lecturer) {
        if (lecturer != null && !assignedLecturers.contains(lecturer)) {
            assignedLecturers.add(lecturer);
        }
    }

    // ======================================================
    // ACADEMIC ANALYTICS
    // ======================================================
    public double calculateAverageScore(List<Assessment> assessments) {
        if (assessments == null || assessments.isEmpty()) return 0;

        double total = 0;
        for (Assessment a : assessments) {
            total += a.getScore();
        }
        return total / assessments.size();
    }

    public double calculatePassRate(List<Assessment> assessments, GradingScale scale) {
        if (assessments == null || assessments.isEmpty() || scale == null) return 0;

        int passCount = 0;
        for (Assessment a : assessments) {
            if (scale.isPass(a.getScore())) {
                passCount++;
            }
        }
        return (passCount * 100.0) / assessments.size();
    }

    public String evaluateAcademicRisk(double avgScore, double passRate) {
        if (avgScore < 60 || passRate < 70) return "HIGH RISK";
        if (avgScore < 70 || passRate < 80) return "MEDIUM RISK";
        return "LOW RISK";
    }

    // ======================================================
    // FEEDBACK AUDIT
    // ======================================================
    public int countPoorFeedback(List<Feedback> feedbacks) {
        if (feedbacks == null) return 0;

        int count = 0;
        for (Feedback f : feedbacks) {
            if (f.getComment() == null || f.getComment().trim().length() < 20) {
                count++;
            }
        }
        return count;
    }

    // ======================================================
    // MODULE-LEVEL ANALYSIS
    // ======================================================
    public List<Assessment> getAssessmentsByModule(List<Assessment> allAssessments, Module module) {
        List<Assessment> result = new ArrayList<>();
        if (allAssessments == null || module == null) return result;

        for (Assessment a : allAssessments) {
            if (a.getModuleCode().equals(module.getModuleCode())) {
                result.add(a);
            }
        }
        return result;
    }

    public String analyzeModulePerformance(Module module,
                                           List<Assessment> assessments,
                                           GradingScale scale) {

        if (assessments == null || assessments.isEmpty()) return "NO DATA";

        double avg = calculateAverageScore(assessments);
        double pass = calculatePassRate(assessments, scale);

        return evaluateAcademicRisk(avg, pass);
    }

    // ======================================================
    // GETTERS
    // ======================================================
    public String getDepartment() {
        return department;
    }

    public String getLeaderID() {
        return leaderID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public List<Lecturer> getAssignedLecturers() {
        return assignedLecturers;
    }

    public List<Module> getManagedModules() {
        return managedModules;
    }

    // ======================================================
    // SETTERS (REQUIRED FOR EDIT PROFILE)
    // ======================================================
    public void setDepartment(String department) {
        this.department = department;
    }

    public void setLeaderID(String leaderID) {
        this.leaderID = leaderID;
    }

}
