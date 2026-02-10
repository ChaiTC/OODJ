import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Assessment implements Serializable {
    private static final long serialVersionUID = 1L;

    private String assessmentID;
    private String assessmentName;
    private AssessmentType assessmentType;
    private Module module;
    private Lecturer createdBy;

    private String classID;          
    private String status;          

    private Date createdDate;
    private Date dueDate;
    private Map<String, Double> studentMarks;

    
    public Assessment(String assessmentID,
                      String assessmentName,
                      AssessmentType assessmentType,
                      Module module,
                      Lecturer createdBy,
                      String classID,
                      Date dueDate) {

        this.assessmentID = assessmentID;
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.module = module;
        this.createdBy = createdBy;
        this.classID = classID;
        this.createdDate = new Date();
        this.dueDate = dueDate;

        this.status = "PENDING"; 
        this.studentMarks = new HashMap<>();
    }

    // Record marks 
    public void recordMarks(Student student, double marks) {
        if (student == null) return;
        if (marks < 0 || marks > assessmentType.getTotalMarks()) return;

        studentMarks.put(student.getStudentID(), marks);

       
        this.status = "GRADED";
    }

   
    public void addStudentMark(String studentID, double marks) {
        if (studentID == null) return;
        studentMarks.put(studentID, marks);

        if (!studentMarks.isEmpty()) this.status = "GRADED";
    }

    public Double getStudentMarks(String studentID) {
        return studentMarks.get(studentID);
    }

    
    public String getAssessmentID() { return assessmentID; }
    public void setAssessmentID(String assessmentID) { this.assessmentID = assessmentID; }

    public String getAssessmentName() { return assessmentName; }
    public void setAssessmentName(String assessmentName) { this.assessmentName = assessmentName; }

    public AssessmentType getAssessmentType() { return assessmentType; }
    public void setAssessmentType(AssessmentType assessmentType) { this.assessmentType = assessmentType; }

    public Module getModule() { return module; }
    public void setModule(Module module) { this.module = module; }

    public Lecturer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Lecturer createdBy) { this.createdBy = createdBy; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; } // needed by FileManager

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Map<String, Double> getStudentMarks() { return studentMarks; }

    
    public String getClassID() { return classID; }
    public void setClassID(String classID) { this.classID = classID; }

    public String getTitle() { return assessmentName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    
    public Double getScore() {
        if (studentMarks.isEmpty()) return 0.0;
        double total = 0;
        for (double m : studentMarks.values()) total += m;
        return total / studentMarks.size();
    }

    public Double getTotalMarks() {
        return assessmentType.getTotalMarks();
    }

    public String getModuleCode() {
        return module != null ? module.getModuleCode() : "";
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "assessmentID='" + assessmentID + '\'' +
                ", assessmentName='" + assessmentName + '\'' +
                ", assessmentType=" + (assessmentType != null ? assessmentType.getAssessmentType() : "N/A") +
                ", module=" + (module != null ? module.getModuleName() : "N/A") +
                ", classID='" + classID + '\'' +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                '}';
    }
}
