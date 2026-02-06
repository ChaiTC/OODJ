import java.util.ArrayList;
import java.util.List;

/**
 * AcademicLeader - enhanced with academic governance & analytics
 */
public class AcademicLeader extends User {

    private static final long serialVersionUID = 1L;

    private String department;
    private String leaderID;
    private List<Lecturer> assignedLecturers;
    private List<Module> managedModules;

    public AcademicLeader(String userID, String username, String password, String email,
                          String fullName, String phoneNumber,
                          String department, String leaderID) {
        super(userID, username, password, email, fullName, phoneNumber, "ACADEMIC_LEADER");
        this.department = department;
        this.leaderID = leaderID;
        this.assignedLecturers = new ArrayList<>();
        this.managedModules = new ArrayList<>();
    }

    /* ===================== CORE FUNCTIONS ===================== */

    public void createModule(Module module) {
        managedModules.add(module);
    }

    public void assignLecturer(Lecturer lecturer) {
        assignedLecturers.add(lecturer);
    }

    /* ===================== ACADEMIC ANALYTICS ===================== */

    public double calculateAverageScore(List<Assessment> assessments) {
        if (assessments == null || assessments.isEmpty()) return 0;
        double total = 0;
        for (Assessment a : assessments) total += a.getScore();
        return total / assessments.size();
    }

    public double calculatePassRate(List<Assessment> assessments, GradingScale scale) {
        if (assessments == null || assessments.isEmpty()) return 0;
        int pass = 0;
        for (Assessment a : assessments) {
            if (scale.isPass(a.getScore())) pass++;
        }
        return (pass * 100.0) / assessments.size();
    }

    public String evaluateAcademicRisk(double avg, double passRate) {
        if (avg < 60 || passRate < 70) return "HIGH RISK";
        if (avg < 70 || passRate < 80) return "MEDIUM RISK";
        return "LOW RISK";
    }

    /* ===================== FEEDBACK AUDIT ===================== */

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

    /* ===================== MODULE-LEVEL ANALYSIS ===================== */

    public List<Assessment> getAssessmentsByModule(List<Assessment> all, Module module) {
        List<Assessment> result = new ArrayList<>();
        for (Assessment a : all) {
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

        if (avg < 60 || pass < 70) return "HIGH RISK";
        if (avg < 70 || pass < 80) return "MEDIUM RISK";
        return "LOW RISK";
    }

    /* ===================== GETTERS ===================== */

    public String getDepartment() { return department; }
    public String getLeaderID() { return leaderID; }
    public List<Lecturer> getAssignedLecturers() { return assignedLecturers; }
    public List<Module> getManagedModules() { return managedModules; }
    
    @Override
    public void displayMenu() {
        System.out.println("=== Academic Leader Menu ===");
        System.out.println("1. Manage Modules");
        System.out.println("2. Manage Lecturers");
        System.out.println("3. View Analytics");
        System.out.println("4. Audit Feedback");
    }
    
    @Override
    public void handleAction(String action) {
        System.out.println("Handling action: " + action);
    }
}
