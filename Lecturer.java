import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Lecturer class - manages assessment and feedback
 * Responsibilities: Assessment design, mark entry, feedback provision
 */
public class Lecturer extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String lecturerID;
    private String staffID;
    private String department;
    private String academicLeaderID;
    private List<Module> assignedModules;
    private List<Assessment> createdAssessments;
    private List<Feedback> providedFeedback;
    
    public Lecturer(String userID, String username, String password, String email,
                    String fullName, String phoneNumber, String lecturerID, String department) {
        super(userID, username, password, email, fullName, phoneNumber, "LECTURER");
        this.lecturerID = lecturerID;
        this.staffID = null;
        this.department = department;
        this.academicLeaderID = null;
        this.assignedModules = new ArrayList<>();
        this.createdAssessments = new ArrayList<>();
        this.providedFeedback = new ArrayList<>();
    }
    
    @Override
    public void displayMenu() {
        System.out.println("=== Lecturer Menu ===");
        System.out.println("1. View Assigned Modules");
        System.out.println("2. Create Assessment");
        System.out.println("3. Enter Marks");
        System.out.println("4. Provide Feedback");
        System.out.println("5. Edit Profile");
        System.out.println("6. Logout");   
        
    }
    
    @Override
    public void handleAction(String action) {
        switch(action) {
            case "1":
                System.out.println("Opening Assigned Modules...");
                break;

            case "2":
                System.out.println("Creating Assessment...");
                break;

            case "3":
                System.out.println("Entering Marks...");
                break;

            case "4":
                System.out.println("Providing Feedback...");
                break;

            case "5":
                System.out.println("Editing Profile...");
                break;
            case "6":
                System.out.println("Logging out of lecturer account...");
                break;

            default:
                System.out.println("Invalid action. Please try again.");
                break;
        }
    }
    
    public void createAssessment(Assessment assessment) {
        createdAssessments.add(assessment);
        
    }
    
    public void keyInMarks(Assessment assessment, Student student, double marks) {
        assessment.recordMarks(student, marks);
        
    }
    
    public void provideFeedback(Feedback feedback) {
        providedFeedback.add(feedback);
    }
    
    public void viewAssessmentSummary(Assessment assessment, GradingSystem gradingSystem) {
    if (assessment.getStudentMarks().isEmpty()) {
        System.out.println("No marks entered yet.");
        return;
    }

    double total = 0;
    double max = Double.MIN_VALUE;
    double min = Double.MAX_VALUE;
    int passCount = 0;

    for (double mark : assessment.getStudentMarks().values()) {
        total += mark;
        max = Math.max(max, mark);
        min = Math.min(min, mark);

        double percentage = (mark / assessment.getTotalMarks()) * 100;
        if (percentage >= gradingSystem.getPassingPercentage()) {
            passCount++;
        }
    }

    int totalStudents = assessment.getStudentMarks().size();
    double average = total / totalStudents;

    System.out.println("=== Assessment Summary ===");
    System.out.println("Assessment: " + assessment.getAssessmentName());
    System.out.println("Average Marks: " + average);
    System.out.println("Highest Marks: " + max);
    System.out.println("Lowest Marks: " + min);
System.out.println("Pass Rate: " + passCount + "/" + totalStudents);
}
    
    public String getLecturerID() { return lecturerID; }
    public void setLecturerID(String lecturerID) { this.lecturerID = lecturerID; }
    
    public String getStaffID() { return staffID; }
    public void setStaffID(String staffID) { this.staffID = staffID; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getAcademicLeaderID() { return academicLeaderID; }
    public void setAcademicLeaderID(String academicLeaderID) { this.academicLeaderID = academicLeaderID; }
    
    public List<Module> getAssignedModules() { return assignedModules; }
    public List<Assessment> getCreatedAssessments() { return createdAssessments; }
    public List<Feedback> getProvidedFeedback() { return providedFeedback; }
}
