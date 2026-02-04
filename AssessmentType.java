import java.io.*;

/**
 * AssessmentType class - represents the type of assessment (Assignment, Test, etc.)
 */
public class AssessmentType implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Type {
        ASSIGNMENT, CLASS_TEST, FINAL_EXAM, PROJECT, QUIZ, PRESENTATION
    }
    
    private String typeID;
    private Type assessmentType;
    private double weightage; // Percentage contribution to final grade
    private double totalMarks;
    
    public AssessmentType(String typeID, Type assessmentType, double weightage, double totalMarks) {
        this.typeID = typeID;
        this.assessmentType = assessmentType;
        this.weightage = weightage;
        this.totalMarks = totalMarks;
    }
    
    public String getTypeID() { return typeID; }
    public void setTypeID(String typeID) { this.typeID = typeID; }
    
    public Type getAssessmentType() { return assessmentType; }
    public void setAssessmentType(Type assessmentType) { this.assessmentType = assessmentType; }
    
    public double getWeightage() { return weightage; }
    public void setWeightage(double weightage) { this.weightage = weightage; }
    
    public double getTotalMarks() { return totalMarks; }
    public void setTotalMarks(double totalMarks) { this.totalMarks = totalMarks; }
    
    @Override
    public String toString() {
        return "AssessmentType{" +
                "typeID='" + typeID + '\'' +
                ", assessmentType=" + assessmentType +
                ", weightage=" + weightage + "%" +
                ", totalMarks=" + totalMarks +
                '}';
    }
}
