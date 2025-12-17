import java.io.Serializable;
import java.util.Date;

public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String feedbackID;
    private String assessmentID;
    private String studentID;
    private String lecturerID;
    private String feedbackContent;
    private Date feedbackDate;
    private double suggestedMarks;
    private String comments;
    private boolean isDelivered;
    
    public Feedback(String feedbackID, String assessmentID, String studentID, 
                    String lecturerID, String feedbackContent, double suggestedMarks) {
        this.feedbackID = feedbackID;
        this.assessmentID = assessmentID;
        this.studentID = studentID;
        this.lecturerID = lecturerID;
        this.feedbackContent = feedbackContent;
        this.feedbackDate = new Date();
        this.suggestedMarks = suggestedMarks;
        this.isDelivered = false;
    }
    
    public void deliverFeedback() {
        this.isDelivered = true;
        System.out.println("Feedback delivered to student " + this.studentID);
    }
    
    public void addStudentComment(String comment) {
        this.comments = comment;
        System.out.println("Student comment added");
    }
    
    public String getFeedbackID() { return feedbackID; }
    public void setFeedbackID(String feedbackID) { this.feedbackID = feedbackID; }
    
    public String getAssessmentID() { return assessmentID; }
    public void setAssessmentID(String assessmentID) { this.assessmentID = assessmentID; }
    
    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }
    
    public String getLecturerID() { return lecturerID; }
    public void setLecturerID(String lecturerID) { this.lecturerID = lecturerID; }
    
    public String getFeedbackContent() { return feedbackContent; }
    public void setFeedbackContent(String feedbackContent) { this.feedbackContent = feedbackContent; }
    
    public Date getFeedbackDate() { return feedbackDate; }
    
    public double getSuggestedMarks() { return suggestedMarks; }
    public void setSuggestedMarks(double suggestedMarks) { this.suggestedMarks = suggestedMarks; }
    
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    
    public boolean isDelivered() { return isDelivered; }
    public void setDelivered(boolean delivered) { isDelivered = delivered; }
    
    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackID='" + feedbackID + '\'' +
                ", assessmentID='" + assessmentID + '\'' +
                ", studentID='" + studentID + '\'' +
                ", feedbackDate=" + feedbackDate +
                ", suggestedMarks=" + suggestedMarks +
                ", isDelivered=" + isDelivered +
                '}';
    }
}
