import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentID;
    private String enrollmentYear;

    private List<ClassModule> registeredClasses = new ArrayList<>();
    private List<Assessment> takenAssessments = new ArrayList<>();
    private List<Feedback> receivedFeedback = new ArrayList<>();

    public Student(String userID, String username, String password, String email,
                   String fullName, String phoneNumber, String studentID, String enrollmentYear) {
        super(userID, username, password, email, fullName, phoneNumber, "STUDENT");
        this.studentID = studentID;
        this.enrollmentYear = enrollmentYear;
    }

    public void registerClass(ClassModule classModule) {
        if (classModule == null) return;
        if (!registeredClasses.contains(classModule)) {
            registeredClasses.add(classModule);
        }
    }

    public void addTakenAssessment(Assessment assessment) {
        if (assessment == null) return;
        if (!takenAssessments.contains(assessment)) {
            takenAssessments.add(assessment);
        }
    }

    public void receiveFeedback(Feedback feedback) {
        if (feedback == null) return;
        receivedFeedback.add(feedback);
    }

    // ===== GETTERS / SETTERS =====
    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }

    public String getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(String enrollmentYear) { this.enrollmentYear = enrollmentYear; }

    public List<ClassModule> getRegisteredClasses() { return registeredClasses; }
    public List<Assessment> getTakenAssessments() { return takenAssessments; }
    public List<Feedback> getReceivedFeedback() { return receivedFeedback; }
}
