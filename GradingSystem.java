import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GradingSystem class - manages the grading scales and grade calculations
 */
public class GradingSystem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String systemID;
    private String systemName;
    private List<GradingScale> grades;
    private double passingPercentage;
    
    public GradingSystem(String systemID, String systemName, double passingPercentage) {
        this.systemID = systemID;
        this.systemName = systemName;
        this.grades = new ArrayList<>();
        this.passingPercentage = passingPercentage;
        initializeDefaultGrades();
    }
    
    private void initializeDefaultGrades() {
        // APU Standard Grading System
        grades.add(new GradingScale("G1", "A+", 90, 100, "Excellent"));
        grades.add(new GradingScale("G2", "A", 80, 89, "Very Good"));
        grades.add(new GradingScale("G3", "B+", 75, 79, "Good"));
        grades.add(new GradingScale("G4", "B", 70, 74, "Satisfactory"));
        grades.add(new GradingScale("G5", "C+", 65, 69, "Acceptable"));
        grades.add(new GradingScale("G6", "C", 60, 64, "Pass"));
        grades.add(new GradingScale("G7", "D", 50, 59, "Weak"));
        grades.add(new GradingScale("G8", "F", 0, 49, "Fail"));
    }
    
    public String getGradeLetterByPercentage(double percentage) {
        for (GradingScale grade : grades) {
            if (grade.isInRange(percentage)) {
                return grade.getGradeLetter();
            }
        }
        return "N/A";
    }
    
    public String getGradeDescriptionByPercentage(double percentage) {
        for (GradingScale grade : grades) {
            if (grade.isInRange(percentage)) {
                return grade.getDescription();
            }
        }
        return "Not Graded";
    }
    
    public boolean isPassed(double percentage) {
        return percentage >= passingPercentage;
    }
    
    public void addGradingScale(GradingScale scale) {
        grades.add(scale);
    }
    
    public String getSystemID() { return systemID; }
    public void setSystemID(String systemID) { this.systemID = systemID; }
    
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    
    public List<GradingScale> getGrades() { return grades; }
    
    public double getPassingPercentage() { return passingPercentage; }
    public void setPassingPercentage(double passingPercentage) { this.passingPercentage = passingPercentage; }
    
    @Override
    public String toString() {
        return "GradingSystem{" +
                "systemName='" + systemName + '\'' +
                ", passingPercentage=" + passingPercentage + "%" +
                ", totalGrades=" + grades.size() +
                '}';
    }
}
