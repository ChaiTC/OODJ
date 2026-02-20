import java.io.*;

/**
 * GradingScale class - represents a grade with its criteria
 */
public class GradingScale implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String gradeID;
    private String gradeLetter;
    private double minPercentage;
    private double maxPercentage;
    private String description;
    private double gpa;
    
    // Original constructor
    public GradingScale(String gradeID, String gradeLetter, double minPercentage, 
                        double maxPercentage, String description) {
        this.gradeID = gradeID;
        this.gradeLetter = gradeLetter;
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
        this.description = description;
        this.gpa = 0.0;
    }
    
    // Constructor with GPA
    public GradingScale(String gradeID, String gradeLetter, double minPercentage, 
                        double maxPercentage, String description, double gpa) {
        this.gradeID = gradeID;
        this.gradeLetter = gradeLetter;
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
        this.description = description;
        this.gpa = gpa;
    }
    
    // Overloaded constructor for A, B, C, D, F thresholds
    public GradingScale(double aThreshold, double bThreshold, double cThreshold, 
                        double dThreshold, double fThreshold) {
        this.gradeID = "DEFAULT";
        this.gradeLetter = "A";
        this.minPercentage = aThreshold;
        this.maxPercentage = 100;
        this.description = "Default grading scale";
    }
    
    public boolean isInRange(double percentage) {
        return percentage >= minPercentage && percentage <= maxPercentage;
    }
    
    public String getGradeID() { 
        return gradeID; 
    }
    public void setGradeID(String gradeID) { 
        this.gradeID = gradeID; 
    }
    
    public String getGradeLetter() { 
        return gradeLetter; 
    }
    public void setGradeLetter(String gradeLetter) { 
        this.gradeLetter = gradeLetter; 
    }
    
    public double getMinPercentage() { 
        return minPercentage; 
    }
    public void setMinPercentage(double minPercentage) { 
        this.minPercentage = minPercentage; 
    }
    
    public double getMaxPercentage() { 
        return maxPercentage; 
    }
    public void setMaxPercentage(double maxPercentage) { 
        this.maxPercentage = maxPercentage; 
    }
    
    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public double getGPA() { 
        return gpa; 
    }
    public void setGPA(double gpa) { 
        this.gpa = gpa; 
    }
    
    public double getAMarkPercentage() { 
        return 80.0; 
    }
    public double getBMarkPercentage() { 
        return 70.0; 
    }
    public double getCMarkPercentage() { 
        return 60.0; 
    }
    public double getDMarkPercentage() { 
        return 50.0; 
    }
    public double getFMarkPercentage() { 
        return 0.0; 
    }
    public boolean isPass(double score) { 
        return score >= 60.0; 
    }
    
    @Override
    public String toString() {
        return "GradingScale{" +
                "gradeLetter='" + gradeLetter + '\'' +
                ", minPercentage=" + minPercentage +
                ", maxPercentage=" + maxPercentage +
                ", description='" + description + '\'' +
                '}';
    }
}
