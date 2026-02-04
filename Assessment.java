import java.io.*;
import java.util.*;


public class Assessment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String assessmentID;
    private String assessmentName;
    private AssessmentType assessmentType;
    private Module module;
    private Lecturer createdBy;
    private Date createdDate;
    private Date dueDate; 
    private Map<String, Double> studentMarks; 
    
    public Assessment(String assessmentID, String assessmentName, AssessmentType assessmentType, Module module, Lecturer createdBy, Date dueDate) {
        this.assessmentID = assessmentID;
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.module = module;
        this.createdBy = createdBy;
        this.createdDate = new Date();
        this.dueDate = dueDate;
        this.studentMarks = new HashMap<>();
    }
    
    public void recordMarks(Student student, double marks) {
        if (marks >= 0 && marks <= assessmentType.getTotalMarks()) {
            studentMarks.put(student.getStudentID(), marks);
            
        } else {
            
        }
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
    
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    
    public Map<String, Double> getStudentMarks() { return studentMarks; }
    
    @Override
    public String toString() {
        return "Assessment{" +
                "assessmentID='" + assessmentID + '\'' +
                ", assessmentName='" + assessmentName + '\'' +
                ", assessmentType=" + assessmentType.getAssessmentType() +
                ", module=" + module.getModuleName() +
                ", dueDate=" + dueDate +
                '}';
    }
}
