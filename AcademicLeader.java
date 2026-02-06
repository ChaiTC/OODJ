import java.util.ArrayList;
import java.util.List;

/**
 * AcademicLeader
 * Role:
 * - Academic governance
 * - Module oversight
 * - Lecturer assignment
 * - Academic analytics & reporting
 */
public class AcademicLeader extends User {

    private static final long serialVersionUID = 1L;

    private String department;
    private String leaderID;

    private List<Lecturer> assignedLecturers;
    private List<Module> managedModules;

    // ======================================================
    // CONSTRUCTOR (DO NOT CHANGE SIGNATURE – LOGIN DEPENDS ON THIS)
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
        this.assignedLecturers = new ArrayList<>();
        this.managedModules = new ArrayList<>();
    }

    // ======================================================
    // MODULE MANAGEMENT
    // ======================================================
    public void createModule(Module module) {
        if (module == null) return;

        // Prevent duplicate module codes
        for (Module m : managedModules) {
            if (m.getModuleCode().equalsIgnoreCase(module.getModuleCode())) {
                return;
            }
        }
        managedModules.add(module);
    }

    public List<Module> getManagedModules() {
        return managedModules;
    }

    public int getTotalModules() {
        return managedModules.size();
    }

    // ======================================================
    // LECTURER MANAGEMENT
    // ======================================================
    public void assignLecturer(Lecturer lecturer) {
        if (lecturer == null) return;

        if (!assignedLecturers.contains(lecturer)) {
            assignedLecturers.add(lecturer);
        }
    }

    public List<Lecturer> getAssignedLecturers() {
        return assignedLecturers;
    }

    public int getTotalLecturers() {
        return assignedLecturers.size();
    }

    // ======================================================
    // ACADEMIC ANALYTICS
    // ======================================================
    public double calculateAverageScore(List<Assessment> assessments) {
        if (assessments == null || assessments.isEmpty()) return 0.0;

        double total = 0;
        for (Assessment a : assessments) {
            total += a.getScore();
        }
        return total / assessments.size();
    }

    public double calculatePassRate(List<Assessment> assessments, GradingScale scale) {
        if (assessments == null || assessments.isEmpty() || scale == null) return 0.0;

        int passCount = 0;
        for (Assessment a : assessments) {
            if (scale.isPass(a.getScore())) {
                passCount++;
            }
        }
        return (passCount * 100.0) / assessments.size();
    }

    public String evaluateAcademicRisk(double averageScore, double passRate) {
        if (averageScore < 60 || passRate < 70) return "HIGH RISK";
        if (averageScore < 70 || passRate < 80) return "MEDIUM RISK";
        return "LOW RISK";
    }

    // ======================================================
    // MODULE-LEVEL ANALYSIS
    // ======================================================
    public List<Assessment> getAssessmentsByModule(List<Assessment> allAssessments,
                                                   Module module) {

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

        if (assessments == null || assessments.isEmpty()) {
            return "NO DATA";
        }

        double avg = calculateAverageScore(assessments);
        double passRate = calculatePassRate(assessments, scale);

        return evaluateAcademicRisk(avg, passRate);
    }

    // ======================================================
    // FEEDBACK QUALITY AUDIT
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
    // ACADEMIC REPORT GENERATION (USED BY DASHBOARD)
    // ======================================================
    public String generateAcademicReport(List<Assessment> allAssessments,
                                         GradingScale scale) {

        StringBuilder sb = new StringBuilder();

        sb.append("ACADEMIC REPORT\n\n");
        sb.append("Department: ").append(department).append("\n");
        sb.append("Academic Leader: ").append(getFullName()).append("\n\n");

        sb.append("SUMMARY\n");
        sb.append("--------------------------------\n");
        sb.append("Total Modules: ").append(managedModules.size()).append("\n");
        sb.append("Total Assessments: ")
          .append(allAssessments == null ? 0 : allAssessments.size()).append("\n");

        double avg = calculateAverageScore(allAssessments);
        double passRate = calculatePassRate(allAssessments, scale);

        sb.append("Average Score: ").append(String.format("%.2f", avg)).append("\n");
        sb.append("Pass Rate: ").append(String.format("%.2f", passRate)).append("%\n");
        sb.append("Fail Rate: ").append(String.format("%.2f", 100 - passRate)).append("%\n");
        sb.append("Academic Risk Level: ")
          .append(evaluateAcademicRisk(avg, passRate)).append("\n\n");

        sb.append("MODULE PERFORMANCE\n");
        sb.append("--------------------------------\n");

        for (Module m : managedModules) {
            List<Assessment> moduleAssessments =
                    getAssessmentsByModule(allAssessments, m);

            sb.append(m.getModuleCode())
              .append(" - ")
              .append(m.getModuleName())
              .append(" : ")
              .append(analyzeModulePerformance(m, moduleAssessments, scale))
              .append("\n");
        }

        return sb.toString();
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

    // ======================================================
    // OPTIONAL: CONSOLE FALLBACK
    // ======================================================
    @Override
    public void displayMenu() {
        System.out.println("=== Academic Leader Menu ===");
        System.out.println("1. Manage Modules");
        System.out.println("2. Assign Lecturers");
        System.out.println("3. View Academic Reports");
        System.out.println("4. Audit Feedback");
    }

    @Override
    public void handleAction(String action) {
        System.out.println("Handling action: " + action);
    }

    @Override
    public String toString() {
        return leaderID + " - " + getFullName();
    }
}
