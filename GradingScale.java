import java.io.Serializable;

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
    
    public GradingScale(String gradeID, String gradeLetter, double minPercentage, 
                        double maxPercentage, String description) {
        this.gradeID = gradeID;
        this.gradeLetter = gradeLetter;
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
        this.description = description;
    }
    
    public boolean isInRange(double percentage) {
        return percentage >= minPercentage && percentage <= maxPercentage;
    }
    
    public String getGradeID() { return gradeID; }
    public void setGradeID(String gradeID) { this.gradeID = gradeID; }
    
    public String getGradeLetter() { return gradeLetter; }
    public void setGradeLetter(String gradeLetter) { this.gradeLetter = gradeLetter; }
    
    public double getMinPercentage() { return minPercentage; }
    public void setMinPercentage(double minPercentage) { this.minPercentage = minPercentage; }
    
    public double getMaxPercentage() { return maxPercentage; }
    public void setMaxPercentage(double maxPercentage) { this.maxPercentage = maxPercentage; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
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
